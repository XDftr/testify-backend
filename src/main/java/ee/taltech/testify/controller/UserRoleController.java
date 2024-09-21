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

package ee.taltech.testify.controller;

import ee.taltech.testify.dto.UserRoleRequestDto;
import ee.taltech.testify.dto.UserRoleResponseDto;
import ee.taltech.testify.service.UserRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/user-role")
@RequiredArgsConstructor
public class UserRoleController {
    private final UserRoleService userRoleService;

    @GetMapping("/{id}")
    public ResponseEntity<UserRoleResponseDto> getUseRole(@PathVariable Integer id) {
        return ResponseEntity.ok(userRoleService.getUserRoleById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserRoleResponseDto>> getUserRoles() {
        return ResponseEntity.ok(userRoleService.getUserRoles());
    }

    @PostMapping
    public ResponseEntity<UserRoleResponseDto> createUserRole(@Valid @RequestBody UserRoleRequestDto dto) {
        return new ResponseEntity<>(userRoleService.createUserRole(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRoleResponseDto> updateUserRole(@PathVariable Integer id,
                                                              @Valid @RequestBody UserRoleRequestDto dto) {
        return ResponseEntity.ok(userRoleService.updateUserRole(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserRole(@PathVariable Integer id) {
        userRoleService.deleteUserRole(id);
        return ResponseEntity.noContent().build();
    }
}
