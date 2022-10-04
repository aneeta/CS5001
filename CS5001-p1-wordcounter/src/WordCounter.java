// TODO
//  ask questions
//  add documentation
//  change var names to something more descriptive
//  implement exception
//  see if can generate the table more elegantly

/* QUESTIONS
 * - Should there be a javadoc comment for every single method?
 * - How expilict should I be with the comments??
 * - Are we marked down for style comments? (for example a magic number warning)
 * - How to setup Java linter in line with  on VSCode
 * - are we allowed to use Java modules or are we supposed to implement everything ourselves??
 * - do we include identifiable information (name, student number) in the submisstion?
 * - why does my expception not work??
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.Scanner;

/**
 * The WordCounter class prints a reports showing
 * the count of chosen words in a given document.
 */
public class WordCounter {

    /** Method to count word occurences and print a count report.
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
            LinkedHashMap<String, Integer> countsMap = getWordCounts(filePath, targetWords);
            // Print out report (sentence or table)
            printReport(countsMap);
        }
    }


    /** 
     * Method to tally the number of occurences of given words in a given file,
     * Outputs a mapping of given words to its counts.
     * @param filePath path to document
     * @param targetWords list of words to count
     * @return LinkedHashMap<String, Integer>
     */
    public static LinkedHashMap<String, Integer> getWordCounts(String filePath, String[] targetWords) {
        //Initialize Map
        LinkedHashMap<String, Integer> countsMap = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < targetWords.length; i++) {
            countsMap.put(targetWords[i], 0);
        }
        try {
            // Read in the file
            File targetDoc = new File(filePath);
            Scanner docReader = new Scanner(targetDoc);
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
            String exceptionMsg = String.format("File not found: %s", filePath);
            System.out.println(exceptionMsg);
            System.exit(1);
        }
        return countsMap;
    }


    /** 
     * General method to print a word count report.
     * Calls two methods depending on size of countsMap.
     * @param countsMap word-count map
     */
    public static void printReport(LinkedHashMap<String, Integer> countsMap) {
        if (countsMap.size() == 1) {
            // single target word case
            printReportString(countsMap);
        } else {
            // multi targetword case
            printReportTable(countsMap);
        }
    }


    /** 
     * Method to construct and print a report string based on the first entry in a word count map.
     * @param countsMap word-count map
     */
    public static void printReportString(LinkedHashMap<String, Integer> countsMap) {
        String baseMsg = "The word '%s' appears %d time%s.";
        Map.Entry<String, Integer> pair = countsMap.entrySet().iterator().next();
        String key = pair.getKey();
        int wordCount = pair.getValue();
        String pluralChar = ((wordCount == 1) ? "" : "s");
        String formattedMsg = String.format(baseMsg, key, wordCount, pluralChar);

        System.out.println(formattedMsg);

    }


    /** 
     * Method to construct and print a table based on a word count map.
     * @param countsMap word-count map
     */
    public static void printReportTable(LinkedHashMap<String, Integer> countsMap) {
        int longestCountLen = countDigits(Collections.max(countsMap.values()));
        int longestWordLen = Collections.max(Arrays.asList(getWordLengths(countsMap.keySet().toArray(String[]::new))));

        // minColVal determined by header/footer titles
        int minColVal = 5;
        int colOneWidth = Math.max(longestWordLen, minColVal);
        int colTwoWidth = Math.max(longestCountLen, minColVal);
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
                                String.format(baseChunkOne, "WORD"),
                                String.format(baseChunkTwo, "COUNT"),
                                "")));
        reportRows.add(String.join("|", horizontalLine));
        int totalCount = 0;
        for (Map.Entry<String, Integer> entry : countsMap.entrySet()) {
            totalCount += entry.getValue();
            reportRows.add(
                    String.join("|",
                            Arrays.asList(
                                    "",
                                    String.format(baseChunkOne, entry.getKey()),
                                    String.format(baseChunkTwo, entry.getValue()),
                                    "")));
        }
        reportRows.add(String.join("|", horizontalLine));
        reportRows.add(
                String.join("|",
                        Arrays.asList(
                                "",
                                String.format(baseChunkOne, "TOTAL"),
                                String.format(baseChunkTwo, Integer.toString(totalCount)),
                                "")));

        reportRows.add(String.join("|", horizontalLine));
        String reportTable = String.join("\n", reportRows);

        System.out.println(reportTable);

    }


    /** 
     * Method returning a counts of each string in a given list.
     * @param wordList list of strings
     * @return Integer[]
     */
    public static Integer[] getWordLengths(String[] wordList) {
        Integer[] wordLengths = new Integer[wordList.length];
        for (int i = 0; i < wordList.length; i++) {
            wordLengths[i] = wordList[i].length();
        }
        return wordLengths;
    }


    /** word-count map
     * Method returning the digit count of a given number.
     * @param num number to count the digits of
     * @return int
     */
    public static int countDigits(int num) {
        int digitCount = 0;
        for (; num != 0; num /= 10) {
            digitCount++;
        }
        return digitCount;
    }
}
