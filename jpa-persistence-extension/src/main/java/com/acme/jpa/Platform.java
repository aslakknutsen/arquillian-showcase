package com.acme.jpa;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Platform implements Serializable {

    private static final long serialVersionUID = -8200813297577242768L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Game> games = new HashSet<Game>();

    Platform() {
        // To satisfy JPA
    }

    public Platform(String name) {
        this.name = name;
    }

    public void addGames(Game... games) {
        this.games.addAll(Arrays.asList(games));
        for (Game game : games) {
            game.addPlatform(this);
        }
    }

    // Accessors

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Game> getGames() {
        return Collections.unmodifiableSet(games);
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

}
