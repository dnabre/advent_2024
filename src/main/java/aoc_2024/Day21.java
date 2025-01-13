package src.main.java.aoc_2024;


import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Optional;

import static java.lang.System.out;

public class Day21 {

    public static final String PART1_ANSWER = "";
    public static final String PART2_ANSWER = "";
    public static String[] lines = null;

    public static void parseInput(String filename) throws IOException {
        lines = Files.readAllLines(Path.of(filename)).toArray(new String[0]);
    }

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

    protected static String getPart1() {




        long answer = -1;
        return String.valueOf(answer);
    }

    protected static String getPart2() {

        long answer = -1;
        return String.valueOf(answer);
    }


    enum DirKey {
        Up, Down, Left, Right, A;

        public DirKey[][] go_press(DirKey next) {
            DirKey[][] moves = {{}};
            switch (this) {
                case Up -> {
                    switch (next) {
                        case Up -> {
                            DirKey[][] Up_Up = {{}};
                            moves = Up_Up;
                        }
                        case Down -> {
                            DirKey[][] Up_Down = {{Down}};
                            moves = Up_Down;
                        }
                        case Left -> {
                            DirKey[][] Up_Left = {{Down, Left}};
                            moves = Up_Left;
                        }
                        case Right -> {
                            DirKey[][] Up_Right = {{Down, Right}, {Right, Down}};
                            moves = Up_Right;
                        }
                        case A -> {
                            DirKey[][] Up_A = {{Right}};
                            moves = Up_A;
                        }
                    }
                }
                case Down -> {
                    switch (next) {
                        case Up -> {
                            DirKey[][] Up_Up = {{Up}};
                            moves = Up_Up;
                        }
                        case Down -> {
                            DirKey[][] Up_Down = {{}};
                            moves = Up_Down;
                        }
                        case Left -> {
                            DirKey[][] Up_Left = {{Left}};
                            moves = Up_Left;
                        }
                        case Right -> {
                            DirKey[][] Up_Right = {{Up, Right}, {Right, Up}};
                            moves = Up_Right;
                        }
                        case A -> {
                            DirKey[][] Up_A = {{Up, Right}, {Right, Up}};
                            moves = Up_A;
                        }
                    }
                }
                case Left -> {
                    switch (next) {
                        case Up -> {
                            DirKey[][] Up_Up = {{Right, Up}};
                            moves = Up_Up;
                        }
                        case Down -> {
                            DirKey[][] Up_Down = {{Right}};
                            moves = Up_Down;
                        }
                        case Left -> {
                            DirKey[][] Up_Left = {{}};
                            moves = Up_Left;
                        }
                        case Right -> {
                            DirKey[][] Up_Right = {{Right, Right}};
                            moves = Up_Right;
                        }
                        case A -> {
                            DirKey[][] Up_A = {{Right, Right, Up}};
                            moves = Up_A;
                        }
                    }
                }
                case Right -> {
                    switch (next) {
                        case Up -> {
                            DirKey[][] Up_Up = {{Up, Left}, {Left, Up}};
                            moves = Up_Up;
                        }
                        case Down -> {
                            DirKey[][] Up_Down = {{Left}};
                            moves = Up_Down;
                        }
                        case Left -> {
                            DirKey[][] Up_Left = {{Left, Left}};
                            moves = Up_Left;
                        }
                        case Right -> {
                            DirKey[][] Up_Right = {{}};
                            moves = Up_Right;
                        }
                        case A -> {
                            DirKey[][] Up_A = {{Up}};
                            moves = Up_A;
                        }
                    }
                }
                case A -> {
                    switch (next) {
                        case Up -> {
                            DirKey[][] Up_Up = {{Left}};
                            moves = Up_Up;
                        }
                        case Down -> {
                            DirKey[][] Up_Down = {{Left, Down}, {Down, Left}};
                            moves = Up_Down;
                        }
                        case Left -> {
                            DirKey[][] Up_Left = {{Down, Left, Left}};
                            moves = Up_Left;
                        }
                        case Right -> {
                            DirKey[][] Up_Right = {{Down}};
                            moves = Up_Right;
                        }
                        case A -> {
                            DirKey[][] Up_A = {{}};
                            moves = Up_A;
                        }
                    }
                }
            }
            for (int i = 0; i < moves.length; i++) {
                DirKey[] move_a = moves[i];
                DirKey[] move_b = new DirKey[move_a.length + 1];
                System.arraycopy(move_a, 0, move_b, 0, move_a.length);
                move_b[move_a.length] = A;
                moves[i] = move_b;
            }
            return moves;
        }


        @Override
        public String toString() {
            return switch (this) {
                case Up -> "^";
                case Down -> "v";
                case Left -> "<";
                case Right -> ">";
                case A -> "A";
            };
        }
    }
    record NumState(NumKey key, ArrayList<NumKey> path) {

    }

    enum NumKey {
        K7, K8, K9, K4, K5, K6, K1, K2, K3, K0, KA;

