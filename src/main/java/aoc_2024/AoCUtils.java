package src.main.java.aoc_2024;

public class AoCUtils {
    public static int[] WhitespaceDelimitedLineToInts(String ln) {
        String[] parts = ln.split("\\s+");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i]);
        }
        return result;
    }

    public static String IntegerArrayToString(int[] arr) {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for(int i = 0; i < arr.length -1; i++) {
            sb.append(arr[i] + ", ");
        }
        sb.append(arr[arr.length - 1]);
        sb.append("]");
        return sb.toString();
    }

}
