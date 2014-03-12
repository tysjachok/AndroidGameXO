package kn.hqup.gamexo.ai.gardnerway;

import java.util.ArrayList;

/**
 * User: Oleg
 * Date: 22.10.13
 * Time: 16:30
 */
public class WriterComparator {

        private final String BASE_DIR;
        private final String FILE_NAME;
        private final int BOARD_SIZE;


        public WriterComparator(String baseDir, String fileName, int boardSize) {

                BASE_DIR = baseDir;
                FILE_NAME = fileName;
                BOARD_SIZE = boardSize;

        }


        public void        writePosition(ArrayList<Integer> history) {
                String sortedPosition = HistoryMaster.sortHistory(history);
                FileMaster file = new FileMaster(BASE_DIR, history.size() + "." + FILE_NAME);
                file.writeFile(sortedPosition);
        }


        public boolean comparePos(ArrayList<Integer> history) {
                String basePosition;
                String tempPosition;
                FileMaster file;
                ArrayList<Integer> tempHistory = new ArrayList<Integer>(history.size());

                for (int i = 0; i <= 22; i += 11) {

                        tempHistory.clear();
                        tempHistory.addAll(history);

                        if (i != 0) {
                                tempHistory = CoordinateConverter.rotateHistory(tempHistory, BOARD_SIZE, i);
                        }

                        for (int j = 0; j <= 270; j += 90) {

                                if (j != 0) {
                                        tempHistory = CoordinateConverter.rotateHistory(tempHistory, BOARD_SIZE, j);
                                }

                                tempPosition = HistoryMaster.sortHistory(tempHistory);
                                file = new FileMaster(BASE_DIR, tempHistory.size() + "." + FILE_NAME);
                                while (true) {
                                        basePosition = file.readFile();
                                        if (basePosition == null){
                                                file.closeReading();
                                                break;
                                        }
                                        if (basePosition.equals(tempPosition)){
                                                file.closeReading();
                                                return true;
                                        }

                                }
                                file.closeReading();
                        }
                }

                return false;
        }

}
