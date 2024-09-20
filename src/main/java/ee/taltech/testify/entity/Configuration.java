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
@Table(name = "configuration", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "uk_configuration_config_key", columnNames = {"config_key"})
})
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "configuration_id_gen")
    @SequenceGenerator(name = "configuration_id_gen", sequenceName = "configuration_config_id_seq", allocationSize = 1)
    @Column(name = "config_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "config_key", nullable = false)
    private String configKey;

    @Column(name = "config_value", length = Integer.MAX_VALUE)
    private String configValue;

}