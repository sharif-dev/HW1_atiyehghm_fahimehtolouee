package com.atiyehandfahimeh.hw1.Network;

import java.util.HashMap;
import java.util.Map;

public enum State{
    FAIL(0), SUCCESS(1);
    private int value;
    private static Map map = new HashMap<>();

    State(final int state) {
        this.value = state;
    }
    static {
        for (State state : State.values()) {
            map.put(state.value, state);
        }
    }

    public int getValue() {
        return value;
    }
};