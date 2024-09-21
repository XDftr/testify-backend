/*
 * This file is part of Testify.
 *
 * Testify is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Testify is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Testify.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2024 Deniel Konstantinov.
 */

package ee.taltech.testify.repository;

import ee.taltech.testify.entity.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import jakarta.validation.ConstraintViolationException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class UserRoleRepositoryTest {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Nested
    @DisplayName("CRUD Operations")
    class CRUDOperations {

        @Test
        @DisplayName("Should save a valid UserRole")
        void shouldSaveValidUserRole() {
            // Given
            UserRole role = new UserRole();
            role.setUserRoleName("Instructor");

            // When
            UserRole savedRole = userRoleRepository.save(role);

            // Then
            assertThat(savedRole.getId()).isNotNull();
            assertThat(savedRole.getUserRoleName()).isEqualTo("Instructor");
        }

        @Test
        @DisplayName("Should find UserRole by name")
        void shouldFindByUserRoleName() {
            // Given
            UserRole role = new UserRole();
            role.setUserRoleName("Instructor");
            userRoleRepository.save(role);

            // When
            Optional<UserRole> foundRole = userRoleRepository.findByUserRoleName("Instructor");

            // Then
            assertThat(foundRole).isPresent();
            assertThat(foundRole.get().getUserRoleName()).isEqualTo("Instructor");
        }

        @Test
        @DisplayName("Should delete a UserRole")
        void shouldDeleteUserRole() {
            // Given
            UserRole role = new UserRole();
            role.setUserRoleName("Instructor");
            UserRole savedRole = userRoleRepository.save(role);

            // When
            userRoleRepository.delete(savedRole);

            // Then
            Optional<UserRole> deletedRole = userRoleRepository.findById(savedRole.getId());
            assertThat(deletedRole).isNotPresent();
        }
    }

    @Nested
    @DisplayName("Validation Constraints")
    class ValidationConstraints {

        @Test
        @DisplayName("Should throw ConstraintViolationException when userRoleName is null")
        void shouldThrowExceptionWhenUserRoleNameIsNull() {
            // Given
            UserRole role = new UserRole();
            role.setUserRoleName(null);

            // When/Then
            assertThatThrownBy(() -> userRoleRepository.saveAndFlush(role))
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("must not be null");
        }

        @Test
        @DisplayName("Should throw ConstraintViolationException when userRoleName exceeds max size")
        void shouldThrowExceptionWhenUserRoleNameExceedsMaxSize() {
            // Given
            UserRole role = new UserRole();
            role.setUserRoleName("A".repeat(51)); // 51 characters

            // When/Then
            assertThatThrownBy(() -> userRoleRepository.saveAndFlush(role))
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("size must be between 1 and 50");
        }
    }

    @Nested
    @DisplayName("Unique Constraints")
    class UniqueConstraints {

        @Test
        @DisplayName("Should enforce unique userRoleName")
        void shouldEnforceUniqueUserRoleName() {
            // Given
            UserRole role1 = new UserRole();
            role1.setUserRoleName("Moderator");
            userRoleRepository.save(role1);

            UserRole role2 = new UserRole();
            role2.setUserRoleName("Moderator");

            // When/Then
            assertThatThrownBy(() -> userRoleRepository.saveAndFlush(role2))
                    .isInstanceOf(DataIntegrityViolationException.class)
                    .hasCauseInstanceOf(org.hibernate.exception.ConstraintViolationException.class)
                    .hasMessageContaining("UK_USER_ROLE_USER_ROLE_NAME"); // Replace with your actual constraint name if different
        }
    }
}
