package com.rip.alt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rip.alt.exceptions.AuthorizationException;
import com.rip.alt.models.Expression;
import com.rip.alt.models.ExpressionRepository;
import com.rip.alt.models.User;
import com.rip.alt.validators.ExpressionValidator;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpressionService {

    @Autowired
    public final ExpressionRepository repository;

    @Autowired
    public final ExpressionValidator validator;

    @Autowired
    public final ExpressionImageService imageService;

    /**
     * Retrieves a list of expressions which the given user may view.
     *
     * @param user the user who is viewing
     * @return a list of expressions
     */
    public List<Expression> index(User user) {
        return repository.findAllByUserId(user.getId());
    }

    /**
     * Retrieves an expression by its id.
     *
     * @param id the id of the expression to retrieve
     * @return the expression associated with the given id
     * @throws NoSuchElementException if no expression is found with the given id
     */
    public Expression find(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Creates a new expression, validating the given expression before saving.
     *
     * @param expression the expression to create
     * @param user       the user who is creating the expression
     * @return the newly created expression
     * @throws ValidationException if the given expression is invalid
     */
    public Expression create(Expression expression, User user) {
        expression.setUser(user);
        validator.validateObject(expression).failOnError(ValidationException::new);

        Expression saved = repository.save(expression);
        saved.setExpressionImage(imageService.attach(expression));
        return repository.save(saved);
    }

    /**
     * Updates an expression by the given id, validating the given expression before
     * saving.
     *
     * @param id         the id of the expression to update
     * @param expression the expression to update to
     * @param user       the user who is updating the expression
     * @return the updated expression
     * @throws NoSuchElementException if no expression is found with the given id
     * @throws ValidationException    if the given expression is invalid
     */
    public Expression update(Long id, Expression expression, User user) {
        return repository.findById(id).map(e -> {
            validator.validateObject(expression).failOnError(ValidationException::new);
            e.setLatex(expression.getLatex());
            e.setExpressionImage(imageService.attach(e));
            return repository.save(e);
        }).orElseGet(() -> {
            return create(expression, user);
        });
    }

    /**
     * Deletes an expression by its id.
     *
     * @param id the id of the expression to delete
     * @return the deleted expression
     * @throws NoSuchElementException if no expression is found with the given id
     */
    public Expression delete(Long id) {
        return repository.findById(id).map(expression -> {
            repository.delete(expression);
            return expression;
        }).get();
    }

    /**
     * Publishes an expression, setting the published field of its image to true.
     *
     * @param expression the expression to publish
     * @return the expression with the updated image
     */
    public Expression publish(Expression expression) {
        expression.getExpressionImage().setPublished(true);
        return repository.save(expression);
    }

    /**
     * Unpublishes an expression, setting the published field of its image to false.
     *
     * @param expression the expression to unpublish
     * @return the expression with the updated image
     */
    public Expression unpublish(Expression expression) {
        expression.getExpressionImage().setPublished(false);
        return repository.save(expression);
    }

    /**
     * Checks if the current user is authorized to access expressions.
     *
     * @param currentUser the user attempting to access the expressions
     * @throws AuthorizationException if the current user is not authorized
     */
    public void authorize(User currentUser) {
        if (currentUser.getId() == null) {
            throw new AuthorizationException();
        }
    }

    /**
     * Checks if the current user is authorized to access the given expression.
     *
     * @param currentUser the user attempting to access the expression
     * @param expression  the expression to check access for
     * @throws AuthorizationException if the current user is not the owner of the
     *                                expression
     */
    public void authorize(User currentUser, Expression expression) {
        if (currentUser.getId() != expression.getUser().getId()) {
            throw new AuthorizationException();
        }
    }
}
