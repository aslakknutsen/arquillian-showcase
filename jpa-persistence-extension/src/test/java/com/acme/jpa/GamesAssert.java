package com.acme.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;

public class GamesAssert extends GenericAssert<GamesAssert, Collection<Game>> {

    private GamesAssert(Collection<Game> actual) {
        super(GamesAssert.class, actual);
    }

    public static GamesAssert assertThat(Collection<Game> games) {
        return new GamesAssert(games);
    }

    public GamesAssert hasSize(int expectedSize) {
        Assertions.assertThat(actual).hasSize(expectedSize);
        return this;
    }

    public GamesAssert containsTitles(String ... titles) {
        List<String> allTitles = extractTitles(actual);
        Assertions.assertThat(allTitles).contains(titles);
        return this;
    }

    // Private utility methods

    private List<String> extractTitles(Collection<Game> games) {
        List<String> titles = new ArrayList<String>();
        for (Game game : games) {
            titles.add(game.getTitle());
        }
        return titles;
    }

}
