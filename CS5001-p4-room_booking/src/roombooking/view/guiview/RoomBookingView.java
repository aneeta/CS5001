package view.guiview;

import controller.RoomBookingController;

import model.RoomBookingModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

// import view.guiview.custom.DatePicker;

public class RoomBookingView extends Thread implements PropertyChangeListener, ActionListener {

    private RoomBookingModel model;
    private RoomBookingController controller;

    private JFrame mainFrame;
    private JMenuBar menu;
    private JPanel infoPanel;
    private JPanel bookingsPanel;
    private JPanel quickButtonsPanel;

    private static int DEFAULT_FRAME_WIDTH = 1000;
    private static int DEFAULT_FRAME_HEIGHT = 600;
    private static String[] TEST_DATES = new String[] { "11/11/2022", "12/11/2022" };
    private static String[] TEST_BLDNGS = new String[] { "A", "B" };
    private static String[] TEST_ROOMS = new String[] { "A1", "A2", "A3", "B1", "B2" };
    private static String[] COL_NAMES = new String[] { "Date", "Time", "Owner", "Room", "Building" };
    private static String[] TEST_PPL = new String[] { "John", "Mary", "Krzysztof", "Ann", "Bob" };
    private static String[][] TEST_DATA = new String[][] {
            { "1/1/22", "11:00-12:00", "John", "A1", "A" },
            { "3/1/22", "11:00-12:00", "Anna", "B34", "B" },
            { "1/1/22", "11:00-12:00", "John", "B22", "B" },
            { "1/1/22", "14:00-17:00", "Mary", "A100", "A" }
    };

    public RoomBookingView(RoomBookingModel model, RoomBookingController controller) {
        // this.model = model;
        this.controller = controller;

        controller.addListener(this);
        setupFrame();
    }

