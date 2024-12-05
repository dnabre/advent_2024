package src.main.java.aoc_2024;

import java.util.List;
import java.util.function.IntFunction;

public class AoCUtils {
    public static int[] WhitespaceDelimitedLineToIntegers(String ln) {
        String[] parts = ln.split("\\s+");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i]);
        }
        return result;
    }

    public static String IntegerArrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i = 0; i < arr.length -1; i++) {
            sb.append(arr[i]);
            sb.append( ", ");
        }
        sb.append(arr[arr.length - 1]);
        sb.append("]");
        return sb.toString();
    }
    public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i);
        }
        return ret;
    }

}
