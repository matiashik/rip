package com.rip.alt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rip.alt.exceptions.AuthenticationException;
import com.rip.alt.exceptions.AuthorizationException;
import com.rip.alt.models.User;
import com.rip.alt.models.UserRepository;
import com.rip.alt.validators.UserValidator;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    public final UserRepository repository;

    @Autowired
    public final UserValidator validator;

    /**
     * Creates a new user, validating the given user before saving.
     *
     * @param user the user to create
     * @return the newly created user
     * @throws ValidationException if the given user is invalid
     */
    public User create(User user) {
        validator.validateObject(user).failOnError(ValidationException::new);

        return repository.save(user);
    }

    /**
     * Retrieves a user by its id.
     *
     * @param id the id of the user to retrieve
     * @return the user associated with the given id
     * @throws NoSuchElementException if no user is found with the given id
     */
    public User find(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Updates a user by its id, validating the given user before saving.
     *
     * @param id   the id of the user to update
     * @param user the user to update to
     * @return the updated user
     * @throws NoSuchElementException if no user is found with the given id
     * @throws ValidationException    if the given user is invalid
     */
    public User update(Long id, User user) {
        return repository.findById(id).map(u -> {
            user.setId(id);
            validator.validateObject(user).failOnError(ValidationException::new);
            u.setLogin(user.getLogin());
            u.setPassword(user.getPassword());
            return repository.save(u);
        }).orElseGet(() -> {
            return create(user);
        });
    }

    /**
     * Deletes a user by their id.
     *
     * @param id the id of the user to delete
     * @return the deleted user
     * @throws NoSuchElementException if no user is found with the given id
     */
    public User delete(Long id) {
        return repository.findById(id).map(user -> {
            repository.delete(user);
            return user;
        }).get();

    }

    /**
     * Authenticates a user with the given login and password.
     *
     * @param user the user to authenticate
     * @return the authenticated user
     * @throws AuthenticationException if the user cannot be authenticated
     */
    public User authenticate(User user) {
        return repository.findByLoginAndPassword(user.getLogin(), user.getPassword())
                .orElseThrow(AuthenticationException::new);
    }

    /**
     * Checks if the current user is authorized to access the given user.
     *
     * @param currentUser the user attempting to access the given user
     * @param user        the user to check access for
     * @throws AuthorizationException if the current user is not the owner of the given user
     */
    public void authorize(User currentUser, User user) {
        if (currentUser.getId() != user.getId()) {
            throw new AuthorizationException();
        }
    }
}
