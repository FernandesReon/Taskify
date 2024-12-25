package com.reonfernandes.Taskify.helper;

import com.reonfernandes.Taskify.forms.RegisterForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterForm> {

    @Override
    public boolean isValid(RegisterForm registerForm, ConstraintValidatorContext constraintValidatorContext) {
        return registerForm.getPassword() != null &&
                registerForm.getPassword().equals(registerForm.getConfirmPassword());
    }
}
