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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "enrollment", schema = "public")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "enrollment_id_gen")
    @SequenceGenerator(name = "enrollment_id_gen", sequenceName = "enrollment_enrollment_id_seq", allocationSize = 1)
    @Column(name = "enrollment_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private ee.taltech.testify.Course course;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private ee.taltech.testify.AppUser student;

}