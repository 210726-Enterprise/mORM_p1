package Logic;

import Annotations.Entity;
import Util.ColumnField;
import Util.ConnectionUtil;
import Util.Metamodel;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Minimal ORM, a "simple" ORM that gets the job done
 * includes methods for CRUD operations
 * requires that a table already exists in the database, and cannot support tables with dependencies
 */
public class mORM {

    /**
     * inserts a row into the database by taking in a metamodel of the data to be entered and using it to find the
     * values that will need to be entered into each column,
     * then entering those values using the getter methods of the passed object
     * @param meta
     * @param obj object to be entered
     * @return returns an int to indicate the insertion was successful
     */
    static public int insertRow(Metamodel<?> meta, Object obj) { //TODO add unit testing
        StringBuilder builder = new StringBuilder();
        List<ColumnField> columns = meta.getColumns();
        builder.append("INSERT INTO " + obj.getClass().getAnnotation(Entity.class).tableName().toLowerCase() + " (" + columns.get(0).getColumnName());
        for (int i = 1; i < columns.size(); i++){
            builder.append(", " + columns.get(i).getColumnName());
        }
        builder.append(") VALUES (?");
        for (int i = 1; i < columns.size(); i++){
            builder.append(", ?");
        }
        builder.append(")");
        String sql = builder.toString();

        PreparedStatement ps;
        try (Connection connection = ConnectionUtil.getConnection())
        {
            Method [] methList = obj.getClass().getMethods();
            List<Method> methList2 = new ArrayList<>();

            for (int i = 0; i < columns.size(); i++){
                for(int k = 0; k < methList.length; k++) {
                    if (methList[k].getName().toLowerCase().contains("get" + columns.get(i).getColumnName().toLowerCase())) {
                        methList2.add(methList[k]);
                    }
                }
            }
            ps = connection.prepareStatement(sql);
            for (int k = 0; k < methList2.size(); k++){
                ps.setObject(k+1, methList2.get(k).invoke(obj));
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * selects a row from the database by first identifying the desired row, then creating an object of the class type of the row
     * and using the setter methods of the class to fill its fields
     * @param meta
     * @param id id of the row to be selected
     * @return returns the object which has been selected
     */
    static public Object selectRow(Metamodel<?> meta, int id) {
        String sql = "SELECT * FROM "
                + meta.getClazz().getAnnotation(Entity.class).tableName().toLowerCase()
                + " WHERE "
                + meta.getPrimaryKey().getColumnName()
                + " = ?";
        PreparedStatement ps;
        try (Connection connection = ConnectionUtil.getConnection()) {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();

            Method [] methList = meta.getClazz().getMethods();
            List<Method> methList2 = new ArrayList<>();

            Object o = meta.getClazz().newInstance();

            for (int i = 1; i <= rsmd.getColumnCount(); i++){
                for(int k = 0; k < methList.length; k++) {
                    if (methList[k].getName().toLowerCase().contains
                            ("set" + rsmd.getColumnName(i).toLowerCase())) {
                        methList2.add(methList[k]); //methlist2 now contains setter methods
                    }
                }
            }
            rs.next();

            for (int i = 0; i < methList2.size(); i++){
                methList2.get(i).invoke(o, rs.getObject(i+1));
            }
            return o;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    } //TODO test this code


    /**
     * updates a cell in the database by taking in "coordinates" for its location and changing the value within
     * @param meta
     * @param column the column or "x" coordinate of the cell
     * @param newVal the value the cell is to be updated with
     * @param id the id or "y" coordinate of the cell
     * @return returns an int indicating whether the update was successful or not
     */
    //TODO test this code
    static public int updateCell(Metamodel<?> meta, String column, Object newVal, int id) {

        String sql = "UPDATE "
                + meta.getClazz().getAnnotation(Entity.class).tableName().toLowerCase()
                + " SET "
                + column
                + " = ? WHERE "
                + meta.getPrimaryKey().getColumnName()
                + " = ?";

        PreparedStatement ps;
        try (Connection connection = ConnectionUtil.getConnection()){

            ps = connection.prepareStatement(sql);
            ps.setObject(1, newVal);
            ps.setInt(2, id);

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * deletes a row from the database by its id
     * @param meta
     * @param id the id of the row to be deleted
     * @return returns a boolean indicating whether the deletion was successful or not
     */
    static public boolean deleteRow(Metamodel<?> meta, int id) { //TODO add unit testing
        String sql = "DELETE FROM "
                + meta.getClazz().getAnnotation(Entity.class).tableName().toLowerCase()
                + " WHERE "
                + meta.getPrimaryKey().getColumnName()
                + "=?";

        PreparedStatement ps;
        try (Connection connection = ConnectionUtil.getConnection()){

            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            if (ps.executeUpdate() > 0){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
