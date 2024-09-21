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

package ee.taltech.testify.service;

import ee.taltech.testify.dto.UserRoleRequestDto;
import ee.taltech.testify.dto.UserRoleResponseDto;
import ee.taltech.testify.entity.UserRole;
import ee.taltech.testify.exception.UserRoleNotFoundException;
import ee.taltech.testify.mapper.UserRoleMapper;
import ee.taltech.testify.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserRoleServiceTest {

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserRoleMapper userRoleMapper;

    @InjectMocks
    private UserRoleService userRoleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("getUserRoleById Tests")
    class GetUserRoleByIdTests {

        @Test
        @DisplayName("Should return UserRoleResponseDto when UserRole is found")
        void shouldReturnUserRoleResponseDtoWhenFound() {
            // Given
            Integer roleId = 1;
            UserRole userRole = new UserRole();
            userRole.setId(roleId);
            userRole.setUserRoleName("Admin");

            UserRoleResponseDto responseDto = new UserRoleResponseDto();
            responseDto.setId(roleId);
            responseDto.setUserRoleName("Admin");

            when(userRoleRepository.findById(roleId)).thenReturn(Optional.of(userRole));
            when(userRoleMapper.toDto(userRole)).thenReturn(responseDto);

            // When
            UserRoleResponseDto result = userRoleService.getUserRoleById(roleId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(roleId);
            assertThat(result.getUserRoleName()).isEqualTo("Admin");

            verify(userRoleRepository, times(1)).findById(roleId);
            verify(userRoleMapper, times(1)).toDto(userRole);
        }

        @Test
        @DisplayName("Should throw UserRoleNotFoundException when UserRole is not found")
        void shouldThrowExceptionWhenUserRoleNotFound() {
            // Given
            Integer roleId = 999;
            when(userRoleRepository.findById(roleId)).thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> userRoleService.getUserRoleById(roleId))
                    .isInstanceOf(UserRoleNotFoundException.class)
                    .hasMessageContaining("No user role with id: " + roleId);

            verify(userRoleRepository, times(1)).findById(roleId);
            verify(userRoleMapper, times(0)).toDto(any());
        }
    }

    @Nested
    @DisplayName("getUserRoleByName Tests")
    class GetUserRoleByNameTests {

        @Test
        @DisplayName("Should return UserRoleResponseDto when UserRole is found by name")
        void shouldReturnUserRoleResponseDtoWhenFoundByName() {
            // Given
            String roleName = "Teacher";
            UserRole userRole = new UserRole();
            userRole.setId(2);
            userRole.setUserRoleName(roleName);

            UserRoleResponseDto responseDto = new UserRoleResponseDto();
            responseDto.setId(2);
            responseDto.setUserRoleName(roleName);

            when(userRoleRepository.findByUserRoleName(roleName)).thenReturn(Optional.of(userRole));
            when(userRoleMapper.toDto(userRole)).thenReturn(responseDto);

            // When
            UserRoleResponseDto result = userRoleService.getUserRoleByName(roleName);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(2);
            assertThat(result.getUserRoleName()).isEqualTo(roleName);

            verify(userRoleRepository, times(1)).findByUserRoleName(roleName);
            verify(userRoleMapper, times(1)).toDto(userRole);
        }

        @Test
        @DisplayName("Should throw UserRoleNotFoundException when UserRole is not found by name")
        void shouldThrowExceptionWhenUserRoleNotFoundByName() {
            // Given
            String roleName = "NonExistentRole";
            when(userRoleRepository.findByUserRoleName(roleName)).thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> userRoleService.getUserRoleByName(roleName))
                    .isInstanceOf(UserRoleNotFoundException.class)
                    .hasMessageContaining("No user role with name: " + roleName);

            verify(userRoleRepository, times(1)).findByUserRoleName(roleName);
            verify(userRoleMapper, times(0)).toDto(any());
        }
    }

    @Nested
    @DisplayName("getUserRoles Tests")
    class GetUserRolesTests {

        @Test
        @DisplayName("Should return list of UserRoleResponseDto when UserRoles exist")
        void shouldReturnListOfUserRoleResponseDto() {
            // Given
            UserRole adminRole = new UserRole();
            adminRole.setId(1);
            adminRole.setUserRoleName("Admin");

            UserRole teacherRole = new UserRole();
            teacherRole.setId(2);
            teacherRole.setUserRoleName("Teacher");

            List<UserRole> userRoles = Arrays.asList(adminRole, teacherRole);

            UserRoleResponseDto adminDto = new UserRoleResponseDto();
            adminDto.setId(1);
            adminDto.setUserRoleName("Admin");

            UserRoleResponseDto teacherDto = new UserRoleResponseDto();
            teacherDto.setId(2);
            teacherDto.setUserRoleName("Teacher");

            List<UserRoleResponseDto> responseDtos = Arrays.asList(adminDto, teacherDto);

            when(userRoleRepository.findAll()).thenReturn(userRoles);
            when(userRoleMapper.toDtoList(userRoles)).thenReturn(responseDtos);

            // When
            List<UserRoleResponseDto> result = userRoleService.getUserRoles();

            // Then
            assertThat(result).isNotNull().hasSize(2).containsExactly(adminDto, teacherDto);

            verify(userRoleRepository, times(1)).findAll();
            verify(userRoleMapper, times(1)).toDtoList(userRoles);
        }

        @Test
        @DisplayName("Should return empty list when no UserRoles exist")
        void shouldReturnEmptyListWhenNoUserRoles() {
            // Given
            when(userRoleRepository.findAll()).thenReturn(List.of());
            when(userRoleMapper.toDtoList(List.of())).thenReturn(List.of());

            // When
            List<UserRoleResponseDto> result = userRoleService.getUserRoles();

            // Then
            assertThat(result).isNotNull().isEmpty();

            verify(userRoleRepository, times(1)).findAll();
            verify(userRoleMapper, times(1)).toDtoList(List.of());
        }
    }

    @Nested
    @DisplayName("createUserRole Tests")
    class CreateUserRoleTests {

        @Test
        @DisplayName("Should create and return UserRoleResponseDto when valid UserRoleRequestDto is provided")
        void shouldCreateAndReturnUserRoleResponseDto() {
            // Given
            UserRoleRequestDto requestDto = new UserRoleRequestDto();
            requestDto.setUserRoleName("Student");

            UserRole userRole = new UserRole();
            userRole.setUserRoleName("Student");

            UserRole savedUserRole = new UserRole();
            savedUserRole.setId(3);
            savedUserRole.setUserRoleName("Student");

            UserRoleResponseDto responseDto = new UserRoleResponseDto();
            responseDto.setId(3);
            responseDto.setUserRoleName("Student");

            when(userRoleMapper.toEntity(requestDto)).thenReturn(userRole);
            when(userRoleRepository.save(userRole)).thenReturn(savedUserRole);
            when(userRoleMapper.toDto(savedUserRole)).thenReturn(responseDto);

            // When
            UserRoleResponseDto result = userRoleService.createUserRole(requestDto);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(3);
            assertThat(result.getUserRoleName()).isEqualTo("Student");

            verify(userRoleMapper, times(1)).toEntity(requestDto);
            verify(userRoleRepository, times(1)).save(userRole);
            verify(userRoleMapper, times(1)).toDto(savedUserRole);
        }
    }

    @Nested
    @DisplayName("updateUserRole Tests")
    class UpdateUserRoleTests {

        @Test
        @DisplayName("Should update and return UserRoleResponseDto when UserRole exists")
        void shouldUpdateAndReturnUserRoleResponseDto() {
            // Given
            Integer roleId = 1;
            UserRoleRequestDto requestDto = new UserRoleRequestDto();
            requestDto.setUserRoleName("SuperAdmin");

            UserRole existingUserRole = new UserRole();
            existingUserRole.setId(roleId);
            existingUserRole.setUserRoleName("Admin");

            UserRole updatedUserRole = new UserRole();
            updatedUserRole.setId(roleId);
            updatedUserRole.setUserRoleName("SuperAdmin");

            UserRoleResponseDto responseDto = new UserRoleResponseDto();
            responseDto.setId(roleId);
            responseDto.setUserRoleName("SuperAdmin");

            when(userRoleRepository.findById(roleId)).thenReturn(Optional.of(existingUserRole));
            doNothing().when(userRoleMapper).updateUserRoleFromDto(requestDto, existingUserRole);
            when(userRoleRepository.save(existingUserRole)).thenReturn(updatedUserRole);
            when(userRoleMapper.toDto(updatedUserRole)).thenReturn(responseDto);

            // When
            UserRoleResponseDto result = userRoleService.updateUserRole(roleId, requestDto);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(roleId);
            assertThat(result.getUserRoleName()).isEqualTo("SuperAdmin");

            verify(userRoleRepository, times(1)).findById(roleId);
            verify(userRoleMapper, times(1)).updateUserRoleFromDto(requestDto, existingUserRole);
            verify(userRoleRepository, times(1)).save(existingUserRole);
            verify(userRoleMapper, times(1)).toDto(updatedUserRole);
        }

        @Test
        @DisplayName("Should throw UserRoleNotFoundException when UserRole to update does not exist")
        void shouldThrowExceptionWhenUserRoleToUpdateNotFound() {
            // Given
            Integer roleId = 999;
            UserRoleRequestDto requestDto = new UserRoleRequestDto();
            requestDto.setUserRoleName("NonExistentRole");

            when(userRoleRepository.findById(roleId)).thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> userRoleService.updateUserRole(roleId, requestDto))
                    .isInstanceOf(UserRoleNotFoundException.class)
                    .hasMessageContaining("No user role with id: " + roleId);

            verify(userRoleRepository, times(1)).findById(roleId);
            verify(userRoleMapper, times(0)).updateUserRoleFromDto(any(), any());
            verify(userRoleRepository, times(0)).save(any());
            verify(userRoleMapper, times(0)).toDto(any());
        }
    }

    @Nested
    @DisplayName("deleteUserRole Tests")
    class DeleteUserRoleTests {

        @Test
        @DisplayName("Should delete UserRole when it exists")
        void shouldDeleteUserRoleWhenExists() {
            // Given
            Integer roleId = 2;
            UserRole existingUserRole = new UserRole();
            existingUserRole.setId(roleId);
            existingUserRole.setUserRoleName("Teacher");

            when(userRoleRepository.findById(roleId)).thenReturn(Optional.of(existingUserRole));
            doNothing().when(userRoleRepository).delete(existingUserRole);

            // When
            userRoleService.deleteUserRole(roleId);

            // Then
            verify(userRoleRepository, times(1)).findById(roleId);
            verify(userRoleRepository, times(1)).delete(existingUserRole);
        }

        @Test
        @DisplayName("Should throw UserRoleNotFoundException when trying to delete non-existent UserRole")
        void shouldThrowExceptionWhenDeletingNonExistentUserRole() {
            // Given
            Integer roleId = 999;
            when(userRoleRepository.findById(roleId)).thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> userRoleService.deleteUserRole(roleId))
                    .isInstanceOf(UserRoleNotFoundException.class)
                    .hasMessageContaining("No user role with id: " + roleId);

            verify(userRoleRepository, times(1)).findById(roleId);
            verify(userRoleRepository, times(0)).delete(any());
        }
    }
}
