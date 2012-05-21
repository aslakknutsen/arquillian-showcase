/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.jpa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
public class Game implements Serializable {

    private static final long serialVersionUID = 2794223977275773955L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    private String title;

    @NotNull
    private BigDecimal price;

    @NotNull
    @Min(1960)
    @Column(name = "YEAR_RELEASED")
    private Integer year;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Platform> supportedPlatforms = new HashSet<Platform>();

    Game() {
        // To satisfy JPA
    }

    public Game(String title, Integer year, BigDecimal price) {
        this.title = title;
        this.year = year;
        this.price = price;
    }

    public void addPlatform(Platform platform) {
        if (supportedPlatforms.contains(platform)) {
            return;
        }
        supportedPlatforms.add(platform);
        platform.addGames(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Game)) {
            return false;
        }

        final Game other = (Game) obj;

        return new EqualsBuilder().append(title, other.getTitle())
                .append(year, other.getYear()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 17).append(title).append(year)
                .toHashCode();
    }

    public String toString() {
        return "Game@" + hashCode() + "[id = " + id + "; title = " + title
                + "; price = " + price + "]";
    }

    // Accessors

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Set<Platform> getSupportedPlatforms() {
        return Collections.unmodifiableSet(supportedPlatforms);
    }

    public void setSupportedPlatforms(Set<Platform> supportedPlatforms) {
        this.supportedPlatforms = supportedPlatforms;
    }

}
