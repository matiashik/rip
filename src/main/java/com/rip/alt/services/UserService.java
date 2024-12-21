package com.rip.alt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public User create(User user) {
        validator.validateObject(user).failOnError(ValidationException::new);

        return repository.save(user);
    }

    public User find(Long id) {
        return repository.findById(id).get();
    }

    public User update(Long id, User user) {
        return repository.findById(id).map(u -> {
            user.setId(id);
            validator.validateObject(user).failOnError(ValidationException::new);
            u.setLogin(user.getLogin());
            u.setPassword(user.getPassword());
            return repository.save(u);
        }).orElseGet(() -> {
            validator.validateObject(user).failOnError(ValidationException::new);
            return repository.save(user);
        });
    }

    public User delete(Long id) {
        return repository.findById(id).map(user -> {
            repository.delete(user);
            return user;
        }).get();
    }
}
