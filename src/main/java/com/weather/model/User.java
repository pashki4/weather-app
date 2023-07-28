package com.weather.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Setter(AccessLevel.PRIVATE)
    private Set<Location> locations = new HashSet<>();

    public void addLocation(Location location) {
        locations.add(location);
        location.setUser(this);
    }

    public void removeLocation(Location location) {
        locations.remove(location);
        location.setUser(null);
    }
}
