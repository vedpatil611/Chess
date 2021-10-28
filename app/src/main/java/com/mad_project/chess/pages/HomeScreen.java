package com.mad_project.chess.pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.mad_project.chess.Player;
import com.mad_project.chess.R;
import com.mad_project.chess.dialog.JoinGameDialog;

public class HomeScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public void createGame(View v) {
        Intent intent = new Intent(this, ChessGame.class);
        intent.putExtra("playerColor", Player.BLACK.value);
        startActivity(intent);
    }

    public void joinGame(View v) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        JoinGameDialog joinGameDialog = new JoinGameDialog(this);
//        joinGameDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        joinGameDialog.getWindow().setLayout(width, height);
        joinGameDialog.show();
//        Intent intent = new Intent(this, ChessGame.class);
//        intent.putExtra("playerColor", Player.BLACK.value);
//        startActivity(intent);
    }
}
