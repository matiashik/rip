package com.rip.alt.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.rip.alt.models.Expression;
import com.rip.alt.models.ExpressionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;

@Service
@RequiredArgsConstructor
public class ExpressionValidator implements Validator {
    @Autowired
    public final ExpressionRepository repository;

    /**
     * Indicates whether this validator supports the given class.
     * @param clazz the class to check
     * @return true if this validator supports the given class, false otherwise
     */
    public boolean supports(Class<?> clazz) {
        return Expression.class.equals(clazz);
    }

    /**
     * Validates the given expression.
     *
     * @param target the expression to validate
     * @param errors the errors object to report any validation errors to
     */
    public void validate(Object target, Errors errors) {
        Expression expression = (Expression) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "latex", "latex.blank");

        if (expression.getLatex() != null) {
            if (expression.getLatex().length() < 3) {
                errors.rejectValue("latex", "latex.short");
            }

            if (expression.getLatex().length() > 2048) {
                errors.rejectValue("latex", "latex.long");
            }

            if (expression.getUser() != null && expression.getUser().getId() != null) {
                var otherExpression = repository.findByLatexAndUserId(expression.getLatex(),
                        expression.getUser().getId());
                if (otherExpression.isPresent() && otherExpression.get().getId() != expression.getId()) {
                    errors.rejectValue("latex", "latex.taken");
                }
            }
        }
    }
}
