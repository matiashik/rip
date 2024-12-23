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

    /**
     * Indicates whether this validator supports the given class.
     *
     * @param clazz the class to check
     * @return true if this validator supports the given class, false otherwise
     */
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    /**
     * Validates the given user.
     *
     * @param target the user to validate
     * @param errors the errors object to report any validation errors to
     */
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "login", "login.blank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.blank");

        if (user.getLogin() != null && user.getLogin().length() < 3) {
            errors.rejectValue("login", "login.short");
        }

        if (user.getPassword() != null && user.getPassword().length() < 3) {
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
