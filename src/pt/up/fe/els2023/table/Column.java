package pt.up.fe.els2023.table;

import java.util.ArrayList;
import java.util.List;

public class Column<T> {
    private String key;
    private final List<T> elements = new ArrayList<>();

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
