import basicmaths.BasicMaths;

public class MyFristProgram {

    public static void main (String[] args) {
        int intVar = 8;
        System.out.println(intVar);
        System.out.println(BasicMaths.square(intVar));
        System.out.println(BasicMaths.timesTwo(intVar));
        System.out.println(BasicMaths.half(intVar));
        System.out.println(BasicMaths.degreesToRadians(intVar));
        System.out.println(BasicMaths.isOdd(intVar));
        System.out.println(BasicMaths.spellOutNumber(intVar));
        System.out.println(BasicMaths.spellOutNumber(392));
    }
}