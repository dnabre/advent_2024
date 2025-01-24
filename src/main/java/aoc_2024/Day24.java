package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.System.out;

/*
        Huge insight for part 2!!!!

        You need find the names that need switching, but you don't need to pair them up!!!
        Since you just need to provide those names in sorted order, you just need to establish w
         which names are in a wrong place.

 */


public class Day24 {

    public static final String PART1_ANSWER = "47666458872582";
    public static final String PART2_ANSWER = "dnt,gdf,gwc,jst,mcm,z05,z15,z30";
    private static final HashMap<String, Gate> gate_map = new HashMap<>();

    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  24");
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
        long answer = getNumberFromGate();
        return String.valueOf(answer);
    }


    protected static String getPart2() {

        HashSet<String> bad_outputs = new HashSet<>();
        String start_str_check;
        HashSet<String> names = new HashSet<>();
        start_str_check = "z";
        for (String output : gate_map.keySet()) {
            Gate g = gate_map.get(output);
            String[] nm = g.getAllNames();
            for (String n : nm) {
                if (!inPart(n)) {
                    names.add(n);
                }
            }
            switch (g.which()) {
                case SRC -> {
                    if (g.getName().startsWith(start_str_check)) {


                        bad_outputs.add(output);
                    }
                }
                case AND -> {
                    if (g.getName().startsWith(start_str_check)) {



                        bad_outputs.add(output);
                    }
                    ANDGate a_g = (ANDGate) g;
                    if ((a_g.left.startsWith(start_str_check)) || (a_g.right.startsWith(start_str_check))) {


                        bad_outputs.add(g.getName());

                    }
                }
                case XOR -> {


                }
                case OR -> {
                    ORGate a_g = (ORGate) g;
                    if (a_g.name.startsWith(start_str_check)) {
                        if (!a_g.name.equals("z45")) {
                            bad_outputs.add(output);
                        }
                    }

                }
            }
        }

        String[] name_a = names.toArray(new String[0]);
        Arrays.sort(name_a);

        for (String name_check : name_a) {
            int[] counts = new int[5];
            start_str_check = name_check;
            for (String output : gate_map.keySet()) {
                Gate g = gate_map.get(output);
                if (output.equals(name_check)) {
                    counts[4]++;
                }
                switch (g.which()) {
                    case SRC -> {
                        if (g.getName().startsWith(start_str_check)) {
                            counts[g.type()]++;
                        }
                    }
                    case AND -> {
                        if (g.getName().startsWith(start_str_check)) {
                            counts[g.type()]++;
                        }
                        ANDGate a_g = (ANDGate) g;
                        if ((a_g.left.startsWith(start_str_check)) || (a_g.right.startsWith(start_str_check))) {
                            counts[g.type()]++;

                        }
                        if (!a_g.left.equals("x00") && !a_g.left.equals("y00") && !a_g.right.equals("x00") && !a_g.right.equals("y00")) {
                            boolean found_good_or = false;
                            for(ORGate og: or_gates) {
                                if(og.left.equals(output)) {
                                    found_good_or = true;
                                    break;
                                }
                                if(og.right.equals(output)) {
                                    found_good_or = true;
                                    break;
                                }
                            }
                            if(!found_good_or) {

                                bad_outputs.add(output);
                            }
                        }


                    }
                    case XOR -> {
                        if (g.getName().startsWith(start_str_check)) {
                            counts[g.type()]++;
                        }
                        XORGate a_g = (XORGate) g;
                        if ((a_g.left.startsWith(start_str_check)) || (a_g.right.startsWith(start_str_check))) {
                            counts[g.type()]++;

                        }
                        if (!a_g.left.startsWith("x") && !a_g.left.startsWith("y")
                                && !a_g.right.startsWith("x") && !a_g.right.startsWith("y")   &&!a_g.name.startsWith("z") ) {

                                bad_outputs.add(output);

                        }
                    }
                    case OR -> {
                        ORGate a_g = (ORGate) g;
                        if (a_g.name.startsWith(start_str_check)) {
                            counts[g.type()]++;
                        }
                        if ((a_g.left.startsWith(start_str_check)) || (a_g.right.startsWith(start_str_check))) {
                            counts[g.type()]++;
                        }
                    }
                }
            }
            if(counts[1] == 0) {
                bad_outputs.add(name_check);
            }

        }


        String[] bad_names = bad_outputs.toArray(new String[0]);
        Arrays.sort(bad_names);
        return String.join(",", bad_names);
    }


    private static ArrayList<ORGate> or_gates;
    protected static void parseInput(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filename));
        or_gates = new ArrayList<>();

        String[] input_lines = lines.toArray(new String[0]);

        int idx = 0;

        while (idx < input_lines.length) {
            String ln = input_lines[idx];
            idx++;
            if (ln.isEmpty()) {
                break;
            }
            String[] parts = ln.split(":");
            String name = parts[0].trim();
            String s_value = parts[1].trim();
            BitValue bv = BitValue.parse(s_value);
            SourceGate sg = new SourceGate(name, bv);

            gate_map.put(name, sg);

        }


        while (idx < input_lines.length) {
            String ln = input_lines[idx];
            idx++;
            String[] parts = ln.split(AoCUtils.WHITESPACE_RE);

            String left = parts[0].trim();
            String op = parts[1].trim();
            String right = parts[2].trim();
            String name = parts[4].trim();
            Gate g = null;
            switch (op) {
                case "AND" -> g = new ANDGate(left, right, name);
                case "XOR" -> g = new XORGate(left, right, name);
                case "OR" -> {
                    ORGate og = new ORGate(left, right, name);
                    or_gates.add(og);
                    g = og;
                }
                default -> {
                    out.printf("Unknown Gate type in input: %s, from line %3d: %s\n", op, idx - 1, ln);
                    System.exit(-1);
                }
            }
            gate_map.put(name, g);


        }

    }

    public enum BitValue {
        One, Zero, Unknown;



        static public BitValue parse(String t) {

            if (t.equals("0")) {
                return Zero;
            }
            if (t.equals("1")) {
                return One;
            }
            throw new NumberFormatException(String.format("String |%s| can't be parsed to BitValue", t));
        }

        public BitValue or(BitValue other) {
            if (this == Unknown) {
                return other;
            } else if (other == Unknown) {
                return this;
            }

            if (this == One || other == One) {
                return One;
            } else {
                return Zero;
            }
        }

        public BitValue and(BitValue other) {
            if (this == One && other == One) {
                return One;
            }
            if (this == Zero || other == Zero) {
                return Zero;
            }
            return Unknown;
        }

        public BitValue xor(BitValue other) {
            if (this == Unknown || other == Unknown) {
                return Unknown;
            }
            if ((this == One && other == Zero) || (this == Zero && other == One)) {
                return One;
            }
            return Zero;
        }

        boolean toBool() {
            return switch (this) {
                case One -> true;
                case Zero -> false;
                default -> throw new IllegalArgumentException("trying to get BitValue.Unknown as boolean");
            };
        }
    }

    enum GateType {
        SRC, AND, XOR, OR
    }

    sealed interface Gate {
        BitValue getValue();

        String getName();


        String[] getAllNames();

        int type();  // s, and, xor, or

        GateType which();


    }

    record SourceGate(String name, BitValue value) implements Gate {
        SourceGate(String name, BitValue value) {
            this.name = name;
            this.value = value;
            gate_map.put(name, this);
        }



        @Override
        public String[] getAllNames() {
            return new String[]{name};
        }

        @Override
        public GateType which() {
            return GateType.SRC;
        }

        @Override
        public int type() {
            return 0;
        }



        public String getName() {
            return this.name;
        }

        @Override
        public BitValue getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return String.format("%s: %s", this.name, this.value == BitValue.One ? "1" : "0");
        }
    }

    record ANDGate(String left, String right, String name) implements Gate {


        ANDGate(String left, String right, String name) {
            this.left = left;
            this.right = right;
            this.name = name;
            gate_map.put(name, this);
        }

        @Override
        public int type() {
            return 1;
        }

        @Override
        public String[] getAllNames() {
            return new String[]{name, left, right};
        }


        public String getName() {
            return this.name;
        }

        @Override
        public GateType which() {
            return GateType.AND;
        }

        @Override
        public String toString() {
            return String.format("%s AND %s -> %s", this.left, this.right, this.name);
        }

        @Override
        public BitValue getValue() {
            BitValue l = gate_map.get(left).getValue();
            BitValue r = gate_map.get(right).getValue();
            return l.and(r);
        }
    }

    record XORGate(String left, String right, String name) implements Gate {


        XORGate(String left, String right, String name) {
            this.left = left;
            this.right = right;
            this.name = name;
                       gate_map.put(name, this);
        }

        @Override
        public int type() {
            return 2;
        }

        @Override
        public GateType which() {
            return GateType.XOR;
        }


        @Override
        public String[] getAllNames() {
            return new String[]{name, left, right};
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String toString() {
            return String.format("%s XOR %s -> %s", this.left, this.right, this.name);
        }

        @Override
        public BitValue getValue() {
            BitValue l = gate_map.get(left).getValue();
            BitValue r = gate_map.get(right).getValue();
            return l.xor(r);

        }
    }

    record ORGate(String left, String right, String name) implements Gate {


        ORGate(String left, String right, String name) {

            this.left = left;
            this.right = right;
            this.name = name;

            gate_map.put(name, this);
        }

        @Override
        public int type() {
            return 3;
        }

        @Override
        public GateType which() {
            return GateType.OR;
        }



        @Override
        public String[] getAllNames() {
            return new String[]{name, left, right};
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String toString() {
            return String.format("%s  OR %s -> %s", this.left, this.right, this.name);
        }

        @Override
        public BitValue getValue() {
            BitValue l = gate_map.get(left).getValue();
            BitValue r = gate_map.get(right).getValue();
            return l.or(r);

        }
    }



    static private long getNumberFromGate() {
        ArrayList<String> gate_names = new ArrayList<>();
        for (Gate g : gate_map.values()) {
            if (g.getName().startsWith(String.valueOf('z'))) {
                gate_names.add(g.getName());
            }
        }
        String[] name_array = gate_names.toArray(new String[0]);
        Arrays.sort(name_array);


        StringBuilder sb = new StringBuilder();
        for (String zs : name_array) {
            Gate g = gate_map.get(zs);
            BitValue bv = g.getValue();
            boolean b_bv = bv.toBool();
            if (b_bv) {
                sb.append("1");
            } else {
                sb.append("0");
            }
        }
        String num = sb.reverse().toString();


        return Long.valueOf(num, 2);
    }

    private static boolean inPart(String s) {
        return (s.startsWith("x") || s.startsWith("y") || s.startsWith("z"));
    }




}