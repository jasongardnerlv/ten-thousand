/*
            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
                    Version 2, December 2004

 Copyright (C) 2015 Jason Gardner <jason.gardner.lv@gmail.com>

 Everyone is permitted to copy and distribute verbatim or modified
 copies of this license document, and changing it is allowed as long
 as the name is changed.

            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
   TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION

  0. You just DO WHAT THE FUCK YOU WANT TO.

*/
package freecake.tenthousand.states;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PlayerTurnStateTest {

    private PlayerTurnState playerTurnState;

    @BeforeClass
    public static void classSetup() throws Exception {

    }

    @Before
    public void init() throws Exception {
        playerTurnState = new PlayerTurnState();
    }

    @Test
    public void calculateScore1() throws Exception {
        //five of a kind
        playerTurnState.userDiceValues = new int[]{1,1,1,1,1};
        assertEquals(1000, playerTurnState.calculateScoreOfRolledDice());
        playerTurnState.userDiceValues = new int[]{2,2,2,2,2};
        assertEquals(2000, playerTurnState.calculateScoreOfRolledDice());
        playerTurnState.userDiceValues = new int[]{3,3,3,3,3};
        assertEquals(3000, playerTurnState.calculateScoreOfRolledDice());
        playerTurnState.userDiceValues = new int[]{4,4,4,4,4};
        assertEquals(4000, playerTurnState.calculateScoreOfRolledDice());
        playerTurnState.userDiceValues = new int[]{5,5,5,5,5};
        assertEquals(5000, playerTurnState.calculateScoreOfRolledDice());
        playerTurnState.userDiceValues = new int[]{6,6,6,6,6};
        assertEquals(6000, playerTurnState.calculateScoreOfRolledDice());
    }

    @Test
    public void calculateScore2() throws Exception {
        //straights
        playerTurnState.userDiceValues = new int[]{1,2,3,4,5};
        assertEquals(750, playerTurnState.calculateScoreOfRolledDice());
        playerTurnState.userDiceValues = new int[]{2,3,4,5,6};
        assertEquals(750, playerTurnState.calculateScoreOfRolledDice());
    }

    @Test
    public void calculateScore3() throws Exception {
        //three of a kind
        playerTurnState.userDiceValues = new int[]{6,6,2,2,2};
        assertEquals(200, playerTurnState.calculateScoreOfRolledDice());
        playerTurnState.userDiceValues = new int[]{2,1,1,2,1};
        assertEquals(1000, playerTurnState.calculateScoreOfRolledDice());
        playerTurnState.userDiceValues = new int[]{6,4,6,2,6};
        assertEquals(600, playerTurnState.calculateScoreOfRolledDice());
        playerTurnState.userDiceValues = new int[]{4,2,4,4,4};
        assertEquals(400, playerTurnState.calculateScoreOfRolledDice());
    }

    @Test
    public void calculateScore4() throws Exception {
        //1's and 5's
        playerTurnState.userDiceValues = new int[]{1,1,3,6,6};
        assertEquals(200, playerTurnState.calculateScoreOfRolledDice());
        playerTurnState.userDiceValues = new int[]{5,1,5,2,1};
        assertEquals(300, playerTurnState.calculateScoreOfRolledDice());
        playerTurnState.userDiceValues = new int[]{5,4,4,6,6};
        assertEquals(50, playerTurnState.calculateScoreOfRolledDice());
        playerTurnState.userDiceValues = new int[]{1,2,1,1,1};
        assertEquals(1100, playerTurnState.calculateScoreOfRolledDice());
        playerTurnState.userDiceValues = new int[]{4,5,5,5,5};
        assertEquals(550, playerTurnState.calculateScoreOfRolledDice());
        playerTurnState.userDiceValues = new int[]{5,5,5,1,5};
        assertEquals(650, playerTurnState.calculateScoreOfRolledDice());
    }

    @Test
    public void calculateScore5() throws Exception {
        //non scoring
        playerTurnState.userDiceValues = new int[]{2,2,3,6,6};
        assertEquals(0, playerTurnState.calculateScoreOfRolledDice());
        playerTurnState.userDiceValues = new int[]{4,4,3,3,2};
        assertEquals(0, playerTurnState.calculateScoreOfRolledDice());
    }

    @Test
    public void calculateScore6() throws Exception {
        //calculate only rolled dice
        playerTurnState.heldDice.clear();
        playerTurnState.heldDice.add(0);
        playerTurnState.heldDice.add(2);

        playerTurnState.userDiceValues = new int[]{1,2,1,6,6};
        assertEquals(0, playerTurnState.calculateScoreOfRolledDice());

        playerTurnState.userDiceValues = new int[]{5,4,1,1,1};
        assertEquals(200, playerTurnState.calculateScoreOfRolledDice());

        playerTurnState.userDiceValues = new int[]{5,5,5,5,5};
        assertEquals(500, playerTurnState.calculateScoreOfRolledDice());
    }
}