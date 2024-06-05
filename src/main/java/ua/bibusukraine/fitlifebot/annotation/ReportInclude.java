package ua.bibusukraine.fitlifebot.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReportInclude {
//  int order() default -1;
  String title() default "";
  String columnName() default "";
}
