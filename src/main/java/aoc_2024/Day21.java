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




    static class DirPad extends InputPad {
        static HashMap<Character, Vector2d> pad_map;
        static HashMap<Vector2d, Character> position_to_value;
        InputPad next_in_chain = null;

        public DirPad(String _name, InputPad next_pad) {
            super(_name);
            arm_pos = pad_map.get('A');
            next_in_chain = next_pad;
        }

        static void initialize() {
            HashMap<Character, Vector2d> my_pad_map = new HashMap<>();
            HashMap<Vector2d, Character> my_position_to_value = new HashMap<>();

            /*
                    | ^ | A |
                | < | v | > |
             */
            my_pad_map.put('^', new Vector2d(1, 0));
            my_pad_map.put('A', new Vector2d(2, 0));
            my_pad_map.put('<', new Vector2d(0, 1));
            my_pad_map.put('v', new Vector2d(1, 1));
            my_pad_map.put('>', new Vector2d(2, 1));


            for (char value : my_pad_map.keySet()) {
                Vector2d pos = my_pad_map.get(value);
                my_position_to_value.put(pos, value);
            }
            DirPad.position_to_value = my_position_to_value;
            DirPad.pad_map = my_pad_map;
        }

        @Override
        char getButtonPush() {
            return position_to_value.get(this.arm_pos);
        }

        @Override
        char currentButtonUnderArm() {
            if(position_to_value.containsKey(this.arm_pos)) {
                return position_to_value.get(arm_pos);
            } else {
                return '!';
            }
        }


        @Override
        Optional<Character> move(char ch) {
            if(ch!= 'A') {
                Directions.Compass dir = Directions.Compass.fromChar(ch);
//                out.printf("moving arm from: %s to %s direct: %s  (DirPad: %s)\n", arm_pos, arm_pos.locationAfterStep(dir), dir, this.name);
            } else {
//                out.printf("pushing button (arm: %s) button: %s (DirPad: %s)\n", arm_pos, position_to_value.get(arm_pos), this.name);
            }
            Optional<Character> r = super.move(ch);
//            out.printf("doing %c, result %s, pushing %s to ", ch, r, r.orElse(null));
            if(r.isPresent() && (next_in_chain != null)) {
//                out.println(next_in_chain);
                r = next_in_chain.move(r.get());

            } else {
//                out.println(" caller");
            }
            return r;
        }

        @Override
        protected boolean armCrashOk() {
            if (DirPad.position_to_value.containsKey(this.arm_pos)) {
                return true;
            } else {
                out.println("arm moved to error state");
                Day21.debug_stack();
                throw new IllegalStateException(String.format("DirPad %s, arm moved to illegal position (%s)",
                        this.name, this.arm_pos));
            }
        }

        @Override
        public String toString() {
            return String.format("DirPad: %s, Arm Position: %s, Over Value: %c, Move Count: %d", this.name, arm_pos,
                    position_to_value.get(arm_pos), this.arm_mount_count);
        }
    }

    static class NumPad extends InputPad {
        static HashMap<Character, Vector2d> numpad_map;
        static HashMap<Vector2d, Character> position_to_value;

        public NumPad(String _name) {
            super(_name);
            arm_pos = NumPad.numpad_map.get('A');
        }
        @Override
        char currentButtonUnderArm() {
            if(position_to_value.containsKey(this.arm_pos)) {
                return position_to_value.get(arm_pos);
            } else {
                return '!';
            }
        }
        static void initialize() {
            HashMap<Character, Vector2d> my_numpad_map = new HashMap<>();
            HashMap<Vector2d, Character> my_position_to_value = new HashMap<>();

        /*
        | 7 | 8 | 9 |
        | 4 | 5 | 6 |
        | 1 | 2 | 3 |
            | 0 | A |
         */
            my_numpad_map.put('7', new Vector2d(0, 0));
            my_numpad_map.put('8', new Vector2d(1, 0));
            my_numpad_map.put('9', new Vector2d(2, 0));
            my_numpad_map.put('4', new Vector2d(0, 1));
            my_numpad_map.put('5', new Vector2d(1, 1));
            my_numpad_map.put('6', new Vector2d(2, 1));
            my_numpad_map.put('1', new Vector2d(0, 2));
            my_numpad_map.put('2', new Vector2d(1, 2));
            my_numpad_map.put('3', new Vector2d(2, 2));
            my_numpad_map.put('0', new Vector2d(1, 3));
            my_numpad_map.put('A', new Vector2d(2, 3));

            for (char value : my_numpad_map.keySet()) {
                Vector2d pos = my_numpad_map.get(value);
                my_position_to_value.put(pos, value);
            }
            NumPad.numpad_map = my_numpad_map;
            NumPad.position_to_value = my_position_to_value;
        }

        @Override
        char getButtonPush() {

            char button = NumPad.position_to_value.get(arm_pos);
//            out.printf("numpad (%s), pushing button: %c \n", this.name,button );
            Day21.debug_buffer.append(String.format("(%s,%c)", this.name, button));
            return button;
        }



        @Override
        protected boolean armCrashOk() {
            if (NumPad.position_to_value.containsKey(this.arm_pos)) {
                return true;
            } else {
                throw new IllegalStateException(String.format("NumPad: %s, arm moved to illegal position (%s)", this.name,
                        this.arm_pos));
            }
        }

        @Override
        public String toString() {
            return String.format("Numpad: %s, Arm Position: %s, Over Value: %c Move Count: %d",
                    this.name, arm_pos, position_to_value.get(arm_pos), this.arm_mount_count);
        }
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


    static InputPad[] pad_stack;
    static StringBuilder output_buffer;
    protected static String getPart1() {

        out.printf("read %d lines\n\n", lines.length);
        HashSet<String> cc_set = new HashSet<>();
        for(String ln: lines) {
            String[] parts = ln.split(AoCUtils.WHITESPACE_RE);
            out.println(Arrays.toString(parts));
            String code = parts[0];
            String commands = parts[1];
            char[] c_array = commands.toCharArray();
            int idx =0;
            while(idx < parts[1].length()) {
                StringBuilder sb = new StringBuilder();
                while((idx < c_array.length) && (c_array[idx] != 'A')) {
                    sb.append(c_array[idx]);
                    idx++;
                }
                sb.append(c_array[idx]);
                idx++;
                while((idx < c_array.length) && (c_array[idx] != 'A')) {
                    sb.append(c_array[idx]);
                    idx++;
                }
                if(idx < c_array.length) {
                    sb.append(c_array[idx]);
                }

                cc_set.add(sb.toString());
            }
        }
        for(String cc: cc_set) {
            go(cc);
            out.printf("cc: %s\t %s\n", cc, output_buffer);
            output_buffer = new StringBuilder();
        }




        out.println(cc_set);
        out.printf("cc_set size: %d\n", cc_set.size());


          out.println(Day21.output_buffer);
          out.println("-----------------------------------------");

        long answer = -1;
        return String.valueOf(answer);
    }

    static void go(String command_string) {

        Optional<Character> r = Optional.empty();
        output_buffer = new StringBuilder();
        String command = command_string;
        out.printf("running %s through chain\n", command);
        InputPad my_pad = new NumPad("number");
        push_pad(my_pad);
        InputPad dir_pad = new DirPad("first dir", my_pad);
        push_pad(dir_pad);
        InputPad dir_pad2 = new DirPad("second dir", dir_pad);
        push_pad(dir_pad2);
        for (char ch : command.toCharArray()) {
//            out.printf("top command: %c \t", ch);
            r = dir_pad2.move(ch);
//            out.printf("r: %s\n",r);
            if(r.isPresent()) {
                output_buffer.append(r.get());
            }
        }
        out.printf("full chain result: %s\n", output_buffer);
 //       debug_stack();
    }



    static ArrayList<InputPad> stack_list = null;
    private static void push_pad(InputPad pad) {
        if(stack_list==null) {
            stack_list = new ArrayList<>();
        }
        stack_list.add(pad);
        pad_stack = stack_list.toArray(new InputPad[stack_list.size()]);
    }

    protected static String getPart2() {
        long answer = -1;
        return String.valueOf(answer);
    }

    protected static void parseInput(String filename) throws IOException {
        lines = Files.readAllLines(Path.of(filename)).toArray(new String[0]);

        DirPad.initialize();
        NumPad.initialize();

    }

    static void debug_stack() {
        out.println("\n debugging full pad stack");
        out.printf("\toutput_buffer: %s\n", Day21.output_buffer.toString());
        out.printf("\tdebug_buffer: %s\n", Day21.debug_buffer.toString());
        for(int i=pad_stack.length-1; i >= 0; i--) {
            InputPad ip = pad_stack[i];
            out.printf("\tpad %s, arm_pos: %s over: %c, count: %d, %s\n",
                    ip.name, ip.arm_pos, ip.currentButtonUnderArm(), ip.arm_mount_count, ip
                    );
        }
    }
    static StringBuilder debug_buffer = new StringBuilder();
}