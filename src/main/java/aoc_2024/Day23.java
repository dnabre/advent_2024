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
    private static HashMap<String, Node> host_to_node;
    private static List<Node> node_list;


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

    static HashMap<String, Node> getHostToNode() {
        HashSet<Node> node_set = new HashSet<>();
        HashMap<String, Node> node_map = new HashMap<>();
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

    public static void parseInput(String filename) throws IOException {
        String[] lines = Files.readAllLines(Path.of(filename)).toArray(new String[0]);
        linkPairs = new LinkPair[lines.length];
        for (int i = 0; i < lines.length; i++) {
            //Host[] pair = Host.fromStringPair(lines[i]);
            LinkPair lp = LinkPair.fromStringPair(lines[i]);
            //LinkPair lp = new LinkPair(pair[0], pair[1]);
            linkPairs[i] = lp;
        }

        host_to_node = getHostToNode();
    }



    public static String getPart1() {
        HashSet<HashSet<String>> triangles = new HashSet<>();
        for (Node a_node : node_list) {
            HashSet<String> a_adj = a_node.adjacent;
            for (String adj_node : a_adj) {
                Node b_node = host_to_node.get(adj_node);
                HashSet<String> b_adj = b_node.adjacent;
                for (String b_adj_node : b_adj) {
                    Node c_node = host_to_node.get(b_adj_node);
                    if (c_node.adjacent.contains(a_node.name)) {
                        String[] tri = new String[3];
                        tri[0] = a_node.name;
                        tri[1] = b_node.name;
                        tri[2] = c_node.name;
                        addTripleToSet(tri, triangles);
                    }
                }
            }
        }

        int count = 0;
        for (HashSet<String> tri : triangles) {
            for (String h : tri) {
                if (h.charAt(0) == 't') {
                    count++;
                    break;
                }
            }
        }

        long answer = count;
        return String.valueOf(answer);
    }

    public static String getPart2() {
        HashMap<String, HashSet<String>> graph = new HashMap<>();
        for (LinkPair lp : linkPairs) {
            HashSet<String> a_set = graph.getOrDefault(lp.left, new HashSet<>());
            a_set.add(lp.right);
            HashSet<String> b_set = graph.getOrDefault(lp.right, new HashSet<>());
            b_set.add(lp.left);
            graph.put(lp.left, a_set);
            graph.put(lp.right, b_set);
        }

        ArrayList<HashSet<String>> cliques = doBronKerbosch(graph, new HashSet<>(graph.keySet()), new HashSet<>(), new HashSet<>());


        HashSet<String> max = cliques.getFirst();
        for (var c : cliques) {
            if (c.size() > max.size()) {
                max = c;
            }
        }
        String[] pass = max.toArray(new String[0]);
        Arrays.sort(pass);

        return String.join(",", pass);
    }

    private static void addTripleToSet(String[] tri, HashSet<HashSet<String>> triangles) {
        HashSet<String> set = new HashSet<>();
        set.add(tri[0]);
        set.add(tri[1]);
        set.add(tri[2]);
        if (set.size() != 3) {
            throw new IllegalArgumentException(String.format("Tri isn't size 3, size: %d, %s", set.size(), set));
        }
        triangles.add(set);
    }

    static public ArrayList<HashSet<String>> doBronKerbosch(HashMap<String, HashSet<String>> graph, HashSet<String> to_explore, HashSet<String> seen, HashSet<String> explored) {
        if (to_explore.isEmpty() && seen.isEmpty()) {
            ArrayList<HashSet<String>> e_result = new ArrayList<>();
            e_result.add(explored);
            return e_result;
        }
        ArrayList<HashSet<String>> cliques = new ArrayList<>();
        while (!to_explore.isEmpty()) {
            String v = to_explore.iterator().next();
            to_explore.remove(v);

            HashSet<String> new_to_explore = new HashSet<>(to_explore);
            new_to_explore.retainAll(graph.get(v));

            HashSet<String> new_seen = new HashSet<>(seen);
            new_seen.retainAll(graph.get(v));
            HashSet<String> new_explored = new HashSet<>(explored);
            new_explored.add(v);

            cliques.addAll(doBronKerbosch(graph, new_to_explore, new_seen, new_explored));

            seen.add(v);
        }
        return cliques;
    }


    record Node(String name, HashSet<String> adjacent) {
    }

    record LinkPair(String left, String right) {
        static LinkPair fromStringPair(String s) {
            String[] part = s.split("-");
            return new LinkPair(part[0].trim(), part[1].trim());
        }

        @Override
        public String toString() {
            return String.format("<%s-%s>", left, right);
        }

    }

}