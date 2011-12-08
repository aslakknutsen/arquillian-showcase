package com.acme.jpa;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.TransactionMode;
import org.jboss.arquillian.persistence.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class GamePersistenceTestCase {

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
                         .addPackage(Game.class.getPackage())
                         // required for remote containers in order to run tests with FEST-Asserts
                         .addPackages(true, "org.fest")
                         .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                         .addAsManifestResource("test-persistence.xml", "persistence.xml");
    }

    @PersistenceContext
    EntityManager em;

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void shouldPersistsPcGames() throws Exception {
        // given
        Game batman = new Game("Batman Arkham Asylum", 2009,
                BigDecimal.valueOf(17.0));
        Game baldurs = new Game("Baldur's Gate", 1998, BigDecimal.valueOf(9.0));
        Platform pc = new Platform("PC");
        pc.addGames(batman, baldurs);

        // when
        em.persist(batman);
        em.persist(baldurs);
        em.flush();
        em.clear();
        @SuppressWarnings("unchecked")
        List<Game> games = em.createQuery(selectAllInJPQL(Game.class))
                .getResultList();

        // then
        assertThat(games).hasSize(2);
    }

    @Test
    @Transactional
    // COMMIT after test execution is default transaction behavior
    public void shouldPersistsPcPlatformWithGames() throws Exception {
        // given
        Game batman = new Game("Batman Arkham Asylum", 2009,
                BigDecimal.valueOf(17.0));
        Game baldurs = new Game("Baldur's Gate", 1998, BigDecimal.valueOf(9.0));
        Platform pc = new Platform("PC");
        pc.addGames(batman, baldurs);

        // when
        em.persist(pc);
        em.flush();
        em.clear();
        @SuppressWarnings("unchecked")
        List<Platform> platforms = em.createQuery(
                selectAllInJPQL(Platform.class)).getResultList();

        // then
        assertThat(platforms).hasSize(1);
        assertThat(platforms.get(0).getGames()).hasSize(2).contains(batman,
                baldurs);
    }

    @Test
    @ShouldMatchDataSet("datasets/pc-games.yml")
    public void shouldPersistsPcGamesAndVerifiesStateAfterTestExecution()
            throws Exception {
        // given
        Game batman = new Game("Batman Arkham Asylum", 2009,
                BigDecimal.valueOf(17.0));
        Game baldurs = new Game("Baldur's Gate", 1998, BigDecimal.valueOf(9.0));
        Platform pc = new Platform("PC");
        pc.addGames(batman, baldurs);

        // when
        em.persist(pc);

        // then
        // state is verified using @ShouldMatchDataSet feature
    }

    // Private helper methods

    private String selectAllInJPQL(Class<?> clazz) {
        return "SELECT entity FROM " + clazz.getSimpleName() + " entity";
    }
}
