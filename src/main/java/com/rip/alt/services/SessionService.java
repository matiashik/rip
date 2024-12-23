package com.rip.alt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rip.alt.exceptions.AuthenticationException;
import com.rip.alt.exceptions.AuthorizationException;
import com.rip.alt.models.Session;
import com.rip.alt.models.SessionRepository;
import com.rip.alt.models.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {

    @Autowired
    public final SessionRepository repository;

    /**
     * Retrieves a list of sessions for the given user.
     * @param user the user to retrieve the sessions for
     * @return a list of sessions
     */
    public List<Session> index(User user) {
        return repository.findAllByUserId(user.getId());
    }

    /**
     * Creates a new session for the given user.
     * @param user the user to create a session for
     * @return the new session
     */
    public Session create(User user) {
        Session session = new Session();
        session.setUser(user);
        String token = generateToken(session);
        session.setToken(token);

        return repository.save(session);
    }

    /**
     * Deletes a session.
     *
     * @param id the id of the session to delete
     * @return the deleted session
     */
    public Session delete(Long id) {
        return repository.findById(id).map(session -> {
            repository.delete(session);
            return session;
        }).get();
    }

    /**
     * Authenticates a user based on the given token.
     * @param token the token to authenticate with
     * @return the authenticated user
     * @throws AuthenticationException if no user is found with the given token
     */
    public User authenticate(String token) {
        return repository.findByToken(token).orElseThrow(AuthenticationException::new).getUser();
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

    /**
     * Generates a secure token for the given session.
     * The token is a concatenation of the user id and a secure random token.
     * The secure random token is generated using the SecureRandom class and
     * is a URL-safe base64 encoded string.
     *
     * @param session the session to generate the token for
     * @return the generated token
     */
    private String generateToken(Session session) {
        Long userId = session.getUser().getId();
        String secureToken = java.util.Base64.getUrlEncoder().withoutPadding()
                .encodeToString(java.security.SecureRandom.getSeed(128));

        String token = userId + "-" + secureToken;
        return token;
    }
}
