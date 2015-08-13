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

public class GameOverState implements TenThousandState {

    public final boolean handleStateTransition(final TenThousand.GameState newState,
                                                TransitionCallback transitionCallback) {
        if (newState == TenThousand.GameState.GameOver) {
            printGameOver();
            return true;
        }
        return false;
    }

    public final TenThousand.GameState handleUserInput(final TenThousand.GameState currentState, final int charCode) {
        //TODO handle new game
        return null; //not handled

    }

    private void printGameOver() {
        System.out.println("  Game Over\n");
        if (TenThousand.playerScore > TenThousand.cpuScore) {
            System.out.println("  You won! " + TenThousand.playerScore + " to " + TenThousand.cpuScore);
        } else {
            System.out.println("  CPU won. " + TenThousand.cpuScore + " to " + TenThousand.playerScore);
        }
        System.out.println("\n  press [q] to quit");
    }

}