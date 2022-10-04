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
$ javac WordCounter <file path> <list of target words>
```

### Example
```bash
$ javac WordCounter test.txt word test document
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

## Design

