package pt.up.fe.els2023.model.table.column;

import javafx.util.Pair;
import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.model.table.ValueType;
import pt.up.fe.els2023.model.table.values.DoubleValue;
import pt.up.fe.els2023.model.table.values.IntegerValue;
import pt.up.fe.els2023.model.table.values.StringValue;
import pt.up.fe.els2023.model.table.values.TableValue;
import pt.up.fe.els2023.model.table.values.Value;
import pt.up.fe.specs.util.classmap.FunctionClassMap;

import javax.swing.table.TableColumn;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Column {
    private String header;
    private final ValueType type;
    private final List<Value> elements = new ArrayList<>();

    Column(String header, Object[] elements, ValueType type) {
        this.header = header;
        this.type = type;

        for (var element : elements)
            addElement(element);
    }

    public static Column withType(String header, ValueType type, Object... elements) {
        return switch (type) {
            case DOUBLE -> Column.ofDoubles(header, Arrays.copyOf(elements, elements.length, Double[].class));
            case STRING -> Column.ofStrings(header, Arrays.copyOf(elements, elements.length, String[].class));
            case TABLE -> Column.ofTables(header, Arrays.copyOf(elements, elements.length, Table[].class));
        };
    }

    public static Column ofDoubles(String header, Double... elements) {
        return new Column(header, elements, ValueType.DOUBLE);
    }

    public static Column ofStrings(String header, String... elements) {
        return new Column(header, elements, ValueType.STRING);
    }

    public static Column ofTables(String header, Table... elements) {
        return new Column(header, elements, ValueType.TABLE);
    }

    public List<Object> getElements() {
        return elements.stream().map(Value::value).toList();
    }

    public void addElements(Object... elements) {
        for (Object element : elements)
            addElement(element);
    }

    public void addElement(Object element) {
        if (element != null && ValueType.fromObject(element) != type)
            throw new RuntimeException("Trying to add element with incorrect type");

        elements.add(switch (type) {
            case DOUBLE -> new DoubleValue((Double) element);
            case STRING -> new StringValue((String) element);
            case TABLE -> new TableValue((Table) element);
        });
    }

    public void removeElement(int index) {
        elements.remove(index);
    }

    public Object getElement(int index) {
        return elements.get(index).value();
    }

    public String getHeader() {
        return header;
    }

    public Pair<String, ValueType> getHeaderAndType() {
        return new Pair<>(header, type);
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int numElements() {
        return elements.size();
    }

    public ValueType getType() {
        return type;
    }
}
