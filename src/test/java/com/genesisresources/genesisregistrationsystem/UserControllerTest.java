package com.genesisresources.genesisregistrationsystem;

import com.genesisresources.genesisregistrationsystem.controller.UserController;
import com.genesisresources.genesisregistrationsystem.request.UserRequest;
import com.genesisresources.genesisregistrationsystem.response.UserResponse;
import com.genesisresources.genesisregistrationsystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    public UserControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_shouldReturnCreatedUser() {
        UserRequest request = new UserRequest();
        request.setName("Jack");
        request.setSurname("Smith");
        request.setPersonId("jXa4g3H7oPq2");

        UserResponse response = new UserResponse();
        response.setName("Jack");
        response.setSurname("Smith");
        response.setPersonId("jXa4g3H7oPq2");

        when(userService.createUser(any(UserRequest.class))).thenReturn(response);

        ResponseEntity<UserResponse> result = userController.createUser(request);

        assertEquals(201, result.getStatusCode().value());
        assertNotNull(result.getBody());
        UserResponse body = result.getBody();
        assertNotNull(body);
        assertEquals("Jack", body.getName());
    }
}
