package pt.up.fe.els2023.model.table;

import java.util.ArrayList;
import java.util.List;

public class Column {
    private String header;
    private final List<String> elements = new ArrayList<>();

    public Column(String header) {
        this.header = header;
    }
    
    public Column(String header, List<String> elements) {
        this.header = header;

        this.elements.addAll(elements);
    }

    public List<String> getElements() {
        return elements;
    }

    void addElement(String element) {
        elements.add(element);
    }

    void removeElement(int index) {
        elements.remove(index);
    }

    String getElement(int index) {
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
