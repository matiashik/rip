package com.rip.alt.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.rip.alt.models.User;
import com.rip.alt.models.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;

@Service
@RequiredArgsConstructor
public class UserValidator implements Validator {
    @Autowired
    public final UserRepository repository;

    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        User user = (User) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "login", "login.blank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.blank");

        if (user.getLogin().length() < 3) {
            errors.rejectValue("login", "login.short");
        }

        if (user.getPassword().length() < 3) {
            errors.rejectValue("password", "password.short");
        }

        {
            var otherUser = repository.findByLogin(user.getLogin());
            if (otherUser.isPresent() && otherUser.get().getId() != user.getId()) {
                errors.rejectValue("login", "login.taken");
            }
        }
    }
}
