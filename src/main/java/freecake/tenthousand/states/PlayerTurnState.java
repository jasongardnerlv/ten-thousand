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

public class PlayerTurnState extends AbstractTurnState {

    @Override
    public final boolean handleStateTransition(final TenThousand.GameState newState,
                                                TransitionCallback transitionCallback) {
        if (TenThousand.playerTurn == true &&
            (newState == TenThousand.GameState.TurnStart ||
            newState == TenThousand.GameState.AfterHold ||
            newState == TenThousand.GameState.AfterRoll ||
            newState == TenThousand.GameState.AfterStay ||
            newState == TenThousand.GameState.BadHold ||
            newState == TenThousand.GameState.AfterBust)) {
            redraw(newState);
            return true;
        }
        return false;
    }

    public final TenThousand.GameState handleUserInput(final TenThousand.GameState currentState, final int charCode) {
        if (TenThousand.playerTurn == true) {
            if (currentState == TenThousand.GameState.TurnStart) {
                if (charCode == SPACE_KEY) {
                    rollDice();
                    if (calculateScoreOfRolledDice() == 0) {
                        return TenThousand.GameState.AfterBust;
                    } else {
                        return TenThousand.GameState.AfterRoll;
                    }
                }
            } else if (currentState == TenThousand.GameState.AfterRoll ||
                        currentState == TenThousand.GameState.AfterHold ||
                        currentState == TenThousand.GameState.BadHold) {
                switch (charCode) {
                    case SPACE_KEY:
                        if (!heldDiceAreValid()) {
                             return TenThousand.GameState.BadHold;
                        }
                        rollDice();
                        if (calculateScoreOfRolledDice() == 0) {
                            return TenThousand.GameState.AfterBust;
                        } else {
                            return TenThousand.GameState.AfterRoll;
                        }
                    case S_KEY_UPPER:
                    case S_KEY_LOWER:
                        if (canStay()) {
                            applyCurrentScore();
                            return TenThousand.GameState.AfterStay;
                        } else {
                            break;
                        }
                    case ONE_KEY:
                    case TWO_KEY:
                    case THREE_KEY:
                    case FOUR_KEY:
                    case FIVE_KEY:
                        int diceIdx = Integer.valueOf(Character.toString((char)charCode)) - 1;
                        if (!heldDice.contains(diceIdx)) {
                            if (heldDiceThisRound.contains(diceIdx)) {
                                heldDiceThisRound.remove(diceIdx);
                            } else {
                                heldDiceThisRound.add(diceIdx);
                            }
                        }
                        return TenThousand.GameState.AfterHold;
                }
            } else if (currentState == TenThousand.GameState.AfterBust ||
                        currentState == TenThousand.GameState.AfterStay) {
                if (charCode == SPACE_KEY) {
                    endTurn();
                    return TenThousand.GameState.ChangeUser;
                }
            }
        }
        return null; //not handled

    }

    @Override
    protected void printCurrentDice(final TenThousand.GameState newState) {
        System.out.println(formatter.format(userDiceValues, heldDice, heldDiceThisRound));
    }

    @Override
    protected void printStatusBar(final TenThousand.GameState newState) {
        String status = "It's your turn. Press [space] to roll the dice.";
        if (newState == TenThousand.GameState.AfterRoll ||
            newState == TenThousand.GameState.AfterHold) {
            status = "Press [1-5] to hold dice, then press [space] to roll again";
        } else if (newState == TenThousand.GameState.BadHold) {
            status = "Invalid dice selection.  Please reselect a valid scoring combination";
        } else if (newState == TenThousand.GameState.AfterStay) {
            status = "You stayed at " + turnScore + " points.  press [space] to continue";
        } else if (newState == TenThousand.GameState.AfterBust) {
            status = "You busted!  press [space] to continue";
        }
        System.out.println(status);
    }

    @Override
    protected void printFooter(final TenThousand.GameState newState) {
        printOptionsDivider();
        String options = "[space] Roll Dice  [q] Quit";
        if (newState == TenThousand.GameState.AfterRoll ||
            newState == TenThousand.GameState.AfterHold ||
            newState == TenThousand.GameState.BadHold) {
            options = "[1-5] Hold Dice  " + options;
            if (canStay()) {
                options = "[s] Stay  " + options;
            }
        } else if (newState == TenThousand.GameState.AfterStay ||
                    newState == TenThousand.GameState.AfterBust) {
            options = "[space] Continue  [q] Quit";
        }
        System.out.println(centerOn70Chars(options));
    }

}