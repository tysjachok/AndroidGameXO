package kn.hqup.gamexo.ai.gardnerway;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * User: Oleg
 * Date: 22.10.13
 * Time: 16:34
 */
public class HistoryMaster {

        /**
         * String position got from history and sorted with TreeSet.
         * It need for write history in the base,and make comparison with
         * positions already stored in the base.
         * @param history got from moves.
         * @return position in String
         */
        public static String sortHistory(ArrayList<Integer> history) {
                TreeSet<String> historySet = new TreeSet<String>();
                String sortedHistory = "";
                String cell;
                for (int i = 0; i < history.size(); i++) {
                        if(i % 2 == 0){
                                cell = "X_";
                        } else {
                                cell = "O_";
                        }
                        historySet.add(cell + history.get(i) + " ");
                }

                for (String s : historySet) {
                        sortedHistory += s;
                }

                return sortedHistory;
        }

        public static ArrayList<Integer> rewindHistoryBack(ArrayList<Integer> history, int step)
        {   int tempHistorySize = history.size() - step;
                ArrayList<Integer> temporaryHistory = new ArrayList<Integer>(tempHistorySize);
                for (int i = 0; i < tempHistorySize; i++) {
                        temporaryHistory.add(history.get(i));
                }
                return temporaryHistory;
        }
}
