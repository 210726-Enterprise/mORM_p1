import Logic.mORM;
import Util.*;
import Model.*;


public class mORMDriver {
    static public void main (String [] args){
       Configuration cfg = new Configuration();
       User u = new User();

       cfg.addAnnotatedClass(u.getClass());
        //System.out.println(cfg.getMetamodels().get(0).getColumns().get(0).getColumnName());

       u = (User)mORM.selectRow(cfg.getMetamodels().get(0), u, 2 );




    }
}
