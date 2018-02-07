package be.kdg.integratieproject2.API.Validation.Validator;

import be.kdg.integratieproject2.API.Dto.UserRegistrationDto;
import be.kdg.integratieproject2.API.Validation.Annotations.PasswordMatches;

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
