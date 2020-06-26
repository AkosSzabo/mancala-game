package com.akosszabo.demo.mancala.domain;

public enum Player {
    PLAYER1, PLAYER2;

    public Player next() {
        if (this == PLAYER1) {
            return PLAYER2;
        } else {
            return PLAYER1;
        }
    }
}
