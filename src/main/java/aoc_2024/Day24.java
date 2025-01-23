package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.lang.System.out;


public class Day24 {

    public static final String PART1_ANSWER = "47666458872582";
    public static final String PART2_ANSWER = "-1";


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
//        for(SourceGate sg: source_list) {
//            out.println(sg);
//        }
//        out.println();
//        for(Gate g: opgate_list) {
//            out.print(g);
//            BitValue bv = g.getValue();
//            out.printf(" := %s", bv);
//            out.println();
//        }

        String[] zgs = z_gate_names.toArray(new String[z_gates.size()]);
        Arrays.sort(zgs);
        StringBuilder sb = new StringBuilder();
        for(String zs: zgs) {
            Gate g = gate_map.get(zs);
            BitValue bv = g.getValue();
            boolean b_bv = bv.toBool();
            if(b_bv) {
                sb.append("1");
            } else {
                sb.append("0");
            }
        }


        String z_binary = sb.reverse().toString();

        long z_value = Long.parseLong(z_binary,2);
//        out.println();
//        out.printf("z-gates spells: %s -> %d\n", z_binary, z_value);



        long answer = z_value;
        return String.valueOf(answer);
    }



    protected static String getPart2() {

        long answer = -1;
        return String.valueOf(answer);
    }

    private static ArrayList<SourceGate> source_list;
    private static ArrayList<Gate> opgate_list;
    private static int z_gate_count=0;
    private static ArrayList<Gate> z_gates;
    private static ArrayList<String> z_gate_names;
    protected static void parseInput(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filename));
        String[] input_lines = lines.toArray(new String[lines.size()]);
        z_gates = new ArrayList<>();
        source_list = new ArrayList<>();
        z_gate_names = new ArrayList<>();
        int idx =0;

        while( idx < input_lines.length){
            String ln = input_lines[idx];
            idx++;
            if(ln.isEmpty()) { break; }
            String[] parts = ln.split(":");
//            out.printf("%3d:\t%s\t%s\n", idx-1, ln, Arrays.toString(parts));
            String name = parts[0].trim();
            String s_value = parts[1].trim();
            BitValue bv = BitValue.parse(s_value);
            SourceGate sg = new SourceGate(name,bv);
            source_list.add(sg);
            gate_map.put(name,sg);
            if(sg.name.startsWith("z")) {
                z_gate_count++;
                z_gates.add(sg);
                z_gate_names.add(sg.getName());
            }
        }
        opgate_list = new ArrayList<>();
//        out.println("---");
        while(idx < input_lines.length) {
            String ln = input_lines[idx];
            idx++;
            String[] parts = ln.split(AoCUtils.WHITESPACE_RE);
//            out.printf("%3d:\t%s\t%s (parts: %d)\n", idx-1, ln, Arrays.toString(parts), parts.length);
            String left = parts[0].trim();
            String op = parts[1].trim();
            String right = parts[2].trim();
            String name = parts[4].trim();
            Gate g=null;
            if(op.equals("AND")) {
                g = new ANDGate(left,right,name);
            }else if (op.equals("XOR")) {
                g = new XORGate(left,right,name);
            } else if (op.equals("OR")) {
                g = new ORGate(left,right,name);
            } else {
                out.printf("Unknown Gate type in input: %s, from line %3d: %s\n", op, idx-1, ln);
                System.exit(-1);
            }
            gate_map.put(name, g);
            opgate_list.add(g);
            if(g.getName().startsWith("z")) {
                z_gate_count++;
                z_gates.add(g);
                z_gate_names.add(g.getName());
            }
        }

    }

    sealed interface Gate {
       public abstract  BitValue getValue();
       public abstract String getName();
    }

    record SourceGate(String name, BitValue value) implements Gate {

        SourceGate(String name, BitValue value) {
            this.name = name;
            this.value = value;
            gate_map.put(name,this);
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
            return String.format("%s: %s", this.name,
                    this.value==BitValue.One?"1":"0");
        }
    }
    private static HashMap<String,Gate> gate_map = new HashMap<>();
    record ANDGate(String left, String right, String name) implements Gate{
        ANDGate(String left, String right, String name) {
            this.left = left;
            this.right = right;
            this.name = name;

            if(this.left.startsWith("z")) {
                out.printf("left input gate of (%s): %s\n", this.left, this);
            }
            if(this.right.startsWith("z")) {
                out.printf("left input gate of (%s): %s\n", this.left, this);
            }

            gate_map.put(name,this);
        }
        public String getName() {
            return this.name;
        }
        @Override
        public String toString() {
            return String.format("%s AND %s -> %s",
                    this.left, this.right, this.name);
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
            if(this.left.startsWith("z")) {
                out.printf("left input gate of (%s): %s\n", this.left, this);
            }
            if(this.right.startsWith("z")) {
                out.printf("left input gate of (%s): %s\n", this.left, this);
            }
            gate_map.put(name,this);
        }
        public  String getName() {
            return this.name;
        }
        @Override
        public String toString() {
            return String.format("%s XOR %s -> %s",
                    this.left, this.right, this.name);
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
            if(this.left.startsWith("z")) {
                out.printf("left input gate of (%s): %s\n", this.left, this);
            }
            if(this.right.startsWith("z")) {
                out.printf("left input gate of (%s): %s\n", this.left, this);
            }
            gate_map.put(name,this);
        }
        public    String getName() {
            return this.name;
        }
        @Override
        public String toString() {
            return String.format("%s  OR %s -> %s",
                    this.left, this.right, this.name);
        }

        @Override
        public BitValue getValue() {
            BitValue l = gate_map.get(left).getValue();
            BitValue r = gate_map.get(right).getValue();
            return l.or(r);

        }
    }


    public enum BitValue  {
            One, Zero, Unknown;
        public BitValue or(BitValue other) {
            if(this == Unknown) {
                return other;
            } else if(other == Unknown) {
                return this;
            }

            if(this == One || other == One) {
                return One;
            } else {
                return Zero;
            }
        }
        static public BitValue parse(String t) {

            if(t.equals("0")) {
                return Zero;
            }
            if(t.equals("1")) {
                return One;
            }
            throw new NumberFormatException(String.format("String |%s| can't be parsed to BitValue", t));
        }

        public BitValue and(BitValue other) {
            if(this == One && other == One) {
                return One;
            }
            if(this == Zero || other== Zero) {
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
            return switch(this) {
                case One -> true;
                case Zero -> false;
                case Unknown ->
                {throw new IllegalArgumentException(String.format("trying to get BitValue.Unknown as boolean"));}
            };
        }

        static public BitValue getBV(String s) {
            if(s.equals("1")) {
                return One;
            } else {
                return Zero;
            }
        }
        static public BitValue fromBool(boolean b) {
            if(b) {
                return One;
            } else {
                return Zero;
            }
        }
    }

}