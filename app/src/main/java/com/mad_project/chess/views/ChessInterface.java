package com.mad_project.chess.views;

import com.mad_project.chess.ChessPiece;
import com.mad_project.chess.Position;

public interface ChessInterface {
    ChessPiece pieceAt(Position position);
    void movePiece(Position from, Position to);
}
