package com.acme.jpa;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class GameRepositoryTestCase {

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
                         .addPackage(Game.class.getPackage())
                         // required for remote containers in order to run tests with FEST-Asserts
                         .addPackages(true, "org.fest")
                         .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                         .addAsManifestResource("test-persistence.xml", "persistence.xml");
    }

    @Inject
    GameRepository gameRepository;

    @Test
    @UsingDataSet("games.yml")
    // It's not needed to provide folder name if it's default one
    public void shouldFindAllGames() throws Exception {
        // when
        List<Game> allGames = gameRepository.fetchAll();

        // then
        assertThat(allGames).hasSize(3);
    }

    @Test
    @UsingDataSet({ "games.yml", "platforms.yml" })
    public void shouldFindAllGamesForPc() throws Exception {
        // given
        Platform pc = gameRepository.getPlatform("PC");
        // when
        List<Game> gamesForPc = gameRepository.fetchAllFor(pc);

        // then
        GamesAssert.assertThat(gamesForPc).hasSize(2)
                                          .containsTitles("Batman Arkham Asylum", "Baldur's Gate");
    }

    @Test
    @UsingDataSet({ "games.yml", "platforms.yml" })
    public void shouldFindAllGamesForXbox360() throws Exception {
        // given
        Platform pc = gameRepository.getPlatform("XBOX 360");
        // when
        List<Game> gamesForXBox = gameRepository.fetchAllFor(pc);

        // then
        GamesAssert.assertThat(gamesForXBox).hasSize(1)
                                            .containsTitles("Batman Arkham Asylum");
    }

    @Test
    @UsingDataSet({ "games.yml", "platforms.yml" })
    public void shouldFindOldestGame() throws Exception {
        // given
        int expectedYear = 1998;

        // when
        List<Game> oldestGames = gameRepository.findOldestGames();

        // then
        GamesAssert.assertThat(oldestGames).hasSize(1)
                                           .containsTitles("Baldur's Gate");
        assertThat(oldestGames.get(0).getYear()).isEqualTo(expectedYear);
    }

}
