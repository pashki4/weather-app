package com.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.weather.dto.WeatherData;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = {"id"})
@ToString(of = "name")
@Builder
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonProperty("lat")
    @Column(precision = 9, scale = 7, nullable = false)
    private BigDecimal latitude;

    @JsonProperty("lon")
    @Column(precision = 10, scale = 7, nullable = false)
    private BigDecimal longitude;

    @Transient
    private String country;

    @Transient
    private WeatherData weatherData;

    public Location(Long locationId) {
        this.id = locationId;
    }
}
