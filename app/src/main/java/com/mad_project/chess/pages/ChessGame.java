package com.mad_project.chess.pages;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.mad_project.chess.ChessPiece;
import com.mad_project.chess.Game;
import com.mad_project.chess.Player;
import com.mad_project.chess.Position;
import com.mad_project.chess.R;
import com.mad_project.chess.views.ChessBoard;
import com.mad_project.chess.views.ChessInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class ChessGame extends Activity implements ChessInterface {

    String socketHost = "127.0.0.1";
    int socketPort = 8080;
    int socketGuestPort = 8080;

    PrintWriter printWriter = null;
    ServerSocket serverSocket = null;
    Socket socket = null;
    boolean isEmulator = Build.FINGERPRINT.contains("generic");

    Game game = new Game(Player.WHITE);
    ChessBoard board;

    Player playerColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.board_view);

        board = findViewById(R.id.game_view);
        board.setChessInterface(this);

        int port = isEmulator ? socketGuestPort : socketPort;

        int intentPLayerValue = getIntent().getIntExtra("playerColor", Player.WHITE.value);
        switch (intentPLayerValue) {
            case 1:
                playerColor = Player.BLACK;
                game = new Game(playerColor);

                // start game server
                Executors.newSingleThreadExecutor().execute((() -> {
                    ServerSocket serverSocket;
                    try {
                        serverSocket = new ServerSocket(port);
                        ChessGame.this.serverSocket = serverSocket;

                        socket = serverSocket.accept();
                        receiveMove(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


//                    try {
//                        socket = serverSocket.accept();
//                        receiveMove(socket);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }));
                break;
            case 0:
                playerColor = Player.WHITE;
                socketHost = getIntent().getStringExtra("host");
                game = new Game(playerColor);

                // join game server
                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        socket = new Socket(socketHost, socketPort);
                        receiveMove(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
//                        ChessGame.this.onBackPressed();
                    }
                });
                break;
        }

//        switch (playerColor) {
//            case WHITE:
//                Executors.newSingleThreadExecutor().execute((() -> {
//                    ServerSocket serverSocket = null;
//                    try {
//                        serverSocket = new ServerSocket(port);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    ChessGame.this.serverSocket = serverSocket;
//
//                    try {
//                        Socket socket = serverSocket.accept();
//                        receiveMove(socket);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }));
//                break;
//            case BLACK:
//                Executors.newSingleThreadExecutor().execute(() -> {
//                    Socket socket;
//                    try {
//                        socket = new Socket(socketHost, socketPort);
//                        receiveMove(socket);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//                break;
//        }
    }

    void receiveMove(Socket socket) throws IOException {
        Scanner sc = new Scanner(socket.getInputStream());
        printWriter = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            if(sc.hasNextLine()) {
                String[] movesStr = sc.nextLine().split(",");
                int[] move = new int[movesStr.length];
                for (int i = 0; i < 4; ++i) {
                    move[i] = Integer.parseInt(movesStr[i]);
                }

                runOnUiThread(() -> {
                    game.movePiece(new Position(move[0], move[1]), new Position(move[2], move[3]));
                    board.invalidate();
                    game.flipTurn();
                });
            }
        }
    }

    @Override
    public ChessPiece pieceAt(Position position) {
        return game.pieceAt(position);
    }

    @Override
    public void movePiece(Position from, Position to) {
        game.movePiece(from, to);
        board.invalidate();

        PrintWriter printWriter = this.printWriter;
        if (printWriter != null) {
            String moveStr = "" + from.col + ',' + from.row + ',' + to.col + ',' + to.row;
            Executors.newSingleThreadExecutor().execute(() -> {
                printWriter.println(moveStr);
            });
        }
    }
}
