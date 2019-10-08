package com.jst_pa_ti.calculator_wars;

import java.util.Random;

public class RacunGenerator {

    private int difficulty;
    private int seed;
    private Random random;
    private int rezultat;

    public RacunGenerator(int seed) {

        this.difficulty = 0;
        this.seed = seed;
        this.random = new Random(seed);

    }

    public String getRacun() {

        int a = getNumber(false);
        int b = getNumber(false);
        String s = getMathSymbol();
        if (b == 0 && s.equals("/"))
            b = getNumber(true);

        setRezultat(a, b, s);

        return a + s + b + " = " + rezultat;

    }

    private void setRezultat(int a, int b, String s) {

        switch (s) {

            case "+":
                rezultat = a + b;
                break;
            case "-":
                rezultat = a - b;
                break;
            case "*":
                rezultat = a * b;
                break;
            case "/":
                if (b == 0)
                    b = getNumber(true);
                rezultat = a / b;
                break;

        }

    }

    private int getRacunType() {

        return random.nextInt(3);

    }

    private String getMathSymbol() {

        int symbol = random.nextInt(4);

        switch (symbol) {

            case 0:
                return "+";
            case 1:
                return "-";
            case 2:
                return "*";
            case 3:
                return "/";

        }

        return null;

    }

    private void setDifficulty(int stRacunov) {

        if (stRacunov % 5 == 0 && stRacunov != 0)
            difficulty++;

    }

    private int getNumber(boolean dz0) {

        if (!dz0) {

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
        else {

            switch (difficulty) {

                case 0:
                    return random.nextInt(9) + 1;
                case 1:
                    return random.nextInt(99) + 1;
                case 2:
                    return random.nextInt(89) + 11;
                case 3:
                    return random.nextInt(989) + 11;
                default:
                    return random.nextInt(899) + 101;

            }

        }

    }

}
