package Util;

import Annotations.Primary;

import java.lang.reflect.Field;

public class PKField {
    private Field field;

    //Constructor
    public PKField(Field field) {
        if (field.getAnnotation(Primary.class) == null) {
            throw new IllegalStateException("Primary annotation not used, cannot create PKField object.");
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
        return field.getAnnotation(Primary.class).keyName();
    }

}
