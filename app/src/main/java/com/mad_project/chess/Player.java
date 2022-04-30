package com.mad_project.chess;

public enum Player {
    WHITE(0),
    BLACK(1);

    public final int value;

    Player(int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }
}
