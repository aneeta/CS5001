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
import java.util.HashMap;
import java.util.Map;

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
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import roombooking.controller.BookingSystemController;
import roombooking.model.BookingSystemModel;
import roombooking.views.gui.customframe.PopUpFrame;

public class BookingSystemGui implements ActionListener, PropertyChangeListener {

    private BookingSystemModel model;
    private BookingSystemController controller;

    private JFrame mainFrame;
    private JMenuBar menu;
    private JPanel infoPanel;
    private JSplitPane bookingsPanel;
    private JPanel quickButtonsPanel;
    private JTable bookingsTable;
    private DefaultTableModel dtm;

    // private PopUpFrame popupHelper;

    private static int DEFAULT_FRAME_WIDTH = 1000;
    private static int DEFAULT_FRAME_HEIGHT = 600;

    private String[] currentDatesSelection;
    private String[] currentRoomsSelection;
    private String[] currentPeopleSelection;

    private Map<String, Map<String, Component>> componentsMappings;

    public BookingSystemGui(BookingSystemModel model, BookingSystemController controller) {
        this.model = model;
        this.controller = controller;
        this.componentsMappings = new HashMap<>();

        model.addListener(this);

        setupFrame();
    }

    private void setupFrame() {
        // create frame for view
        mainFrame = new JFrame("Room Booking System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT); // set frame size

        setupMenu();
        setupBody();
        // mainFrame.addPropertyChangeListener(this);
        mainFrame.setVisible(true); // display frame
        mainFrame.paintAll(mainFrame.getGraphics());
        mainFrame.pack();
    }

    private void setupMenu() {
        this.menu = new JMenuBar();
        // setup the menu ribbon
        JMenu menuNew = new JMenu("New...");
        JMenu menuRemove = new JMenu("Remove...");
        JButton menuSave = new JButton("Save");
        JButton menuLoad = new JButton("Load");
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
        menuLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadSystem();
            }
        });

        // save
        menuSave.addActionListener(new ActionListener() {
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
                "Booking System Start Up", JOptionPane.YES_NO_OPTION);
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
            setupBookings();
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
        // componentsMappings.put("body", new HashMap<>());
        bookingsPanel = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                setupDataPanel(),
                setupSelectionPanel());
        bookingsPanel.setResizeWeight(0.5);
    }

    private JComponent setupDataPanel() {
        // Map map = componentsMappings.get(body);

        String[][] data = controller.controlGetBookings();
        String[][] body = Arrays.copyOfRange(data, 1, data.length);
        dtm = new DefaultTableModel(body, data[0]);
        // dtm.addTableModelListener(this);
        // dtm.addTableModelListener(new TableModelListener() {
        // public void tableChanged(TableModelEvent e) {
        // refreshTable();
        // }

        // });
        bookingsTable = new JTable(dtm);
        // bookingsTable.addPropertyChangeListener(new PropertyChangeListener() {
        // public void propertyChange(PropertyChangeEvent e) {
        // String[][] data = controller.controlGetBookings();
        // String[][] body = Arrays.copyOfRange(data, 1, data.length);
        // DefaultTableModel dtm = new DefaultTableModel(body, data[0]);
        // bookingsTable.setModel(dtm);
        // bookingsTable.repaint();
        // }
        // });
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
        addBookingQuickButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newBookingPopUp();
            }
        });
        quickButtonsPanel.add(addBookingQuickButton);
        quickButtonsPanel.add(removeBookingQuickButton);
        mainFrame.add(quickButtonsPanel);
    }

    // private JFrame addBaseFrame(String action, int w, int h, JPanel form) {
    // final JFrame newFrame = new JFrame(frameTitle);
    // newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    // newFrame.setSize(w, h); // set frame
    // // size

    // JPanel panel = new JPanel();
    // panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
    // JButton saveButton = new JButton("OK");
    // JButton resetButton = new JButton("Reset");
    // componentsMappings.get(action).put("saveButton", saveButton);
    // componentsMappings.get(action).put("resetButton", resetButton);

    // resetButton.addActionListener(new ActionListener() {
    // public void actionPerformed(ActionEvent e) {
    // // TODO reset values
    // for (Component comp : form.getComponents()) {
    // if (comp instanceof JTextField) {
    // ((JTextField) comp).setText("");
    // // this is the text. Do what you want with it....
    // }
    // // } else if (comp instanceof JComboBox) {
    // // Object obj = ((JComboBox) comp).setSelectedItem(null);
    // // }
    // }
    // }
    // });

    // panel.add(resetButton);
    // panel.add(Box.createHorizontalGlue());
    // panel.add(saveButton);

    // newFrame.getContentPane().add(form, BorderLayout.CENTER);
    // newFrame.getContentPane().add(panel, BorderLayout.SOUTH);
    // newFrame.setVisible(true); // display frame

    // return newFrame;

    // }

    private void newBookingPopUp() {
        PopUpFrame.newBooking(controller);
    }

    private void newPersonPopUp() {
        PopUpFrame.newPerson(controller);
        // String action = "New Person";
        // Map<String, Component> map = new HashMap<>();
        // componentsMappings.put(action, map);
        // String[] labels = { "Name", "Email" };
        // int numPairs = labels.length;

        // JPanel p = new JPanel(new GridLayout(2, 2, 1, 1));
        // for (int i = 0; i < numPairs; i++) {
        // JLabel l = new JLabel(labels[i], JLabel.LEADING);
        // p.add(l);
        // JTextField textField = new JTextField(10);
        // map.put(labels[i], textField);
        // l.setLabelFor(textField);
        // p.add(textField);
        // }

        // final JFrame newBookingFrame = addBaseFrame(action, 300, 200, p, action);
        // JButton saveButton = (JButton)
        // componentsMappings.get(action).get("saveButton");
        // saveButton.addActionListener(new ActionListener() {
        // public void actionPerformed(ActionEvent e) {
        // JTextField name = (JTextField) map.get("Name");
        // JTextField email = (JTextField) map.get("Email");
        // String msg = controller.controlAddPerson(
        // name.getText(),
        // email.getText());
        // JOptionPane.showMessageDialog(newBookingFrame, msg);
        // newBookingFrame.dispose();
        // }
        // });
        // newBookingFrame.paintAll(newBookingFrame.getGraphics());
        // newBookingFrame.pack();
    }

    private void newRoomPopUp() {
        PopUpFrame.newRoom(controller);

    }

    private void newBuildingPopUp() {
        PopUpFrame.newBuilding(controller);

    }

    private void removeBookingPopUp() {
        PopUpFrame.removeBooking(controller);
    }

    private void removePersonPopUp() {
        PopUpFrame.removePerson(controller);

    }

    private void removeRoomPopUp() {
        PopUpFrame.removeRoom(controller);

    }

    private void removeBuildingPopUp() {
        PopUpFrame.removeBuilding(controller);
    }

    // IllegalAccessException {
    // return new ActionListener() {
    //
    // public void actionPerformed(ActionEvent e) {
    // func.invoke(null, null);
    // }
    // };
    // }
    public void propertyChange(PropertyChangeEvent event) {

        // if (event.getSource().equals(model.bu))

        System.out.printf("%s source %s\n", event.getNewValue().toString(), event.getSource().toString());
        System.out.println(Arrays.deepToString(controller.controlGetBookings()));

        // DefaultTableModel dtm = new DefaultTableModel(body, data[0]);
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        refreshTable();

                        // dtm.fireTableDataChanged();
                        // setupBookings();
                        // String[][] data = controller.controlGetBookings();
                        // String[][] body = Arrays.copyOfRange(data, 1, data.length);
                        // bookingsTable = new JTable(body, data[0]);
                        // mainFrame.repaint();
                        // bookingsTable.repaint();

                        // bookingsTable.setModel();
                        // // setupDataPanel();
                        // bookingsTable.repaint();
                        // System.out.println("new model");
                        // infoPanel.add(new JLabel("meep"));
                        // mainFrame.repaint();
                    }
                });

        // // act // TODO Auto-generated method stub
    }

    public void refreshTable() {
        String[][] data = controller.controlGetBookings();
        String[][] body = Arrays.copyOfRange(data, 1, data.length);
        // dtm.setDataVector(body, data[0]);
        bookingsTable.setModel(new DefaultTableModel(body, data[0]));
        bookingsTable.repaint();
    }

    // @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-gen

    }

    // @Override
    // public void tableChanged(TableModelEvent e) {
    // System.out.println("Table changed");
    // // TODO Auto-generated method stub
    // refreshTable();

}

    

    

    