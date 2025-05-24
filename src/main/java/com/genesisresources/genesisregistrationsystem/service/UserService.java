package com.genesisresources.genesisregistrationsystem.service;

import com.genesisresources.genesisregistrationsystem.exception.PersonIdAlreadyExistsException;
import com.genesisresources.genesisregistrationsystem.exception.UserNotFoundException;
import com.genesisresources.genesisregistrationsystem.model.User;
import com.genesisresources.genesisregistrationsystem.repository.UserRepository;
import com.genesisresources.genesisregistrationsystem.request.UserRequest;
import com.genesisresources.genesisregistrationsystem.response.UserResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Set<String> validPersonIds = new HashSet<>();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("personID.txt").getInputStream(),
                        StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                validPersonIds.add(line.trim());
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot load personID.txt", e);
        }
    }

    private boolean isPersonIdValid(String personId) {
        return validPersonIds.contains(personId);
    }

    @Transactional
    public UserResponse createUser(UserRequest request) {

        if (!isPersonIdValid(request.getPersonId())) {
            throw new IllegalArgumentException("Invalid personID!");
        }

        if (userRepository.findByPersonId(request.getPersonId()).isPresent()) {
            throw new PersonIdAlreadyExistsException("PersonID already exists.");
        }

        String uuid;
        do {
            uuid = UUID.randomUUID().toString();
        } while (userRepository.findByUuid(uuid).isPresent());

        User user = new User();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setPersonId(request.getPersonId());
        user.setUuid(uuid);

        User saved = userRepository.save(user);
        return mapToResponse(saved, true);
    }

    public UserResponse getUserById(Long id, boolean detail) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return mapToResponse(user, detail);
    }

    public List<UserResponse> getAllUsers(boolean detail) {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> mapToResponse(user, detail))
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse updateUser(UserRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getId()));

        user.setName(request.getName());
        user.setSurname(request.getSurname());

        User updated = userRepository.save(user);
        return mapToResponse(updated, true);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserResponse mapToResponse(User user, boolean detail) {
        if (detail) {
            return new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getSurname(),
                    user.getPersonId(),
                    user.getUuid());
        } else {
            return new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getSurname(),
                    null,
                    null);
        }
    }
}