package userapp.brian.duran.userappapi.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Optional;
import userapp.brian.duran.userappapi.enums.RoleEnum;

public class RoleValidator implements ConstraintValidator<ValidRole, String> {

  private String validRoles;

  @Override
  public void initialize(ValidRole constraintAnnotation) {
    this.validRoles = Arrays.toString(RoleEnum.values());
  }

  @Override
  public boolean isValid(String roleName, ConstraintValidatorContext context) {

    boolean isValid = Optional.ofNullable(roleName)
            .map(RoleEnum::exist)
            .orElse(true);

    if (!isValid) {
      context.disableDefaultConstraintViolation();

      String dynamicMessage = String.format(
              "El rol '%s' no es válido. Roles permitidos: %s",
              roleName,
              validRoles
      );

      context.buildConstraintViolationWithTemplate(dynamicMessage)
              .addConstraintViolation();
    }

    return isValid;
  }
}
