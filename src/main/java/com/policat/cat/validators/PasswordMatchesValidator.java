package com.policat.cat.validators;

import com.policat.cat.dtos.UserRegistrationDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        UserRegistrationDTO userRegistrationDTO = (UserRegistrationDTO) obj;
        return userRegistrationDTO.getPassword().equals(userRegistrationDTO.getPassword_confirm());
    }
}
