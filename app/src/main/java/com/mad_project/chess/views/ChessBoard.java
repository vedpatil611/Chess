package com.mad_project.chess.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mad_project.chess.ChessPiece;
import com.mad_project.chess.Position;
import com.mad_project.chess.R;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ChessBoard extends View {

    Paint paint;
    int lightColor, darkColor;
    int originX = 20;
    int originY = 500;
    int cellSize = 200;

    HashMap<Integer, Bitmap> bitmaps = new HashMap<>();
    Bitmap movingPieceBitmap = null;
    ChessPiece movingPiece = null;
    byte fromCol = -1;
    byte fromRow = -1;
    float movingPieceX = -1.0f;
    float movingPieceY = -1.0f;

    ChessInterface chessInterface = null;

    Set<Integer> imgResIds = new HashSet<>();

    public ChessBoard(Context context) {
        super(context);
        paint = new Paint();
        lightColor = Color.parseColor("#EEEEEE");
        darkColor = Color.parseColor("#BBBBBB");

        imgResIds.add(R.drawable.b_bishop);
        imgResIds.add(R.drawable.b_king);
        imgResIds.add(R.drawable.b_knight);
        imgResIds.add(R.drawable.b_queen);
        imgResIds.add(R.drawable.b_rook);
        imgResIds.add(R.drawable.b_pawn);

        imgResIds.add(R.drawable.w_bishop);
        imgResIds.add(R.drawable.w_king);
        imgResIds.add(R.drawable.w_knight);
        imgResIds.add(R.drawable.w_queen);
        imgResIds.add(R.drawable.w_rook);
        imgResIds.add(R.drawable.w_pawn);

        loadBitmaps();
    }

    public ChessBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        lightColor = Color.parseColor("#EEEEEE");
        darkColor = Color.parseColor("#BBBBBB");

        imgResIds.add(R.drawable.b_bishop);
        imgResIds.add(R.drawable.b_king);
        imgResIds.add(R.drawable.b_knight);
        imgResIds.add(R.drawable.b_queen);
        imgResIds.add(R.drawable.b_rook);
        imgResIds.add(R.drawable.b_pawn);

        imgResIds.add(R.drawable.w_bishop);
        imgResIds.add(R.drawable.w_king);
        imgResIds.add(R.drawable.w_knight);
        imgResIds.add(R.drawable.w_queen);
        imgResIds.add(R.drawable.w_rook);
        imgResIds.add(R.drawable.w_pawn);

        loadBitmaps();
    }

    public void setChessInterface(ChessInterface chessInterface) {
        this.chessInterface = chessInterface;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int displayWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        int displayHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        cellSize = Math.min(displayHeight, displayWidth);
        cellSize -= 40;
        cellSize /= 8;

        drawBoard(canvas);
        drawPiece(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null) return false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            {
                fromCol = (byte) ((event.getX() - originX) / cellSize);
                fromRow = (byte) (7 - ((event.getY() - originY) / cellSize));

                if(chessInterface != null) {
                    movingPiece = chessInterface.pieceAt(new Position(fromCol, fromRow));
                    if(movingPiece != null) {
                        movingPieceBitmap = bitmaps.get(movingPiece.resId);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                movingPieceX = event.getX();
                movingPieceY = event.getY();
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                byte col = (byte) ((event.getX() - originX) / cellSize);
                byte row = (byte) (7 - ((event.getY() - originY) / cellSize));
                if (fromCol != col || fromRow != row) {
                    chessInterface.movePiece(new Position(fromCol, fromRow), new Position(col, row));
                }
                movingPiece = null;
                movingPieceBitmap = null;
                invalidate();
                break;
            }
        }
        return true;
    }

    void drawSquare(Canvas canvas, byte col, byte row, boolean isBlack) {
        paint.setColor(isBlack ? darkColor : lightColor);
        canvas.drawRect(originX + col * cellSize, originY + row * cellSize,
                originX + (col + 1) * cellSize, originY + (row - 1) * cellSize, paint);
    }

    void drawBoard(Canvas canvas) {
        for(byte row = 0; row < 8; ++row) {
            for(byte col = 0; col < 8; ++col) {
                drawSquare(canvas, col, row, (col + row) % 2 == 1);
            }
        }
    }

    void drawPieceAt(Canvas canvas, byte col, byte row, int resId) {
        canvas.drawBitmap(bitmaps.get(resId), null, new RectF(originX + col * cellSize, originY + (7 - row - 1) * cellSize,
                originX + (col + 1) * cellSize, originY + (7 - row) * cellSize), paint);
    }

    void drawPiece(Canvas canvas) {
        for(byte row = 0; row < 8; ++row) {
            for(byte col = 0; col < 8; ++col) {
                if(chessInterface != null) {
                    ChessPiece piece = chessInterface.pieceAt(new Position(col, row));
                    if(piece != movingPiece && piece != null) {
                        drawPieceAt(canvas, col, row, piece.resId);
                    }
                }
            }
        }
    }

    void loadBitmaps() {
        for(Integer id: imgResIds) {
            bitmaps.put(id, BitmapFactory.decodeResource(getContext().getResources(), id));
        }
    }
}
