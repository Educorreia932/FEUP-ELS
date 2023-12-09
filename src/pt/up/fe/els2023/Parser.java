package pt.up.fe.els2023;

import com.google.inject.Inject;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.parser.IParser;

import org.xtext.example.tablefork.TableForkStandaloneSetup;
import org.xtext.example.tablefork.tableFork.*;
import org.xtext.example.tablefork.tableFork.impl.ProgramImpl;
import pt.up.fe.els2023.internal.Selection;
import pt.up.fe.els2023.internal.TableInteraction;
import pt.up.fe.els2023.model.table.Table;

import java.io.IOException;
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

        var treeIterator = resource.getAllContents();

        TableInteraction tableInteraction = TableInteraction.fromTable(new Table());
        
        while (treeIterator.hasNext()) {
            var element = treeIterator.next();

            if (element instanceof Load load) {
                String path = removeQuotes(load.getPath());
                tableInteraction = TableInteraction.load(path);
            }

            if (element instanceof Select select) {
                var fields = select.getFields();
                var froms = select.getFrom();
                
                Selection selection = tableInteraction.select();
                
                for (var field : fields) {
                    var fieldNames = field.getFieldNames().stream().map(this::removeQuotes).toList();

                    selection = selection
                        .fields(fieldNames.toArray(String[]::new));
                }
                
                for (var from : froms) {
                    String fromField = removeQuotes(from.getFromField());

                    var fieldNames = from.getFields().getFieldNames().stream().map(this::removeQuotes).toList();

                    selection = selection
                        .from(fromField)
                            .fields(fieldNames.toArray(String[]::new))
                        .end();
                }
                
                tableInteraction = selection.end();
            }

            if (element instanceof Rename rename) {
                String fieldName = removeQuotes(rename.getFieldName());
                String newName = removeQuotes(rename.getNewName());
                
                tableInteraction.rename(fieldName, newName);
            }

            if (element instanceof Save program) {
                tableInteraction.save("result.csv");
            }
        }
    }
    
    private String removeQuotes(String string) {
        return string.substring(1, string.length() - 1);
    }
}
