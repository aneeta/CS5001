import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * The WordCounter class prints a reports showing
 * the count of chosen words in a given document.
 */
public class WordCounter {
    private WordProcessor wordCounter;

    WordCounter(String filePath) {
        this.wordCounter = new WordProcessor(filePath);
    }

    /**
     * Method to count word occurences and print a count report.
     * CASE SENSITIVE. {'Word', 'word'} are not equivalent.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Check if the arguments are passed in correctly
        if (args.length < 2) {
            String usageMsg = "Usage: java WordCounter <filename> <searchTerm>";
            System.out.println(usageMsg);
        } else {
            String filePath = args[0];
            String[] targetWords = Arrays.copyOfRange(args, 1, args.length);

            // Count given words and store their counts in a map
            WordProcessor wordCounter = new WordProcessor(filePath);
            LinkedHashMap<String, Integer> countsMap = wordCounter.getWordCounts(targetWords);

            // Print out report (sentence or table)
            new StringGenerator(countsMap).printReport("The word '%s' appears %d time%s.",
                    "COUNT",
                    true);
        }
    }

    /**
     * Method to print a report of counts of words between given lines.
     * Will print counts either as sentence (if only one word found) or a table (if
     * multiple words found).
     * List of words in a table will be sorted alphabetically.
     * This method is CASE INSENSITIVE. {'word', 'WORD', 'wOrD'} are all equiqalent
     * and will be counted 3 times.
     * 
     * @param start      first line (inclusive). index starts at 0.
     * @param end        last line (inclusive).
     * @param colName    name for right (count) columnt in report table
     * @param printTotal flag determining if to show a footer row with TOTAL count
     * @throws FileNotFoundException
     */
    public void printWordsCountsInLines(int start, int end, String colName, Boolean printTotal)
            throws FileNotFoundException {
        TreeMap<String, Integer> countsMap = this.wordCounter.getWordsCountsInLines(start, end);
        new StringGenerator(countsMap).printReport("The found word is '%s' and it appears %d time%s.",
                colName, printTotal);
    }

    /**
     * Method to print report of N longest words.
     * Will print length either in sentence (if N=1) or a table (if N > 1).
     * List of words in a table will be sorted alphabetically.
     * This method is CASE INSENSITIVE. {'word', 'WORD', 'wOrD'} are all equiqalent
     * and will be counted 3 times.
     * 
     * @param num        number of longest words
     * @param colName    name for right (count) columnt in report table
     * @param printTotal flag determining if to show a footer row with TOTAL count
     * @throws FileNotFoundException
     */
    public void printLongestWords(int num, String colName, Boolean printTotal) throws FileNotFoundException {
        TreeMap<String, Integer> countsMap = this.wordCounter.getLongestWords(num);
        new StringGenerator(countsMap).printReport("The longest word is '%s' and it has %d letter%s.",
                colName, printTotal);
    }
}

class WordProcessor {
    private String filePath;
    private File targetFile;

    WordProcessor(String filePath) {
        this.filePath = filePath;
        this.targetFile = new File(filePath);
    }

    LinkedHashMap<String, Integer> getWordCounts(String[] targetWords) {
        LinkedHashMap<String, Integer> countsMap = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < targetWords.length; i++) {
            countsMap.put(targetWords[i], 0);
        }

        try {
            Scanner docReader = new Scanner(this.targetFile);
            // Split each line on non alphanumeric characters
            // Count instances of each keyword in a line
            while (docReader.hasNextLine()) {
                String rawLine = docReader.nextLine();
                String[] splitLine = rawLine.split("\\W");
                for (int i = 0; i < splitLine.length; i++) {
                    for (int j = 0; j < countsMap.size(); j++) {
                        String key = targetWords[j];
                        // chose equals method besause the given tests
                        // implied that the counts should be case sensitive
                        if (splitLine[i].equals(key)) {
                            countsMap.put(key, countsMap.get(key) + 1);
                        }
                    }
                }
            }
            docReader.close();

        } catch (FileNotFoundException e) {
            String exceptionMsg = String.format("File not found: %s", this.filePath);
            System.out.println(exceptionMsg);
            System.exit(1);
        }
        return countsMap;
    }

    TreeMap<String, Integer> getWordsCountsInLines(int start, int end) throws FileNotFoundException {
        try {
            assert start >= 0;
            assert start < end;
        } catch (AssertionError e) {
            System.out.println(
                    "Invalid start or end parameters. Must be non-negative and start must be greater than end.");
        }

        TreeMap<String, Integer> countsMap = new TreeMap<String, Integer>();

        Scanner docReader = new Scanner(this.targetFile);
        int counter = 0;
        while (docReader.hasNextLine()) {
            String rawLine = docReader.nextLine();
            if (counter >= start && counter <= end) {
                // filter out empty strings
                String[] splitLine = Arrays.stream(rawLine.toLowerCase().split("\\W"))
                        .filter(x -> !x.isEmpty()).toArray(String[]::new);
                for (int i = 0; i < splitLine.length; i++) {
                    String key = splitLine[i];
                    if (countsMap.containsKey(key)) {
                        countsMap.put(key, countsMap.get(key) + 1);
                    } else {
                        countsMap.put(key, 1);
                    }
                }
            }
            counter++;
        }
        docReader.close();
        return countsMap;
    }

    TreeMap<String, Integer> getLongestWords(int num) throws FileNotFoundException {
        String[] longestWords = new String[num];
        for (int i = 0; i < num; i++) {
            longestWords[i] = "";
        }
        Scanner docReader = new Scanner(this.targetFile);
        while (docReader.hasNextLine()) {
            String rawLine = docReader.nextLine();
            String[] splitLine = rawLine.toLowerCase().split("\\W");
            String[] jointArr = Utils.concatStringArrays(longestWords, splitLine);
            // get distinct
            String[] unique = Arrays.stream(jointArr).distinct().toArray(String[]::new);
            // filter nulls out
            String[] uniqueFiltered = Arrays.stream(unique).filter(Objects::nonNull).toArray(String[]::new);
            // sort shortest to longest
            Arrays.sort(uniqueFiltered, Comparator.comparingInt(String::length).reversed());
            // update longestWords array with N longest words
            longestWords = Arrays.copyOfRange(uniqueFiltered, 0, num);
        }
        docReader.close();

        Integer[] wordCounts = Utils.getWordLengths(longestWords);
        TreeMap<String, Integer> countsMap = new TreeMap<String, Integer>();

        for (int i = 0; i < longestWords.length; i++) {
            countsMap.put(longestWords[i], wordCounts[i]);
        }
        return countsMap;
    }
}

