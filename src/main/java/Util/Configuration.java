package Util;

import java.util.*;

public class Configuration {
    private ArrayList<Metamodel<Class<?>>> metamodelList;

    //Constructor
    public Configuration(){
        metamodelList = new ArrayList<>();
    }

    /**
     * Adds the provided class to the list of metamodels via the Metamodel.of() method
     * @param annotatedClass the class to be added
     * @return returns the current instance of the Configuration, allowing for multiple additions on one line
     */
    public Configuration addAnnotatedClass(Class annotatedClass) {

        if(metamodelList == null) {
            metamodelList = new ArrayList<>();
        }

        metamodelList.add(Metamodel.of(annotatedClass));

        return this;
    }

    //Getter
    public List<Metamodel<Class<?>>> getMetamodels() {
        return (metamodelList == null) ? Collections.emptyList() : metamodelList;
    }
}
