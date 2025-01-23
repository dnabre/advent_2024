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
//        answers[0] = getPart1();
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



        long x_num = getNumberFromGate('x');
        long y_num = getNumberFromGate('y');
        long z_num = getNumberFromGate('z');

        out.printf("X+Y=Z => %d + %d = %d \n", x_num,y_num,z_num);
        if(x_num + y_num != z_num) {
            out.println("addition failreu");
        }

        long answer = z_num;
        return String.valueOf(answer);
    }

    static private long getNumberFromGate(char ch) {
        ArrayList<String> gate_names = new ArrayList<>();
        for(Gate g:gate_map.values()) {
            if(g.getName().startsWith(String.valueOf(ch))) {
                gate_names.add(g.getName());
            }
        }
        String[] name_array = gate_names.toArray(new String[gate_names.size()]);
        Arrays.sort(name_array);

//        String[] zgs = z_gate_names.toArray(new String[z_gates.size()]);
//        Arrays.sort(zgs);
        StringBuilder sb = new StringBuilder();
        for(String zs:name_array) {
            Gate g = gate_map.get(zs);
            BitValue bv = g.getValue();
            boolean b_bv = bv.toBool();
            if(b_bv) {
                sb.append("1");
            } else {
                sb.append("0");
            }
        }
        String num = sb.reverse().toString();
        return Long.valueOf(num, 2);
    }


    /*
     to produce wrong answers. The first pair of gates with swapped outputs
     is x00 AND y00 -> z05 and x05 AND y05 -> z00; the second pair of
      gates is x01 AND y01 -> z02 and x02 AND y02 -> z01.
     */
static private String[][] swaps = {{ "z05", "jst"},{"mcm", "gdf"},{"z15", "dnt"},{"z30", "gwc"}};

