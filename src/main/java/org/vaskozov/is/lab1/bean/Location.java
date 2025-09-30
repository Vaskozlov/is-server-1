package org.vaskozov.is.lab1.bean;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.persistence.*;
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
    private Double x;

    @NotNull
    private Double y;

    @NotNull
    @Size(max=409)
    private String name;
}
