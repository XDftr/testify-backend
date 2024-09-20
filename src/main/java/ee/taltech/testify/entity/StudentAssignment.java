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
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "student_assignment", schema = "public")
public class StudentAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_assignment_id_gen")
    @SequenceGenerator(name = "student_assignment_id_gen", sequenceName = "student_assignment_student_assignment_id_seq", allocationSize = 1)
    @Column(name = "student_assignment_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private ee.taltech.testify.AppUser student;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assignment_id", nullable = false)
    private ee.taltech.testify.Assignment assignment;

    @Column(name = "submission_url", length = Integer.MAX_VALUE)
    private String submissionUrl;

    @Column(name = "grade", precision = 5, scale = 2)
    private BigDecimal grade;

    @Column(name = "feedback", length = Integer.MAX_VALUE)
    private String feedback;

    @Column(name = "submission_date")
    private LocalDate submissionDate;

}