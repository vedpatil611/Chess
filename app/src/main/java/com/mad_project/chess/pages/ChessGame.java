package com.mad_project.chess.pages;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.mad_project.chess.R;

public class ChessGame extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_view);
    }
}
