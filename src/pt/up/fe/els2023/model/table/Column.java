package pt.up.fe.els2023.model.table;

import java.util.ArrayList;
import java.util.List;

public class Column {
    private String header;
    private final List<Object> elements;

    public Column(String header, Object... elements) {
        this.header = header;
        
        if (elements.length == 0)
            this.elements = new ArrayList<>();
                
        else
            this.elements = List.of(elements);
    }

    void addElement(Object element) {
        // TODO: Is it possible to change it to T?
        elements.add(element);
    }

    void removeElement(int index) {
        elements.remove(index);
    }

    Object getElement(int index) {
        return elements.get(index);
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
