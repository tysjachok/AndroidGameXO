package kn.hqup.gamexo.gamefield.players;

import kn.hqup.gamexo.ai.IBrainAI;
import kn.hqup.gamexo.ai.IPlayerBot;
import kn.hqup.gamexo.ai.WayEnum;
import kn.hqup.gamexo.ai.brutforceway.BrutforceAI;
import kn.hqup.gamexo.ai.gardnerway.Gardner;
import kn.hqup.gamexo.gamefield.Game;
import kn.hqup.gamexo.gamefield.GameField;
import kn.hqup.gamexo.utils.Logger;

import android.os.Bundle;
import android.os.Message;

/**
 *
 * @author Andrew2212
 *
 * @param <T>
 */
public class PlayerBot<T> implements IPlayer, IPlayerBot<T> {

    public static final String KEY_MOVE = "keyMove";
    private static final String KEY_X = "keyX";
    private static final String KEY_Y = "keyY";
    private static final int X = 0;
    private static final int Y = 1;
    private static int[] position = new int[2];
    private static IBrainAI<?> iBrainAI;
    private WayEnum wayEnum;
    private char signPlayer;

    private int fieldSize;
    private int numChecked;

    public PlayerBot(int fieldSize, int numChecked, char signPlayer) {
        this.signPlayer = signPlayer;
        this.fieldSize = fieldSize;
        this.numChecked = numChecked;

        wayEnum = Game.getWayEnum();

        Logger.v("PlayerBot::CONSTRUCTOR::" + " wayEnum = " + wayEnum);
        iBrainAI = createBrain(wayEnum);
    }

    private IBrainAI<?> createBrain(WayEnum wayEnum) {
        switch (wayEnum) {

            case GARDNER:
                iBrainAI = new Gardner(fieldSize, numChecked);
                // iBrainAI = new BrutforceAI(fieldSize, numChecked);
                break;

            case MINIMAX:
                // iBrainAI = new Minimax(fieldSize, numChecked);
                iBrainAI = new BrutforceAI(fieldSize, numChecked);
                break;

            case SPARE:
                // iBrainAI = new Spare(fieldSize, numChecked);
                iBrainAI = new BrutforceAI(fieldSize, numChecked);
                break;

            case BRUTFORCE:
                iBrainAI = new BrutforceAI(fieldSize, numChecked);
                break;

            case NONE:
                // iBrainAI = new BrutforceAI(fieldSize, numChecked);
                break;

            default:
                break;
        }

        return iBrainAI;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int[] doMove() {

        Logger.v();
        do {
            position = getCoordinate(iBrainAI,
                    (T[][]) GameField.getFieldMatrix(),
                    (T) (Character) signPlayer);
        } while (GameField.getFieldMatrix()[position[X]][position[Y]] != GameField.VALUE_DEFAULT);

        return position;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })

    // Check out whether it is necessary
    @Override
    public boolean setMove(int cellX, int cellY, char signPlayer) {
        return GameField.setSignToCell(cellX, cellY, signPlayer);
    }

    public char getSignPlayer() {
        return signPlayer;
    }

    public static IBrainAI<?> getBrain() {
        return iBrainAI;
    }

    /**
     *
     * @return message that contains 'int[] move'
     */
    public Message obtainMessage() {
        Logger.v();
        // PlayerBot by 'doMove()' returns 'the best AI move'
        int[] move = doMove();
        Logger.v("***move[X] = " + move[X] + " move[Y] = " + move[Y]);
        Bundle msgData = new Bundle();
        msgData.putIntArray(KEY_MOVE, move);
        msgData.putInt(KEY_X, move[X]);
        msgData.putInt(KEY_Y, move[Y]);

        Message message = new Message();
        message.what = HandlerNotHumanLocalMove.MSG_BOT;
        message.setData(msgData);

        message.getData().getIntArray(KEY_MOVE);
        return message;
    }

    @Override
    public void killBrain() {
        iBrainAI = null;

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public IBrainAI<Object> getIBrain() {
        return ((IBrainAI) iBrainAI);
    }


    public int[] getCoordinate(IBrainAI iBrainAI, T[][] fieldMatrix, T figure) {
        Logger.v("fieldMatrix.length = " + fieldMatrix.length + " figure = "
                + figure);
        if (iBrainAI == null)
            return null;
        Logger.v("iBrain = " + iBrainAI.toString());
        position = iBrainAI.findMove(fieldMatrix, figure);
        Logger.v("PlayerBot::getCoordinate::position[X] = " + position[X]
                + ", position[Y] = " + position[Y]);
        return position;
    }


    // **************CHECK OUT IT!********NOT USED YET!
    // public int[] setCalculatedMove() {
    // int[] move = doMove();
    // GameField.setSignToCell(move[X], move[Y], getSignPlayer());
    // return move;
    // }

}