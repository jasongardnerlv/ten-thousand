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

import freecake.tenthousand.TenThousand;
import freecake.tenthousand.TransitionCallback;
import freecake.tenthousand.util.ScoringUtilities;

public class RulesPageState implements TenThousandState {

    public final boolean handleStateTransition(final TenThousand.GameState newState,
                                                TransitionCallback transitionCallback) {
        if (newState == TenThousand.GameState.RulesPage1) {
            printRulesPage(1);
            return true;
        } else if (newState == TenThousand.GameState.RulesPage2) {
            printRulesPage(2);
            return true;
        } else if (newState == TenThousand.GameState.RulesPage3) {
            printRulesPage(3);
            return true;
        }
        return false;
    }

    public final TenThousand.GameState handleUserInput(final TenThousand.GameState currentState, final int charCode) {
        if (currentState == TenThousand.GameState.RulesPage1 && charCode == 32) {
            return TenThousand.GameState.RulesPage2;
        } else if (currentState == TenThousand.GameState.RulesPage2 && charCode == 32) {
            return TenThousand.GameState.RulesPage3;
        } else if (currentState == TenThousand.GameState.RulesPage3 && charCode == 32) {
            return TenThousand.GameState.TurnStart;
        }
        return null; //not handled

    }

    private final void printRulesPage(final int pageNum) {
        switch (pageNum) {
            case 1:
                System.out.println("The object of the game is to reach a score of 10,000 before your");
                System.out.println("opponent. On a player's turn, the dice are rolled and, should the");
                System.out.println("player have at least one scoring die, the player can 'hold' them and");
                System.out.println("roll the remaining dice. If a player reaches a minimum of " +
                                                            ScoringUtilities.MIN_HOLD_THRESHOLD + " points");
                System.out.println("during their turn, they can choose to 'stay' (and collect their");
                System.out.println("points) or they could press their luck and continue rolling.");
                System.out.println("\npress [space] to continue...");
                break;
            case 2:
                System.out.println("If all 5 dice are scoring, accumlated points will rollover, and all 5");
                System.out.println("dice must be rolled again. If, at any point during a turn, a roll");
                System.out.println("results in no new scoring dice, the turn is over and any points");
                System.out.println("accumulated are lost. Before a player is allowed to 'stay' at the");
                System.out.println("minimum " + ScoringUtilities.MIN_HOLD_THRESHOLD +
                                        " points, they must first be 'on the board'. This is");
                System.out.println("accomplished by reaching an initial minimum of " + ScoringUtilities.ON_THE_BOARD_THRESHOLD + " points.");
                System.out.println("\npress [space] to continue...");
                break;
            case 3:
                System.out.println("=======================  Scoring Combinations  =======================");
                System.out.println("  5's           = 50pts                                               ");
                System.out.println("  1's           = 100pts                                              ");
                System.out.println("  3 of a kind   = 100pts * the die value (3 1's are worth 1000pts)    ");
                System.out.println("  Straight      = 750pts                                              ");
                System.out.println("  5 of a kind   = 1000pts * the die value                             ");
                System.out.println("======================================================================");
                System.out.println("\npress [space] to continue...");
                break;
        }
    }

}