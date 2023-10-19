package com.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Comparator;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = {"latitude", "longitude"})
@ToString(of = {"name", "country", "state"})
@Entity
@Table(name = "locations")
public class Location{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonProperty("lat")
    @Column(precision = 9, scale = 7)
    private BigDecimal latitude;

    @JsonProperty("lon")
    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @Transient
    private String country;
    @Transient
    private String state;

    public Location(Long locationId) {
        this.id = locationId;
    }
}
