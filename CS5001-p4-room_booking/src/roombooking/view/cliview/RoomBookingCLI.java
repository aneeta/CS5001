package view.cliview;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import controller.RoomBookingController;
import model.RoomBookingModel;

// implements PropertyChangeListener
public class RoomBookingCLI {

    // private RoomBookingModel model;
    private RoomBookingController controller;
    private boolean exit;
    private boolean exitWarned;

    private InputStream inStream;
    private OutputStream outStream;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public RoomBookingCLI(RoomBookingController controller) {
        // this.model = model;
        this.controller = controller;
        this.exit = false;

        inStream = System.in;
        outStream = System.out;
        this.bufferedReader = new BufferedReader(new InputStreamReader(this.inStream));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.outStream));
        // try {

        // } catch (IOException ioe) {
        // System.out.println(
        // "Could not set up CLI: " + ioe.getMessage());
        // }

    }

    // @Override
    public void run() {
        try {
            write("""
                    ╔════════════════════╗\n
                    ║ November ░░░░ 2022 ║  Welcome to the Command Line Interface\n
                    ╟──┬──┬──┬──┬──┬──┬──╢  of a ==Room Booking System== app\n
                    ║░░│░░│01│02│03│04│05║\n
                    ╟──┼──┼──┼──┼──┼──┼──╢  Commands:\n
                    ║06│07│08│09│10│11│12║  <NEW | REMOVE | LIST> <BOOKING | PERSON | ROOM | BUILDING> <...ARGS>\n
                    ╟──┼──┼──┼──┼──┼──┼──╢  <LOAD|SAVE> <PATH>\n
                    ║13│14│15│16│17│18│19║  EXIT\n
                    ╟──┼──┼──┼──┼──╔══╗──╢\n
                    ║20│21│22│23│24║25║26║  All input must be on one line. Enter will trigger input.\n
                    ╟──┼──┼──┼──┼──╚══╝──╢  For more information type HELP\n
                    ║27│28│29│30│░░│░░│░░║\n
                    ╚══╧══╧══╧══╧══╧══╧══╝
                        """);

            handleCommand();
        } catch (IOException e) {
            // TODO
        }
    }

    private void handleCommand() {
        while (!exit) {
            // wait until client gives an input
            String line = null;
            while (line == null) {
                try {
                    line = bufferedReader.readLine();
                    runCommand(line);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
    }

    public void runCommand(String line) throws IOException {
        String[] args = line.split(" ");
        String cmd = args[0].toUpperCase();
        switch (cmd) {
            case "NEW":
            case "REMOVE":
            case "LIST":
                getSubject(cmd);
            case "LOAD":
            case "SAVE":
                getPath(cmd);
            case "HELP":
                displayHelp();
                break;
            case "EXIT":
                handleExit();
                break;
            default:
                write("Unrecognised command. Type HELP for command options.");
                break;

        }
    }

    public void getSubject(String cmd) throws IOException {
        write("> Choose subject (BUILDING/ROOM/PERSON/BOOKING): ");
        String subject = bufferedReader.readLine();
        switch (subject.toUpperCase()) {
            case "BUILDING":
            case "ROOM":
            case "PERSON":
            case "BOOKING":
                getArgs(cmd, subject.toUpperCase());
                break;
            default:
                write("Unrecognised subject.");
                break;
        }
    }

    public void getArgs(String cmd, String subject) throws IOException {
        if (subject.equals("BUILDING")) {
            if (cmd.equals("LIST")) {
                // printData(controller.controlListBuildings());
            } else {
                write(">> Building name: ");
                String buildingName = bufferedReader.readLine().strip();
                if (cmd.equals("NEW")) {
                    write(">> Building address: ");
                    String address = bufferedReader.readLine().strip();
                    write(controller.controlAddBuilding(buildingName, address));
                } else {
                    // write(controller.controlRemoveBuilding(buildingName));
                }
            }
        } else if (subject.equals("PERSON")) {
            if (cmd.equals("LIST")) {
                // printData(controller.controlListPeople());
            } else {
                write(">> Person email: ");
                String email = bufferedReader.readLine().strip();
                if (cmd.equals("NEW")) {
                    write(">> Person name: ");
                    String name = bufferedReader.readLine().strip();
                    write(controller.controlAddPerson(name, email));
                } else {
                    // write(controller.controlRemovePerson(email));
                }
            }
        }

    }

    public void getPath(String cmd) throws IOException {
        write("> Choose path: ");
        String in = bufferedReader.readLine();

    }

    // public void runCommand(String line) throws IOException,
    // IndexOutOfBoundsException {
    // // parse input line
    // String[] args = line.split(" ");
    // String[] methodArgs = Arrays.copyOfRange(args, 2, args.length);

    // // call a method corresponding to the parsed command
    // switch (args[0].toUpperCase()) {
    // case "NEW":
    // case "REMOVE":
    // case "LIST":
    // switch (args[1].toUpperCase()) {
    // case "BUILDING":
    // case "ROOM":
    // case "PERSON":
    // case "BOOKING":
    // routeCommand(args[0], args[1], methodArgs);
    // break;
    // default:
    // write("Unrecognised command.");
    // break;
    // }
    // case "LOAD":
    // write(controller.controlLoad(args[1]));
    // break;
    // case "SAVE":
    // write(controller.controlSave(args[1]));
    // break;
    // case "HELP":
    // displayHelp();
    // break;
    // case "EXIT":
    // handleExit();
    // break;
    // default:
    // // unrecognized command - ignore
    // break;
    // }
    // }

    // public void routeCommand(String action, String subject, String[] args) throws
    // IndexOutOfBoundsException {
    // switch (subject) {
    // case "BUILDING":
    // if (action.equals("NEW")) {
    // controller.controlAddBuilding(args[0], args[1])
    // }
    // case "ROOM":
    // case "PERSON":
    // case "BOOKING":
    // }

    // }

    public void displayHelp() throws IOException {
        String helpString = """
                The Room Booking System allows you to schedule
                using a room at the %s.\n
                The available commands are:
                %s
                """;
        String formattedHelpString = String.format(helpString, controller.getInstitutionName());
        write(formattedHelpString);
    }

    public void handleExit() throws IOException {
        if (!exitWarned) {
            String exitMsg = """
                    The changes you made will not be saved upon exit.\n
                    Make sure to save your changes if desired.\n
                    Next time EXIT is called the CLI will be closed.
                    """;
            write(exitMsg);
            exitWarned = true;
            return;
        }
        write("Exiting...");
        exit = true;
    }

    public void write(String msg) throws IOException {
        if (msg != null) {
            this.bufferedWriter.write(msg);
            this.bufferedWriter.flush();
        }
    }

}
