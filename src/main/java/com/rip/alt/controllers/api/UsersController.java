package com.rip.alt.controllers.api;

import com.rip.alt.models.User;
import com.rip.alt.services.SessionService;
import com.rip.alt.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
class UsersController {
    @Autowired
    private final UserService service;

    @Autowired
    private final SessionService sessionService;

    /**
     * Retrieves a user by their id.
     *
     * @param token the authentication token
     * @param id    the id of the user to retrieve
     * @return the user
     */
    @GetMapping("/{id}")
    public User show(@RequestHeader("Authentication") String token, @PathVariable Long id) {
        User currentUser = sessionService.authenticate(token);
        User u = service.find(id);
        service.authorize(currentUser, u);
        return service.find(id);
    }

    /**
     * Creates a new user.
     *
     * @param user the user to create
     * @return the created user
     */
    @PostMapping("")
    public User create(@RequestBody User user) {
        return service.create(user);
    }

    /**
     * Updates a user.
     *
     * @param token the authentication token
     * @param id    the id of the user to update
     * @param user  the user to update to
     * @return the updated user
     */
    @PutMapping("/{id}")
    public User update(@RequestHeader("Authentication") String token, @PathVariable Long id, @RequestBody User user) {
        User currentUser = sessionService.authenticate(token);
        User u = service.find(id);
        service.authorize(currentUser, u);
        return service.update(id, user);
    }

    /**
     * Deletes a user.
     *
     * @param token the authentication token
     * @param id    the id of the user to delete
     * @return the deleted user
     */
    @DeleteMapping("/{id}")
    public User delete(@RequestHeader("Authentication") String token, @PathVariable Long id) {
        User currentUser = sessionService.authenticate(token);
        User u = service.find(id);
        service.authorize(currentUser, u);
        return service.delete(id);
    }
}