        public ArrayList<ArrayList<NumKey>> find_all_paths_to(NumKey to) {
            ArrayList<ArrayList<NumKey>> paths = new ArrayList<>();
            ArrayDeque<NumState> queue = new ArrayDeque<>();
            HashMap<NumKey,Integer> shortest_distances = new HashMap<>();

            ArrayList<NumKey> t = new ArrayList<>();
            t.add(this);
            queue.addLast(new NumState(this,t));
            shortest_distances.put(this,1);

            int shortest_path_length = Integer.MAX_VALUE;
            while(!queue.isEmpty()) {
                NumState current = queue.removeFirst();
                int current_path_lenth = current.path.size();

                if(current.key == to) {
                    if (current_path_lenth  < shortest_path_length) {
                        shortest_path_length = current_path_lenth;
                        paths = new ArrayList<>();
                        paths.addLast(current.path);
                    } else if (current_path_lenth == shortest_path_length) {
                        paths.addLast(current.path);
                    }
                    continue;
                }

                if (current_path_lenth >= shortest_path_length) {
                    continue;
                }

                for(NumKey next_key: current.key.neighbor_keys_iter()) {
                    int next_dist = shortest_distances.getOrDefault(next_key, Integer.MAX_VALUE);
                    if (current_path_lenth < next_dist) {
                        next_dist = current_path_lenth + 1;
                        shortest_distances.put(next_key,next_dist);
                        ArrayList<NumKey> next_path = new ArrayList<>(current.path);
                        next_path.addLast(next_key);
                        queue.addLast(new NumState(next_key, next_path));
                    } else {
                        shortest_distances.put(next_key,next_dist);
                    }
                }
            }
            return paths;
        }


        private static final NumKey_Internal[][] lookup = {{NumKey_Internal.NN, NumKey_Internal.K4, NumKey_Internal.NN, NumKey_Internal.K8},           //K7
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

        public Optional<NumKey> next_key(DirKey dir) {
            if (dir.equals(DirKey.A)) {
                throw new IllegalArgumentException("Expected NSEW Direction, got A");
            }
            int a = this.ordinal();
            int b = dir.ordinal();
            NumKey_Internal t = lookup[a][b];
            if (t == NumKey_Internal.NN) {
                return Optional.empty();
            } else {
                return Optional.of(NumKey.values()[t.ordinal()]);
            }
        }

        public DirKey dir(NumKey next) {
            switch (this) {
                case K7 -> {
                    switch (next) {
                        case K8:
                            return DirKey.Right;
                        case K4:
                            return DirKey.Down;
                    }
                }
                case K8 -> {
                    switch (next) {
                        case K7:
                            return DirKey.Left;
                        case K9:
                            return DirKey.Right;
                        case K5:
                            return DirKey.Down;
                    }
                }
                case K9 -> {
                    switch (next) {
                        case K8:
                            return DirKey.Left;
                        case K6:
                            return DirKey.Down;
                    }
                }
                case K4 -> {
                    switch (next) {
                        case K7:
                            return DirKey.Up;
                        case K5:
                            return DirKey.Right;
                        case K1:
                            return DirKey.Down;
                    }
                }
                case K5 -> {
                    switch (next) {
                        case K8:
                            return DirKey.Up;
                        case K4:
                            return DirKey.Left;
                        case K6:
                            return DirKey.Right;
                        case K2:
                            return DirKey.Down;
                    }
                }
                case K6 -> {
                    switch (next) {
                        case K9:
                            return DirKey.Up;
                        case K5:
                            return DirKey.Left;
                        case K3:
                            return DirKey.Down;
                    }
                }
                case K1 -> {
                    switch (next) {
                        case K4:
                            return DirKey.Up;
                        case K2:
                            return DirKey.Right;
                    }
                }
                case K2 -> {
                    switch (next) {
                        case K5:
                            return DirKey.Up;
                        case K1:
                            return DirKey.Left;
                        case K3:
                            return DirKey.Right;
                        case K0:
                            return DirKey.Down;
                    }
                }
                case K3 -> {
                    switch (next) {
                        case K6:
                            return DirKey.Up;
                        case K2:
                            return DirKey.Left;
                        case KA:
                            return DirKey.Down;
                    }
                }
                case K0 -> {
                    switch (next) {
                        case K2:
                            return DirKey.Up;
                        case KA:
                            return DirKey.Right;
                    }
                }
                case KA -> {
                    switch (next) {
                        case K0:
                            return DirKey.Left;
                        case K3:
                            return DirKey.Up;
                    }
                }
            }
            throw new IllegalArgumentException(String.format("%s and %s aren't neighbors!", this, next));
        }

        public ArrayList<NumKey> neighbor_keys_iter() {
            ArrayList<NumKey> r = new ArrayList<>();
            for (DirKey d : DirKey.values()) {
                Optional<NumKey> o = this.next_key(d);
                if (o.isPresent()) {
                    r.add(o.get());
                }
            }
            return r;
        }

        @Override
        public String toString() {
            return switch (this) {
                case K7 -> "7";
                case K8 -> "8";
                case K9 -> "9";
                case K4 -> "4";
                case K5 -> "5";
                case K6 -> "6";
                case K1 -> "1";
                case K2 -> "2";
                case K3 -> "3";
                case K0 -> "0";
                case KA -> "A";
            };
        }

        private enum NumKey_Internal {
            K7, K8, K9, K4, K5, K6, K1, K2, K3, K0, KA, NN
        }


    }

    static private String dirs_to_string(DirKey[] dirs) {
        StringBuilder sb = new StringBuilder();
        for (DirKey d : dirs) {
            sb.append(d.toString());
        }
        return sb.toString();
    }
}