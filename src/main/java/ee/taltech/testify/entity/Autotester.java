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

package ee.taltech.testify.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "autotester", schema = "public")
public class Autotester {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "autotester_id_gen")
    @SequenceGenerator(name = "autotester_id_gen", sequenceName = "autotester_autotester_id_seq", allocationSize = 1)
    @Column(name = "autotester_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "student_gitlab_url", nullable = false, length = Integer.MAX_VALUE)
    private String studentGitlabUrl;

    @NotNull
    @Column(name = "tests_gitlab_url", nullable = false, length = Integer.MAX_VALUE)
    private String testsGitlabUrl;

    @NotNull
    @Column(name = "student_code_path", nullable = false, length = Integer.MAX_VALUE)
    private String studentCodePath;

    @NotNull
    @Column(name = "test_code_path", nullable = false, length = Integer.MAX_VALUE)
    private String testCodePath;

    @NotNull
    @Column(name = "docker_image_url", nullable = false, length = Integer.MAX_VALUE)
    private String dockerImageUrl;

    @Column(name = "gitlab_token", length = Integer.MAX_VALUE)
    private String gitlabToken;

}