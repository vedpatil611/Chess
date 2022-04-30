package com.mad_project.chess;

import androidx.annotation.NonNull;

public class ChessPiece implements Cloneable {
    public int col;
    public int row;
    public PieceType pieceType;
    public Player player;
    public int resId;

    public ChessPiece(int col, int row, PieceType pieceType, Player player, int resId) {
        this.row = row;
        this.col = col;
        this.pieceType = pieceType;
        this.player = player;
        this.resId = resId;
    }

    public ChessPiece(ChessPiece chessPiece) {
        this.row = chessPiece.row;
        this.col = chessPiece.col;
        this.pieceType = chessPiece.pieceType;
        this.player = chessPiece.player;
        this.resId = chessPiece.resId;
    }
}
