package com.memoink.trainingmemory;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainGamePart extends Fragment implements View.OnClickListener {
    public static final String COUNTER_KEY = "COUNTER";
    private EndGamePart endGamePart;
    private View view;
    private Bundle bundle;
    private Resources resources;
    private boolean isGameRunning;
    private AtomicInteger gameLvl;
    private AtomicInteger answerCounter;
    private AtomicInteger scoreCounter;
    private TextView scoreView;
    private Drawable visible;
    private Drawable invisible;
    private List<Button> buttons;
    private List<Button> expectAns;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        endGamePart = new EndGamePart();
        view = inflater.inflate(R.layout.game_fragment, container, false);
        bundle = getArguments();
        resources = getResources();
        isGameRunning = true;
        buttons = new ArrayList<>();
        gameLvl = new AtomicInteger(1);
        answerCounter = new AtomicInteger(1);
        scoreCounter = new AtomicInteger(0);
        visible = ResourcesCompat.getDrawable(resources, R.drawable.active_button, null);
        invisible = ResourcesCompat.getDrawable(resources, R.drawable.inactive_button, null);
        expectAns = new ArrayList<>();
        initButtons();
        scoreView = view.findViewById(R.id.scoreView);
        initButtonList();
        game();
        return view;
    }

    @Override
    public void onClick(View v) {
        Button received = view.findViewById(v.getId());
        if (answerCounter.getAndIncrement() <= expectAns.size()) {
            int receivedId = received.getId();
            int expectId = expectAns.get(answerCounter.get() - 2).getId();
            if (receivedId == expectId) {
                String scoreText = resources.getString(R.string.score);
                int score = scoreCounter.getAndIncrement() * 10;
                scoreView.setText(String.format(scoreText, score));
            } else {
                gameLvl.set(1);
                answerCounter.set(1);
                expectAns = new ArrayList<>();
                endGame();
            }
            if (answerCounter.get() - 1 == expectAns.size() && isGameRunning) {
                gameLvl.incrementAndGet();
                answerCounter.set(1);
                game();
            }
        }
    }

    private void game() {
        String scoreText = resources.getString(R.string.score);
        int score = scoreCounter.get() * 10;
        scoreView.setText(String.format(scoreText, score));
        bundle.putInt(COUNTER_KEY, score);
        expectAns.add(randomizeButton());
        final ListIterator<Button> buttonListIterator = expectAns.listIterator();
        Handler handler = new Handler();
        for (int i = 0; i < gameLvl.get(); i++) {
            if (buttonListIterator.hasNext()) {
                handler.postDelayed(() -> {
                    if (isGameRunning) {
                        Button b = buttonListIterator.next();
                        b.setBackground(visible);
                        new Handler().postDelayed(() -> {
                            b.setBackground(invisible);
                        }, 700);
                    }
                }, 1000 * i);
            }
        }
    }

    private void endGame() {
        isGameRunning = false;
        ((MainActivity)getActivity()).loadFragment(endGamePart);
    }

    private void initButtons() {
        button1 = view.findViewById(R.id.button1);
        button1.setBackground(invisible);
        button1.setOnClickListener(this);
        button2 = view.findViewById(R.id.button2);
        button2.setBackground(invisible);
        button2.setOnClickListener(this);
        button3 = view.findViewById(R.id.button3);
        button3.setBackground(invisible);
        button3.setOnClickListener(this);
        button4 = view.findViewById(R.id.button4);
        button4.setBackground(invisible);
        button4.setOnClickListener(this);
        button5 = view.findViewById(R.id.button5);
        button5.setBackground(invisible);
        button5.setOnClickListener(this);
        button6 = view.findViewById(R.id.button6);
        button6.setBackground(invisible);
        button6.setOnClickListener(this);
        button7 = view.findViewById(R.id.button7);
        button7.setBackground(invisible);
        button7.setOnClickListener(this);
        button8 = view.findViewById(R.id.button8);
        button8.setBackground(invisible);
        button8.setOnClickListener(this);
        button9 = view.findViewById(R.id.button9);
        button9.setBackground(invisible);
        button9.setOnClickListener(this);
    }

    private void initButtonList(){
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        buttons.add(button4);
        buttons.add(button5);
        buttons.add(button6);
        buttons.add(button7);
        buttons.add(button8);
        buttons.add(button9);
    }

    private Button randomizeButton() {
        Random random = new Random();
        int randIdx = random.nextInt(9);
        return buttons.get(randIdx);
    }


}
