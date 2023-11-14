package com.weather.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@ToString(of = "login")
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.REMOVE)
    @Setter(AccessLevel.PRIVATE)
    private List<Location> locations = new ArrayList<>();

    public void addLocation(Location location) {
        locations.add(location);
        location.setUser(this);
    }

    public void removeLocation(Location location) {
        locations.remove(location);
        location.setUser(null);
    }
}
