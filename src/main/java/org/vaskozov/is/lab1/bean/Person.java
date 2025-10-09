package org.vaskozov.is.lab1.bean;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "PERSON")
public class Person {
    @Id
    @Positive
    @JsonbNillable
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @JsonbNillable
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinates coordinates;

    @NotNull
    @JsonbNillable
    @CreationTimestamp
    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime creationTime;

    @NotNull
    @JsonbNillable
    private Color eyeColor;

    @NotNull
    @JsonbNillable
    private Color hairColor;

    @NotNull
    @JsonbNillable
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "location_id")
    private Location location;

    @Positive
    @NotNull
    @JsonbNillable
    private Double height;

    @NotNull
    @Positive
    @JsonbNillable
    private Float weight;

    @Nullable
    @JsonbNillable
    private Country nationality;
}
