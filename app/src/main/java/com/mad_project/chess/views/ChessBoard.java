package com.mad_project.chess.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ChessBoard extends View {

    Paint paint;
    int lightColor, darkColor;
    int originX = 20;
    int originY = 500;
    int cellSize = 200;

    public ChessBoard(Context context) {
        super(context);
        paint = new Paint();
        lightColor = Color.parseColor("#EEEEEE");
        darkColor = Color.parseColor("#BBBBBB");
    }

    public ChessBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        lightColor = Color.parseColor("#EEEEEE");
        darkColor = Color.parseColor("#BBBBBB");
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
    }

    void drawSquare(Canvas canvas, int col, int row, boolean isBlack) {
        paint.setColor(isBlack ? darkColor : lightColor);
        canvas.drawRect(originX + col * cellSize, originY + row * cellSize,
                originX + (col + 1) * cellSize, originY + (row - 1) * cellSize, paint);
    }

    void drawBoard(Canvas canvas) {
        for(int row = 0; row < 8; ++row) {
            for(int col = 0; col < 8; ++col) {
                drawSquare(canvas, col, row, (col + row) % 2 == 1);
            }
        }
    }
}
