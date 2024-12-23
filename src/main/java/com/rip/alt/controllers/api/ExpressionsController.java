package com.rip.alt.controllers.api;

import com.rip.alt.models.Expression;
import com.rip.alt.models.User;
import com.rip.alt.services.ExpressionService;
import com.rip.alt.services.SessionService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/expressions", produces = MediaType.APPLICATION_JSON_VALUE)
class ExpressionsController {
    @Autowired
    private final SessionService sessionService;

    @Autowired
    private final ExpressionService service;

    /**
     * Retrieves a list of expressions which the authenticated user may view.
     * @param token the authentication token
     * @return a list of expressions
     */
    @GetMapping("")
    public List<Expression> index(@RequestHeader("Authentication") String token) {
        User currentUser = sessionService.authenticate(token);
        service.authorize(currentUser);
        return service.index(currentUser);
    }

    /**
     * Retrieves an expression by its id.
     * @param token the authentication token
     * @param id the id of the expression
     * @return the expression
     */
    @GetMapping("/{id}")
    public Expression show(@RequestHeader("Authentication") String token, @PathVariable Long id) {
        User currentUser = sessionService.authenticate(token);
        Expression e = service.find(id);
        service.authorize(currentUser, e);
        return e;
    }

    /**
     * Creates a new expression.
     * @param token the authentication token
     * @param expression the expression to create
     * @return the created expression
     */
    @PostMapping("")
    public Expression create(@RequestHeader("Authentication") String token, @RequestBody Expression expression) {
        User currentUser = sessionService.authenticate(token);
        service.authorize(currentUser);
        return service.create(expression, currentUser);
    }

    /**
     * Updates an existing expression.
     * @param token the authentication token
     * @param id the id of the expression to update
     * @param expression the updated expression data
     * @return the updated expression
     */

    @PutMapping("/{id}")
    public Expression update(@RequestHeader("Authentication") String token, @PathVariable Long id,
            @RequestBody Expression expression) {
        User currentUser = sessionService.authenticate(token);
        Expression e = service.find(id);
        service.authorize(currentUser, e);
        return service.update(id, expression, currentUser);
    }

    /**
     * Deletes an expression.
     * @param token the authentication token
     * @param id the id of the expression to delete
     * @return the deleted expression
     */
    @DeleteMapping("/{id}")
    public Expression delete(@RequestHeader("Authentication") String token, @PathVariable Long id) {
        User currentUser = sessionService.authenticate(token);
        Expression e = service.find(id);
        service.authorize(currentUser, e);
        return service.delete(id);
    }

    /**
     * Retrieves the image associated with an expression.
     * @param token the authentication token, which is only required if the expression image is not published
     * @param id the id of the expression
     * @return the image content
     */
    @GetMapping("{id}/image")
    public String image(@RequestHeader(name = "Authentication", required = false) String token, @PathVariable Long id) {
        Expression e = service.find(id);
        if (!e.getExpressionImage().getPublished()) {
            User currentUser = sessionService.authenticate(token);
            service.authorize(currentUser, e);
        }
        return e.getExpressionImage().getContent();
    }

    /**
     * Publishes an expression's image.
     * @param token the authentication token
     * @param id the id of the expression to publish
     * @return the expression with the updated image
     */
    @PatchMapping("{id}/image/publish")
    public Expression publish(@RequestHeader("Authentication") String token, @PathVariable Long id) {
        User currentUser = sessionService.authenticate(token);
        Expression e = service.find(id);
        service.authorize(currentUser, e);
        return service.publish(e);
    }

    /**
     * Unpublishes an expression's image.
     * @param token the authentication token
     * @param id the id of the expression to unpublish
     * @return the expression with the updated image
     */
    @PatchMapping("{id}/image/unpublish")
    public Expression unpublish(@RequestHeader("Authentication") String token, @PathVariable Long id) {
        User currentUser = sessionService.authenticate(token);
        Expression e = service.find(id);
        service.authorize(currentUser, e);
        return service.unpublish(e);
    }
}
