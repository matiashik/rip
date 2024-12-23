package com.rip.alt.controllers.api;

import com.rip.alt.models.Session;
import com.rip.alt.models.User;
import com.rip.alt.services.SessionService;
import com.rip.alt.services.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/users/{user_id}/sessions", produces = MediaType.APPLICATION_JSON_VALUE)
class SessionsController {
    @Autowired
    private final SessionService service;

    @Autowired
    private final UserService userService;

    /**
     * Retrieves a list of active sessions for the given user.
     * @param token the authentication token
     * @param user_id the user id
     * @return a list of sessions
     */
    @GetMapping("")
    public List<Session> index(@RequestHeader("Authentication") String token, @PathVariable Long user_id) {
        User currentUser = service.authenticate(token);
        User u = userService.find(user_id);
        service.authorize(currentUser, u);
        return service.index(u);
    }

    /**
     * Creates a new session for the given user.
     * @param user_id the user id
     * @param user the user info to create the session with
     * @return the new session
     */
    @PostMapping("")
    public Session create(@PathVariable Long user_id, @RequestBody User user) {
        User currentUser = userService.authenticate(user);
        User u = userService.find(user_id);
        userService.authorize(currentUser, u);
        return service.create(u);
    }

    /**
     * Deletes a session.
     * @param token the authentication token
     * @param user_id the user id
     * @param id the id of the session to delete
     * @return the deleted session
     */
    @DeleteMapping("/{id}")
    public Session delete(@RequestHeader("Authentication") String token, @PathVariable Long user_id, @PathVariable Long id) {
        User currentUser = service.authenticate(token);
        User u = userService.find(user_id);
        userService.authorize(currentUser, u);
        return service.delete(id);
    }
}
