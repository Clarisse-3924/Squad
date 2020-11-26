package Herosquard;

import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SquadTest {
    @Test
    public void squadInstatiation() throws Exception{
        Squad squad= setUpNewSquad();
        assertEquals(true,squad instanceof Squad);
    }

    @Test
    public void returnAllInstances_true() throws Exception {
        Squad squad=setUpNewSquad();
        Squad otherSquad=setUpNewSquad();
        assertEquals(2,Squad.getSquads().size());
    }

    @Test
    public void allSquadsContainedInSquad() throws Exception {
        Squad squad=setUpNewSquad();
        Squad otherSquad=setUpNewSquad();
        assertTrue(Squad.getSquads().contains(squad));
        assertTrue(Squad.getSquads().contains(otherSquad));
    }

    @Test
    public void findById() throws Exception {
        Squad squad=setUpNewSquad();
        Squad otherSquad=setUpNewSquad();
        Squad foundSquad=Squad.findById(1);
        assertEquals(squad,foundSquad);
    }



    @Test
    public void deleteSquadByID() {
        Squad squad=setUpNewSquad();
        Squad otherSquad=setUpNewSquad();
        squad.deleteSquad();
        assertEquals(1,Squad.getSquads().size());
        assertEquals(Squad.getSquads().get(0).getId(),2);

    }
    @Test
    public void deleteAllSquads() {
        Squad squad=setUpNewSquad();
        Squad otherSquad=setUpNewSquad();
        Squad.clearAll();
        assertEquals(0,Squad.getSquads().size());

    }

    //helper
    private Squad setUpNewSquad() {
        ArrayList<Hero> heroes=new ArrayList<Hero>();
        Hero hero=new Hero("Absorbing Man",30,"Absorbing ","Can Absorb evil thought",20,60);
        Hero otherHero=new Hero("Abraxas",60,"Read Minds ","Gets tired fast",20,60);
        heroes.add(hero);
        heroes.add(otherHero);
        return new Squad(10,"queen","sexism",heroes);
    }
}

