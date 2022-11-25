package view.guiview;

import controller.RoomBookingController;

import model.RoomBookingModel;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.*;

// import view.guiview.custom.DatePicker;

public class RoomBookingView {

    private RoomBookingModel model;
    private RoomBookingController controller;

    private JFrame mainFrame;
    private JMenuBar menu;
    private JPanel infoPanel;
    private JPanel bookingsPanel;
    private JPanel quickButtonsPanel;

    private static int DEFAULT_FRAME_WIDTH = 1000;
    private static int DEFAULT_FRAME_HEIGHT = 600;
    private static final int TEXT_HEIGHT = 10;
    private static final int TEXT_WIDTH = 10;

    // private JMenu menuNew;
    // private JMenu menuRemove;
    // private JMenu menuSave;
    // private JMenu menuLoad;

    // private JMenuItem menuNewBooking;
    // private JMenuItem menuNewPerson;
    // private JMenuItem menuNewBuilding;
    // private JMenuItem menuNewRoom;

    // protected static String BUTTON_NAV_NEW_COMMAND = "New...";
    // protected static String BUTTON_NAV_REMOVE_COMMAND = "Remove...";
    // protected static String BUTTON_NAV_SAVE_COMMAND = "Save";
    // protected static String BUTTON_NAV_LOAD_COMMAND = "Load";

    // private JToolBar toolbar;
    // private JTextField inputField;
    // private JRadioButton button1;
    // private JButton button2;
    // private JScrollPane outputPane;
    // private JTextArea outputField;

    // private JButton clearButton;
    // private JButton addButton;
    // private JButton subtractButton;
    // private JButton multiplyButton;
    // private JButton divideButton;
    // private JTextField valueField;
    // private JTextField totalField;

