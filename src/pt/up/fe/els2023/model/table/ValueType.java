package pt.up.fe.els2023.model.table;

public enum ValueType {
    DOUBLE,
    STRING,
    TABLE;

    public static ValueType fromObject(Object object) {
        if (object.getClass() == Double.class)
            return DOUBLE;

        if (object.getClass() == String.class)
            return STRING;
        
        if (object.getClass() == Table.class)
            return TABLE;
        

        return null;
    }
}
