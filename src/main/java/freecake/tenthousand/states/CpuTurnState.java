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

import java.util.Timer;
import java.util.TimerTask;

public class CpuTurnState extends AbstractTurnState {

    private final Timer timer = new Timer();

    @Override
    public final boolean handleStateTransition(final TenThousand.GameState newState, TransitionCallback transitionCallback) {
        if (TenThousand.playerTurn == false &&
            (newState == TenThousand.GameState.TurnStart ||
            newState == TenThousand.GameState.AfterHold ||
            newState == TenThousand.GameState.AfterRoll ||
            newState == TenThousand.GameState.AfterStay ||
            newState == TenThousand.GameState.BadHold ||
            newState == TenThousand.GameState.AfterBust)) {
            redraw(newState);
            switch (newState) {
                case TurnStart:
                    rollDice();
                    if (calculateScoreOfRolledDice() == 0) {
                        timer.schedule(new TransitionTask(TenThousand.GameState.AfterBust, transitionCallback), 2000);
                    } else {
                        timer.schedule(new TransitionTask(TenThousand.GameState.AfterRoll, transitionCallback), 2000);
                    }
                    break;
                case AfterRoll:
                    holdAllDicePossible();
                    timer.schedule(new TransitionTask(TenThousand.GameState.AfterHold, transitionCallback), 2000);
                    break;
                case AfterHold:
                    if (canStay()) {
                        applyCurrentScore();
                        timer.schedule(new TransitionTask(TenThousand.GameState.AfterStay, transitionCallback), 1000);
                    } else {
                        rollDice();
                        if (calculateScoreOfRolledDice() == 0) {
                            timer.schedule(new TransitionTask(TenThousand.GameState.AfterBust, transitionCallback), 1000);
                        } else {
                            timer.schedule(new TransitionTask(TenThousand.GameState.AfterRoll, transitionCallback), 1000);
                        }
                    }
                    break;
                case AfterStay:
                case AfterBust:
                    endTurn();
                    timer.schedule(new TransitionTask(TenThousand.GameState.ChangeUser, transitionCallback), 3000);
                    break;
                //case BadHold:  //never should happen with the CPU
            }
            return true;
        }
        return false;
    }

    private final void holdAllDicePossible() {
        ScoringUtilities.ScoringResult result = calculateScore(false, false);
        for (int i=0;i<ScoringUtilities.NUMBER_OF_DICE;i++) {
            if (!heldDice.contains(i)) {
                if (result.scoringDice.contains(userDiceValues[i])) {
                    heldDiceThisRound.add(i);
                    result.scoringDice.remove(result.scoringDice.indexOf(userDiceValues[i]));
                }
            }
        }
    }

    @Override
    protected void printCurrentDice(final TenThousand.GameState newState) {
        System.out.println(formatter.format(userDiceValues, heldDice, heldDiceThisRound));
    }

    @Override
    protected void printStatusBar(final TenThousand.GameState newState) {
        String status = "It's the CPU's turn.";
        if (newState == TenThousand.GameState.AfterStay) {
            status = "CPU scored " + turnScore + " points this round";
        } else if (newState == TenThousand.GameState.AfterBust) {
            status = "CPU busted!";
        }
        System.out.println(status);
    }

    @Override
    protected void printFooter(final TenThousand.GameState newState) {
        printOptionsDivider();
        String options = "[q] Quit";
        System.out.println(centerOn70Chars(options));
    }

    private final class TransitionTask extends TimerTask {
        private TenThousand.GameState newState;
        private TransitionCallback callback;

        public TransitionTask(TenThousand.GameState newState, TransitionCallback callback) {
            this.newState = newState;
            this.callback = callback;
        }

        public void run() {
            callback.transitionToGameState(newState);
        }
    }

}