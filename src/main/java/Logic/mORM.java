package Logic;

import Annotations.Entity;
import Annotations.Primary;
import Util.ColumnField;
import Util.Configuration;
import Util.ConnectionUtil;
import Util.Metamodel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Minimal ORM, a "simple" ORM that gets the job done
 * includes methods for CRUD operations
 * requires that a table already exists in the database, and cannot support tables with dependencies
 */
public class mORM {

    /**
     * inserts a row into the database by taking in a metamodel of the data to be entered and using it to find the
     * values that will need to be entered into each column for a database insertion to function properly
     * @param meta
     */
    static public void insertRow(Metamodel<?> meta, Object obj) { //TODO add unit testing
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
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }



    static public Object selectRow(Metamodel<?> meta, Object obj, int id) {
        String sql = "SELECT * FROM "
                + obj.getClass().getAnnotation(Entity.class).tableName().toLowerCase()
                + " WHERE "
                + meta.getPrimaryKey().getColumnName()
                + " = ?";
        PreparedStatement ps;
        try (Connection connection = ConnectionUtil.getConnection()) {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();

            Method [] methList = obj.getClass().getMethods();
            List<Method> methList2 = new ArrayList<>();
            //Method setPK = null;

            Object o = meta.getClazz().newInstance();

            for (int i = 1; i <= rsmd.getColumnCount(); i++){
                for(int k = 0; k < methList.length; k++) {
                    if (methList[k].getName().toLowerCase().contains
                            ("set" + rsmd.getColumnName(i).toLowerCase())) {
                        methList2.add(methList[k]); //methlist2 now contains setter methods
                    }
//                    else if (methList[k].getName().toLowerCase().contains
//                            ("set" + meta.getPrimaryKey().getColumnName().toLowerCase())){
//                        setPK = methList[k];
//                    }
                }
            }
            rs.next();
            //setPK.invoke(o, rs.getObject(1));

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


    //TODO unit test for sure, seems a little rickety tbh (though it passed THIS test)
    static public void updateCell(Metamodel<?> meta, Object obj, String column, Object newVal, int id) {

        String sql = "UPDATE "
                + obj.getClass().getAnnotation(Entity.class).tableName().toLowerCase()
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

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static public boolean deleteRow(Metamodel<?> meta, Object obj, int id) { //TODO add unit testing
        String sql = "DELETE FROM "
                + obj.getClass().getAnnotation(Entity.class).tableName().toLowerCase()
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
