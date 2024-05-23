package com.example.racinggame;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class RacingActivity extends AppCompatActivity implements OnListenerClickCheckbox {

    private SeekBar mSeekBar1, mSeekBar2, mSeekBar3;
    private CheckBox mCheckBox1, mCheckBox2, mCheckBox3;
    private TextView mTvCountDown, mTvResult;
    private ImageView mResultImage;
    private Dialog mDialog;
    private Button mButtonPlayAgain, mButtonExit;
    private CountDownTimer mCountDownTimer, mCountDownTimer2;
    private OnListenerClickCheckbox mOnListenerClickCheckbox;
    private int mCountDownNumber;

    private int mCarWin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_racing);

        //Ánh xạ các View Vào
        mCheckBox1 = findViewById(R.id.checkbox_1);
        mCheckBox2 = findViewById(R.id.checkbox_2);
        mCheckBox3 = findViewById(R.id.checkbox_3);
        mSeekBar1 = findViewById(R.id.seekBar1);
        mSeekBar2 = findViewById(R.id.seekBar2);
        mSeekBar3 = findViewById(R.id.seekBar3);
        mTvCountDown = findViewById(R.id.countDownNumber);
        mDialog = new Dialog(this);
        mOnListenerClickCheckbox = this;

        //Ánh xạ Dialog
        mDialog.setContentView(R.layout.alert_winner_dialog);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.getWindow().getAttributes().windowAnimations =
                android.R.style.Animation_Dialog;
        mButtonPlayAgain = mDialog.findViewById(R.id.btn_playAgain);
        mButtonExit = mDialog.findViewById(R.id.btn_exit);
        mTvResult = mDialog.findViewById(R.id.tv_result);
        mResultImage = mDialog.findViewById(R.id.img_result);

        //Kiểm tra tương tác của người dùng
        mCheckBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mOnListenerClickCheckbox.OnCheckBoxItemCheck(1);
                } else {
                    mOnListenerClickCheckbox.OnCheckBoxItemCheck(-1);
                }
                mCheckBox2.setChecked(false);
                mCheckBox3.setChecked(false);
            }
        });

        mCheckBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mOnListenerClickCheckbox.OnCheckBoxItemCheck(2);
                } else {
                    mOnListenerClickCheckbox.OnCheckBoxItemCheck(-1);
                }
                mCheckBox1.setChecked(false);
                mCheckBox3.setChecked(false);
            }
        });

        mCheckBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mOnListenerClickCheckbox.OnCheckBoxItemCheck(3);
                } else {
                    mOnListenerClickCheckbox.OnCheckBoxItemCheck(-1);
                }
                mCheckBox1.setChecked(false);
                mCheckBox2.setChecked(false);
            }
        });


    }

    private void startPlaying() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mSeekBar1.setProgress(0);
            mSeekBar2.setProgress(0);
            mSeekBar3.setProgress(0);
            mCountDownTimer = null;
        }
        if (mCheckBox1.isChecked() || mCheckBox2.isChecked() || mCheckBox3.isChecked()) {
            mCheckBox1.setEnabled(false);
            mCheckBox2.setEnabled(false);
            mCheckBox3.setEnabled(false);
            mCountDownNumber = 4;
            //sau 3s thì mới được start
            mCountDownTimer2 = new CountDownTimer(4000, 1000) {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTick(long millisUntilFinished) {
                    mTvCountDown.setVisibility(View.VISIBLE);
                    mCountDownNumber = mCountDownNumber - 1;
                    mTvCountDown.setText(mCountDownNumber + "");
                }

                @Override
                public void onFinish() {
                    playGame();
                }
            };
            mCountDownTimer2.start();
        }
    }

    private void playGame() {
        mTvCountDown.setVisibility(View.INVISIBLE);
        mCountDownTimer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mSeekBar1.setProgress(mSeekBar1.getProgress() + getRandomNumber(10));
                mSeekBar2.setProgress(mSeekBar2.getProgress() + getRandomNumber(10));
                mSeekBar3.setProgress(mSeekBar3.getProgress() + getRandomNumber(10));
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                if (mSeekBar1.getProgress() < 100 && mSeekBar2.getProgress() < 100 && mSeekBar3.getProgress() < 100) {
                    mCountDownTimer.start();
                    return;
                }

                this.cancel();

                mCheckBox1.setEnabled(true);
                mCheckBox2.setEnabled(true);
                mCheckBox3.setEnabled(true);

                if (mCheckBox1.isChecked() && mSeekBar1.getProgress() >= 100) {
                    mResultImage.setImageResource(R.drawable.ic_winner_cup);
                    mTvResult.setText("You Win!!!");
                    mDialog.show();
                } else if (mCheckBox2.isChecked() && mSeekBar2.getProgress() >= 100) {
                    mResultImage.setImageResource(R.drawable.ic_winner_cup);
                    mTvResult.setText("You Win!!!");
                    mDialog.show();
                } else if (mCheckBox3.isChecked() && mSeekBar3.getProgress() >= 100) {
                    mResultImage.setImageResource(R.drawable.ic_winner_cup);
                    mTvResult.setText("You Win!!!");
                    mDialog.show();
                } else {
                    mResultImage.setImageResource(R.drawable.ic_game_over);
                    mTvResult.setText("You Lost!!!");
                    mDialog.show();

                    String mCarWinString;

                    switch (mCarWin) {
                        case 0:
                            mCarWinString = "First Car Win";
                            break;

                        case 1:
                            mCarWinString = "Second Car Win";
                            break;

                        case 2:
                            mCarWinString = "Third Car Win";
                            break;

                        default:
                            mCarWinString = "Error";
                            break;
                    }

                    Toast.makeText(RacingActivity.this, mCarWinString, Toast.LENGTH_SHORT).show();
                }

                playAgain();
                exitApp();
            }
        };

        mCountDownTimer.start();
    }

    //Function tạo ra số random
    private int getRandomNumber(int bound) {
        return new Random().nextInt(bound);
    }

    //    Dialog Play again button function
    private void playAgain() {
        mButtonPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar1.setProgress(0);
                mSeekBar2.setProgress(0);
                mSeekBar3.setProgress(0);
                mCheckBox1.setChecked(false);
                mCheckBox2.setChecked(false);
                mCheckBox3.setChecked(false);
                mDialog.cancel();
                mCountDownTimer.cancel();
                mCountDownTimer2.cancel();
                mCountDownTimer = null;
                mCountDownTimer2 = null;
            }
        });
    }

    //    Dialog Exit button function
    private void exitApp() {
        mButtonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /*
    Interface function cho việc tương tác với checkbox
    - Kiểm tra vị trí nếu mà onCheckBoxItemCheck mà lớn hơn -1 thì sẽ chạy hàm
     */
    @Override
    public void OnCheckBoxItemCheck(int position) {
        if (position > -1) {
            startPlaying();
        }
    }
}
