package roombooking.views.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import roombooking.controller.BookingSystemController;
import roombooking.model.BookingSystemModel;

public class BookingSystemGui implements ActionListener, PropertyChangeListener {

    private BookingSystemModel model;
    private BookingSystemController controller;

    private JFrame mainFrame;
    private JMenuBar menu;
    private JPanel infoPanel;
    private JPanel bookingsPanel;
    private JPanel quickButtonsPanel;

    private static int DEFAULT_FRAME_WIDTH = 1000;
    private static int DEFAULT_FRAME_HEIGHT = 600;

    private String[] currentDatesSelection;
    private String[] currentRoomsSelection;
    private String[] currentPeopleSelection;

    public BookingSystemGui(BookingSystemModel model, BookingSystemController controller) {
        this.model = model;
        this.controller = controller;

        model.addListener(this);
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

        // define Action Listeners and associated actions

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
                loadSystem();
            }
        });

        // save
        menuLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveSystem();
            }
        });

        // add menubar to frame
        mainFrame.setJMenuBar(menu);
    }

    private void saveSystem() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "json");
        JFileChooser j = new JFileChooser();
        j.setFileFilter(filter);
        int r = j.showOpenDialog(mainFrame);

        if (r == JFileChooser.APPROVE_OPTION) {
            String filePath = j.getSelectedFile().getAbsolutePath();
            String msg = controller.controlSave(filePath);
            JOptionPane.showMessageDialog(mainFrame, msg, "Save Status", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadSystem() {
        int loadOption = JOptionPane.showConfirmDialog(
                mainFrame,
                "Loading a system will wipe out unsaved changed.\nDo you want to proceed?",
                "Load Status",
                JOptionPane.YES_NO_OPTION);
        if (loadOption == JOptionPane.YES_OPTION) {
            loadSelector();
        }
    }

    private void setupBody() {
        initLoadPopUp();

        setupInfo();
        setupBookings();
        setupQuickButtons();
        mainFrame.getContentPane().add(bookingsPanel, BorderLayout.CENTER);
        mainFrame.getContentPane().add(infoPanel, BorderLayout.NORTH);
        mainFrame.getContentPane().add(quickButtonsPanel, BorderLayout.SOUTH);

    }

    private void initLoadPopUp() {
        // System.out.println("Setting up GUI load popup");
        int loadOption = JOptionPane.showConfirmDialog(
                mainFrame,
                "Would you like to load an existing system?",
                "Booking System Start Up",
                JOptionPane.YES_NO_OPTION);
        if (loadOption == JOptionPane.YES_OPTION) {
            loadSelector();
        } else {
            // pop up informing new system will be initalised
            JOptionPane.showMessageDialog(mainFrame, "Starting fresh session", "Load Status",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadSelector() {
        // System.out.println("Choosing load file");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "json");
        JFileChooser j = new JFileChooser();
        j.setFileFilter(filter);
        int r = j.showOpenDialog(mainFrame);

        if (r == JFileChooser.APPROVE_OPTION) {
            String filePath = j.getSelectedFile().getAbsolutePath();
            String msg = controller.controlLoad(filePath);
            JOptionPane.showMessageDialog(mainFrame, msg, "Load Status", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void setupInfo() {
        infoPanel = new JPanel();
        JPanel subInfoPanel = new JPanel();
        subInfoPanel.setLayout(new BoxLayout(subInfoPanel, BoxLayout.LINE_AXIS));

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
        JLabel nameInfo = new JLabel("<html>Room Booking System for<br>%s Univeristy");

        subInfoPanel.add(useInfo);
        subInfoPanel.add(Box.createHorizontalGlue());
        subInfoPanel.add(nameInfo);
        subInfoPanel.setMinimumSize(new Dimension(500, 100));

        infoPanel.add(subInfoPanel);
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
        String[][] data = controller.controlGetBookings();
        String[][] body = Arrays.copyOfRange(data, 1, data.length);
        JTable bookingsTable = new JTable(body, data[0]);
        bookingsTable.setAutoCreateRowSorter(true);
        JScrollPane scrollTable = new JScrollPane(bookingsTable);
        bookingsTable.setFillsViewportHeight(true);

        return scrollTable;
    }

    private JPanel setupSelectionPanel() {
        // Selection Pane
        JPanel selectionPane = new JPanel();
        selectionPane.setLayout(new BoxLayout(selectionPane, BoxLayout.PAGE_AXIS));
        currentDatesSelection = controller.controlGetDateList();
        currentRoomsSelection = controller.controlGetRoomList();
        currentPeopleSelection = controller.controlGetPersonList();

        selectionPane.add(getSelectionPane("Date", currentDatesSelection));
        // selectionPane.add(getSelectionPane("Building",
        // controller.controlGetBuildingList()));
        selectionPane.add(getSelectionPane("Room", currentRoomsSelection));
        selectionPane
                .add(getSelectionPane("Booking Owner", currentPeopleSelection));
        JButton resetButton = new JButton("Reset selection");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO reset
                JOptionPane.showMessageDialog(mainFrame, "reset button pressed");
            }
        });
        selectionPane.add(resetButton);

        return selectionPane;
    }

    private JPanel getSelectionPane(String labelString, String[] options) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(new JLabel(labelString));
        JList listBox = new JList<>(options);
        listBox.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION); // TODO change to multiple
        // TODO listen to list selection & do something with it
        listBox.getSelectedIndices();
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

    private void setupQuickButtons() {
        quickButtonsPanel = new JPanel();
        JButton addBookingQuickButton = new JButton("+");
        JButton removeBookingQuickButton = new JButton("-");
        quickButtonsPanel.add(addBookingQuickButton);
        quickButtonsPanel.add(removeBookingQuickButton);
        mainFrame.add(quickButtonsPanel);
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

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO save values
                for (Component comp : form.getComponents()) {
                    if (comp instanceof JTextField) {
                        String text = ((JTextField) comp).getText();
                        System.out.println(text);
                        // this is the text. Do what you want with it....
                    } else if (comp instanceof JComboBox) {
                        Object obj = ((JComboBox) comp).getSelectedItem();
                        System.out.println(obj.toString());
                    }
                }
            }
        });

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

    private void newBookingPopUp() {
        String[] labels = { "Date: ", "Start Time: ", "End Time: " };
        int numPairs = labels.length;

        JPanel p = new JPanel(new GridLayout(7, 2, 1, 1));
        for (int i = 0; i < numPairs; i++) {
            JLabel l = new JLabel(labels[i], JLabel.TRAILING);
            p.add(l);
            JTextField textField = new JTextField(10);
            l.setLabelFor(textField);
            p.add(textField);
        }

        // Building
        JLabel l = new JLabel("Building: ", JLabel.TRAILING);
        p.add(l);
        // JComboBox c = new JComboBox<>(model.getBuildings());
        JComboBox c = new JComboBox<>(controller.controlGetBuildingList());
        l.setLabelFor(c);
        p.add(c);

        // Room
        JLabel l2 = new JLabel("Room: ", JLabel.TRAILING);
        p.add(l2);
        JComboBox c2 = new JComboBox<>(controller.controlGetRoomList());
        l2.setLabelFor(c2);
        p.add(c2);

        // Owner
        JLabel l3 = new JLabel("Owner: ", JLabel.TRAILING);
        p.add(l3);
        JComboBox c3 = new JComboBox<>(controller.controlGetPersonList());
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
        String[] values = { null, null };
        int numPairs = labels.length;

        JPanel p = new JPanel(new GridLayout(2, 2, 1, 1));
        for (int i = 0; i < numPairs; i++) {
            JLabel l = new JLabel(labels[i], JLabel.TRAILING);
            p.add(l);
            JTextField textField = new JTextField(10);
            final int j = i;
            textField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    values[j] = textField.getText();
                    System.out.println(">>> value changed in %s to %s".formatted(labels[j], values[j]));
                }
            });

            l.setLabelFor(textField);
            p.add(textField);
        }

        final JFrame newBookingFrame = addBaseFrame("New Person", 300, 200, p);
        String name = ((JTextField) newBookingFrame.getContentPane().getComponent(0)).getText();
        String email = ((JTextField) newBookingFrame.getContentPane().getComponent(1)).getText();

        newBookingFrame.paintAll(newBookingFrame.getGraphics());
        newBookingFrame.pack();
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

    // public ActionListener act(Method func) throws InvocationTargetException,
    // IllegalAccessException {
    // return new ActionListener() {
    // public void actionPerformed(ActionEvent e) {
    // func.invoke(null, null);
    // }
    // };
    // }
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

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
    }

}