private static void doSwaps(HashMap<String,Gate> g_map) {
    for(int i=0; i < swaps.length; i++) {
        String one = swaps[i][0];
        String two = swaps[i][1];
        Gate g_one = g_map.get(one);
        Gate g_two = g_map.get(two);
        g_map.remove(one);
        g_map.remove(two);
        Gate n_g_one = g_one.getGateSwappedOutput(two);
        Gate n_g_two = g_two.getGateSwappedOutput(one);
        g_map.put(one,n_g_two);
        g_map.put(two,n_g_one);
    }
}
/*
    Adder
    X Y -> Z, C
        Z = X XOR Y XOR C
        C = (X AND Y)  OR (C AND (X XOR Y))

 */

    protected static String getPart2() {
//doSwaps(gate_map);
out.println("\n ---- swap corrections made ---- \n");
        {
            HashSet<Gate> x_gates = new HashSet<>();
            HashSet<Gate> y_gates = new HashSet<>();
            HashSet<Gate> z_gates = new HashSet<>();
            // s, and, xor, or
            int[] x_counts = new int[4];
            int[] y_counts = new int[4];
            int[] z_counts = new int[4];

            for (Gate g : gate_map.values()) {
                String[] vs = g.getAllNames();
                for (String gn : vs) {
                    if (gn.startsWith("x")) {
                        x_gates.add(g);
                        x_counts[g.type()]++;
                    }
                    if (gn.startsWith("y")) {
                        y_gates.add(g);
                        y_counts[g.type()]++;
                    }
                    if (gn.startsWith("z")) {
                        z_gates.add(g);
                        z_counts[g.type()]++;
                    }
                }

            }

            out.printf("total number of gate: %d\n", gate_map.size());
            out.printf("x_counts:  \t\t (c: %d)\n", x_counts[0] + x_counts[1] + x_counts[2] + x_counts[3]);
            printCounts(x_counts);
            out.printf("y_counts:  \t\t (c: %d)\n", y_counts[0] + y_counts[1] + y_counts[2] + y_counts[3]);
            printCounts(y_counts);
            out.printf("z_counts:  \t\t (c: %d)\n", z_counts[0] + z_counts[1] + z_counts[2] + z_counts[3]);
            printCounts(z_counts);
        }
        out.printf("Total of %d gates\n", gate_map.size());

        HashSet<String> bad_outputs = new HashSet<>();
        HashSet<Gate> bad_gates = new HashSet<>();
        // z should only be the result of XOR with the exception of the bit (z45) being the result of last  OR (from carry)

        out.println("\n---------------------checking assertions---------------------\n");






        //----------------- x time
        out.println("checking assumption for x??");

        out.printf("\tx?? Should directly feed into 45 (number of bits) SRC, AND, & XOR gates\n");
        int x_xor_count=0;
        int x_and_count=0;
        int x_src_count=0;
        boolean x_in_or = false;
        String start_str_check = "x";
        for(String output: gate_map.keySet()) {
            Gate g = gate_map.get(output);
            switch (g.which()) {
                case SRC -> {
                    if (g.getName().startsWith(start_str_check)) {
                        x_src_count++;
                    }
                }
                case AND -> {
                    ANDGate a_g = (ANDGate) g;
                    if ((a_g.left.startsWith(start_str_check)) || (a_g.right.startsWith(start_str_check))) {
                        x_and_count++;
                    }
                }
                case XOR -> {
                    XORGate a_g = (XORGate) g;
                    if ((a_g.left.startsWith(start_str_check)) || (a_g.right.startsWith(start_str_check))) {
                        x_xor_count++;
                    }
                }
                case OR -> {
                    ORGate a_g = (ORGate) g;
                    if ((a_g.left.startsWith(start_str_check)) || (a_g.right.startsWith(start_str_check))) {
                        x_in_or = true;
                        out.printf("\t\terror: x?? feeding directly into OR in, %s", g);
                        bad_gates.add(g);
                        bad_outputs.add(g.getName());
                    }
                }
            }
        }
            if(x_in_or) {
                out.printf("\t\terror: x?? feeding into OR gates that it should\n");
            }
            if(x_xor_count != x_and_count) {
                out.printf("\t\terror: x?? is feeding into an unequal number of XOR and AND gates  (%d, %d)\n", x_xor_count, x_and_count);
            }
            if(x_src_count != x_xor_count) {
                out.printf("\t\terror: x?? is feeding into an unequal number of XOR and SRC gates  (%d, %d)\n", x_xor_count, x_src_count);
            }
            if(x_and_count != x_src_count) {
                out.printf("\t\terror: x?? is feeding into an unequal number of AND and SRC gates  (%d, %d)\n", x_and_count, x_src_count);
            }
            if(x_and_count == x_src_count && x_src_count == x_xor_count) {
                out.printf("\tx?? feeds the same number of XOR, SRC, AND gates (%d)\n", x_and_count);
            }

            //----------------- y time
        out.println("checking assumption for y??");
        out.printf("\ty?? Should directly feed into 45 (number of bits) SRC, AND, & XOR gates\n");
        int y_xor_count=0;
        int y_and_count=0;
        int y_src_count=0;
        boolean y_in_or = false;
        start_str_check="y";
        for(String output: gate_map.keySet()) {
            Gate g = gate_map.get(output);
            switch (g.which()) {
                case SRC -> {
                    if (g.getName().startsWith(start_str_check)) {
                        y_src_count++;
                    }
                }
                case AND -> {
                    ANDGate a_g = (ANDGate) g;
                    if ((a_g.left.startsWith(start_str_check)) || (a_g.right.startsWith(start_str_check))) {
                        y_and_count++;
                    }
                }
                case XOR -> {
                    XORGate a_g = (XORGate) g;
                    if ((a_g.left.startsWith(start_str_check)) || (a_g.right.startsWith(start_str_check))) {
                        y_xor_count++;
                    }
                }
                case OR -> {
                    ORGate a_g = (ORGate) g;
                    if ((a_g.left.startsWith(start_str_check)) || (a_g.right.startsWith(start_str_check))) {
                        y_in_or = true;
                        out.printf("\t\terror: y?? feeding directly into OR in, %s", g);
                        bad_gates.add(g);
                        bad_outputs.add(g.getName());
                    }
                }
            }
        }
        if(y_in_or) {
            out.printf("\t\terror: y?? feeding into OR gates that it should\n");
        }
        if(y_xor_count != y_and_count) {
            out.printf("\t\terror: y?? is feeding into an unequal number of XOR and AND gates  (%d, %d)\n", y_xor_count, y_and_count);
        }
        if(y_src_count != y_xor_count) {
            out.printf("\t\terror: y?? is feeding into an unequal number of XOR and SRC gates  (%d, %d)\n", y_xor_count, y_src_count);
        }
        if(y_and_count != y_src_count) {
            out.printf("\t\terror: y?? is feeding into an unequal number of AND and SRC gates  (%d, %d)\n", y_and_count, y_src_count);
        }
        if(y_and_count == y_src_count && y_src_count == y_xor_count) {
            out.printf("\ty?? feeds the same number of XOR, SRC, AND gates (%d)\n", y_and_count);
        }


        //----------------- z time
         out.println("checking assumption for z??");
        out.printf("\tz?? Should be the output of  45 (number of bits-1 ) XOR gate and a single OR gate\n");
        int z_xor_count=0;
        int z_and_count=0;
        int z_src_count=0;
        int z_or_count=0;
        boolean z_in_or = false;
        start_str_check="z";
        ORGate z_or_gate = null;
        for(String output: gate_map.keySet()) {
            Gate g = gate_map.get(output);

            switch (g.which()) {
                case SRC -> {
                    if(g.getName().startsWith(start_str_check)) {
                        z_src_count++;
                        out.printf("\t\terror: z?? is source gate, this shouldn't be: %s\n", g);
                        bad_gates.add(g);
                        bad_outputs.add(output);
                    }
                }
                case AND -> {
                    if(g.getName().startsWith(start_str_check)) {
                        z_and_count++;
                        out.printf("\t\terror: z?? is the output of AND gate, this shouldn't be: %s\n",g);
                        bad_gates.add(g);
                        bad_outputs.add(output);
                    }
                    ANDGate a_g = (ANDGate) g;
                    if ((a_g.left.startsWith(start_str_check)) || (a_g.right.startsWith(start_str_check))) {
                        out.printf("\t\terror: z?? is the input to AND gate: %s\n", a_g);
                        bad_gates.add(g);
                        bad_outputs.add(g.getName());

                    }
                }
                case XOR -> {
                    if(g.getName().startsWith(start_str_check)) {
                        z_xor_count++;
                        if(z_xor_count > 45) {
                            out.printf("\t\terror: z?? is the output of too many XOR gates (%d), this shouldn't be: %s\n",z_xor_count, g);
                            bad_gates.add(g);
                            bad_outputs.add(output);
                        }
                    }
                    XORGate a_g = (XORGate) g;
                    if ((a_g.left.startsWith(start_str_check)) || (a_g.right.startsWith(start_str_check))) {
                        out.printf("\t\terror: z?? is the input to XOR gate: %s\n", a_g);
                        bad_gates.add(g);
                        bad_outputs.add(output);

                    }
                }
                case OR -> {
                    ORGate a_g = (ORGate) g;
                    if(a_g.name.startsWith(start_str_check)) {
                        z_or_count++;
                        z_or_gate = a_g;
                        z_in_or = true;
                        if(!a_g.name.equals("z45")) {
                        out.printf("\t\terror: z?? is output of OR gate (not the single z45): %s \n",g );
                            bad_gates.add(g);
                            bad_outputs.add(output);
                        }
                    }
                    if ((a_g.left.startsWith(start_str_check)) || (a_g.right.startsWith(start_str_check))) {
                        z_in_or = true;
                        out.printf("\t\terror: z?? is the input to  OR gate: %s\n", a_g);
                        bad_gates.add(g);
                        bad_outputs.add(g.getName());
                    }
                }
            }
        }

        if(z_src_count > 0){
            out.printf("\t\terror: z?? is output of %d SRC gates instead of 0\n", z_src_count);
        } else {
            out.printf("\tz?? is output of  no SRC gates, correct\n");
        }
        if(z_and_count > 0){
            out.printf("\t\terror: z?? is output of %d AND gates instead of 0\n", z_and_count);
        } else {
            out.printf("\tz?? is output of  no AND gates, correct\n");
        }
        if(z_xor_count != 45){
            out.printf("\t\terror: z?? is output of %d XOR gates instead of 0\n", z_xor_count);
        } else {
            out.printf("\tz?? is output of %d XOR gates, correct\n", z_xor_count);
        }


        if(z_or_gate == null) {
           assert(z_or_gate ==null);
            out.printf("\t\terror: z?? is not in any OR gates, should be in precisely 1\n");
        }

       if(z_or_count==1) {
            if(z_or_gate.name.equals("z45")) {
                out.printf("\tz?? are in the single OR it should being (output to z45)\n");
            } else{
                out.printf("\t\terror: z?? is in one OR gate (ok), but the gate should be z45 but isn't: %s\n", z_or_gate);
            }

       } else{
           out.printf("\t\terror: z?? is %d  OR gates, should be in precisely 1\n", z_or_count);
       }
out.println("\n end of assumption checking\n");

       out.printf("bad outputs: %s\n", bad_outputs);
       out.printf("bad gates:\n");
       for(Gate b : bad_gates) {
           out.printf("\t %s \n", b);
       }

       out.println("------------------------look at intermediate gates--------------------------");
       out.printf("total number of gates: %d\n", gate_map.size());
       HashSet<String> names = new HashSet<>();
       for(Gate g :gate_map.values()) {
           String[] nm = g.getAllNames();
           for(String n: nm) {
               if(!inPart(n)) {
                   names.add(n);
               }
           }
       }
       String[] name_a = names.toArray(new String[names.size()]);
       Arrays.sort(name_a);
       HashMap<String,int[]> name_to_counts = new HashMap<>();
       out.printf("total number of names: %d\n", names.size());
        for(String name_check : name_a) {
            int[] counts = new int[4];
            start_str_check = name_check;
            for (String output : gate_map.keySet()) {
                Gate g = gate_map.get(output);

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
                    }
                    case XOR -> {
                        if (g.getName().startsWith(start_str_check)) {
                            counts[g.type()]++;
                        }
                        XORGate a_g = (XORGate) g;
                        if ((a_g.left.startsWith(start_str_check)) || (a_g.right.startsWith(start_str_check))) {
                            counts[g.type()]++;

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
            name_to_counts.put(name_check,counts);
        }

        out.println("names information");

    for(String name: name_a) {
        int[] counts = name_to_counts.get(name);
    //    out.printf("\t  %5s \t [s: %3d, &: %3d, x: %3d, v: %3d] \n",name,counts[0],counts[1],counts[2],counts[3]);
        if(counts[1] ==0) {
            out.printf("\t\t suspicious number of ands (%d)\n", counts[1]);
            bad_outputs.add(name);
        }
    }




    out.println();
    out.printf("swaps:\n");
    for(String[] swap_pair: swaps) {
        out.printf("\t %s\n", Arrays.toString(swap_pair));
    }
    out.printf("bad outputs: %s\n", bad_outputs);
    for(Gate g:bad_gates) {
        out.print("\t");
        out.println(g);
    }
    String[] swap_names = new String[swaps.length * 2];
    int sw_idx=0;
    for(String[] ps: swaps) {
        for(String s: ps) {
            swap_names[sw_idx] = s;
            sw_idx++;
        }
    }
    Arrays.sort(swap_names);
    String sw_answer = String.join(",", swap_names);

    String[] bad_names = bad_outputs.toArray(new String[bad_outputs.size()]);
    Arrays.sort(bad_names);
    String answer = String.join(",",bad_names);
//dnt,gdf,gwc,jst,mcm,z05,z15,z30
        out.print("bad  : ");
        out.println(sw_answer);
        out.print("mine : ");
        out.println(answer);

        // So close
        // my rpj needs to be jst


        //long answer = -1;
        return String.valueOf(answer);
    }

    /*
    Adder
    X Y -> Z, C
        Z = X XOR Y XOR C
        C = (X AND Y)  OR (C AND (X XOR Y))

 */


    private static void printCounts(int[] v) {
        out.printf("\tSRC: %d\n", v[0]);
        out.printf("\tAND: %d\n", v[1]);
        out.printf("\tXOR: %d\n", v[2]);
        out.printf("\t OR: %d\n", v[3]);}
    private static void checkGates()
    {
        long x_num = getNumberFromGate('x');
        long y_num = getNumberFromGate('y');
        long z_num = getNumberFromGate('z');
        long rr = x_num + y_num;

        String x = Long.toString(x_num, 2);
        String y = Long.toString(y_num, 2);
        String z = Long.toString(z_num, 2);
        String r = Long.toString(rr, 2);
        int ll = Math.max(z.length(), r.length());
        x = "0".repeat(ll - x.length()) + x;
        y = "0".repeat(ll - y.length()) + y;
        z = "0".repeat(ll - z.length()) + z;
        r = "0".repeat(ll - r.length()) + r;

        out.printf("input   x: %s\n", x);
        out.printf("input   y: %s\n", y);
        out.printf("output  z: %s\n", z);
        out.printf("target  r: %s\n", r);
        var z_a = z.toCharArray();
        var r_a = r.toCharArray();
        out.print("diff  z/r: ");
        int d_points = 0;
        for (int i = 0; i < r.length(); i++) {
            char ch_z = z_a[i];
            char ch_r = r_a[i];
            if (ch_z != ch_r) {
                out.print('*');
                d_points++;
            } else {
                out.print(ch_z);
            }
        }
        out.println();
        out.printf("points of difference: %d\n", d_points);
        out.printf("X + Y=Z => %d  + %d = %d    should be: %d \n", x_num, y_num, z_num, rr);
    }

    private static ArrayList<Gate> gateStartingWith(String ch) {
        ArrayList<Gate> r = new ArrayList<>();
        for(Gate g: gate_map.values()) {
            if(g.getName().startsWith(ch)) {
                r.add(g);
            }

        }
        return r;
    }

//    private static ArrayList<SourceGate> source_list;
    private static ArrayList<Gate> opgate_list;

    protected static void parseInput(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filename));
        String r_lines = Files.readString(Path.of(filename));


        String[] input_lines = lines.toArray(new String[lines.size()]);

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

            gate_map.put(name,sg);

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

        }

    }