class StringGenerator {
    private static final String HEADER = "WORD";
    private static final String TOTAL = "TOTAL";
    private int minColTwoVal;
    private int minColOneVal;
    private Map<String, Integer> data;

    StringGenerator(Map<String, Integer> data) {
        this.data = data;
    }

    void printReport(String baseMsg, String colTwoName, Boolean includeTotal) {
        this.minColOneVal = (includeTotal ? TOTAL.length() : HEADER.length());
        this.minColTwoVal = colTwoName.length();

        if (this.data.size() == 1) {
            // single target word case
            printReportString(baseMsg);
        } else {
            // multi targetword case
            printReportTable(colTwoName, includeTotal);
        }
    }

    void printReportString(String baseMsg) {
        Map.Entry<String, Integer> pair = this.data.entrySet().iterator().next();
        String key = pair.getKey();
        int wordCount = pair.getValue();
        String pluralChar = ((wordCount == 1) ? "" : "s");
        String formattedMsg = String.format(baseMsg, key, wordCount, pluralChar);

        System.out.println(formattedMsg);

    }

    void printReportTable(String colTwoName, Boolean includeTotal) {
        int longestCountLen = Utils.countDigits(Collections.max(this.data.values()));
        int longestWordLen = Collections.max(
                Arrays.asList(Utils.getWordLengths(this.data.keySet().toArray(String[]::new))));

        // MIN_COL_VAL determined by header/footer titles
        int colOneWidth = Math.max(longestWordLen, minColOneVal);
        int colTwoWidth = Math.max(longestCountLen, minColTwoVal);
        String baseChunkOne = " %-" + Integer.toString(colOneWidth) + "s ";
        String baseChunkTwo = " %" + Integer.toString(colTwoWidth) + "s ";

        List<String> horizontalLine = Arrays.asList(
                "",
                String.format(baseChunkOne, "").replace(" ", "-"),
                String.format(baseChunkTwo, "").replace(" ", "-"),
                "");

        List<String> reportRows = new ArrayList<String>();
        reportRows.add(String.join("|", horizontalLine));
        reportRows.add(
                String.join("|",
                        Arrays.asList(
                                "",
                                String.format(baseChunkOne, StringGenerator.HEADER),
                                String.format(baseChunkTwo, colTwoName),
                                "")));
        reportRows.add(String.join("|", horizontalLine));

        int totalCount = 0;
        for (Map.Entry<String, Integer> entry : this.data.entrySet()) {
            totalCount += entry.getValue();
            reportRows.add(
                    String.join("|",
                            Arrays.asList(
                                    "",
                                    String.format(baseChunkOne, entry.getKey()),
                                    String.format(baseChunkTwo, entry.getValue()),
                                    "")));
        }

        if (includeTotal) {
            reportRows.add(String.join("|", horizontalLine));
            reportRows.add(
                    String.join("|",
                            Arrays.asList(
                                    "",
                                    String.format(baseChunkOne, StringGenerator.TOTAL),
                                    String.format(baseChunkTwo, Integer.toString(totalCount)),
                                    "")));
        }

        reportRows.add(String.join("|", horizontalLine));
        String reportTable = String.join("\n", reportRows);

        System.out.println(reportTable);
    }
}

class Utils {

    static String[] concatStringArrays(String[] arrOne, String[] arrTwo) {
        String[] jointArr = new String[arrOne.length + arrTwo.length];
        System.arraycopy(arrOne, 0, jointArr, 0, arrOne.length);
        System.arraycopy(arrTwo, 0, jointArr, arrOne.length, arrTwo.length);
        return jointArr;
    }

    static Integer[] getWordLengths(String[] wordList) {
        Integer[] wordLengths = new Integer[wordList.length];
        for (int i = 0; i < wordList.length; i++) {
            wordLengths[i] = wordList[i].length();
        }
        return wordLengths;
    }

    static int countDigits(int num) {
        int digitCount = 0;
        for (; num != 0; num /= 10) {
            digitCount++;
        }
        return digitCount;
    }
}
