package kn.hqup.gamexo.ai.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Andrew2212
 * <br>This class was made for the independence the AI logic from the 'class' of the Game core fieldMatrix and player's Sign</br>
 */
public class FieldMatrixConverter<T> {

    /*
     * It's such as into 'ai' package made by 'teamAI'
     */
    private static final Character SIGN_X = 'X';
    private static final Character SIGN_O = 'O';
    public static final Character SIGN_EMPTY = '_';

    private static final String X = "X";
    private static final String O = "O";

    private Character[][] fieldMatrixCharacter;
    private int fieldSize;

    private Character signCharacter;


    /**
     * @param originalSign  player's figure from 'core Game'
     * @return Character 'sign'
     */
    public Character convertSignToCharacter(T originalSign) {
        setSignCharacter(originalSign);
        return signCharacter;
    }

    /**
     * @param originalFieldMatrix - fieldMatrix that is obtained from 'core of Game'
     * @return Character[][] fieldMatrix for AI logic
     */
    public Character[][] convertFieldMatrixToCharacter(T[][] originalFieldMatrix) {
        setFieldMatrixCharacter(originalFieldMatrix);
        return fieldMatrixCharacter;
    }

    //----------------Convert PlayerHumanLocal Figure (Sign)-------------------

    private void setSignCharacter(T originalSign) {

        if (originalSign instanceof Enum) {
            signCharacter = returnSignToCharacterFromEnum(originalSign);
        }
        if (originalSign instanceof Character) {
            signCharacter = returnSignToCharacterFromCharacter(originalSign);
        }
    }

    private Character returnSignToCharacterFromEnum(T originalSign) {
        if(originalSign == null) return null;
        Character sign = null;
        if(originalSign.toString().equals(X)) sign = SIGN_X;
        if(originalSign.toString().equals(O)) sign = SIGN_O;

        return sign;
    }

    private Character returnSignToCharacterFromCharacter(T originalSign) {
        if(originalSign == null) return null;
        Character sign = null;
        if(originalSign.equals(SIGN_X)) sign = SIGN_X;
        if(originalSign.equals(SIGN_O)) sign = SIGN_O;

        return sign;
    }


//-------------Convert Field Matrix-------------------------------

    private void setFieldMatrixCharacter(T[][] originalFieldMatrix) {

        if (originalFieldMatrix == null) return;

        if (originalFieldMatrix instanceof Enum[][]) {
            fieldMatrixCharacter = returnMatrixToCharacterFromEnum(originalFieldMatrix);
        }
        if (originalFieldMatrix instanceof Character[][]) {
            fieldMatrixCharacter = returnMatrixToCharacterFromCharacter(originalFieldMatrix);
        }
    }

    private Character[][] returnMatrixToCharacterFromCharacter(T[][] originalFieldMatrix) {

        if (originalFieldMatrix == null) return null;
        fieldSize = originalFieldMatrix.length;
        Character[][] fieldMatrixCharacter = new Character[fieldSize][fieldSize];
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                T sign = originalFieldMatrix[i][j];
//                    System.out.println("Converter::Character sign = " + sign);
                if (sign.equals(SIGN_X))
                    fieldMatrixCharacter[i][j] = SIGN_X;
                else if (sign.equals(SIGN_O))
                    fieldMatrixCharacter[i][j] = SIGN_O;
                else
                    fieldMatrixCharacter[i][j] = SIGN_EMPTY;
            }
        }
        return fieldMatrixCharacter;

    }

    private Character[][] returnMatrixToCharacterFromEnum(T[][] originalFieldMatrix) {

        if (originalFieldMatrix == null) return null;
        fieldSize = originalFieldMatrix.length;
        Character[][] fieldMatrixCharacter = new Character[fieldSize][fieldSize];
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                T sign = originalFieldMatrix[i][j];
//                System.out.println("Converter::Enum sign = " + sign);
                if (sign.toString().equals(X))
                    fieldMatrixCharacter[i][j] = SIGN_X;
                else if (sign.toString().equals(O))
                    fieldMatrixCharacter[i][j] = SIGN_O;
                else
                    fieldMatrixCharacter[i][j] = SIGN_EMPTY;
            }
        }
        return fieldMatrixCharacter;
    }

}