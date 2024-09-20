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
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "grading_element", schema = "public")
public class GradingElement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grading_element_id_gen")
    @SequenceGenerator(name = "grading_element_id_gen", sequenceName = "grading_element_grading_element_id_seq", allocationSize = 1)
    @Column(name = "grading_element_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assignment_id", nullable = false)
    private ee.taltech.testify.Assignment assignment;

    @Size(max = 255)
    @NotNull
    @Column(name = "element_name", nullable = false)
    private String elementName;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @NotNull
    @Column(name = "max_points", nullable = false, precision = 5, scale = 2)
    private BigDecimal maxPoints;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_deduction", nullable = false)
    private Boolean isDeduction = false;

}