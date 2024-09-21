package ee.taltech.testify.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.taltech.testify.dto.UserRoleRequestDto;
import ee.taltech.testify.entity.UserRole;
import ee.taltech.testify.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for UserRoleController.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("UserRoleController Integration Tests")
class UserRoleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Set up test data before each test case.
     */
    @BeforeEach
    void setUp() {
        userRoleRepository.deleteAll();

        UserRole adminRole = new UserRole();
        adminRole.setUserRoleName("Admin");
        userRoleRepository.save(adminRole);

        UserRole teacherRole = new UserRole();
        teacherRole.setUserRoleName("Teacher");
        userRoleRepository.save(teacherRole);
    }

    /**
     * Tests for GET /api/v1/admin/user-role/{id}
     */
    @Nested
    @DisplayName("GET /api/v1/admin/user-role/{id}")
    class GetUserRoleByIdTests {

        @Test
        @DisplayName("Should return UserRoleResponseDto when UserRole exists")
        void shouldReturnUserRoleWhenExists() throws Exception {
            // Given
            UserRole adminRole = userRoleRepository.findByUserRoleName("Admin").orElseThrow();
            Integer roleId = adminRole.getId();

            // When & Then
            mockMvc.perform(get("/api/v1/admin/user-role/{id}", roleId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(roleId)))
                    .andExpect(jsonPath("$.userRoleName", is("Admin")));
        }

        @Test
        @DisplayName("Should return 404 Not Found when UserRole does not exist")
        void shouldReturn404WhenUserRoleNotFound() throws Exception {
            // Given
            Integer nonExistentId = 999;

            // When & Then
            mockMvc.perform(get("/api/v1/admin/user-role/{id}", nonExistentId))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.message", containsString("No user role with id: " + nonExistentId)))
                    .andExpect(jsonPath("$.timestamp", notNullValue()));
        }
    }

    /**
     * Tests for GET /api/v1/admin/user-role
     */
    @Nested
    @DisplayName("GET /api/v1/admin/user-role")
    class GetAllUserRolesTests {

        @Test
        @DisplayName("Should return list of UserRoleResponseDto when UserRoles exist")
        void shouldReturnListOfUserRoles() throws Exception {
            // When & Then
            mockMvc.perform(get("/api/v1/admin/user-role"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].userRoleName", is("Admin")))
                    .andExpect(jsonPath("$[1].userRoleName", is("Teacher")));
        }

        @Test
        @DisplayName("Should return empty list when no UserRoles exist")
        void shouldReturnEmptyListWhenNoUserRoles() throws Exception {
            // Given
            userRoleRepository.deleteAll();

            // When & Then
            mockMvc.perform(get("/api/v1/admin/user-role"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    /**
     * Tests for POST /api/v1/admin/user-role
     */
    @Nested
    @DisplayName("POST /api/v1/admin/user-role")
    class CreateUserRoleTests {

        @Test
        @DisplayName("Should create a new UserRole and return UserRoleResponseDto")
        void shouldCreateNewUserRole() throws Exception {
            // Given
            UserRoleRequestDto requestDto = new UserRoleRequestDto();
            requestDto.setUserRoleName("Student");

            // When & Then
            mockMvc.perform(post("/api/v1/admin/user-role")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", notNullValue()))
                    .andExpect(jsonPath("$.userRoleName", is("Student")));

            // Verify that the role was actually created in the repository
            UserRole createdRole = userRoleRepository.findByUserRoleName("Student").orElseThrow();
            assertThat(createdRole.getUserRoleName()).isEqualTo("Student");
        }

        @Test
        @DisplayName("Should return 400 Bad Request when userRoleName is null")
        void shouldReturn400WhenUserRoleNameIsNull() throws Exception {
            // Given
            UserRoleRequestDto requestDto = new UserRoleRequestDto();
            requestDto.setUserRoleName(null); // Invalid: @NotNull

            // When & Then
            mockMvc.perform(post("/api/v1/admin/user-role")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.errors", hasSize(2)))
                    .andExpect(jsonPath("$.errors", containsInAnyOrder("User role must not be null",
                            "User role must not be empty")))
                    .andExpect(jsonPath("$.timestamp", notNullValue()));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when userRoleName exceeds max size")
        void shouldReturn400WhenUserRoleNameExceedsMaxSize() throws Exception {
            // Given
            UserRoleRequestDto requestDto = new UserRoleRequestDto();
            requestDto.setUserRoleName("A".repeat(51)); // Exceeds @Size(max = 50)

            // When & Then
            mockMvc.perform(post("/api/v1/admin/user-role")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.errors[0]", containsString("size must be between")))
                    .andExpect(jsonPath("$.timestamp", notNullValue()));
        }

        @Test
        @DisplayName("Should return 409 Conflict when userRoleName already exists")
        void shouldReturn409WhenUserRoleNameAlreadyExists() throws Exception {
            // Given
            UserRoleRequestDto requestDto = new UserRoleRequestDto();
            requestDto.setUserRoleName("Admin"); // Already exists

            // When & Then
            mockMvc.perform(post("/api/v1/admin/user-role")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isConflict())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(409)))
                    .andExpect(jsonPath("$.message", containsString("User role with name " +
                            requestDto.getUserRoleName() + " already exists")))
                    .andExpect(jsonPath("$.timestamp", notNullValue()));
        }
    }

    /**
     * Tests for PUT /api/v1/admin/user-role/{id}
     */
    @Nested
    @DisplayName("PUT /api/v1/admin/user-role/{id}")
    class UpdateUserRoleTests {

        @Test
        @DisplayName("Should update existing UserRole and return UserRoleResponseDto")
        void shouldUpdateExistingUserRole() throws Exception {
            // Given
            UserRole existingRole = userRoleRepository.findByUserRoleName("Teacher").orElseThrow();
            Integer roleId = existingRole.getId();

            UserRoleRequestDto requestDto = new UserRoleRequestDto();
            requestDto.setUserRoleName("Senior Teacher");

            // When & Then
            mockMvc.perform(put("/api/v1/admin/user-role/{id}", roleId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(roleId)))
                    .andExpect(jsonPath("$.userRoleName", is("Senior Teacher")));

            // Verify that the role was actually updated in the repository
            UserRole updatedRole = userRoleRepository.findById(roleId).orElseThrow();
            assertThat(updatedRole.getUserRoleName()).isEqualTo("Senior Teacher");
        }

        @Test
        @DisplayName("Should return 404 Not Found when updating non-existent UserRole")
        void shouldReturn404WhenUpdatingNonExistentUserRole() throws Exception {
            // Given
            Integer nonExistentId = 999;
            UserRoleRequestDto requestDto = new UserRoleRequestDto();
            requestDto.setUserRoleName("NonExistentRole");

            // When & Then
            mockMvc.perform(put("/api/v1/admin/user-role/{id}", nonExistentId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.message", containsString("No user role with id: " + nonExistentId)))
                    .andExpect(jsonPath("$.timestamp", notNullValue()));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when userRoleName is invalid during update")
        void shouldReturn400WhenUserRoleNameIsInvalidDuringUpdate() throws Exception {
            // Given
            UserRole existingRole = userRoleRepository.findByUserRoleName("Teacher").orElseThrow();
            Integer roleId = existingRole.getId();

            UserRoleRequestDto requestDto = new UserRoleRequestDto();
            requestDto.setUserRoleName("B".repeat(51)); // Exceeds @Size(max = 50)

            // When & Then
            mockMvc.perform(put("/api/v1/admin/user-role/{id}", roleId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.errors[0]", containsString("size must be between")))
                    .andExpect(jsonPath("$.timestamp", notNullValue()));
        }

        @Test
        @DisplayName("Should return 409 Conflict when updating UserRole to a name that already exists")
        void shouldReturn409WhenUpdatingToExistingUserRoleName() throws Exception {
            // Given
            UserRole existingRole = userRoleRepository.findByUserRoleName("Teacher").orElseThrow();
            Integer roleId = existingRole.getId();

            UserRoleRequestDto requestDto = new UserRoleRequestDto();
            requestDto.setUserRoleName("Admin"); // "Admin" already exists

            // When & Then
            mockMvc.perform(put("/api/v1/admin/user-role/{id}", roleId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isConflict())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(409)))
                    .andExpect(jsonPath("$.message", containsString("User role with name " +
                            requestDto.getUserRoleName() + " already exists")))
                    .andExpect(jsonPath("$.timestamp", notNullValue()));
        }
    }

    /**
     * Tests for DELETE /api/v1/admin/user-role/{id}
     */
    @Nested
    @DisplayName("DELETE /api/v1/admin/user-role/{id}")
    class DeleteUserRoleTests {

        @Test
        @DisplayName("Should delete existing UserRole and return 204 No Content")
        void shouldDeleteExistingUserRole() throws Exception {
            // Given
            UserRole existingRole = userRoleRepository.findByUserRoleName("Teacher").orElseThrow();
            Integer roleId = existingRole.getId();

            // When & Then
            mockMvc.perform(delete("/api/v1/admin/user-role/{id}", roleId))
                    .andExpect(status().isNoContent());

            // Verify that the role was actually deleted from the repository
            boolean exists = userRoleRepository.existsById(roleId);
            assertThat(exists).isFalse();
        }

        @Test
        @DisplayName("Should return 404 Not Found when deleting non-existent UserRole")
        void shouldReturn404WhenDeletingNonExistentUserRole() throws Exception {
            // Given
            Integer nonExistentId = 999;

            // When & Then
            mockMvc.perform(delete("/api/v1/admin/user-role/{id}", nonExistentId))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.message", containsString("No user role with id: " + nonExistentId)))
                    .andExpect(jsonPath("$.timestamp", notNullValue()));
        }
    }
}
