package pt.up.fe.els2023.table;

import java.util.ArrayList;
import java.util.List;

public class Column<T> {
    private String header;
    private final List<T> elements;

    public Column(String header) {
        this.header = header;
        this.elements = new ArrayList<>();
    }

    @SafeVarargs
    public Column(String header, T... elements) {
        this.header = header;
        this.elements = List.of(elements);
    }

    void addElement(Object element) {
        // TODO: Is it possible to change it to T?
        elements.add((T) element);
    }

    void removeElement(int index) {
        elements.remove(index);
    }

    T getElement(int index) {
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
