package ua.bringoff.developer.fifteenpuzzle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;


public class GameBoardActivity extends Activity{
    public static int SIZE;
    public static boolean SOUNDS = true;
    public static int COLOR = 0;
    public static final String MOVES_EXTRA = "ua.bringoff.developer.fifteenpuzzle.moves";

    private GameField mGameField;

    private final int[] ids = {R.id.btn00, R.id.btn01, R.id.btn02, R.id.btn03,
            R.id.btn10, R.id.btn11, R.id.btn12, R.id.btn13,
            R.id.btn20, R.id.btn21, R.id.btn22, R.id.btn23,
            R.id.btn30, R.id.btn31, R.id.btn32, R.id.btn33,
            R.id.btn40, R.id.btn41, R.id.btn42, R.id.btn43,
            R.id.btn50, R.id.btn51, R.id.btn52, R.id.btn53,
            R.id.btn60, R.id.btn61, R.id.btn62, R.id.btn63,
            R.id.btn70, R.id.btn71, R.id.btn72, R.id.btn73,
            R.id.btn80, R.id.btn81, R.id.btn82, R.id.btn83,};

    private LinearLayout mButtonsField;
    private TextView mMovesTextView;

    private int mButtonSize;

    private int mMoves = 0;

    private boolean mButtonIsDown;

    SoundPool mSoundPool;
    int moveId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        SIZE = PrefsManager.getFieldSize();
        SOUNDS = PrefsManager.isPlayingSounds();
        COLOR = PrefsManager.getMainColor();

        mGameField = new GameField(SIZE);

        mButtonsField = (LinearLayout) findViewById(R.id.buttons_board);

        mSoundPool = new SoundPool(SIZE, AudioManager.STREAM_MUSIC, 0);
        moveId = mSoundPool.load(this, R.raw.button_move, 1);
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.d("SoundPool_15", "onLoadComplete, sampleId = " + sampleId + ", status = " + status);
            }
        });

        mMovesTextView = (TextView) findViewById(R.id.moves_text_view);

        mGameField.shufflePuzzle();
        setSizes();
        addButtons();
        updateButtons();
    }

    private void setMovesText() {
        mMovesTextView.setText(getResources().getString(R.string.moves_count) + mMoves);
    }

    private void setSizes() {
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        mButtonSize = width / SIZE - SIZE * 2;
        mButtonsField.setPadding(2, (height - (mButtonSize * SIZE)) / 2, 2, 0);
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
                        mButtonSize, mButtonSize, 1f);

                layoutParams.setMargins(1, 1, 1, 1);

                Button button = new Button(this);
                button.setId(ids[count]);
                button.setTag(i + "," + j);
                button.setLayoutParams(layoutParams);
                button.setTextColor(Color.WHITE);
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, mButtonSize / 3);
                Log.d("Buttons", "Width" + String.valueOf(button.getWidth()));
                button.setBackgroundColor(COLOR);
                Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/DROID.TTF");
                button.setTypeface(typeface);
                mMovesTextView.setTypeface(typeface);

                button.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        float x1 = 0;
                        float y1 = 0;
                        float x2 = 0, y2 = 0;

                        char swipeDirection = 'N';

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                mButtonIsDown = true;
                                x1 = event.getX();
                                y1 = event.getY();
                                System.out.println( "(" + x1 + "," + y1 + ")");
                                break;

                            case MotionEvent.ACTION_UP:
                                x2 = event.getX();
                                y2 = event.getY();
                                Log.d("OnTouch", "(" + x2 + "," + y2);

                                if (x2 - x1 > mButtonSize & x2 - x1 > y2 - y1) {
                                    swipeDirection = 'R';
                                } else if (x2 < 0 & x2 - x1 < y2 - y1) {
                                    swipeDirection = 'L';
                                } else if (y2 - y1 > mButtonSize & y2 - y1 > x2 - x1) {
                                    swipeDirection = 'D';
                                } else if (y2 < 0 & y2 - y1 < x2 - x1) {
                                    swipeDirection = 'U';
                                }

                                System.out.println(swipeDirection + "");
                                break;
                        }

                        String[] rowCol = v.getTag().toString().split(",");
                        int row = Integer.parseInt(rowCol[0]);
                        int col = Integer.parseInt(rowCol[1]);
                        char acceptedDirection = mGameField.canMove(row, col);

                        if (acceptedDirection != 'N' & acceptedDirection == swipeDirection) {
                            mGameField.movePuzzle(row, col, acceptedDirection);

                            if (SOUNDS) {
                                if (moveId != -1) {
                                    mSoundPool.play(moveId, 1, 1, 0, 0, 1);
                                }
                            }

                            mMoves++;
                            setMovesText();

                            updateButtons();

                            if (mGameField.checkIsWin()) {
                                Intent winIntent = new Intent(GameBoardActivity.this, WinActivity.class).putExtra(MOVES_EXTRA, mMoves);
                                startActivity(winIntent);
                                finish();
                            }
                        }


                        return false;
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String[] rowCol = view.getTag().toString().split(",");
                        int row = Integer.parseInt(rowCol[0]);
                        int col = Integer.parseInt(rowCol[1]);
                        char direction = mGameField.canMove(row, col);

                        if (direction != 'N') {
                            mGameField.movePuzzle(row, col, direction);

                            if (SOUNDS) {
                                if (moveId != -1) {
                                    mSoundPool.play(moveId, 1, 1, 0, 0, 1);
                                }
                            }

                            mMoves++;
                            setMovesText();

                            updateButtons();

                            if (mGameField.checkIsWin()) {
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

    @Override
    public void onBackPressed() {
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
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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

    private void updateButtons() {
        int count = 0x0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Button btn = (Button) findViewById(R.id.btn00 + count);
                if (mGameField.getPuzzle()[i][j] == 0) {
                    btn.setText(" ");
                    btn.setBackgroundColor(mButtonsField.getSolidColor());
                } else {
                    btn.setText("" + mGameField.getPuzzle()[i][j]);
                    btn.setBackgroundColor(COLOR);
                }
                count += 0x1;
            }

        }
    }

}
