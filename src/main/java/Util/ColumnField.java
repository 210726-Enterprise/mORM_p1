package Util;

import Annotations.Column;
import java.lang.reflect.Field;

public class ColumnField {
    private Field field;

    //Constructor
    public ColumnField(Field field) {
        if (field.getAnnotation(Column.class) == null) {
            throw new IllegalStateException("Column annotation not used, cannot create ColumnField object.");
        }
        this.field = field;
    }

    //Getters
    public String getName() {
        return field.getName();
    }

    public Class<?> getType() {
        return field.getType();
    }

    public String getColumnName() {
        return field.getAnnotation(Column.class).columnName();
    }

}
