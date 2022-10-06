import java.io.FileNotFoundException;

/**
 * Class to demonstrate functionality of additional WordCount class methods.
 */
public class Example {

    /** Main method. Prints a handful of examples.
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        String filePath = args[0];
        WordCounter wc = new WordCounter(filePath);
        wc.printWordsCountsInLines(0, 1, "COUNT", true);
        wc.printWordsCountsInLines(3, 8, "TALLY", false);
        wc.printLongestWords(1, "", true);
        wc.printLongestWords(3, "WORD LENGTH", true);
        wc.printLongestWords(6, "LETTER COUNT", false);
    }
}
