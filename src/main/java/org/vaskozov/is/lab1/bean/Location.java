package org.vaskozov.is.lab1.bean;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Location {
    @Id
    @JsonbNillable
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @JsonbNillable
    private Double x;

    @NotNull
    @JsonbNillable
    private Double y;

    @NotNull
    @JsonbNillable
    @Size(max = 409)
    private String name;
}