    public RoomBookingView(RoomBookingModel model, RoomBookingController controller) {
        this.model = model;
        this.controller = controller;

        // model.addListener(this);

        // create frame for view
        mainFrame = new JFrame("Room Booking System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT); // set frame size
        mainFrame.setVisible(true); // display frame

        setupComponenets();

        mainFrame.paintAll(mainFrame.getGraphics());
        mainFrame.pack();

    }

    public void addActionListenerForButtons(ActionListener al) {
        // clearButton.addActionListener(al);
        // addButton.addActionListener(al);
        // subtractButton.addActionListener(al);
        // multiplyButton.addActionListener(al);
        // divideButton.addActionListener(al);
    }

    public void propertyChange(PropertyChangeEvent event) {
        // event has the new value written into it
        // double newTotal = (double) event.getNewValue();

        // SwingUtilities.invokeLater(
        // new Runnable() {
        // public void run() {
        // totalField.setText("" + newTotal); // we could have used model.getTotal()
        // instead
        // mainFrame.repaint();
        // }
        // });
    }

    // public void actionPerformed(ActionEvent e) {
    // if (e.getSource() == clearButton) {
    // controller.controlClear();
    // } else if (e.getSource() == addButton) {
    // controller.controlAdd(valueField.getText());
    // } else if (e.getSource() == subtractButton) {
    // controller.controlSubtract(valueField.getText());
    // } else if (e.getSource() == multiplyButton) {
    // controller.controlMultiply(valueField.getText());
    // } else if (e.getSource() == divideButton) {
    // controller.controlDivide(valueField.getText());
    // }
    // }

    private void setupComponenets() {
        setupMenu();
        setupBody();
    }

    private void setupMenu() {
        this.menu = new JMenuBar();
        // setup the uter menu ribbon
        JMenu menuNew = new JMenu("New...");
        JMenu menuRemove = new JMenu("Remove...");
        JMenu menuSave = new JMenu("Save");
        JMenu menuLoad = new JMenu("Load");
        menu.add(menuNew);
        menu.add(menuRemove);
        menu.add(menuSave);
        menu.add(menuLoad);

        // items in menuNew
        JMenuItem menuNewBooking = new JMenuItem("Booking");
        JMenuItem menuNewPerson = new JMenuItem("Person");
        JMenuItem menuNewBuilding = new JMenuItem("Building");
        JMenuItem menuNewRoom = new JMenuItem("Room");
        menuNew.add(menuNewBooking);
        menuNew.add(menuNewPerson);
        menuNew.add(menuNewBuilding);
        menuNew.add(menuNewRoom);
        // items in menuRemove
        JMenuItem menuRemoveBooking = new JMenuItem("Booking");
        JMenuItem menuRemovePerson = new JMenuItem("Person");
        JMenuItem menuRemoveBuilding = new JMenuItem("Building");
        JMenuItem menuRemoveRoom = new JMenuItem("Room");
        menuRemove.add(menuRemoveBooking);
        menuRemove.add(menuRemovePerson);
        menuRemove.add(menuRemoveBuilding);
        menuRemove.add(menuRemoveRoom);

        // add data
        menuNewBooking.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newBookingPopUp();
            }
        });
        menuNewPerson.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newPersonPopUp();
            }
        });

        menuNewRoom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newRoomPopUp();
            }
        });

        menuNewBuilding.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newBuildingPopUp();
            }
        });
        // remove data
        menuRemoveBooking.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeBookingPopUp();
            }
        });
        menuRemovePerson.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removePersonPopUp();
            }
        });
        menuRemoveRoom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeRoomPopUp();
            }
        });
        menuRemoveBuilding.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeBuildingPopUp();
            }
        });

        // load
        menuSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeBuildingPopUp();
            }
        });

        // save
        menuLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeBuildingPopUp();
            }
        });

        // add menubar to frame
        mainFrame.setJMenuBar(menu);
    }

    private void newBookingPopUp() {
        JOptionPane.showMessageDialog(mainFrame, "Ooops, New Booking not linked to model!");
    }

    private void newPersonPopUp() {
        JOptionPane.showMessageDialog(mainFrame, "Ooops, New Person not linked to model!");

    }

    private void newRoomPopUp() {
        JOptionPane.showMessageDialog(mainFrame, "Ooops, New Room not linked to model!");

    }

    private void newBuildingPopUp() {
        JOptionPane.showMessageDialog(mainFrame, "Ooops, New Building not linked to model!");

    }

    private void removeBookingPopUp() {
        JOptionPane.showMessageDialog(mainFrame, "Ooops, remove Booking not linked to model!");

    }

    private void removePersonPopUp() {
        JOptionPane.showMessageDialog(mainFrame, "Ooops, remove Person not linked to model!");

    }

    private void removeRoomPopUp() {
        JOptionPane.showMessageDialog(mainFrame, "Ooops, remove Room not linked to model!");

    }

    private void removeBuildingPopUp() {
        JOptionPane.showMessageDialog(mainFrame, "Ooops, remove Building not linked to model!");

    }

    private int initLoadPopUp() {
        System.out.println("Setting up GUI load popup");
        int n = JOptionPane.showConfirmDialog(
                mainFrame,
                "Would you like to load an existing system?",
                "Booking System Start Up",
                JOptionPane.YES_NO_OPTION);
        return n;
        // Object[] possibilities = {"ham", "spam", "yam"};
        // String s = (String)JOptionPane.showInputDialog(
        // mainFrame,
        // "Load an existing system:\n"
        // + "(Select cancel to initialise new session)",
        // "Booking System Start Up",
        // JOptionPane.QUESTION_MESSAGE,
        // possibilities);

    }

    private void setupBody() {
        System.out.println("Setting up GUI body");
        int loadOption = initLoadPopUp();
        if (loadOption == JOptionPane.YES_OPTION) {
            String filePath = loadSelector();
            controller.controlLoad(filePath);
        } else {

        }

        setupInfo();
        setupBookings();
        setupQuickButtons();
        // add grid to centre of frame
        mainFrame.getContentPane().add(bookingsPanel, BorderLayout.CENTER);
        // add info to top of frame
        mainFrame.getContentPane().add(infoPanel, BorderLayout.NORTH);

    }

    private void setupQuickButtons() {
        quickButtonsPanel = new JPanel();
        JButton addBookingQuickButton = new JButton("+");
        JButton removeBookingQuickButton = new JButton("-");
        quickButtonsPanel.add(addBookingQuickButton);
        quickButtonsPanel.add(removeBookingQuickButton);
        mainFrame.add(quickButtonsPanel);
    }

    private void setupInfo() {
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.LINE_AXIS));

        System.out.println("Setting up GUI info");
        String usageIntructions = """
                <html>
                1. dfghvbjn,gffghvjbnj,m.<br>
                2. dfghvbjn,gffghvjbnj,m.<br>
                3. dfghvbjn,gffghvjbnj,m.<br>
                4. dfghvbjn,gffghvjbnj,m.
                """;
        // ImageIcon infoIcon = new ImageIcon("../../assets/imgs/info_icon.png");
        JLabel useInfo = new JLabel(usageIntructions);
        JLabel nameInfo = new JLabel("<html>Room Booking System for<br>Test Univeristy");

        infoPanel.add(useInfo);
        infoPanel.add(Box.createHorizontalGlue());
        infoPanel.add(nameInfo);

    }

    private void setupBookings() {
        bookingsPanel = new JPanel();

        System.out.println("Setting up GUI Bookings Pane");
        // Output pane
        JTable bookingsTable = new JTable();
        bookingsTable.setBounds(30, 40, 200, 300);
        JScrollPane scrollTable = new JScrollPane(bookingsTable);

        // Selection Pane
        JPanel selectionPane = new JPanel();
        selectionPane.setLayout(new BoxLayout(selectionPane, BoxLayout.PAGE_AXIS));

        // Calendar calendar = Calendar.getInstance();
        // Date initDate = calendar.getTime();
        // Date earliestDate = calendar.getTime();
        // calendar.add(Calendar.YEAR, 100);
        // Date latestDate = calendar.getTime();
        // SpinnerModel dateModel = new SpinnerDateModel(initDate,
        // earliestDate,
        // latestDate,
        // Calendar.YEAR);// ignored for user input
        // JSpinner dateSpinner = new JSpinner(dateModel);
        // selectionPane.add(dateSpinner);

        selectionPane.add(getSelectionPane("Date", new String[] { "11/11/2022", "12/11/2022" }));
        selectionPane.add(getSelectionPane("Building", new String[] { "A", "B" }));
        selectionPane.add(getSelectionPane("Room", new String[] { "A1", "A2", "A3", "B1", "B2" }));
        selectionPane
                .add(getSelectionPane("Booking Owner", new String[] { "John", "Mary", "Krzysztof", "Ann", "Bob" }));
        selectionPane.add(new JButton("Reset selection"));

        // Date
        // JLabel dateLabel = new JLabel("Selected Date:");
        // final JTextField dateTextField = new JTextField(20);
        // JButton dateSelecButton = new JButton("Select");
        // JPanel datePanel = new JPanel();
        // dateSelecButton.addActionListener(new ActionListener() {
        // public void actionPerformed(ActionEvent e) {
        // dateTextField.setText(new DatePicker(datePanel).setPickedDate());
        // }
        // });

        // JTabbedPane tabbedPane = new JTabbedPane();
        // JComponent dateTab = makeTextPanel("Date");
        // tabbedPane.addTab("Date", icon, dateTab,"Select Booking Date");
        // tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                selectionPane,
                scrollTable);
        bookingsPanel.add(splitPane);
    }

    private String loadSelector() {
        System.out.println("Choosing load file");
        JFileChooser j = new JFileChooser();
        int r = j.showOpenDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            return j.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }

    private JPanel getSelectionPane(String labelString, String[] options) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel(labelString), c);
        JComboBox comboBox = new JComboBox<>(options);
        c.anchor = GridBagConstraints.EAST;
        panel.add(comboBox, c);
        return panel;
    }

}
