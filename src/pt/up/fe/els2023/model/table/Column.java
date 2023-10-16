package pt.up.fe.els2023.model.table;

import java.util.ArrayList;
import java.util.List;

public class Column<T> {
    private String header;
    private final List<T> elements = new ArrayList<>();

    public Column(String header) {
        this.header = header;
    }
    
    public Column(String header, List<T> elements) {
        this.header = header;

        this.elements.addAll(elements);
    }

    public List<T> getElements() {
        return elements;
    }

    void addElement(T element) {
        elements.add(element);
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
