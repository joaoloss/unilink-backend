package com.unilink.api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unilink.api.dtos.UserRequestDTO;
import com.unilink.api.exception.InvalidFieldException;
import com.unilink.api.model.User;
import com.unilink.api.service.UserService;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints for user management")
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(
        summary = "List all users",
        description = "Returns a list of all users registered in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of users returned successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class)
            )
        )
    })
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get user by ID",
        description = "Returns a specific user based on the provided ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    })
    public ResponseEntity<User> getUserById(
        @Parameter(description = "Unique ID of the user", required = true)
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok(this.userService.getUserById(id));
    }

    @PostMapping
    @Operation(
        summary = "Create new user",
        description = "Creates a new user in the system with the provided data"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid data provided"
        )
    })
    public ResponseEntity<User> createUser(
        @RequestBody 
        @Schema(description = "User data for creation", requiredMode = Schema.RequiredMode.REQUIRED)
        UserRequestDTO userRequest
    ) {
        if(!userRequest.isValidForCreation()) {
            throw new InvalidFieldException("Invalid user data provided for creation.");
        }
        
        return ResponseEntity.ok(this.userService.createUser(userRequest));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update user",
        description = "Updates the data of an existing user based on the provided ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    })
    public ResponseEntity<User> updateUser(
        @Parameter(description = "Unique ID of the user", required = true)
        @PathVariable UUID id, 
        @RequestBody 
        @Schema(description = "Updated user data", requiredMode = Schema.RequiredMode.REQUIRED)
        UserRequestDTO updatedUser
    ) {
        return ResponseEntity.ok(this.userService.updateUser(id, updatedUser));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete user",
        description = "Removes a user from the system based on the provided ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "User deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    })
    public ResponseEntity<Void> deleteUser(
        @Parameter(description = "Unique ID of the user", required = true)
        @PathVariable UUID id
    ) {
        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
