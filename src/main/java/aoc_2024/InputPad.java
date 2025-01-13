package src.main.java.aoc_2024;

import java.util.Optional;

public abstract class InputPad {
    public InputPad(String name) {
        this.name = name;
    }

    public int arm_mount_count = 0;
    Vector2d arm_pos;
    String name;

    abstract char getButtonPush();

    abstract char currentButtonUnderArm();

    String runMoves(String moves) {
        StringBuilder sb = new StringBuilder();
        for (char ch : moves.toCharArray()) {
            Optional<Character> r = move(ch);
            if (r.isPresent()) {
                char button_result = getButtonPush();
                sb.append(button_result);
            }
        }
        return sb.toString();
    }

    Optional<Character> move(char ch) {
        arm_mount_count++;
        if (ch == 'A') {
            return Optional.of(getButtonPush());
        } else {
            arm_pos = arm_pos.move(ch);
            if (!armCrashOk()) throw new IllegalStateException(String.format("Arm moved to crash position %s\n", arm_pos));
        }
        return Optional.empty();
    }

    protected abstract boolean armCrashOk();


}




