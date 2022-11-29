package roombooking.views.gui2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
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
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.w3c.dom.events.MouseEvent;

import roombooking.controller.BookingSystemController;
import roombooking.model.BookingSystemModel;
import roombooking.views.gui.customframe.PopUpFrame;

public class BookingSystemGui
        implements ActionListener, PropertyChangeListener, TableModelListener {
    private BookingSystemModel model;

    private BookingSystemController controller;

    private JMenuBar menu;
    private JPanel infoPanel;
    private JSplitPane bookingsPanel;
    private JTable bookingsTable;
    private DefaultTableModel bookingsTableModel;

    private JButton saveButton;
    private JButton loadButton;
    private JMenuItem newBooking;
    private JMenuItem removeBooking;
    private JMenuItem newPerson;
    private JMenuItem removePerson;
    private JMenuItem newBuilding;
    private JMenuItem removeBuilding;
    private JMenuItem newRoom;
    private JMenuItem removeRoom;

    private JButton resetButton;
    private JButton filterButton;

    private JButton quickPlus;
    private JButton quickMinus;

    private JFrame mainFrame;

    public BookingSystemGui(BookingSystemModel model, BookingSystemController controller) {
        this.model = model;
        this.controller = controller;

        setupFrame();
    }

    private void setupFrame() {
        // create frame for view
        mainFrame = new JFrame("Room Booking System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupMenu();
        setupBody();
        addActionListenerForButtons(this);
        this.model.addListener(this);
        // mainFrame.addPropertyChangeListener(this);
        mainFrame.setVisible(true); // display frame
        mainFrame.paintAll(mainFrame.getGraphics());
        mainFrame.pack();
    }

    private void setupMenu() {
        menu = new JMenuBar();
        // setup the menu ribbon
        JMenu menuNew = new JMenu("New...");
        JMenu menuRemove = new JMenu("Remove...");
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        menu.add(menuNew);
        menu.add(menuRemove);
        menu.add(saveButton);
        menu.add(loadButton);

        // items in menuNew
        newBooking = new JMenuItem("Booking");
        newPerson = new JMenuItem("Person");
        newBuilding = new JMenuItem("Building");
        newRoom = new JMenuItem("Room");
        menuNew.add(newBooking);
        menuNew.add(newPerson);
        menuNew.add(newBuilding);
        menuNew.add(newRoom);
        // items in menuRemove
        removeBooking = new JMenuItem("Booking");
        removePerson = new JMenuItem("Person");
        removeBuilding = new JMenuItem("Building");
        removeRoom = new JMenuItem("Room");
        menuRemove.add(removeBooking);
        menuRemove.add(removePerson);
        menuRemove.add(removeBuilding);
        menuRemove.add(removeRoom);

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
        setupTable();
        setupInfo();
        // setupBookings();
        mainFrame.getContentPane().add(bookingsPanel, BorderLayout.CENTER);
        mainFrame.getContentPane().add(infoPanel, BorderLayout.NORTH);
    }

    private void setupTable() {
        bookingsTable = new JTable();
        bookingsTableModel = new DefaultTableModel();
        bookingsTable.setModel(bookingsTableModel);
        System.out.println("bookingsTable initialised");

        bookingsPanel = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                setupDataPanel(),
                setupSelectionPanel());
        bookingsPanel.setResizeWeight(0.5);

        bookingsTable.setAutoCreateRowSorter(true);
        bookingsTable.setFillsViewportHeight(true);
        // TO DO (doesnt work rn)
        // bookingsTable.addMouseListener(new MouseInputListener() {
        // public void mousePressed(MouseEvent e) {
        // int selectedRowIdx = bookingsTable.gerSelectedRow()
        // // bookingsTable.row .getValueAt(bookingsTable.getSelectedRow());
        // }

        // public void mouseExited(MouseEvent e) {
        // }

        // public void mouseEntered(MouseEvent e) {
        // }

        // public void mouseClicked(MouseEvent e) {
        // }

        // public void mouseReleased(MouseEvent e) {
        // }
        // });

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
        JLabel nameInfo = new JLabel(
                "<html>Room Booking System for<br>%s Univeristy".formatted(controller.controlGetInstitutionName()));

        subInfoPanel.add(useInfo);
        subInfoPanel.add(Box.createHorizontalGlue());
        subInfoPanel.add(nameInfo);
        infoPanel.add(subInfoPanel);
    }

    private void setupBookings() {
        // componentsMappings.put("body", new HashMap<>());
        // bookingsPanel.getLeftComponent().repaint();

    }

    private void updateTable() {
        String[][] data = controller.controlGetBookings();
        String[][] body = Arrays.copyOfRange(data, 1, data.length);
        System.out.println("Setting new table model in setupDataPanel()");
        System.out.println(Arrays.deepToString(data));
        bookingsTableModel.setDataVector(body, data[0]);
    }

    private JComponent setupDataPanel() {
        updateTable();
        bookingsTableModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                System.out.println("Inside tablechange event");
                // e.get

                // String[][] data = controller.controlGetBookings();
                // String[][] body = Arrays.copyOfRange(data, 1, data.length);
                // bookingsTableModel.setDataVector(body, data[0]);
                // bookingsTable.up
                // bookingsTable.revalidate();
                bookingsTable.repaint();
            }
        });
        // bookingsTableModel.fireTableDataChanged();

        // bookingsTable.repaint();
        JScrollPane scrollTable = new JScrollPane(bookingsTable);

        return scrollTable;
    }

    private JPanel setupSelectionPanel() {
        // Selection Pane
        JPanel selectionPane = new JPanel();
        selectionPane.setLayout(new BoxLayout(selectionPane, BoxLayout.PAGE_AXIS));
        // currentDatesSelection = controller.controlGetDateList();
        // currentRoomsSelection = controller.controlGetRoomList();
        // currentPeopleSelection = controller.controlGetPersonList();

        selectionPane.add(getSelectionPane("Date", controller.controlGetDateList()));
        // selectionPane.add(getSelectionPane("Building",
        // controller.controlGetBuildingList()));
        selectionPane.add(getSelectionPane("Room", controller.controlGetRoomList()));
        selectionPane
                .add(getSelectionPane("Booking Owner", controller.controlGetPersonList()));

        JButton resetButton = new JButton("Reset selection");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO reset
                JOptionPane.showMessageDialog(mainFrame, "reset button pressed");
            }
        });
        selectionPane.add(resetButton);
        JButton filterButton = new JButton("Filter selection");
        filterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO reset
                JOptionPane.showMessageDialog(mainFrame, "reset button pressed");
            }
        });
        // selectionPane.add()
        selectionPane.add(filterButton);

        return selectionPane;
    }

    private JPanel getSelectionPane(String labelString, String[] options) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(new JLabel(labelString));
        JList<String> listBox = new JList<>(options);
        listBox.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // TODO change to multiple
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

    public void addActionListenerForButtons(ActionListener al) {
        loadButton.addActionListener(al);
        saveButton.addActionListener(al);
        newBooking.addActionListener(al);
        newPerson.addActionListener(al);
        newBuilding.addActionListener(al);
        newRoom.addActionListener(al);
        removeBooking.addActionListener(al);
        removePerson.addActionListener(al);
        removeBuilding.addActionListener(al);
        removeRoom.addActionListener(al);

        // resetButton.addActionListener(al);
        // filterButton.addActionListener(al);
    }

    /** Displays new total. Called by model whenever a value updates. */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        System.out.printf("name: [%s]\n", event.getPropertyName());

        // event has the new value written into it
        if (event.getPropertyName().equals("bookings")) {

            System.out.println("Bookings changed");
            updateTable();
            // try {
            // SwingUtilities.invokeAndWait(
            // new Runnable() {
            // public void run() {
            // updateTable();

            // // bookingsTableModel.fireTableDataChanged();
            // // System.out.println("repaint");

            // // bookingsTable.setModel(new DefaultTableModel(body, data[0]));

            // }
            // });

            // } catch (InvocationTargetException | InterruptedException e) {
            // // TODO: handle exception
            // System.out.printf("%s\n", e.getMessage());

            // }

            SwingUtilities.invokeLater(
                    new Runnable() {
                        public void run() {
                            updateTable();

                            // bookingsTableModel.fireTableDataChanged();
                            // System.out.println("repaint");

                            // bookingsTable.setModel(new DefaultTableModel(body, data[0]));

                        }

                    });
        }
    }

    public void actionPerformed(ActionEvent e) {
        System.out.printf("Action src %s, action cmd %s\n", e.getSource(), e.getActionCommand());
        if (e.getSource() == loadButton) {
            loadSystem();
        } else if (e.getSource() == saveButton) {
            saveSystem();
        } else if (e.getSource() == newBooking) {
            PopUpFrame.newBooking(controller);
        } else if (e.getSource() == newPerson) {
            PopUpFrame.newPerson(controller);
        } else if (e.getSource() == newBuilding) {
            PopUpFrame.newBuilding(controller);
        } else if (e.getSource() == newRoom) {
            PopUpFrame.newRoom(controller);
        } else if (e.getSource() == removeRoom) {
            PopUpFrame.removeRoom(controller);
        } else if (e.getSource() == removeBooking) {
            PopUpFrame.removeBooking(controller);
        } else if (e.getSource() == removeBuilding) {
            PopUpFrame.removeBuilding(controller);
        } else if (e.getSource() == removePerson) {
            PopUpFrame.removePerson(controller);
        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        System.out.println("Inside table change overriden");
        // TODO Auto-generated method stub

    }
}
