package com.mad_project.chess;

import android.content.Context;
import android.widget.Toast;

import java.lang.Math;

import java.util.HashSet;
import java.util.Set;

public class Game {
    private final Context context;
    private final Set<ChessPiece> pieceBox = new HashSet<>();
    private Player playerTurn = Player.WHITE;
    private final Player playerColor;
    private ChessPiece wKing;
    private ChessPiece bKing;

    public Game(Context context, Player playerColor) {
        this.context = context;
        this.playerColor = playerColor;
        reset();
    }

    public void clear() {
        pieceBox.clear();
    }

    public void addPiece(ChessPiece piece) {
        pieceBox.add(piece);
    }

    private boolean canKnightMove(Position from, Position to) {
        return Math.abs(from.col - to.col) == 2 && Math.abs(from.row - to.row) == 1 ||
                Math.abs(from.col - to.col) == 1 && Math.abs(from.row - to.row) == 2;
    }

    private boolean canRookMove(Position from, Position to) {
        return from.col == to.col && isClearVerticallyBetween(from, to) || from.row == to.row && isClearHorizontallyBetween(from, to);
    }

    private boolean isClearVerticallyBetween(Position from, Position to) {
        if (from.col != to.col) return false;
        int gap = Math.abs(from.row - to.row) - 1;
        if (gap == 0) return true;

        for (int i = 1; i <= gap; ++i) {
            int nextRow = (to.row > from.row) ? from.row + i : from.row - i;
            if (pieceAt(new Position(from.col, nextRow)) != null) {
                return false;
            }
        }
        return true;
    }

    private boolean isClearHorizontallyBetween(Position from, Position to) {
        if (from.row != to.row) return false;
        int gap = Math.abs(from.col - to.col) - 1;
        if (gap == 0) return true;

        for (int i = 1; i <= gap; ++i) {
            int nextCol = (to.col > from.col) ? from.col + i : from.col - i;
            if (pieceAt(new Position(nextCol, from.row)) != null) {
                return false;
            }
        }
        return true;
    }

    private boolean isClearDiagonally(Position from, Position to) {
        if (Math.abs(from.col - to.col) != Math.abs(from.row - to.row)) return false;
        int gap = Math.abs(from.col - to.col) - 1;

        for (int i = 1; i <= gap; ++i) {
            int nextCol = (to.col > from.col) ? from.col + i : from.col - i;
            int nextRow = (to.row > from.row) ? from.row + i : from.row - i;
            if (pieceAt(new Position(nextCol, nextRow)) != null) {
                return false;
            }
        }
        return true;
    }

    private boolean canBishopMove(Position from, Position to) {
        if (Math.abs(from.col - to.col) == Math.abs(from.row - to.row)) {
            return isClearDiagonally(from, to);
        }
        return false;
    }

    private boolean canQueenMove(Position from, Position to) {
        return canRookMove(from, to) || canBishopMove(from, to);
    }

    private boolean canKingMove(Position from, Position to) {
        if (canQueenMove(from, to)) {
            int deltaCol = Math.abs(from.col - to.col);
            int deltaRow = Math.abs(from.row - to.row);
            return deltaCol == 1 && deltaRow == 1 || deltaCol + deltaRow == 1;
        }
        return false;
    }

    private boolean canPawnMove(Position from, Position to) {
        if (from.col == to.col) {
            if (from.row == 1 && pieceAt(from).player == Player.WHITE) {
                return to.row == 2 || to.row == 3;
            } else if (from.row == 6 && pieceAt(from).player == Player.BLACK) {
                return to.row == 5 || to.row == 4;
            } else if(from.row + 1 == to.row && pieceAt(to) == null && pieceAt(from).player == Player.WHITE) {
                return true;
            } else return from.row - 1 == to.row && pieceAt(to) == null && pieceAt(from).player == Player.BLACK;
        } else if((from.col + 1 == to.col && from.row + 1 == to.row) || (from.col - 1 == to.col && from.row + 1 == to.row)) {
            ChessPiece atTo = pieceAt(to);
            return (atTo != null && atTo.player == Player.BLACK);
        } else if((from.col + 1 == to.col && from.row - 1 == to.row) || (from.col - 1 == to.col && from.row - 1 == to.row)) {
            ChessPiece atTo = pieceAt(to);
            return (atTo != null && atTo.player == Player.WHITE);
        }
        return false;
    }