private static boolean inPart(String s) {
        return (s.startsWith("x") || s.startsWith("y") || s.startsWith("z") );
}
    sealed interface Gate {
       public abstract  BitValue getValue();
       public abstract String getName();
       public abstract Gate getGateSwappedOutput(String new_out);
       public abstract String[] getAllNames();
        public abstract int type();  // s, and, xor, or
        public abstract GateType which();
    }

    record SourceGate(String name, BitValue value) implements Gate {
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

        @Override
        public Gate getGateSwappedOutput(String new_out) {
            return new SourceGate(new_out,this.value);
        }

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
        @Override
        public int type() {
            return 1;
        }
        @Override
        public String[] getAllNames() {
            return new String[]{name,left,right};
        }
        @Override
        public Gate getGateSwappedOutput(String new_out) {
            return new ANDGate(left,right,new_out);
        }


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
        public GateType which() {
            return GateType.AND;
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
        @Override
        public int type() {
            return 2;
        }
        @Override
        public GateType which() {
            return GateType.XOR;
        }
        @Override
        public Gate getGateSwappedOutput(String new_out) {
            return new XORGate(left,right,new_out);
        }
        @Override
        public String[] getAllNames() {
            return new String[]{name,left,right};
        }

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
        @Override
        public int type() {
            return 3;
        }
        @Override
        public GateType which() {
            return GateType.OR;
        }
        @Override
        public Gate getGateSwappedOutput(String new_out) {
            return new ORGate(left,right,new_out);
        }
        @Override
        public String[] getAllNames() {
            return new String[]{name,left,right};
        }

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


    enum GateType {
        SRC, AND,XOR,OR;
    }

    private static GateType[] g_type = {GateType.SRC,GateType.AND,GateType. XOR,GateType. OR};


}