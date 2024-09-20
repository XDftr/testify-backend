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

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "student_grading_element", schema = "public")
public class StudentGradingElement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_grading_element_id_gen")
    @SequenceGenerator(name = "student_grading_element_id_gen", sequenceName = "student_grading_element_student_grading_element_id_seq", allocationSize = 1)
    @Column(name = "student_grading_element_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_assignment_id", nullable = false)
    private ee.taltech.testify.StudentAssignment studentAssignment;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grading_element_id", nullable = false)
    private ee.taltech.testify.GradingElement gradingElement;

    @NotNull
    @Column(name = "points_awarded", nullable = false, precision = 5, scale = 2)
    private BigDecimal pointsAwarded;

    @Column(name = "feedback", length = Integer.MAX_VALUE)
    private String feedback;

}