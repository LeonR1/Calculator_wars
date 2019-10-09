package com.jst_pa_ti.calculator_wars;

import java.util.Random;

public class RacunGenerator {

    private int difficulty;
    private int seed;
    private Random random;
    private int rezultat, type;
    private boolean karkoli = false;
    private String racun;

    public RacunGenerator(int seed) {

        this.difficulty = 0;
        this.seed = seed;
        this.random = new Random(seed);

    }

    public String getRacun() {

        return racun;

    }

    public String getGenRacun() {

        setRacunType();

        switch (type) {

            case 0:
                racun = getRacun0();
                return racun;
            case 1:
                racun = getRacun1();
                return racun;
            case 2:
                racun = getRacun2();
                return racun;

        }

        return null;

    }

    public void setDifficulty(int stRacunov) {

        if (stRacunov % 5 == 0 && stRacunov != 0)
            difficulty++;

    }

    public boolean checkRezultat(int rez) {

        if (rezultat < 0)
            rezultat *= -1;

        if (karkoli || rez == rezultat)
            return true;

        return false;

    }

    private String getRacun0() {

        int a = getNumber(false);
        int b = getNumber(false);
        String s = getMathSymbol();
        if (b == 0 && s.equals("/"))
            b = getNumber(true);

        setRezultat(a, b, s);
        System.out.println(rezultat);

        if (rezultat >= 0)
            return a + " " + s + " " + b + " = ?";
        else
            return a + " " + s + " " + b + " = -?";

    }

    private String getRacun1() {

        int a = getNumber(false);
        int b = getNumber(false);
        String s = getMathSymbol();

        while (s.equals("/") && (b == 0 || a % b != 0)) {

            b = getNumber(true);
            System.out.println(a + " " + b);

        }

        if (a == 0 && (s.equals("*") || s.equals("/")))
            karkoli = true;
        else
            karkoli = false;

        setRezultat(a, b, s);
        int r = rezultat;
        rezultat = b;
        System.out.println(rezultat + " " + karkoli);

        return a + " " + s + " ? = " + r;

    }

    private String getRacun2() {

        int a = getNumber(false);
        int b = getNumber(false);
        String s = getMathSymbol();

        while (s.equals("/") && (b == 0 || a % b != 0)) {

            b = getNumber(true);
            System.out.println(a + " " + b);

        }

        if (b == 0 && (s.equals("*") || s.equals("/")))
            karkoli = true;
        else
            karkoli = false;

        setRezultat(a, b, s);
        int r = rezultat;
        rezultat = a;
        System.out.println(rezultat + " " + karkoli);

        return "? " + s + " " + b + " = " + r;

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
                rezultat = a / b;
                break;

        }

    }

    private void setRacunType() {

        type = random.nextInt(3);

    }

    private boolean jePra(int a) {

        for (int i = 2; i < a; i++) {

            if (a % i == 0)
                return false;

        }

        return true;

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

    private int getNumber(boolean dz0) {

        if (!dz0) {

            switch (difficulty) {

                case 0:
                    return random.nextInt(5);
                case 1:
                    return random.nextInt(10);
                case 2:
                    return random.nextInt(15);
                case 3:
                    return random.nextInt(20);
                default:
                    return random.nextInt(25);

            }

        }
        else {

            switch (difficulty) {

                case 0:
                    return random.nextInt(4) + 1;
                case 1:
                    return random.nextInt(9) + 1;
                case 2:
                    return random.nextInt(14) + 1;
                case 3:
                    return random.nextInt(19) + 1;
                default:
                    return random.nextInt(24) + 1;

            }

        }

    }

}
