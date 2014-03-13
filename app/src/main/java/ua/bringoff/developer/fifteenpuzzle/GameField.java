package ua.bringoff.developer.fifteenpuzzle;

import android.util.Log;

/**
 * Created by Bringoff on 12.03.14.
 */
public class GameField {
    private final int SIZE;

    private int[][] mPuzzle;

    private static final String TAG = "FifteenGame_Tag";
    
    public GameField(int size)
    {
        SIZE = size;
        mPuzzle = new int[SIZE][SIZE];
    }

    public int[][] getNormalPuzzle() {
        int[][] normalPuzzle = new int[SIZE][SIZE];

        int count = 1;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (count == SIZE * SIZE) {
                    normalPuzzle[i][j] = 0;
                } else {
                    normalPuzzle[i][j] = count;
                }
                count++;
            }
        }

        return normalPuzzle;
    }

    public int[] getEmptyBlock() {
        int[] result = new int[2];
        for (int i = 0; i < mPuzzle.length; i++) {
            for (int j = 0; j < mPuzzle[0].length; j++) {
                if (mPuzzle[i][j] == 0) {
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }

        return result;
    }

    public char canMove(int row, int col) {

        if (getEmptyBlock()[0] == row && getEmptyBlock()[1] > col) {
            Log.d(TAG, "R");
            return 'R';
        }

        if (getEmptyBlock()[0] == row && getEmptyBlock()[1] < col) {
            Log.d(TAG, "L");
            return 'L';
        }

        if (getEmptyBlock()[1] == col && getEmptyBlock()[0] > row) {
            Log.d(TAG, "D");
            return 'D';
        }

        if (getEmptyBlock()[1] == col && getEmptyBlock()[0] < row) {
            Log.d(TAG, "U");
            return 'U';
        }
        return 'N';
    }


    public void shufflePuzzle() {
        mPuzzle = getNormalPuzzle();

        while (true) {
            int i = 1;
            int x, y;
            while (i < SIZE * 50) {
                if (((int) (Math.random() * 2.0d)) == 0) {
                    x = (int) (Math.random() * ((double) SIZE));
                    y = getEmptyBlock()[1];
                    movePuzzle(x, y, canMove(x, y));
                } else {
                    x = getEmptyBlock()[0];
                    y = (int) (Math.random() * ((double) SIZE));
                    movePuzzle(x, y, canMove(x, y));
                }
                i++;
            }
            movePuzzle(SIZE - 1, getEmptyBlock()[1], canMove(SIZE - 1, getEmptyBlock()[1]));
            movePuzzle(SIZE - 1, SIZE - 1, canMove(SIZE - 1, SIZE - 1));
            return;
        }
    }

    public void movePuzzle(int fromRow, int fromCol, char direction) {
        int number;
        int[] empty = getEmptyBlock();
        switch (direction) {
            case 'R':
                for (int i = empty[1]; i > fromCol; i--) {
                    number = mPuzzle[fromRow][i - 1];
                    mPuzzle[fromRow][i - 1] = mPuzzle[fromRow][i];
                    mPuzzle[fromRow][i] = number;
                }
                break;
            case 'L':
                for (int i = empty[1]; i < fromCol; i++) {
                    number = mPuzzle[fromRow][i + 1];
                    mPuzzle[fromRow][i + 1] = mPuzzle[fromRow][i];
                    mPuzzle[fromRow][i] = number;
                }
                break;
            case 'D':
                for (int i = empty[0]; i > fromRow; i--) {
                    number = mPuzzle[i - 1][fromCol];
                    mPuzzle[i - 1][fromCol] = mPuzzle[i][fromCol];
                    mPuzzle[i][fromCol] = number;
                }
                break;
            case 'U':
                for (int i = empty[0]; i < fromRow; i++) {
                    number = mPuzzle[i + 1][fromCol];
                    mPuzzle[i + 1][fromCol] = mPuzzle[i][fromCol];
                    mPuzzle[i][fromCol] = number;
                }
                break;
            default:
                break;
        }
    }

    public boolean checkIsWin() {
        for (int i = 0; i < mPuzzle.length; i++) {
            for (int j = 0; j < mPuzzle[0].length; j++) {
                if (mPuzzle[i][j] != getNormalPuzzle()[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public int[][] getPuzzle() {
        return mPuzzle;
    }
}
