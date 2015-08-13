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
package freecake.tenthousand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import freecake.tenthousand.states.*;
import freecake.tenthousand.util.ScoringUtilities;


public class TenThousand implements InputListener {

    public static int playerScore = 0;
    public static int cpuScore = 0;
    public static boolean playerTurn = true;

    /* TODO
        * Randomly pick who goes first
        * AI needs to "go for it" sometimes, too conservative
        * Clear screen on M$ Windoze
    */

    public enum GameState {
                            Welcome,
                            RulesPage1,
                            RulesPage2,
                            RulesPage3,
                            TurnStart,
                            AfterHold,
                            AfterRoll,
                            AfterStay,
                            AfterBust,
                            BadHold,
                            ChangeUser,
                            GameOver
                            };

    private Console console;
    private GameState state;
    private boolean prevPlayerOut = false;

    private final List<TenThousandState> stateHandlers =
            Collections.unmodifiableList(Arrays.asList((new WelcomeState()),
                                                       (new RulesPageState()),
                                                       (new PlayerTurnState()),
                                                       (new CpuTurnState()),
                                                       (new GameOverState())));

    public final void run() {
        console = new Console(this);
        console.start();
        transitionTo(GameState.Welcome);
    }

    public final void handleInput(int charCode) {
        if (charCode == 81 || charCode == 113) {
            clearScreen();
            System.exit(0);
        }
        for (TenThousandState handler : stateHandlers) {
            GameState newState = handler.handleUserInput(state, charCode);
            if (newState != null) {
                transitionTo(newState);
                return;
            }
        }
    }

    private final void transitionTo(GameState newState) {
        if (newState == GameState.ChangeUser) {
            if (prevPlayerOut) {
                newState = GameState.GameOver;
            } else {
                if (playerTurn && playerScore >= ScoringUtilities.GAME_OVER_THRESHOLD ||
                    !playerTurn && cpuScore >= ScoringUtilities.GAME_OVER_THRESHOLD) {
                    prevPlayerOut = true;
                }
                playerTurn = !playerTurn;
                newState = GameState.TurnStart;
            }
        }
        state = newState;
        clearScreen();
        printHeader();
        for (TenThousandState handler : stateHandlers) {
            boolean result = handler.handleStateTransition(state, new TransitionCallback() {
                                                                        public void transitionToGameState(
                                                                                TenThousand.GameState newState) {
                                                                            transitionTo(newState);
                                                                        }
                                                                    });
            if (result == true) {
                break;
            }
        }
    }

    private final void clearScreen() {
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            String ANSI_CSI = "\u001b[";
            System.out.print(ANSI_CSI + 2 + "J");
            System.out.flush();
            System.out.print(ANSI_CSI + "1;1H");
            System.out.flush();
        }
    }

    private final void printHeader() {
        System.out.println("**********************************************************************");
        System.out.println("*************************  10,000 Dice Game  *************************");
        System.out.println("**********************************************************************");
    }

    public static void main(String[] args) {
        TenThousand tt = new TenThousand();
        tt.run();
    }

}
