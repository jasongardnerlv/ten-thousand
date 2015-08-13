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

public class WelcomeState implements TenThousandState {

    public final boolean handleStateTransition(final TenThousand.GameState newState,
                                                TransitionCallback transitionCallback) {
        if (newState == TenThousand.GameState.Welcome) {
            printWelcome();
            return true;
        }
        return false;
    }

    public final TenThousand.GameState handleUserInput(final TenThousand.GameState currentState, final int charCode) {
        if (currentState == TenThousand.GameState.Welcome) {
            switch (charCode) {
                case 32: //Space
                    return TenThousand.GameState.RulesPage1;
                case 27:  //Esc
                    return TenThousand.GameState.TurnStart;
            }
        }
        return null; //not handled

    }

    private void printWelcome() {
        System.out.println("Welcome to the dice game called 10,000!\n");
        System.out.println("Press [space] to view game rules or [esc] to skip");
    }

}