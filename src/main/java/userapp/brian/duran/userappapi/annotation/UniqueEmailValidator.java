package userapp.brian.duran.userappapi.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import userapp.brian.duran.userappapi.repository.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {

        return Optional.ofNullable(email)
                .map(e -> !userRepository.existsByEmail(e))
                .orElse(true);
    }
}
