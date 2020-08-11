package com.mapplic.slots.logic;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mapplic.slots.R;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;

public class IgraActivity extends AppCompatActivity {

    private static final String CREDIT_EXTRA = "rreee";
    private static final String BET_INDEX_EXTRA = "222wwww";
    private int mScores;
    private int mBetIndex;
    private int[] mBets;

    private TextView mCreditView;
    private TextView mBetView;
    private Button mSpinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mSpinButton = findViewById(R.id.btn_spin);
        mCreditView = findViewById(R.id.score);
        mBetView = findViewById(R.id.bet);
        mBets = getResources().getIntArray(R.array.bets_array);

        if (savedInstanceState == null) {
            mScores = getResources().getInteger(R.integer.default_score);

            setScoreAmounts();
        }

        setupSlotsMachine();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CREDIT_EXTRA, mScores);
        outState.putInt(BET_INDEX_EXTRA, mBetIndex);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mScores = savedInstanceState.getInt(CREDIT_EXTRA);
        mBetIndex = savedInstanceState.getInt(BET_INDEX_EXTRA);
        setScoreAmounts();
    }

    private void decreaseScores() {
        mScores -= mBets[mBetIndex];
        if (mBets[mBetIndex] > mScores) mBetIndex = 0;
        setScoreAmounts();
        if (mScores == 0) mSpinButton.setEnabled(false);

    }

    private void updateBet() {
        int size = mBets.length;
        mBetIndex = mBetIndex >= size - 1 ? 0 : mBetIndex + 1;
        if (mBets[mBetIndex] > mScores) mBetIndex = 0;
        setScoreAmounts();
    }

    private void setScoreAmounts() {
        mCreditView.setText(String.valueOf(mScores));
        mBetView.setText(String.valueOf(mBets[mBetIndex]));
    }

    private void setupSlotsMachine() {
        final CasinoBuilder.Builder builder = CasinoBuilder.builder(this);
        final CasinoBuilder slots = builder
                .addSlots(R.id.slot_one, R.id.slot_two, R.id.slot_three, R.id.slot_four, R.id.slot_five)
                .addDrawables(R.drawable.item1, R.drawable.item2, R.drawable.item3, R.drawable.item4)
                .setScrollTimePerInch(1f)
                .setDockingTimePerInch(0f)
                .setScrollTime(500 + new SecureRandom().nextInt(1500))
                .setChildIncTime(1000)
                .setOnFinishListener(new Callback() {
                    @Override
                    public void OnFinishListener() {

                        List<LinearLayoutManager> layoutManagers = getLayoutManagers();


                        Map<Integer, Integer> match = new HashMap<>(4);

                        for (int i = 0; i < 5; i++) {
                            ImageView imageView = (ImageView) layoutManagers.get(i)
                                    .findViewByPosition(layoutManagers.get(i)
                                            .findFirstVisibleItemPosition() + 3);
                            Integer drawableId = (Integer) imageView.getTag();

                            if (match.containsKey(drawableId)) {
                                match.put(drawableId, match.get(drawableId) + 1);
                            } else {
                                match.put(drawableId, 1);
                            }
                        }

                        int resultMatch = 0;
                        for (Integer val : match.values()) {
                            if (resultMatch < val) {
                                resultMatch = val;
                            }
                        }

                        if (resultMatch >= 3 && !mSpinButton.isEnabled()) {
                            mSpinButton.setEnabled(true);
                        }

                        switch (resultMatch) {
                            case 3:
                                mScores += mBets[mBetIndex] * 2;
                                break;
                            case 4:
                                mScores += mBets[mBetIndex] * 3;
                                break;
                            case 5:
                                mScores += mBets[mBetIndex] * 4;
                                break;
                            default:
                                if (mScores <= 0) {
                                    showScoresDialog();
                                }
                        }
                        setScoreAmounts();

                    }
                })
                .build();

        findViewById(R.id.btn_menu).setOnClickListener(v -> startActivity(MenuActivity.class));
        findViewById(R.id.btn_bet).setOnClickListener(v -> updateBet());
        mSpinButton.setOnClickListener(v -> {
            decreaseScores();
            slots.start();
        });
    }

    private void showScoresDialog() {
        PrettyDialog dialog = new PrettyDialog(this)
                .setIcon(R.drawable.pdlg_icon_info)
                .setTitle(getString(R.string.game_result))
                .setMessage(getString(R.string.point_balance))
                .addButton(
                        getString(R.string.play),
                        R.color.pdlg_color_white,
                        R.color.pdlg_color_green,
                        () -> startActivity(IgraActivity.class)
                )
                .addButton(
                        getString(R.string.title_menu),
                        R.color.pdlg_color_white,
                        R.color.pdlg_color_red,
                        () -> startActivity(MenuActivity.class)
                );
        dialog.setCancelable(false);
        dialog.show();
    }

    private void startActivity(Class<? extends AppCompatActivity> targetActivity) {
        Intent intent = new Intent(IgraActivity.this, targetActivity);
        startActivity(intent);
        IgraActivity.this.finish();
    }
}
