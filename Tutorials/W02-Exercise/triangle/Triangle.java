package triangle;

public class Triangle {
    private int size;

    public Triangle(int size) {
        this.size = size;
    }

    public void printToScreen() {
        String top = centerStr("*", this.size);
            System.out.println(top);
            for (int i = 1; i < this.size-1; i+=2) {
                String mid = "*" + new String(new char[i]).replace('\0', ' ') + "*";
                System.out.println(centerStr(mid, this.size));
            }

        String btm = new String(new char[this.size]).replace('\0', '*');
        System.out.println(btm);
    }

    static String centerStr (String s, int width) {
        return String.format("%-" + width  + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }
}
