package Util;

import Annotations.Column;
import Annotations.Entity;
import Annotations.Primary;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class Metamodel<T> {
    private Class<T> clazz;
    private List<ColumnField> columns;
    private PKField primaryKey;

    /**
     * Performs a sort of input validation by checking whether the Entity annotation has been used on the provided class,
     * then calls the Metamodel constructor if it has or throws an exception if it hasn't
     * @param clazz the class provided
     * @param <T>
     * @return returns the result of the Metamodel constructor
     */
    public static <T> Metamodel<T> of(Class<T> clazz) {
        if (clazz.getAnnotation(Entity.class) == null) {
            throw new IllegalStateException("Entity annotation not used, cannot create Metamodel object.");
        }
        return new Metamodel<>(clazz);
    }

    //Constructor
    public Metamodel(Class<T> clazz) {
        this.clazz = clazz;
        columns = new LinkedList<>();
        setColumns();
        setPrimaryKey();
    }

    //Getters

    public Class<?> getClazz(){
        return clazz;
    }

    public PKField getPrimaryKey(){
        return primaryKey;
    }

    public List<ColumnField> getColumns() {
        if (columns.isEmpty()){
            throw new RuntimeException("No columns found in: " + clazz.getSimpleName());
        }
        return columns;
    }


    //Setters
    public void setColumns(){
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                columns.add(new ColumnField(field));
            }
        }
    }

    public void setPrimaryKey(){
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Primary primary = field.getAnnotation(Primary.class);
            if (primary != null) {
                primaryKey = new PKField(field);
            }
        }
    }


}
