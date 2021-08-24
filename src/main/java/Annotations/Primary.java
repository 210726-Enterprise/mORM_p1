package Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Annotation indicating a primary field
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Primary {
    String keyName();
}
