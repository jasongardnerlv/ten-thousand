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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Random;

import freecake.tenthousand.TenThousand;
import freecake.tenthousand.TransitionCallback;
import freecake.tenthousand.DiceFormatter;
import freecake.tenthousand.util.ScoringUtilities;

public abstract class AbstractTurnState implements TenThousandState {

    protected static final int SPACE_KEY = 32;
    protected static final int S_KEY_UPPER = 83;
    protected static final int S_KEY_LOWER = 115;
    protected static final int ONE_KEY = 49;
    protected static final int TWO_KEY = 50;
    protected static final int THREE_KEY = 51;
    protected static final int FOUR_KEY = 52;
    protected static final int FIVE_KEY = 53;

    protected DiceFormatter formatter = new DiceFormatter();
    protected ScoringUtilities scoringUtil = new ScoringUtilities();
    protected Random rng = new Random();

    int[] userDiceValues = {0,0,0,0,0};
    Set<Integer> heldDice = new HashSet<Integer>();
    Set<Integer> heldDiceThisRound = new HashSet<Integer>();
    int turnScore = 0;
    boolean onTheBoard = false;

    public boolean handleStateTransition(final TenThousand.GameState newState, TransitionCallback transitionCallback) {
        //override me
        return false;
    }

    public TenThousand.GameState handleUserInput(final TenThousand.GameState currentState, final int charCode) {
        //override me
        return null;
    }

    protected void redraw(final TenThousand.GameState newState) {
        printPointsBar(newState);
        printCurrentDice(newState);
        printStatusBar(newState);
        printFooter(newState);
    }

    protected final void endTurn() {
        heldDice.clear();
        heldDiceThisRound.clear();
        turnScore = 0;
        for (int i=0;i<ScoringUtilities.NUMBER_OF_DICE;i++) {
            userDiceValues[i] = 0;
        }
    }

    protected final boolean applyCurrentScore() {
        onTheBoard = true;
        heldDiceThisRound.clear();
        turnScore += calculateScoreOfRolledDice();
        int newScore = 0;
        if (TenThousand.playerTurn) {
            TenThousand.playerScore += turnScore;
            newScore = TenThousand.playerScore;
        } else {
            TenThousand.cpuScore += turnScore;
            newScore = TenThousand.cpuScore;
        }
        return newScore >= ScoringUtilities.GAME_OVER_THRESHOLD;
    }

    protected final void rollDice() {
        turnScore += calculateScoreOfHeldThisRound(true);
        if (shouldRollover()) {
            heldDice.clear();
        } else {
            if (heldDiceThisRound.size() > 0) {
                heldDice.addAll(heldDiceThisRound);
            }
        }
        heldDiceThisRound.clear();
        for (int i=0;i<ScoringUtilities.NUMBER_OF_DICE;i++) {
            if (heldDice.contains(i)) {
                continue;
            }
            userDiceValues[i] = rng.nextInt(ScoringUtilities.DIE_FACES) + 1;
        }
    }

    protected final boolean canStay() {
        int min = (onTheBoard == true) ? ScoringUtilities.MIN_HOLD_THRESHOLD :
                                            ScoringUtilities.ON_THE_BOARD_THRESHOLD;
        boolean allDiceScore = false;
        try {
            scoringUtil.calculateScore(userDiceValues, true);
            allDiceScore = true;
        } catch (IllegalArgumentException iae) {
            allDiceScore = false;
        }
        return (turnScore + calculateScoreOfRolledDice()) >= min && allDiceScore == false &&
                behindOnLastAttempt() == false;
    }

    protected final int getOtherPlayersScore() {
        if (TenThousand.playerTurn) {
            return TenThousand.cpuScore;
        } else {
            return TenThousand.playerScore;
        }
    }

    protected final boolean playerWinning() {
        return (turnScore + calculateScoreOfRolledDice()) > getOtherPlayersScore();
    }

    protected final boolean otherPlayerWentOut() {
        return getOtherPlayersScore() >= ScoringUtilities.GAME_OVER_THRESHOLD;
    }

    protected final boolean behindOnLastAttempt() {
        return otherPlayerWentOut() && playerWinning() == false;
    }

    protected final boolean shouldRollover() {
        return heldDice.size() + heldDiceThisRound.size() == 5;
    }

    final int calculateScoreOfRolledDice() {
        try {
            return calculateScore(false, false).score;
        } catch (IllegalArgumentException iae) {
            return 0;
        }
    }

    final int calculateScoreOfHeldThisRound(boolean validate) {
        try {
            return calculateScore(true, validate).score;
        } catch (IllegalArgumentException iae) {
            return 0;
        }
    }

    protected final boolean heldDiceAreValid() {
        try {
            calculateScore(true, true);
            return true;
        } catch (IllegalArgumentException iae) {
            return false;
        }
    }

    protected final ScoringUtilities.ScoringResult calculateScore(boolean includeHeld, boolean validate)
            throws IllegalArgumentException {
        Set<Integer> rolledDice = new HashSet<Integer>();
        for (int i=0;i<ScoringUtilities.NUMBER_OF_DICE;i++) {
            if (!includeHeld && heldDice.contains(i)) {
                //counting all dice not previously held
                continue;
            } else if (includeHeld && !heldDiceThisRound.contains(i)) {
                //counting only dice held this round
                continue;
            }
            rolledDice.add(i);
        }
        List<Integer> rolledValues = new ArrayList<Integer>();
        for (Integer d : rolledDice) {
            rolledValues.add(userDiceValues[d]);
        }
        return scoringUtil.calculateScore(rolledValues, validate);
    }

    protected void printOptionsDivider() {
        System.out.println("----------------------------------------------------------------------");
    }

    private void printPointsBar(final TenThousand.GameState newState) {
        String turnPoints = "Turn Points:" + (turnScore + calculateScoreOfHeldThisRound(false));
        String totalPoints = "User:" + TenThousand.playerScore + " CPU:" + TenThousand.cpuScore;
        System.out.println(leftRightAlignOn70Chars(turnPoints, totalPoints) + "\n");
    }

    protected void printCurrentDice(final TenThousand.GameState newState) {
        //override me
    }

    protected void printStatusBar(final TenThousand.GameState newState) {
        //override me
    }

    protected void printFooter(final TenThousand.GameState newState) {
        //override me
    }

    protected final String leftRightAlignOn70Chars(String s1, String s2) {
        StringBuilder sb = new StringBuilder();
        sb.append(s1);
        for (int i=0;i<(70 - s1.length() - s2.length());i++) {
            sb.append(" ");
        }
        sb.append(s2);
        return sb.toString();
    }

    protected final String centerOn70Chars(String s) {
        int padding = 70 - s.length();
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<padding/2;i++) {
            sb.append(" ");
        }
        sb.append(s);
        for (int i=0;i<padding/2;i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

}