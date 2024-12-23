package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static java.lang.System.out;

public class Day23 {

    public static final String PART1_ANSWER = "1064";
    public static final String PART2_ANSWER = "aq,cc,ea,gc,jo,od,pa,rg,rv,ub,ul,vr,yy";
    private static LinkPair[] linkPairs;
    private static Host[] hosts;
    private static HashMap<Host, HashSet<Host>> connections;
    private static String[] part1_test_output;

    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  23");
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

    record Host(String name) implements Comparable<Host> {
        static Host[] fromStringPair(String s) {
            Host[] ha = new Host[2];
            String[] part = s.split("-");
            ha[0] = new Host(part[0].trim());
            ha[1] = new Host(part[1].trim());
            return ha;
        }


        @Override
        public int compareTo(Host other) {
            return this.name.compareTo(other.name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    record LinkPair(Host left, Host right) {
        public static boolean doesConnect(Host me, Host other) {
            if (connections.containsKey(me) && connections.containsKey(other)) {
                HashSet<Host> me_connects = connections.get(me);
                HashSet<Host> other_connects = connections.get(other);
                return me_connects.contains(other) && other_connects.contains(me);
            } else {
                return false;
            }
        }

        public static HashMap<Host, HashSet<Host>> getConnectiontMap() {
            HashMap<Host, HashSet<Host>> connects = new HashMap<>();
            for (LinkPair lp : linkPairs) {

                HashSet<Host> set1 = connects.getOrDefault(lp.left, new HashSet<>());
                set1.add(lp.left);
                set1.add(lp.right);
                connects.put(lp.left, set1);

                HashSet<Host> set2 = connects.getOrDefault(lp.right, new HashSet<>());
                set2.add(lp.left);
                set2.add(lp.right);
                connects.put(lp.left, set2);

            }
            return connects;
        }

        @Override
        public String toString() {
            return String.format("<%s-%s>", left, right);
        }

        public boolean contains(Host h) {
            return left.equals(h) || right.equals(h);
        }

        public Host other(Host h) {
            if (h.equals(left)) {
                return right;
            } else {
                return left;
            }
        }

    }

    public static void parseInput(String filename) throws IOException {
        String[] lines = Files.readAllLines(Path.of(filename)).toArray(new String[0]);
        HashSet<Host> h_set = new HashSet<>();
        linkPairs = new LinkPair[lines.length];
        for (int i = 0; i < lines.length; i++) {
            Host[] pair = Host.fromStringPair(lines[i]);
            LinkPair lp = new LinkPair(pair[0], pair[1]);
            h_set.add(lp.left);
            h_set.add(lp.right);
            linkPairs[i] = lp;
        }
        hosts = h_set.toArray(new Host[0]);
        Arrays.sort(hosts);
        connections = LinkPair.getConnectiontMap();

        if(AdventOfCode2024.TESTING) {
            String i2_filename = AdventOfCode2024.test_files[23-1][2-1];
            String[] i_lines =  Files.readAllLines(Path.of(i2_filename)).toArray(new String[0]);
            part1_test_output = new String[i_lines.length];
            for(int j=0; j < i_lines.length; j++) {
                part1_test_output[j] = i_lines[j].trim();
            }
        }


    }


    public static String getPart1() {
        out.printf("LinkPairs: %d \n", linkPairs.length);
        out.printf("Hosts    : %d \n", hosts.length);

        HashSet<HashSet<Host>> triangles = new HashSet<>();

        for (int one = 0; one < hosts.length; one++) {
            for (int two = one + 1; two < hosts.length; two++) {
                for (int three = two + 1; three < hosts.length; three++) {
                    if ((one == two) || (two == three) || (one == three)) {
                        out.printf("ERROR: overlap: %d, %d, %d\n", one, two, three);
                        return "ERROR: overlap";
                    }
                    Host[] tri = {hosts[one], hosts[two], hosts[three]};
                    if (LinkPair.doesConnect(tri[0], tri[1]) && LinkPair.doesConnect(tri[1], tri[2]) && LinkPair.doesConnect(tri[2], tri[0])) {

                        AddTri(tri, triangles);

                    }
                }
            }
        }
        out.printf("triangles: %d\n", triangles.size());
        out.println();
        String[] just_for_output = new String[triangles.size()];
        HashSet<String> test_set = new HashSet<>();
        int s_idx = 0;
        for (HashSet<Host> triple : triangles) {
            String[] tri = new String[3];
            int idx = 0;
            for (Host h : triple) {
                tri[idx] = h.name;
                idx++;
            }
            Arrays.sort(tri);
            String str = String.format("%s,%s,%s", tri[0], tri[1], tri[2]);
            just_for_output[s_idx] = str;
            s_idx++;
            test_set.add(str);
        }
        if(AdventOfCode2024.TESTING) {
            HashSet<String> expected = new HashSet<String>();
            for (String s : part1_test_output) {
                expected.add(s);
            }
            Arrays.sort(just_for_output);
            for(String s: just_for_output) {
                boolean ok = expected.contains(s);
                out.println(s);
                if (!ok) {
                    out.println("\t ^ bad");
                }
            }
            out.println();
            for(String g:part1_test_output) {
                if(!test_set.contains(g)) {
                    out.printf("missing output: %s\n", g);
                }
            }
        }






        long answer = -1;
        return String.valueOf(answer);
    }

    private static void AddTri(Host[] tri, HashSet<HashSet<Host>> triangles) {
        HashSet<Host> set = new HashSet<>();
        set.add(tri[0]);
        set.add(tri[1]);
        set.add(tri[2]);
        if (set.size() != 3) {
            throw new IllegalArgumentException(String.format("Tri isn't size 3, size: %d, %s", set.size(), set));
        }
        triangles.add(set);
    }


    public static String getPart2() {
        long answer = -1;
        return String.valueOf(answer);
    }

}