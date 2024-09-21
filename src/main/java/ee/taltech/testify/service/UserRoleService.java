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
import ee.taltech.testify.exception.UserRoleAlreadyExistsException;
import ee.taltech.testify.exception.UserRoleNotFoundException;
import ee.taltech.testify.mapper.UserRoleMapper;
import ee.taltech.testify.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;
    private final UserRoleMapper userRoleMapper;
    private static final String NO_USER_ROLE_WITH_ID = "No user role with id: ";

    /**
     * Retrieves the user role corresponding to the specified ID.
     *
     * @param id the ID of the user role to be retrieved
     * @return the user role data transfer object (DTO) containing the user role details
     * @throws UserRoleNotFoundException if no user role is found for the given ID
     */
    public UserRoleResponseDto getUserRoleById(Integer id) {
        return userRoleMapper.toDto(userRoleRepository.findById(id).orElseThrow(
                () -> new UserRoleNotFoundException(NO_USER_ROLE_WITH_ID + id)
        ));
    }

    /**
     * Retrieves the user role corresponding to the specified name.
     *
     * @param name the name of the user role to be retrieved
     * @return the user role data transfer object (DTO) containing the user role details
     * @throws UserRoleNotFoundException if no user role is found for the given name
     */
    public UserRoleResponseDto getUserRoleByName(String name) {
        return userRoleMapper.toDto(userRoleRepository.findByUserRoleName(name).orElseThrow(
                () -> new UserRoleNotFoundException("No user role with name: " + name)
        ));
    }

    /**
     * Retrieves a list of all user roles.
     *
     * @return a list of UserRoleResponseDto containing the details of all user roles
     */
    public List<UserRoleResponseDto> getUserRoles() {
        return userRoleMapper.toDtoList(userRoleRepository.findAll());
    }

    /**
     * Creates a new user role.
     *
     * @param userRole the user role request data transfer object containing details of the user role to be created
     * @return the user role response data transfer object containing the created user role details
     */
    public UserRoleResponseDto createUserRole(UserRoleRequestDto userRole) {
        if (userRoleRepository.findByUserRoleName(userRole.getUserRoleName()).isPresent()) {
            throw new UserRoleAlreadyExistsException("User role with name " + userRole.getUserRoleName() +
                    " already exists");
        }
        return userRoleMapper.toDto(userRoleRepository.save(userRoleMapper.toEntity(userRole)));
    }

    /**
     * Updates an existing user role identified by the specified id with details provided in the userRole request DTO.
     *
     * @param id the ID of the user role to be updated
     * @param userRoleDto the user role request data transfer object containing updated user role details
     * @return the user role response data transfer object containing the updated user role details
     * @throws UserRoleNotFoundException if no user role is found for the given ID
     */
    public UserRoleResponseDto updateUserRole(Integer id, UserRoleRequestDto userRoleDto) {
        UserRole userRoleEntity = userRoleRepository.findById(id).orElseThrow(
                () -> new UserRoleNotFoundException(NO_USER_ROLE_WITH_ID + id)
        );

        Optional<UserRole> existingUserRole = userRoleRepository.findByUserRoleName(userRoleDto.getUserRoleName());
        if (existingUserRole.isPresent() && !existingUserRole.get().getId().equals(userRoleEntity.getId())) {
            throw new UserRoleAlreadyExistsException("User role with name " + userRoleDto.getUserRoleName() +
                    " already exists");
        }

        userRoleMapper.updateUserRoleFromDto(userRoleDto, userRoleEntity);

        return userRoleMapper.toDto(userRoleRepository.save(userRoleEntity));
    }

    /**
     * Deletes the user role identified by the specified ID.
     *
     * @param id the ID of the user role to be deleted
     * @throws UserRoleNotFoundException if no user role is found for the given ID
     */
    public void deleteUserRole(Integer id) {
        UserRole userRoleEntity = userRoleRepository.findById(id).orElseThrow(
                () -> new UserRoleNotFoundException(NO_USER_ROLE_WITH_ID + id)
        );

        userRoleRepository.delete(userRoleEntity);
    }

}
