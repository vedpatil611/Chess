package com.mad_project.chess.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.mad_project.chess.Player;
import com.mad_project.chess.R;
import com.mad_project.chess.pages.ChessGame;

public class JoinGameDialog extends Dialog {
    Context context;
    Button join, cancel;
    EditText ipField;
    public JoinGameDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_join_game);

        ipField = findViewById(R.id.ip_address_text);
        join = findViewById(R.id.button_join);
        cancel = findViewById(R.id.button_cancel);

        cancel.setOnClickListener(view -> {
            dismiss();
        });

        join.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChessGame.class);
            intent.putExtra("playerColor", Player.WHITE.value);
            intent.putExtra("host", ipField.getText().toString());
            context.startActivity(intent);
            dismiss();
        });
    }
}
