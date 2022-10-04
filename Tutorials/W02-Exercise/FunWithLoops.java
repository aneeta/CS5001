import triangle.Triangle;

public class FunWithLoops {
    public static void main(String[] args) {
        printLoop(-2, 4, 1);
        printLoop(6, 18, 2);
        printLoop(10, 50, 10);
        printDecLoop(68, 44, 3);
        printCharLoop('x', 8);
        whileLoop(10, 50, 10);
        asciiArt(1, 10, "square");
        asciiArt(10, 10, "square");
        asciiArt(3, 6, "square");
        asciiArt(10, 20, "triangle");
        asciiArt(10, 10, "triangle");
        asciiArt(5, 10, "triangle");
        Triangle myTri = new Triangle(16);
        myTri.printToScreen();
    }

    static void printLoop(int start, int finish, int increment) {
        for (int i = start; i <= finish; i+=increment) {
            System.out.print(String.format(" %d,", i));
        }
        System.out.println("");
    }

    static void printDecLoop(int start, int finish, int increment) {
        for (int i = start; i >= finish; i-=increment) {
            System.out.print(String.format(" %d,", i));
        }
        System.out.println("");
    }

    static void printCharLoop(char c, int times) {
        for (int i = 1; i <= times; i++) {
            String s = new String(new char[i]).replace('\0', c);
            System.out.print(String.format(" %s,", s));
        }
        System.out.println("");
    }

    static void whileLoop(int start, int end, int inc) {
        int i = start;
        while (i <= end) {
            System.out.print(String.format(" %d,", i));
            i += inc;
        }
        System.out.println("");
    }

    static void asciiArt(int row, int col, String shape) {
        String infoMsg = String.format("Printing %s with dimensions (%d, %d)", shape, row, col);
        System.out.println(infoMsg);
        
        String baseStr = new String(new char[col]);
        String topBtm = baseStr.replace('\0', '*');

        if (shape.equalsIgnoreCase("square")) {
            String middle = "*" + topBtm.substring(1,col-1).replace('*', ' ') + "*";
            for (int i = 0; i < row; i++) {
                System.out.println(((i==0 || i==row-1)? topBtm: middle));
            }
        } else if (shape.equalsIgnoreCase("triangle")) {
            String top = centerStr("*", col);
            System.out.println(top);
            for (int i = 1; i < 2*row-1; i+=2) {
                int minLen = ((i < col-2)? i : col-2 );
                String mid = "*" + new String(new char[minLen]).replace('\0', ' ') + "*";
                System.out.println(centerStr(mid, col));
            }
            System.out.println(topBtm);

        } else {
            String printMsg = String.format("Shape '%s' not recognized.", shape);
            System.out.println(printMsg);
        }

    }

    // static String centerStr(String str, int width) {
    //     int midPoint = ((width % 2 == 0)? width/2 : (width+1)/2);
    //     String formatStrRight = "%" + midPoint + "s" ;
    //     String formatStr = String.format(formatStrRight, "%-" + width + "s");
    //     return String.format(formatStr, str);
    // }

    public static String centerStr (String s, int width) {
        return String.format("%-" + width  + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }

    static void printRangeTen() {
        for (int i = 1; i <= 10; i++) {
            System.out.println(i);
        }
    }
}
