package src.main.java.aoc_2024;


import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.System.out;

public class Day21 {

    public static final String PART1_ANSWER = "";
    public static final String PART2_ANSWER = "";
    public static String[] lines = null;





    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  21");
        if (AdventOfCode2024.TESTING) {
            out.print("\t (testing)");
        }
        out.println();

        String[] answers = {"", ""};

        parseInput(inputString);
        answers[0] = getPart1();
        answers[1] = getPart2();

        if (!AdventOfCode2024.TESTING) {
            if (!answers[0].equals(PART1_ANSWER)) {
                out.printf("\t\tWRONG ANSWER got: %s, expected %s\n", answers[0], PART1_ANSWER);
            }

            if (!answers[1].equals(PART2_ANSWER)) {
                out.printf("\t\tWRONG ANSWER got: %s, expected %s\n", answers[1], PART2_ANSWER);
            }
        }
        return answers;
    }
    public static void parseInput(String filename) throws IOException {
        lines = Files.readAllLines(Path.of(filename)).toArray(new String[0]);
    }

    protected static String getPart1() {

        for(NumKey nk: NumKey.values()) {
           out.printf("NumKey: %s :\n", nk);
           for(DirKey dk: DirKey.values()) {
               if (dk == DirKey.A) {
                   out.printf("\tDirKey: %s  ---- n/a\n", dk);
               } else {
                   out.printf("\tDirKey: %s -> %s\n", dk, nk.next_key(dk));
               }
           }
           out.println();
        }


        long answer = -1;
        return String.valueOf(answer);
    }
    protected static String getPart2() {

        long answer = -1;
        return String.valueOf(answer);
    }



     enum DirKey{
        Up, Down, Left, Right, A;

        public DirKey[][] go_press(DirKey next) {
            DirKey[][] moves = {{}};
             switch(this) {
                case Up -> {
                    switch(next) {
                        case Up -> {DirKey[][] Up_Up = {{}};
                            moves = Up_Up;
                        }
                        case Down -> {    DirKey[][] Up_Down = {{Down}};
                            moves = Up_Down;
                        }
                        case Left -> {DirKey[][] Up_Left = {{Down,Left}};
                            moves = Up_Left;
                        }
                        case Right -> {  DirKey[][] Up_Right = {{Down, Right},{Right, Down}};
                            moves = Up_Right;
                        }
                        case A -> {
                            DirKey[][] Up_A = {{Right}};
                            moves = Up_A;
                        }
                    }
                }
                case Down -> {
                    switch(next) {
                        case Up -> {DirKey[][] Up_Up = {{Up}};
                            moves = Up_Up;
                        }
                        case Down -> {    DirKey[][] Up_Down = {{}};
                            moves = Up_Down;
                        }
                        case Left -> {DirKey[][] Up_Left = {{Left}};
                            moves = Up_Left;
                        }
                        case Right -> {  DirKey[][] Up_Right = {{Up, Right},{Right,Up}};
                            moves = Up_Right;
                        }
                        case A -> {
                            DirKey[][] Up_A = {{Up,Right},{Right,Up}};
                            moves = Up_A;
                        }
                    }
                }
                case Left -> {
                    switch(next) {
                        case Up -> {DirKey[][] Up_Up = {{Right, Up}};
                            moves = Up_Up;
                        }
                        case Down -> {    DirKey[][] Up_Down = {{Right}};
                            moves = Up_Down;
                        }
                        case Left -> {DirKey[][] Up_Left = {{}};
                            moves = Up_Left;
                        }
                        case Right -> {  DirKey[][] Up_Right = {{Right,Right}};
                            moves = Up_Right;
                        }
                        case A -> {
                            DirKey[][] Up_A = {{Right,Right,Up}};
                            moves = Up_A;
                        }
                    }
                }
                case Right -> {
                    switch(next) {
                        case Up -> {DirKey[][] Up_Up = {{Up, Left},{Left,Up}};
                            moves = Up_Up;
                        }
                        case Down -> {    DirKey[][] Up_Down = {{Left}};
                            moves = Up_Down;
                        }
                        case Left -> {DirKey[][] Up_Left = {{Left,Left}};
                            moves = Up_Left;
                        }
                        case Right -> {  DirKey[][] Up_Right = {{}};
                            moves = Up_Right;
                        }
                        case A -> {
                            DirKey[][] Up_A = {{Up}};
                            moves = Up_A;
                        }
                    }
                }
                case A -> {
                    switch(next) {
                        case Up -> {DirKey[][] Up_Up = {{Left}};
                            moves = Up_Up;
                        }
                        case Down -> {    DirKey[][] Up_Down = {{Left, Down},{Down,Left}};
                            moves = Up_Down;
                        }
                        case Left -> {DirKey[][] Up_Left = {{Down,Left,Left}};
                            moves = Up_Left;
                        }
                        case Right -> {  DirKey[][] Up_Right = {{Down}};
                            moves = Up_Right;
                        }
                        case A -> {
                            DirKey[][] Up_A = {{}};
                            moves = Up_A;
                        }
                    }
                }
            }
            for(int i=0; i < moves.length; i++) {
                DirKey[] move_a = moves[i];
                DirKey[] move_b = new DirKey[move_a.length+1];
                for(int j=0; j < move_a.length; j++){
                    move_b[j] = move_a[j];
                }
                move_b[move_a.length] = A;
                moves[i]=move_b;
            }
             return moves;
        }


         @Override
         public String toString() {
             return switch(this){
                 case Up -> "^";
                 case Down -> "v";
                 case Left -> "<";
                 case Right -> ">";
                 case A -> "A";
             };
         }
     }

     static private String dirs_to_string(DirKey[] dirs) {
        StringBuilder sb = new StringBuilder();
        for(DirKey d: dirs) {
            sb.append(d.toString());
        }
        return sb.toString();
     }

     enum NumKey {
         K7, K8, K9, K4, K5, K6, K1, K2, K3, K0, KA;
        private enum NumKey_Internal {
            K7, K8, K9, K4, K5, K6, K1, K2, K3, K0, KA, NN;
        }
         public Optional<NumKey> next_key(DirKey dir) {
             if(dir.equals(DirKey.A)) {
                 throw new IllegalArgumentException(String.format("Expected NSEW Direction, got A"));
             }
             int a = this.ordinal();
             int b = dir.ordinal();
             NumKey_Internal t = lookup[a][b];
             if(t == NumKey_Internal.NN) {
                 return Optional.empty();
             } else {
                 return Optional.of(NumKey.values()[t.ordinal()]);
             }
         }


         private static NumKey_Internal[][] lookup =
                 {
                         {NumKey_Internal.NN, NumKey_Internal.K4, NumKey_Internal.NN, NumKey_Internal.K8},           //K7
                         {NumKey_Internal.NN, NumKey_Internal.K5, NumKey_Internal.K7, NumKey_Internal.K9},           //K8
                         {NumKey_Internal.NN, NumKey_Internal.K6, NumKey_Internal.K8, NumKey_Internal.NN},           //K9

                         {NumKey_Internal.K7, NumKey_Internal.K1, NumKey_Internal.NN, NumKey_Internal.K5},           //K4
                         {NumKey_Internal.K8, NumKey_Internal.K2, NumKey_Internal.K4, NumKey_Internal.K6},           //K5
                         {NumKey_Internal.K9, NumKey_Internal.K3, NumKey_Internal.K5, NumKey_Internal.NN},           //K6

                         {NumKey_Internal.K4, NumKey_Internal.NN, NumKey_Internal.NN, NumKey_Internal.K2},           //K1
                         {NumKey_Internal.K5, NumKey_Internal.K0, NumKey_Internal.K1, NumKey_Internal.K3},           //K2
                         {NumKey_Internal.K6, NumKey_Internal.KA, NumKey_Internal.K2, NumKey_Internal.NN},           //K3

                         {NumKey_Internal.K2, NumKey_Internal.NN, NumKey_Internal.NN, NumKey_Internal.KA},           //K0
                         {NumKey_Internal.K3, NumKey_Internal.NN, NumKey_Internal.K0, NumKey_Internal.NN},           //Ka
                 };
     }


}