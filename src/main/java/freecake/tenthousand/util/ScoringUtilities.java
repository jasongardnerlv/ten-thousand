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
package freecake.tenthousand.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ScoringUtilities {

    public static final int MIN_HOLD_THRESHOLD = 300;
    public static final int ON_THE_BOARD_THRESHOLD = 650;
    public static final int GAME_OVER_THRESHOLD = 10000;
    public static final int DIE_FACES = 6;
    public static final int NUMBER_OF_DICE = 5;

    public final ScoringResult calculateScore(int[] dice, boolean validate) throws IllegalArgumentException {
        List<Integer> list = new ArrayList<Integer>();
        for (int value : dice) {
            list.add(value);
        }
        return calculateScore(list, validate);
    }

    public final ScoringResult calculateScore(List<Integer> dice, boolean validate)
            throws IllegalArgumentException {
        if (validate && dice.size() == 0) {
            throw new IllegalArgumentException("You must hold at least one dice per round");
        }
        int i = 0;
        Map<Integer, Integer> occuranceMap = new HashMap<>();
        for (i=1;i<=DIE_FACES;i++) {
            occuranceMap.put(i,0);
        }
        Collections.sort(dice);
        for (i=0;i<dice.size();i++) {
            if (dice.get(i) == 0) {
                return new ScoringResult(0, (new ArrayList<Integer>()));
            }
            occuranceMap.put(dice.get(i), occuranceMap.get(dice.get(i)) + 1);
        }
        if (dice.size() == 5) {
            //check 5 of a kind
            for (i=1;i<=DIE_FACES;i++) {
                if (occuranceMap.get(i) == 5) {
                    return new ScoringResult(1000 * i, (new ArrayList<Integer>(Arrays.asList(i,i,i,i,i))));
                }
            }
            //check straight
            if (dice.get(0) == 1 &&
                dice.get(1) == 2 &&
                dice.get(2) == 3 &&
                dice.get(3) == 4 &&
                dice.get(4) == 5) {
                return new ScoringResult(750, (new ArrayList<Integer>(Arrays.asList(1,2,3,4,5))));
            }
            if (dice.get(0) == 2 &&
                dice.get(1) == 3 &&
                dice.get(2) == 4 &&
                dice.get(3) == 5 &&
                dice.get(4) == 6) {
                return new ScoringResult(750, (new ArrayList<Integer>(Arrays.asList(2,3,4,5,6))));
            }
        }
        int combined = 0;
        List<Integer> scoringDice = new ArrayList<Integer>();
        if (dice.size() >= 3) {
            //check 3 of a kind
            for (i=1;i<=DIE_FACES;i++) {
                if (occuranceMap.get(i) >= 3) {
                    combined += (i == 1) ? 1000 : 100 * i;
                    for (int j=0;j<3;j++) {
                        dice.remove(dice.indexOf(i));
                        scoringDice.add(i);
                    }
                }
            }
        }
        Iterator<Integer> iter = dice.iterator();
        while (iter.hasNext()) {
            Integer itg = iter.next();
            //1's and 5's
            if (itg == 1) {
                combined += 100;
                scoringDice.add(itg);
                iter.remove();
            } else if (itg == 5) {
                combined += 50;
                scoringDice.add(itg);
                iter.remove();
            }
        }
        if (validate && dice.size() > 0) {
            throw new IllegalArgumentException("Invalid Scoring Combination");
        }
        return new ScoringResult(combined, scoringDice);
    }

    public final class ScoringResult {
        public final int score;
        public final List<Integer> scoringDice;

        public ScoringResult(final int score, final List<Integer> scoringDice) {
            this.score = score;
            this.scoringDice = scoringDice;
        }
    }

}