package com.genesisresources.genesisregistrationsystem;

import com.genesisresources.genesisregistrationsystem.request.UserRequest;
import com.genesisresources.genesisregistrationsystem.response.UserResponse;
import com.genesisresources.genesisregistrationsystem.repository.UserRepository;
import com.genesisresources.genesisregistrationsystem.model.User;
import com.genesisresources.genesisregistrationsystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_shouldReturnUserResponse() {
        UserRequest request = new UserRequest();
        request.setName("Jack");
        request.setSurname("Smith");
        request.setPersonId("abc123");

        User user = new User();
        user.setId(1L);
        user.setName("Jack");
        user.setSurname("Smith");
        user.setPersonId("abc123");

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.createUser(request);

        assertNotNull(response);
        assertEquals("Jack", response.getName());
        assertEquals("Smith", response.getSurname());
        assertEquals("abc123", response.getPersonId());
    }
}