    private boolean isHorizontalCheck(Position kingPos) {
        ChessPiece kingPiece = pieceAt(kingPos);
        for(int i = kingPos.col - 1; i >= 0; --i) {
            ChessPiece at = pieceAt(new Position(i, kingPos.row));
            if(at != null && at.player != kingPiece.player && (at.pieceType == PieceType.ROOK || at.pieceType == PieceType.QUEEN))
                return true;
            else if(at != null)
                break;
        }
        for(int i = kingPos.col + 1; i < 8; ++i) {
            ChessPiece at = pieceAt(new Position(i, kingPos.row));
            if(at != null && at.player != kingPiece.player && (at.pieceType == PieceType.ROOK || at.pieceType == PieceType.QUEEN))
                return true;
            else if(at != null)
                break;
        }
        return false;
    }

    private boolean isVerticalCheck(Position kingPos) {
        ChessPiece kingPiece = pieceAt(kingPos);
        for(int i = kingPos.row - 1; i >= 0; --i) {
            ChessPiece at = pieceAt(new Position(kingPos.col, i));
            if(at != null && at.player != kingPiece.player && (at.pieceType == PieceType.ROOK || at.pieceType == PieceType.QUEEN))
                return true;
            else if(at != null)
                break;
        }
        for(int i = kingPos.row + 1; i < 8; ++i) {
            ChessPiece at = pieceAt(new Position(kingPos.col, i));
            if(at != null && at.player != kingPiece.player && (at.pieceType == PieceType.ROOK || at.pieceType == PieceType.QUEEN))
                return true;
            else if(at != null)
                break;
        }
        return false;
    }

    private boolean isDiagonallyCheck(Position kingPos) {
//        for(int i = 0; i < 8; ++i) {
//            if(PieceType)
//        }
//        if (Math.abs(from.col - to.col) != Math.abs(from.row - to.row)) return false;
//        int gap = Math.abs(from.col - to.col) - 1;
//
//        for (int i = 1; i <= gap; ++i) {
//            int nextCol = (to.col > from.col) ? from.col + i : from.col - i;
//            int nextRow = (to.row > from.row) ? from.row + i : from.row - i;
//            if (pieceAt(new Position(nextCol, nextRow)) != null) {
//                return false;
//            }
//        }
        return false;
    }