    private void setupFrame() {
        // create frame for view
        mainFrame = new JFrame("Room Booking System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT); // set frame size
        mainFrame.setVisible(true); // display frame
        setupMenu();
        setupBody();
        mainFrame.paintAll(mainFrame.getGraphics());
        mainFrame.pack();
    }

    private void setupMenu() {
        this.menu = new JMenuBar();
        // setup the menu ribbon
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

        // menu.addListener( new ActionListener() {
        // public void actionPerformed(ActionEvent e) {
        // switch(e.getSource) {
        // case menuSave:
        // break;
        // case menuLoad:
        // break;
        // case menuNewBooking:
        // break;
        // case menuNewPerson:
        // break;
        // case menuNewRoom:
        // break;
        // case menuNewBuilding:
        // break;
        // case menuRemoveBooking:
        // break;
        // case menuRemovePerson:
        // break;
        // case menuRemoveBuilding:
        // break;
        // case menuRemoveRoom:
        // break;
        // }
        // }
        // });

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

    private void setupBody() {

        System.out.println("Setting up GUI body");
        int loadOption = initLoadPopUp();
        if (loadOption == JOptionPane.YES_OPTION) {
            String filePath = loadSelector();
            String msg = controller.controlLoad(filePath);
            JOptionPane.showMessageDialog(mainFrame, msg, "Load Status", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Started new session", "Load Status",
                    JOptionPane.INFORMATION_MESSAGE);
            // TODO pop up informing new system will be initalised
        }

        setupInfo();
        setupBookings();
        setupQuickButtons();
        mainFrame.getContentPane().add(bookingsPanel, BorderLayout.CENTER);
        mainFrame.getContentPane().add(infoPanel, BorderLayout.NORTH);
        mainFrame.getContentPane().add(quickButtonsPanel, BorderLayout.SOUTH);

    }

    public void addActionListenerForButtons(ActionListener al) {
        // clearButton.addActionListener(al);
        // addButton.addActionListener(al);
        // subtractButton.addActionListener(al);
        // multiplyButton.addActionListener(al);
        // divideButton.addActionListener(al);
    }

    public void propertyChange(PropertyChangeEvent event) {
        // refresh the bookings table
        String[][] newTableData = (String[][]) event.getNewValue()
        // refrash the selectioon options



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

    public void warn(JTextField textField) {
        if (Integer.parseInt(textField.getText()) <= 0) {
            JOptionPane.showMessageDialog(null,
                    "Error: Please enter number bigger than 0", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void newBookingPopUp() {
        String[] labels = { "Date: ", "Start Time: ", "End Time: " };
        int numPairs = labels.length;

        JPanel p = new JPanel(new GridLayout(7, 2, 1, 1));
        for (int i = 0; i < numPairs; i++) {
            JLabel l = new JLabel(labels[i], JLabel.TRAILING);
            p.add(l);
            JTextField textField = new JTextField(10);
            // textField.getDocument().addDocumentListener(new DocumentListener() {
            // public void changedUpdate(DocumentEvent e) {
            // warn(textField);
            // }

            // public void removeUpdate(DocumentEvent e) {
            // warn(textField);
            // }

            // public void insertUpdate(DocumentEvent e) {
            // warn(textField);
            // }
            // });
            l.setLabelFor(textField);
            p.add(textField);
        }

        // Building
        JLabel l = new JLabel("Building: ", JLabel.TRAILING);
        p.add(l);
        // JComboBox c = new JComboBox<>(model.getBuildings());
        JComboBox c = new JComboBox<>(TEST_BLDNGS);
        l.setLabelFor(c);
        p.add(c);

        // Room
        JLabel l2 = new JLabel("Room: ", JLabel.TRAILING);
        p.add(l2);
        JComboBox c2 = new JComboBox<>(TEST_ROOMS);
        l2.setLabelFor(c2);
        p.add(c2);

        // Owner
        JLabel l3 = new JLabel("Owner: ", JLabel.TRAILING);
        p.add(l3);
        JComboBox c3 = new JComboBox<>(TEST_PPL);
        l3.setLabelFor(c3);
        p.add(c3);

        final JFrame newBookingFrame = addBaseFrame("New Booking", 300, 400, p);
        // newBookingFrame.add(save);
        // JComboBox c3 = new JComboBox<>(model.getPeople());
        // l3.setLabelFor(c3);
        // p.add(c3);

        newBookingFrame.paintAll(newBookingFrame.getGraphics());
        newBookingFrame.pack();

    }

    private void newPersonPopUp() {
        String[] labels = { "Name: ", "Email: " };
        int numPairs = labels.length;

        JPanel p = new JPanel(new GridLayout(2, 2, 1, 1));
        for (int i = 0; i < numPairs; i++) {
            JLabel l = new JLabel(labels[i], JLabel.TRAILING);
            p.add(l);
            JTextField textField = new JTextField(10);
            l.setLabelFor(textField);
            p.add(textField);
        }

        final JFrame newBookingFrame = addBaseFrame("New Person", 300, 200, p);
        String name = ((JTextField) newBookingFrame.getContentPane().getComponent(0)).getText();
        String email = ((JTextField) newBookingFrame.getContentPane().getComponent(1)).getText();

        newBookingFrame.paintAll(newBookingFrame.getGraphics());
        newBookingFrame.pack();

        // JButton saveButton = (JButton) newBookingFrame.getContentPane()
        // saveButton.addActionListener(new ActionListener() {
        // public void actionPerformed(ActionEvent e) {
        // String msg = controller.controlAddNewPerson(name, email);
        // JOptionPane.showMessageDialog(newBookingFrame, msg); // TODO change to return
        // code and decide on the
        // // message in view
        // newBookingFrame.dispose();
        // }

        // });

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
        // Left Pane
        JComponent left = setupDataPanel();
        // Right Pane
        JPanel right = setupSelectionPanel();

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                left,
                right);
        bookingsPanel.add(splitPane);
    }

    private JComponent setupDataPanel() {

        System.out.println("Setting up GUI Bookings Pane");
        JTable bookingsTable = new JTable(TEST_DATA, COL_NAMES);
        bookingsTable.setAutoCreateRowSorter(true);
        bookingsTable.setBounds(30, 40, 200, 300);
        JScrollPane scrollTable = new JScrollPane(bookingsTable);
        bookingsTable.setFillsViewportHeight(true);

        return scrollTable;
    }

    private JPanel setupSelectionPanel() {
        // Selection Pane
        JPanel selectionPane = new JPanel();
        selectionPane.setLayout(new BoxLayout(selectionPane, BoxLayout.PAGE_AXIS));

        selectionPane.add(getSelectionPane("Date", TEST_DATES));
        selectionPane.add(getSelectionPane("Building", TEST_BLDNGS));
        selectionPane.add(getSelectionPane("Room", TEST_ROOMS));
        selectionPane
                .add(getSelectionPane("Booking Owner", TEST_PPL));
        selectionPane.add(new JButton("Reset selection"));

        return selectionPane;
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
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(new JLabel(labelString));
        JList listBox = new JList<>(options);
        listBox.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION); // TODO change to multiple
        // TODO listen to list selection & do something with it
        listBox.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                // TODO
                JOptionPane.showMessageDialog(mainFrame, listBox.getSelectedValuesList().toString());
            }
        });
        panel.add(Box.createHorizontalGlue());
        panel.add(listBox);
        return panel;
    }

    private String[][] getTableData() {
        // TODO
        return null;
    }

    private JFrame addBaseFrame(String frameTitle, int w, int h, JPanel form) {
        final JFrame newFrame = new JFrame(frameTitle);
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newFrame.setSize(w, h); // set frame
        // size
        newFrame.setVisible(true); // display frame

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        JButton saveButton = new JButton("Save");
        JButton resetButton = new JButton("Reset");

        // saveButton.addActionListener(new ActionListener() {
        // public void actionPerformed(ActionEvent e) {
        // // TODO save values
        // for (Component comp : form.getComponents()) {
        // if (comp instanceof JTextField) {
        // String text = ((JTextField) comp).getText();
        // System.out.println(text);
        // // this is the text. Do what you want with it....
        // } else if (comp instanceof JComboBox) {
        // Object obj = ((JComboBox) comp).getSelectedItem();
        // System.out.println(obj.toString());
        // }
        // }
        // }
        // });

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO reset values
                System.out.println("Reset button clicked");
            }
        });

        panel.add(saveButton);
        panel.add(Box.createHorizontalGlue());
        panel.add(resetButton);

        newFrame.getContentPane().add(form, BorderLayout.CENTER);
        newFrame.getContentPane().add(panel, BorderLayout.SOUTH);

        return newFrame;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }

}
