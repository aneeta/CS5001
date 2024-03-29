package roombooking.views.cli;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import roombooking.controller.BookingSystemController;

/**
 * Room Booking System Command Line Interface.
 */
public class BookingSystemCli {
    private BookingSystemController controller;
    private boolean exit;
    private boolean exitWarned;

    private InputStream inStream;
    private OutputStream outStream;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public BookingSystemCli(BookingSystemController controller) {
        this.controller = controller;
        this.exit = false;

        inStream = System.in;
        outStream = System.out;
        this.bufferedReader = new BufferedReader(new InputStreamReader(this.inStream));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.outStream));
    }

<<<<<<< HEAD:CS5001-p4-room-booking/src/roombooking/views/cli/BookingSystemCli.java
    // public void propertyChange(PropertyChangeEvent event) {
    // try {
    // write("%%% Property changed\n");
    // } catch (Exception e) {
    // // TODO: handle exception
    // }

    // }
    // @Override
=======
>>>>>>> 93b3af23433bde3ace8b13aac207642aa47ac42d:new-p4/src/roombooking/views/cli/BookingSystemCli.java
    public void run() {
        try {
            write("""
                    ╔════════════════════╗
                    ║ November ░░░░ 2022 ║  Welcome to the Command Line Interface
                    ╟──┬──┬──┬──┬──┬──┬──╢  of the ==Room Booking System== app
                    ║░░│░░│01│02│03│04│05║
                    ╟──┼──┼──┼──┼──┼──┼──╢  Commands:
                    ║06│07│08│09│10│11│12║  <NEW | REMOVE | LIST> <BOOKING | PERSON | ROOM | BUILDING> <...ARGS>
                    ╟──┼──┼──┼──┼──┼──┼──╢  <LOAD|SAVE> <PATH>
                    ║13│14│15│16│17│18│19║  EXIT
                    ╟──┼──┼──┼──┼──╔══╗──╢
                    ║20│21│22│23│24║25║26║  All input must be on one line. Enter will trigger input.
                    ╟──┼──┼──┼──┼──╚══╝──╢  For more information type HELP
                    ║27│28│29│30│░░│░░│░░║
                    ╚══╧══╧══╧══╧══╧══╧══╝
                        """);

            handleCommand();
        } catch (IOException e) {
<<<<<<< HEAD:CS5001-p4-room-booking/src/roombooking/views/cli/BookingSystemCli.java
            System.out.println(e.getMessage());
            // TODO
=======
            System.out.printf(
                    "Error initialising the CLI (%s)!\n Traceback:\n%s",
                    e.getMessage(), e.getStackTrace().toString());
>>>>>>> 93b3af23433bde3ace8b13aac207642aa47ac42d:new-p4/src/roombooking/views/cli/BookingSystemCli.java
        }
    }

    /**
     * High-level command handling method.
     * Gets user input and resonds approprietly.
     */
    public void handleCommand() {
        while (!exit) {
            // wait until client gives an input
            String line = null;
            while (line == null) {
                try {
                    line = bufferedReader.readLine();
                    runCommand(line);
                } catch (Exception e) {
                    System.out.printf(
                            "Error running the command (%s)!\n Traceback:\n%s",
                            e.getMessage(), e.getStackTrace().toString());
                }
            }
        }
    }

    /**
     * Direct command input to dedicated methods.
     * 
     * @param line
     * @throws IOException
     */
    public void runCommand(String line) throws IOException {
        String[] args = line.split(" ");
        String cmd = args[0].toUpperCase();
        switch (cmd) {
            case "NEW":
            case "REMOVE":
            case "LIST":
                getSubject(cmd);
                break;
            case "LOAD":
            case "SAVE":
                getPath(cmd);
                break;
            case "HELP":
                displayHelp();
                break;
            case "EXIT":
                handleExit();
                break;
            default:
                // unrecorgnised command - ignore
                break;
        }
    }

    /**
     * Method to prompt the input subject for 'NEW' or 'REMOVE' commands.
     * 
     * @param cmd
     * @throws IOException
     */
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
                write("Unrecognised subject. Type HELP for command options.");
                break;
        }
    }

    /**
     * Method to prompt and handle the input arguments for user command.
     * 
     * @param cmd     base command (NEW or REMOVE)
     * @param subject subject of command (BUILDING or ROOM or PERSON or BOOKING)
     * @throws IOException
     */
    public void getArgs(String cmd, String subject) throws IOException {
        if (subject.equals("BUILDING")) {
            if (cmd.equals("LIST")) {
                write(Arrays.deepToString(controller.controlListBuildings()));
            } else {
                write(">> Building name: ");
                String buildingName = bufferedReader.readLine().strip();
                if (cmd.equals("NEW")) {
                    write(">> Building address: ");
                    String address = bufferedReader.readLine().strip();
                    write(controller.controlAddBuilding(buildingName, address));
                } else {
                    // validate building exists
                    write(controller.controlRemoveBuilding(buildingName));
                }
            }
        } else if (subject.equals("PERSON")) {
            if (cmd.equals("LIST")) {
                write(Arrays.deepToString(controller.controlGetPeople()));
            } else {
                write(">> Person name: ");
                String name = bufferedReader.readLine().strip();
                write(">> Person email: ");
                String email = bufferedReader.readLine().strip();
                if (cmd.equals("NEW")) {
                    write(controller.controlAddPerson(name, email));
                } else {
                    write(controller.controlRemovePerson(name, email));
                }
            }
        } else if (subject.equals("ROOM")) {
            if (cmd.equals("LIST")) {
                write(Arrays.deepToString(controller.controlGetRooms()));
            } else {
                write(">> Room name: ");
                String roomId = bufferedReader.readLine().strip();
                write(">> Room building name: ");
                String roomBuilding = bufferedReader.readLine().strip();
                if (cmd.equals("NEW")) {
                    write(controller.controlAddRoom(roomId, roomBuilding));
                } else {
                    write(controller.controlRemoveRoom(roomId, roomBuilding));
                }
            }
        } else {
            if (cmd.equals("LIST")) {
                write(Arrays.deepToString(controller.controlGetBookings()));
            } else {
                write(">> Date: ");
                String date = null;
                while (date == null) {
                    String in = bufferedReader.readLine().strip();
                    try {
                        controller.validateDate(in);
                        date = in;
                    } catch (Exception e) {
                        write(e.getMessage());
                    }
                }
                write(">> Start Time: ");
                String sTime = null;
                while (sTime == null) {
                    String in = bufferedReader.readLine().strip();
                    try {
                        controller.validateTime(in);
                        sTime = in;
                    } catch (Exception e) {
                        write(e.getMessage());
                    }
                }
                write(">> End Time: ");
                String eTime = null;
                while (eTime == null) {
                    String in = bufferedReader.readLine().strip();
                    try {
                        controller.validateTime(in);
                        eTime = in;
                    } catch (Exception e) {
                        write(e.getMessage());
                    }
                }
                write(">> Room name: ");
                String rName = bufferedReader.readLine().strip();
                write(">> Building name: ");
                String bName = bufferedReader.readLine().strip();
                write(">> Owner name: ");
                String owner = bufferedReader.readLine().strip();
                if (cmd.equals("NEW")) {
                    write(controller.controlAddBooking(date, sTime, eTime, bName, rName, owner));
                } else {
                    write(controller.controlRemoveBooking(date, sTime, eTime, bName, rName, owner));
                }
            }
        }
    }

    /**
     * Method to get a path for 'LOAD' or 'SAVE' command.
     * 
     * @param cmd
     * @throws IOException
     */
    public void getPath(String cmd) throws IOException {
        write("> Choose path:  (must be an abolute path and include the file name and extension, e.g. /home/test.json)");
        String in = bufferedReader.readLine().strip();
        String returnMsg;
        if (cmd.equals("SAVE")) {
            returnMsg = controller.controlSave(in);
        } else {
            returnMsg = controller.controlLoad(in);
        }
        write(returnMsg);
    }

    /**
     * Logic for 'HELP' command inputted by user.
     * 
     * @throws IOException
     */
    public void displayHelp() throws IOException {
        String helpString = """
                The Room Booking System allows you to schedule
                using a room at the %s.\n
                The available commands are:\n
                NEW     (BUILDING/ROOM/PERSON/BOOKING)
                REMOVE  (BUILDING/ROOM/PERSON/BOOKING)
                LIST  (BUILDING/ROOM/PERSON/BOOKING)
                LOAD    (PATH)
                SAVE    (PATH)
                EXIT
                """;
        String formattedHelpString = String.format(helpString, controller.controlGetInstitutionName(), "TODO");
        write(formattedHelpString);
    }

    /**
     * Logic for 'EXIT' command inputted by user.
     * 
     * @throws IOException
     */
    public void handleExit() throws IOException {
        if (!exitWarned) {
            String exitMsg = """
                    The changes you made will not be saved upon exit.
                    Make sure to save your changes if desired.
                    Next time EXIT is called the CLI will be closed.
                    """;
            write(exitMsg);
            exitWarned = true;
            return;
        }
        write("Exiting...");
        exit = true;
    }

    /**
     * Helper hethod to safely write out a message.
     * 
     * @param msg
     * @throws IOException
     */
    public void write(String msg) throws IOException {
        if (msg != null) {
            this.bufferedWriter.write(msg + "\n");
            this.bufferedWriter.flush();
        }
    }

}
