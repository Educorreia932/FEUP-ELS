package pt.up.fe.els2023.model.table;

import org.apache.commons.collections.map.ListOrderedMap;
import pt.up.fe.els2023.model.table.values.DoubleValue;
import pt.up.fe.els2023.model.table.values.IntegerValue;
import pt.up.fe.els2023.model.table.values.StringValue;
import pt.up.fe.els2023.model.table.values.TableValue;
import pt.up.fe.els2023.model.table.values.Value;
import pt.up.fe.specs.util.classmap.FunctionClassMap;

import java.util.ArrayList;
import java.util.List;

public class Column<T extends Value> {
    private String header;
    private final List<T> elements = new ArrayList<>();
    private static final FunctionClassMap<Object, Value> convertToValue = new FunctionClassMap<>();

    static {
        convertToValue.put(Double.class, DoubleValue::new);
        convertToValue.put(Integer.class, IntegerValue::new);
        convertToValue.put(String.class, StringValue::new);
        convertToValue.put(Table.class, TableValue::new);
    }
    
    public Column(String header) {
        this.header = header;
    }
    
    public Column(String header, List<Object> elements) {
        this.header = header;

        for (var element : elements)
            addElement(element);
    }

    public List<Object> getElements() {
        return elements.stream().map(Value::value).toList();
    }

    @SuppressWarnings("unchecked")
    void addElement(Object element) {
        elements.add((T) convertToValue.apply(element));
    }

    void removeElement(int index) {
        elements.remove(index);
    }

    Object getElement(int index) {
        return elements.get(index).value();
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int numElements() {
        return elements.size();
    }
}
