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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class DiceFormatter {

    public final String format(int[] dice, Set<Integer> heldDice, Set<Integer> heldDiceThisRound)
            throws IllegalArgumentException {
        if (dice.length != 5) {
            throw new IllegalArgumentException("There should be exactly 5 dice formatted at all times");
        }
        Map<Integer, String[]> dies = new HashMap<>();
        int idx;
        for (idx=0;idx<5;idx++) {
            switch (dice[idx]) {
                case 1:
                    dies.put(idx, dieOne);
                    break;
                case 2:
                    dies.put(idx, dieTwo);
                    break;
                case 3:
                    dies.put(idx, dieThree);
                    break;
                case 4:
                    dies.put(idx, dieFour);
                    break;
                case 5:
                    dies.put(idx, dieFive);
                    break;
                case 6:
                    dies.put(idx, dieSix);
                    break;
                default:
                    dies.put(idx, dieEmpty);
                    break;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<6;i++) {
            if (i > 0) {
                sb.append("\n");
            }
            for (idx=0;idx<5;idx++) {
                if (i == 5 && heldDice.contains(idx)) {
                    sb.append(heldIndicator);
                } else if (i == 5 && heldDiceThisRound.contains(idx)) {
                    sb.append(heldThisRoundIndicator);
                } else {
                    sb.append(dies.get(idx)[i]);
                }
            }
        }
        return sb.toString();
    }


    private final String heldIndicator = "  ##########  ";
    private final String heldThisRoundIndicator = "  ^^^^^^^^^^  ";

    private final String[] dieOne = {
                                        "  ┌────────┐  ",
                                        "  │        │  ",
                                        "  │   ⬤    │  ",
                                        "  │        │  ",
                                        "  └────────┘  ",
                                        "              "
                                    };

    private final String[] dieTwo = {
                                        "  ┌────────┐  ",
                                        "  │     ⬤  │  ",
                                        "  │        │  ",
                                        "  │ ⬤      │  ",
                                        "  └────────┘  ",
                                        "              "
                                    };

    private final String[] dieThree = {
                                        "  ┌────────┐  ",
                                        "  │     ⬤  │  ",
                                        "  │   ⬤    │  ",
                                        "  │ ⬤      │  ",
                                        "  └────────┘  ",
                                        "              "
                                    };

    private final String[] dieFour = {
                                        "  ┌────────┐  ",
                                        "  │ ⬤   ⬤  │  ",
                                        "  │        │  ",
                                        "  │ ⬤   ⬤  │  ",
                                        "  └────────┘  ",
                                        "              "
                                    };

    private final String[] dieFive = {
                                        "  ┌────────┐  ",
                                        "  │ ⬤   ⬤  │  ",
                                        "  │   ⬤    │  ",
                                        "  │ ⬤   ⬤  │  ",
                                        "  └────────┘  ",
                                        "              "
                                    };

    private final String[] dieSix = {
                                        "  ┌────────┐  ",
                                        "  │ ⬤   ⬤  │  ",
                                        "  │ ⬤   ⬤  │  ",
                                        "  │ ⬤   ⬤  │  ",
                                        "  └────────┘  ",
                                        "              "
                                    };

    private final String[] dieEmpty = {
                                        "  ┌────────┐  ",
                                        "  │        │  ",
                                        "  │        │  ",
                                        "  │        │  ",
                                        "  └────────┘  ",
                                        "              "
                                    };

}