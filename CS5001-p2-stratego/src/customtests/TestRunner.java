package customtests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {

        Result result = JUnitCore.runClasses(GameMoveTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        if (result.wasSuccessful()) {
            System.out.println("Passed all " + result.getRunCount() + " Player JUnit tests!");
            System.exit(0);
        } else {
            System.out.println(
                    "Failed " + result.getFailureCount() + " out of " + result.getRunCount() + " Player JUnit tests!");
            System.exit(1);
        }
    }
}
