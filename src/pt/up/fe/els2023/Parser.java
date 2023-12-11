package pt.up.fe.els2023;

import com.google.inject.Inject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.parser.IParser;
import org.xtext.example.tablefork.TableForkStandaloneSetup;
import org.xtext.example.tablefork.tableFork.*;
import pt.up.fe.els2023.internal.Aggregation;
import pt.up.fe.els2023.internal.Selection;
import pt.up.fe.els2023.internal.TableInteraction;
import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.model.table.ValueType;
import pt.up.fe.els2023.model.table.column.Column;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Parser {
    @Inject
    private IParser parser;

    public Parser() {
        var injector = new TableForkStandaloneSetup().createInjectorAndDoEMFRegistration();

        injector.injectMembers(this);
    }

    public void parse(String filePath) {
        ResourceSet resourceSet = new ResourceSetImpl();
        Resource resource = resourceSet.createResource(URI.createFileURI(filePath));

        try {
            resource.load(Collections.emptyMap());
        }

        catch (IOException e) {
            throw new RuntimeException(e);
        }

        TableInteraction tableInteraction = TableInteraction.fromTable(null);

        parseInstruction(resource.getContents().get(0), tableInteraction);
    }

    private TableInteraction parseInstruction(EObject operation, TableInteraction tableInteraction) {
        System.out.println(operation);
        if (operation instanceof Load load)
            return parseLoad(load);

        if (operation instanceof Merge merge)
            return parseMerge(merge);

        if (operation instanceof Select select)
            return parseSelect(select, tableInteraction);

        if (operation instanceof Rename rename)
            return parseRename(rename, tableInteraction);

        if (operation instanceof Unstack)
            return parseUnstack(tableInteraction);

        if (operation instanceof Stack)
            return parseStack(tableInteraction);

        if (operation instanceof Max max)
            return parseMax(max, tableInteraction);

        if (operation instanceof ForEach forEach)
            return parseForEach(forEach, tableInteraction);

        if (operation instanceof Aggregate aggregate)
            return parseAggregate(aggregate, tableInteraction);
        
        if (operation instanceof Unravel)
            return parseUnravel(tableInteraction);
        
        if (operation instanceof Slice slice)
            return parseSlice(slice, tableInteraction);

        if (operation instanceof Save save)
            parseSave(save, tableInteraction);

        return tableInteraction;
    }
    
    private TableInteraction parseSlice(Slice slice, TableInteraction tableInteraction) {
        return tableInteraction.slice(slice.getFrom(), slice.getTo());
    }

    private TableInteraction parseLoad(Load load) {
        String path = removeQuotes(load.getPath());

        TableInteraction tableInteraction = TableInteraction.load(path);

        for (Operation operation : load.getOperations())
            tableInteraction = parseInstruction(operation, tableInteraction);

        return tableInteraction;
    }

    private TableInteraction parseMerge(Merge merge) {
        List<Load> loads = merge.getLoad();
        List<TableInteraction> parameterLoads = new ArrayList<>();

        // Parse load instructions
        for (Load load : loads) {
            TableInteraction newLoadInstruction = parseLoad(load);
            parameterLoads.add(newLoadInstruction);
        }

        TableInteraction tableInteraction = TableInteraction.merge(parameterLoads.toArray(TableInteraction[]::new));

        for (Operation operation : merge.getOperations())
            tableInteraction = parseInstruction(operation, tableInteraction);

        return tableInteraction;
    }

    private TableInteraction parseSelect(Select select, TableInteraction tableInteraction) {
        var fields = select.getFields();
        var froms = select.getFrom();
        var types = select.getType();

        Selection selection = tableInteraction.select();

        // Type selection
        for (var type : types) {
            var typeName = removeQuotes(type.getFromType());

            switch (typeName) {
                case "DOUBLE" -> selection = selection
                    .type(ValueType.DOUBLE);
                case "STRING" -> selection = selection
                    .type(ValueType.STRING);
                case "TABLE" -> selection = selection
                    .type(ValueType.TABLE);
            }
        }

        // Fields selection
        for (var field : fields) {
            var fieldNames = field.getFieldNames().stream().map(this::removeQuotes).toList();

            selection = selection
                .fields(fieldNames.toArray(String[]::new));
        }

        // From selection
        for (var from : froms) {
            String fromField = removeQuotes(from.getFromField());

            var fieldNames = from.getFields().getFieldNames().stream().map(this::removeQuotes).toList();

            selection = selection
                .from(fromField)
                .fields(fieldNames.toArray(String[]::new))
                .end();
        }

        return selection.end();
    }

    private TableInteraction parseRename(Rename rename, TableInteraction tableInteraction) {
        String fieldName = removeQuotes(rename.getFieldName());
        String newName = removeQuotes(rename.getNewName());

        return tableInteraction.rename(fieldName, newName);
    }

    private TableInteraction parseUnstack(TableInteraction tableInteraction) {
        return tableInteraction.unstack();
    }

    private TableInteraction parseStack(TableInteraction tableInteraction) {
        return tableInteraction.stack();
    }

    private TableInteraction parseMax(Max max, TableInteraction tableInteraction) {
        String maxParam = removeQuotes(max.getMaxName());
        return tableInteraction.max(maxParam);
    }

    private TableInteraction parseForEach(ForEach forEach, TableInteraction tableInteraction) {
        // Array table
        for (Column column : tableInteraction.getTable().getColumns()) {
            Table subTable = (Table) column.getElement(0);
            TableInteraction interaction = TableInteraction.fromTable(subTable);

            for (Operation operation : forEach.getOperations())
                interaction = parseInstruction(operation, interaction);

            column.setElement(0, interaction.getTable());
        }

        return tableInteraction;
    }

    private TableInteraction parseAggregate(Aggregate aggregate, TableInteraction tableInteraction) {
        Aggregation[] aggregations = aggregate.getAggregateNames().stream()
            .map(aggregation -> switch (removeQuotes(aggregation)) {
                case "SUM" -> Aggregation.SUM;
                case "AVERAGE" -> Aggregation.AVERAGE;
                default -> throw new RuntimeException("Invalid aggregation");
            })
            .toArray(Aggregation[]::new);

        tableInteraction.aggregate(aggregations);

        return tableInteraction;
    }

    private TableInteraction parseUnravel(TableInteraction tableInteraction) {
        tableInteraction.unravel();

        return tableInteraction;
    }

    private void parseSave(Save save, TableInteraction tableInteraction) {
        String path = removeQuotes(save.getPath());

        tableInteraction.save(path);
    }

    private String removeQuotes(String string) {
        return string.substring(1, string.length() - 1);
    }
}
