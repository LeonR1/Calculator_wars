package com.jst_pa_ti.calculator_wars;

import java.util.Random;

public class RacunGenerator {

    private int difficulty;
    private int seed;
    private Random random;

    public RacunGenerator(int seed) {

        this.difficulty = 0;
        this.seed = seed;
        this.random = new Random(seed);

    }

    private int getRacunType() {

        return random.nextInt(3);

    }

    private char getMathSymbol() {

        int symbol = random.nextInt(4);

        switch (symbol) {

            case 0:
                return '+';
            case 1:
                return '-';
            case 2:
                return '*';
            case 3:
                return '/';

        }

        return 0;

    }

    public void setDifficulty(int stRacunov) {

        if (stRacunov % 5 == 0 && stRacunov != 0)
            difficulty++;

    }

    public int getNumber() {

        switch (difficulty) {

            case 0:
                return random.nextInt(10);
            case 1:
                return random.nextInt(100);
            case 2:
                return random.nextInt(90) + 10;
            case 3:
                return random.nextInt(990) + 10;
            default:
                return random.nextInt(900) + 100;

        }

    }

}
