package userapp.brian.duran.userappapi.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.TYPE_USE})
@Constraint(validatedBy = {RoleValidator.class})
public @interface ValidRole {
  String message() default "Role is invalid";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