    private boolean isCheck(Position kingPos) {
        boolean isCheck = isHorizontalCheck(kingPos) || isVerticalCheck(kingPos);
        if(isCheck) {
            Toast.makeText(context, "King checked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private boolean canMove(Position from, Position to) {
//        if (playerTurn != playerColor) return false;

        if (from.col == to.col && from.row == to.row) {
            return  false;
        }

        ChessPiece movingPiece = pieceAt(from);
        if(movingPiece != null) {
//            if (movingPiece.player != playerColor) return false;

            switch (movingPiece.pieceType) {
                case KING:
                    return canKingMove(from, to);
                case PAWN:
                    return canPawnMove(from, to);
                case ROOK:
                    return canRookMove(from, to);
                case QUEEN:
                    return canQueenMove(from, to);
                case BISHOP:
                    return canBishopMove(from, to);
                case KNIGHT:
                    return canKnightMove(from, to);
            }
        }
        return false;
    }

    void reset() {
        clear();
        for (int i = 0; i < 2; ++i) {
            addPiece(new ChessPiece(0 + i * 7, 0, PieceType.ROOK, Player.WHITE, R.drawable.w_rook));
            addPiece(new ChessPiece(0 + i * 7, 7, PieceType.ROOK, Player.BLACK, R.drawable.b_rook));

            addPiece(new ChessPiece(1 + i * 5, 0, PieceType.KNIGHT, Player.WHITE, R.drawable.w_knight));
            addPiece(new ChessPiece(1 + i * 5, 7, PieceType.KNIGHT, Player.BLACK, R.drawable.b_knight));

            addPiece(new ChessPiece(2 + i * 3, 0, PieceType.BISHOP, Player.WHITE, R.drawable.w_bishop));
            addPiece(new ChessPiece(2 + i * 3, 7, PieceType.BISHOP, Player.BLACK, R.drawable.b_bishop));
        }

        for (int i = 0; i < 8; ++i) {
            addPiece(new ChessPiece(i, 1, PieceType.PAWN, Player.WHITE, R.drawable.w_pawn));
            addPiece(new ChessPiece(i, 6, PieceType.PAWN, Player.BLACK, R.drawable.b_pawn));
        }

        addPiece(new ChessPiece(3, 0, PieceType.QUEEN, Player.WHITE, R.drawable.w_queen));
        addPiece(new ChessPiece(3, 7, PieceType.QUEEN, Player.BLACK, R.drawable.b_queen));

        wKing = new ChessPiece(4, 0, PieceType.KING, Player.WHITE, R.drawable.w_king);
        bKing = new ChessPiece(4, 7, PieceType.KING, Player.BLACK, R.drawable.b_king);
        addPiece(wKing);
        addPiece(bKing);
    }

    public ChessPiece pieceAt(Position position) {
        for (ChessPiece piece : pieceBox) {
            if (position.col == piece.col && position.row == piece.row) {
                return piece;
            }
        }
        return null;
    }

    public void movePiece(Position from, Position to) {
        if (canMove(from, to)) {
            if (from.col == to.col && from.row == to.row) return;
            ChessPiece movingPiece = pieceAt(new Position(from.col, from.row));
            if(movingPiece != null) {
                ChessPiece pieceAt = pieceAt(new Position(to.col, to.row));
                if(pieceAt != null) {
                    if(pieceAt.player == movingPiece.player) {
                        return;
                    }
                    pieceBox.remove(pieceAt);
                }

                pieceBox.remove(movingPiece);
                ChessPiece newPiece = new ChessPiece(movingPiece);
                newPiece.col = to.col;
                newPiece.row = to.row;

                if(newPiece.pieceType == PieceType.KING && newPiece.player == Player.WHITE)
                    wKing = newPiece;
                else if(newPiece.pieceType == PieceType.KING && newPiece.player == Player.BLACK)
                    bKing = newPiece;

                addPiece(newPiece);

                isCheck(new Position(wKing.col, wKing.row));
                isCheck(new Position(bKing.col, bKing.row));
                flipTurn();
            }
        }
    }

    public void recievedMovePiece(Position from, Position to) {
        if (canMove(from, to)) {
            if (from.col == to.col && from.row == to.row) return;
            ChessPiece movingPiece = pieceAt(new Position(from.col, from.row));
            if(movingPiece != null) {
                ChessPiece pieceAt = pieceAt(new Position(to.col, to.row));
                if(pieceAt != null) {
//                    if(pieceAt.player != movingPiece.player) {
//                        return;
//                    }
                    pieceBox.remove(pieceAt);
                }

                pieceBox.remove(movingPiece);
                ChessPiece newPiece = new ChessPiece(movingPiece);
                newPiece.col = to.col;
                newPiece.row = to.row;
                addPiece(newPiece);

                flipTurn();
            }
        }
    }

    public void flipTurn() {
        if(playerTurn == Player.WHITE)
            playerTurn = Player.BLACK;
        else
            playerTurn = Player.WHITE;
    }
}
