package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.System.out;

public class Day23 {

    public static final String PART1_ANSWER = "1064";
    public static final String PART2_ANSWER = "aq,cc,ea,gc,jo,od,pa,rg,rv,ub,ul,vr,yy";
    private static LinkPair[] linkPairs;
    private static Host[] hosts;
    private static HashMap<Host, Node> host_to_node;
    private static List<Node> node_list;
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

     record Host(String name)  {
        static Host[] fromStringPair(String s) {
            Host[] ha = new Host[2];
            String[] part = s.split("-");
            ha[0] = new Host(part[0].trim());
            ha[1] = new Host(part[1].trim());
            return ha;
        }

    }

    record Node(Host name, HashSet<Host> adjacent) {
    }


     static HashMap<Host, Node> getHostToNode() {

        HashSet<Node> node_set = new HashSet<>();
        HashMap<Host, Node> node_map = new HashMap<>();
        for (LinkPair lp : linkPairs) {
            Node node1, node2;
            if (node_map.containsKey(lp.left)) {
                node1 = node_map.get(lp.left);
            } else {
                node1 = new Node(lp.left, new HashSet<>());
                node_map.put(lp.left, node1);
            }
            if (node_map.containsKey(lp.right)) {
                node2 = node_map.get(lp.right);
            } else {
                node2 = new Node(lp.right, new HashSet<>());
                node_map.put(lp.right, node2);
            }
            node_set.add(node1);
            node_set.add(node2);
            node1.adjacent.add(node2.name);
            node2.adjacent.add(node1.name);
        }
        node_list = node_set.stream().toList();
        return node_map;
    }

    record LinkPair(Host left, Host right) {
        @Override
        public String toString() {
            return String.format("<%s-%s>", left, right);
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
         host_to_node= getHostToNode();
    }


    public static String getPart1() {
        HashSet<HashSet<Host>> triangles = new HashSet<>();
        for (Node a_node : node_list) {
            HashSet<Host> a_adj = a_node.adjacent;
            for (Host adj_node : a_adj) {
                Node b_node = host_to_node.get(adj_node);
                HashSet<Host> b_adj = b_node.adjacent;
                for (Host b_adj_node : b_adj) {
                    Node c_node = host_to_node.get(b_adj_node);
                    if (c_node.adjacent.contains(a_node.name)) {
                        Host[] tri = new Host[3];
                        tri[0] = a_node.name;
                        tri[1] = b_node.name;
                        tri[2] = c_node.name;
                        AddTri(tri, triangles);
                    }
                }
            }
        }

        String[] stringles = new String[triangles.size()];
        boolean[] tness = new boolean[triangles.size()];
        int idx = 0;
        for (HashSet<Host> tri : triangles) {
            tness[idx]=false;
            String t = "";
            for(Host h: tri) {
                if(h.name.charAt(0)== 't') {
                    tness[idx] = true;
                }
                t =t+ h.name + ",";
            }
            stringles[idx] = t.substring(0,t.length()-1);
            idx++;
        }
        int t_count =0;
        Arrays.sort(stringles);
        for(int i=0;i < stringles.length; i++) {
            if(tness[i]) t_count++;
  //          out.printf("%3d\t %s\t%b\n", i+1, stringles[i], tness[i]);

        }






        long answer = t_count;
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