package basicmaths;

import java.lang.Math;
import java.util.HashMap;
import java.util.Map;


public class BasicMaths {
    public static Map<Integer, String> NumberMap;
    static {
        NumberMap = new HashMap<Integer, String>();
        NumberMap.put(1, "One");
        NumberMap.put(2, "Two");
        NumberMap.put(3, "Three");
        NumberMap.put(4, "Four");
        NumberMap.put(5, "Five");
        NumberMap.put(6, "Six");
        NumberMap.put(7, "Seven");
        NumberMap.put(8, "Eight");
        NumberMap.put(9, "Nine");
    }

    public static int square(int num) {
        return num*num;
    }

    public static int timesTwo(int num) {
        return num*2;
    }

    public static float half(int num) {
        return num/2;
    }

    public static double degreesToRadians(int num) {
        return num*Math.PI/180;
    }

    public static Boolean isOdd(int num) {
        return (num % 2 == 1 ? true : false);
    }

    public static String spellOutNumber(int num) {
        String s = Integer.toString(num);
        String[] numList = new String[s.length()]; 
        for (int i = 0; i < s.length(); i++) {
            numList[i] = NumberMap.get(Character.getNumericValue(s.charAt(i)));
        }

        return String.join("-", numList);
    }
}


