package ua.bringoff.developer.fifteenpuzzle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class GameBoardActivity extends Activity {
    public static int SIZE;
    private static final String TAG = "FifteenGame_Tag";
    public static boolean SOUNDS = true;
    public static final String MOVES_EXTRA = "ua.bringoff.developer.fifteenpuzzle.moves";

    private final int[] ids = {R.id.btn00, R.id.btn01, R.id.btn02, R.id.btn03,
            R.id.btn10, R.id.btn11, R.id.btn12, R.id.btn13,
            R.id.btn20, R.id.btn21, R.id.btn22, R.id.btn23,
            R.id.btn30, R.id.btn31, R.id.btn32, R.id.btn33,
            R.id.btn40, R.id.btn41, R.id.btn42, R.id.btn43,
            R.id.btn50, R.id.btn51, R.id.btn52, R.id.btn53,
            R.id.btn60, R.id.btn61, R.id.btn62, R.id.btn63,
            R.id.btn70, R.id.btn71, R.id.btn72, R.id.btn73,
            R.id.btn80, R.id.btn81, R.id.btn82, R.id.btn83,};

    private int[][] mPuzzle;

    private LinearLayout mButtonsField;
    private TextView mMovesTextView;

    private int mButtonSize;

    private int mMoves;

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        SharedPreferences sharedPreferences = getSharedPreferences(
                PrefsActivity.APP_PREFERENCES, MODE_PRIVATE);
        SIZE = sharedPreferences.getInt(PrefsActivity.KEY_RADIOBUTTON_INDEX, 1) + 3;
        SOUNDS = sharedPreferences.getBoolean(PrefsActivity.KEY_CHECKBOX_SOUND_CHECKED, true);

        mPuzzle = new int[SIZE][SIZE];

        mButtonsField = (LinearLayout) findViewById(R.id.buttons_board);

        mp = MediaPlayer.create(this, R.raw.button_move);

        mMoves = 0;
        mMovesTextView = (TextView) findViewById(R.id.moves_text_view);

        shufflePuzzle();
        setSizes();
        addButtons();
        printField(mPuzzle);
        updateButtons();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setMovesText() {
        mMovesTextView.setText(getResources().getString(R.string.moves_count) + mMoves);
    }

    private void printField(int[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println("");
        }
    }

    private void setSizes() {
        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        int width = screenSize.x;
        int height = screenSize.y;
        mButtonSize = width / SIZE - 16;
        mButtonsField.setPadding(0, (height - (mButtonSize * SIZE)) / 2, 0, (height - (mButtonSize * SIZE)) / 2);
    }


    private void addButtons() {

        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            LinearLayout layout = new LinearLayout(this);
            layout.setGravity(Gravity.CENTER);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            mButtonsField.addView(layout);

            for (int j = 0; j < SIZE; j++) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(1, 1, 1, 1);

                Button button = new Button(getBaseContext());
                button.setId(ids[count]);
                button.setTag(i + "," + j);
                button.setWidth(mButtonSize);
                button.setHeight(mButtonSize);
                button.setTextColor(Color.WHITE);
                button.setTextSize(mButtonSize / SIZE);
                button.setBackgroundColor(getResources().getColor(R.color.orange_cool));
                button.setLayoutParams(layoutParams);
                Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/DROID.TTF");
                button.setTypeface(typeface);
                mMovesTextView.setTypeface(typeface);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String[] rowCol = view.getTag().toString().split(",");
                        int row = Integer.parseInt(rowCol[0]);
                        int col = Integer.parseInt(rowCol[1]);
                        char direction = canMove(row, col);

                        if (direction != 'N') {
                            movePuzzle(row, col, direction);

                            if (SOUNDS) {
                                if (mp.isPlaying()) {
                                    mp.stop();
                                }
                                mp.start();
                            }

                            mMoves++;
                            setMovesText();

                            updateButtons();

                            if (checkIsWin()) {
                                Intent winIntent = new Intent(GameBoardActivity.this, WinActivity.class).putExtra(MOVES_EXTRA, mMoves);
                                startActivity(winIntent);
                                finish();
                            }
                        }
                    }
                });
                layout.addView(button);

                count++;
            }
        }
    }

    public int[] getEmptyButton() {
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

        if (getEmptyButton()[0] == row && getEmptyButton()[1] > col) {
            Log.d(TAG, "R");
            return 'R';
        }

        if (getEmptyButton()[0] == row && getEmptyButton()[1] < col) {
            Log.d(TAG, "L");
            return 'L';
        }

        if (getEmptyButton()[1] == col && getEmptyButton()[0] > row) {
            Log.d(TAG, "D");
            return 'D';
        }

        if (getEmptyButton()[1] == col && getEmptyButton()[0] < row) {
            Log.d(TAG, "U");
            return 'U';
        }
        return 'N';
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mp.release();
        openLeavePuzzleDialog();
    }

    private void openLeavePuzzleDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(GameBoardActivity.this);
        dialog.setTitle(getResources().getString(R.string.sure_leave_puzzle));

        dialog.setPositiveButton(getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        Intent intent = new Intent(GameBoardActivity.this, MenuActivity.class);
                        startActivity(intent);
                    }
                }
        );

        dialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //empty
            }
        });

        dialog.show();
    }

    public void movePuzzle(int fromRow, int fromCol, char direction) {
        int number;
        int[] empty = getEmptyButton();
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

        printField(mPuzzle);
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

    public int[][] getNormalPuzzle() {
        int[][] normalPuzzle = new int[mPuzzle.length][mPuzzle[0].length];

        int count = 1;
        for (int i = 0; i < normalPuzzle.length; i++) {
            for (int j = 0; j < normalPuzzle[0].length; j++) {
                if (count == 16) {
                    normalPuzzle[i][j] = 0;
                } else {
                    normalPuzzle[i][j] = count;
                }
                count++;
            }
        }

        return normalPuzzle;
    }


    public void shufflePuzzle() {
        List<Integer> availableNumbers = new LinkedList<>();
        for (int i = 0; i <= SIZE * SIZE - 1; i++) {
            availableNumbers.add(i);
        }

        Random r = new Random();
        for (int i = 0; i < mPuzzle.length; i++) {
            for (int j = 0; j < mPuzzle[0].length; j++) {

                int num = r.nextInt(availableNumbers.size());
                mPuzzle[i][j] = availableNumbers.get(num);
                availableNumbers.remove(num);
            }
        }
    }

    private void updateButtons() {
        int count = 0x0;
        for (int i = 0; i < mPuzzle.length; i++) {
            for (int j = 0; j < mPuzzle[0].length; j++) {
                Button btn = (Button) findViewById(R.id.btn00 + count);
                if (mPuzzle[i][j] == 0) {
                    btn.setText(" ");
                    btn.setBackgroundColor(mButtonsField.getSolidColor());
                } else {
                    btn.setText("" + mPuzzle[i][j]);
                    btn.setBackgroundColor(getResources().getColor(R.color.orange_cool));
                }
                count += 0x1;
            }

        }
    }
}
