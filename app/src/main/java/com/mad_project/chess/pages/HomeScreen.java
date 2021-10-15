package com.mad_project.chess.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mad_project.chess.Player;
import com.mad_project.chess.R;

public class HomeScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
    }

    public void createGame(View v) {
        Intent intent = new Intent(this, ChessGame.class);
        intent.putExtra("playerColor", Player.WHITE.ordinal());
        startActivity(new Intent(this, ChessGame.class));
    }

    public void joinGame(View v) {
        Intent intent = new Intent(this, ChessGame.class);
        intent.putExtra("playerColor", Player.BLACK.ordinal());
        startActivity(new Intent(this, ChessGame.class));
    }
}
