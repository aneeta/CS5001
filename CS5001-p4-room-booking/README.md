# Room Booking System

## Design
The data is save to and loaded from a JSON file.
I chose to save in JSON because:
1. **JSON files are more portable than Java Serialised objects.** JSON can be used across different languages and is not limited to JAva. Also, in case the app were to be extended to use a database (especially a non-relational one), it would be easy to write a separate script loading and 
2. **JSON is human readable.** The data can be audeted easily without having to load it into a program.
3. **JSON is more flexible.** It supports serializing objects that don't implement `Serializable`, such as the `Local DateTime` (which I use as a field in a `Booking` object).
To do this, I chose to use a [Jackson Databind library](https://github.com/FasterXML/jackson-databind).

## How to run

### Depndencies
The app is dependent on Jackson Databind, which itself is dependednt on Core and Annotations.
They are oncluded with the submission in the `lib` directory.

### Compiling
To compile the app, navigate to the `src` directory and run:

```bash
javac -cp "$(printf %s: ../lib/*.jar)" roombooking/**/*.java
```

### Running the system

To start the app, navigate to the `src` directory and run:

```bash
java -cp "$(printf %s: ../lib/*.jar)" roombooking.main.RoomBookingMain
```

The above will not work if you are not running from `src` since it includes a relative path to `lib`.


### Running the tests

```bash
javac -cp "$(printf %s: ../lib/*.jar)" tests/*.java
```

```bash
java -cp "$(printf %s: ../lib/*.jar)" tests.TestRunner
```

