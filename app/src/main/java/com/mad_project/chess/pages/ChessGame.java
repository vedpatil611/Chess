package com.mad_project.chess.pages;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;

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
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class ChessGame extends Activity implements ChessInterface {

    String socketHost = "127.0.0.1";
    int socketPort = 50000;
    int socketGuestPort = 50001;

    PrintWriter printWriter = null;
    ServerSocket serverSocket = null;
    boolean isEmulator = Build.FINGERPRINT.contains("generic");

    Game game = new Game(Player.WHITE);
    ChessBoard board;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_view);

        board = findViewById(R.id.game_view);
        board.setChessInterface(this);

        int port = isEmulator ? socketGuestPort : socketPort;
        Executors.newSingleThreadExecutor().execute((() -> {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ChessGame.this.serverSocket = serverSocket;

            try {
                Socket socket = serverSocket.accept();
                receiveMove(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    void receiveMove(Socket socket) throws IOException {
        Scanner sc = new Scanner(socket.getInputStream());
        printWriter = new PrintWriter(socket.getOutputStream(), true);

        while (sc.hasNextLine()) {
            String movesStr[] = sc.nextLine().split(",");
            int[] move = new int[movesStr.length];
            for(int i = 0; i < 4; ++i) {
                move[i] = Integer.parseInt(movesStr[i]);
            }

            runOnUiThread(() -> {
                game.movePiece(new Position(move[0], move[1]), new Position(move[2], move[3]));
                board.invalidate();
            });
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
            PrintWriter var3 = printWriter;
            String moveStr = "" + from.col + ',' + from.row + ',' + to.col + ',' + to.row;
            Executors.newSingleThreadExecutor().execute(() -> {
                printWriter.println(moveStr);
            });
        }
    }
}
