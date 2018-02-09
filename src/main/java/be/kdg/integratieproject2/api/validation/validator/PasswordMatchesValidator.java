package be.kdg.integratieproject2.api.validation.validator;

import be.kdg.integratieproject2.api.dto.UserRegistrationDto;
import be.kdg.integratieproject2.api.validation.annotations.PasswordMatches;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {


    @Override
    public void initialize(PasswordMatches passwordMatches) {

    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        UserRegistrationDto user = (UserRegistrationDto) o;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}
