# WordCounter
WordCounter is the first ptactical assignment for the CS5001 Object-Oriented Modelling, Design and Programming module at the University of St Andrews.

WordCounter reports counts of given word or words in a given file.
When ran for a single word, it will give a one sentence summary.
Otherwise, it will display a table of counts sorted in the order of word entry.

## How to run
Navigate to the `src` directory. Alternatively, adjust the below commands to include correct paths.

To compile:
```bash
$ javac WordCounter.java
```

To run:
```bash
$ java WordCounter <file path> <list of target words>
```

### Example
```bash
$ java WordCounter ../example/test.txt word test document
|----------|-------|
| WORD     | COUNT |
|----------|-------|
| word     |     4 |
| test     |     3 |
| document |     4 |
|----------|-------|
| TOTAL    |    11 |
|----------|-------|
```

<b>Note:</b> words passed twice will be ignored. 

```bash
$ java WordCounter ../example/test.txt test test
The word 'test' appears 3 times.
```

### Additional functions
`WordCounter` implements two methods other than `main`: `printWordsCountsInLines` and `printLongestWords`.

```java
// Initialize WordCounter class
WordCounter wc = new WordCounter(filePath);

// pick some variables
int start = 0;
int end = 5;
int num = 3;
String colName = "OUTPUT";
Boolean printTotal = true;

// get word counts in given line scope
wc.printWordsCountsInLines(start, end, colName, printTotal);
// get N longest words across the file
wc.printLongestWords(num, colName, printTotal);
```

To view demo of extra functions run:
```bash
$ javac *.java
$ java Example <text file path>
```
For example:
```bash
$ java Example ../example/test.txt
|------------|-------|
| WORD       | COUNT |
|------------|-------|
| a          |     1 |
| assignment |     1 |
| counter    |     1 |
| document   |     1 |
| for        |     1 |
| is         |     1 |
| practical  |     1 |
| test       |     1 |
| the        |     1 |
| this       |     2 |
| word       |     1 |
|------------|-------|
| TOTAL      |    12 |
|------------|-------|

...
```
(Showing only first output table for brevity)

```bash
$ java Example /cs/studres/CS5001/Coursework/p1-wordcounter/Resources/pride-and-prejudice.txt

...

|-------------------|--------------|
| WORD              | LETTER COUNT |
|-------------------|--------------|
| communicativeness |           17 |
| discontentedness  |           16 |
| disinterestedness |           17 |
| incomprehensible  |           16 |
| misrepresentation |           17 |
| superciliousness  |           16 |
|-------------------|--------------|
```
(Showing only last output table for brevity)

## Design

The implementation consists of four classes: `WordCounter`, `WordProcessor`, `StringGenerator`, and `Utils`.

Only `WordCounter` is a public class and it serves as gateway to given tasks.

`WordProcessor` is a defualt class meant to perform tasks on a given document. It implements the following methods
- `getWordCounts(String[] targetWords)` - counts occurences of given words across the document.
- `getWordsCountsInLines(int start, int end)` - reports any words and their counts on line interval $[start, end]$.
- `getLongestWords(int num)` - gives $num$ longest words from across the document. 

`StringGenerator` is a defualt class constructing the report string or table.

`Utils` is a defult class containing utility methods used in `WordProcessor` and `StringGenerator`.

The source files also include an additional `Example` class hold demo of additional functionality.
