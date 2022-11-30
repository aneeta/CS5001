package roombooking.views.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

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

    private JFrame mainFrame;

    public BookingSystemGui(BookingSystemModel model, BookingSystemController controller) {
        this.model = model;
        this.controller = controller;
        setupFrame();
    }

    /**
     * Draws GUI main frame.
     */
    private void setupFrame() {
        mainFrame = new JFrame("Room Booking System");
        // Dispose on close to keep running the app until the CLI is also closed
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initLoadPopUp();
        setupMenu();
        setupBody();
        addActionListenerForButtons(this);
        this.model.addListener(this);
        mainFrame.setVisible(true);
        mainFrame.paintAll(mainFrame.getGraphics());
        mainFrame.pack();
    }

    /**
     * Data GUI menu bar.
     */
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

    /**
     * Data saving pop up.
     */
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

    /**
     * Data loading pop up.
     */
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

    /**
     * Draws GUI body.
     */
    private void setupBody() {
        setupTable();
        setupInfo();
        mainFrame.getContentPane().add(bookingsPanel, BorderLayout.CENTER);
        mainFrame.getContentPane().add(infoPanel, BorderLayout.NORTH);
    }

    /**
     * Draws the bookings table.
     */
    private void setupTable() {
        bookingsTable = new JTable();
        bookingsTableModel = new DefaultTableModel();
        bookingsTable.setModel(bookingsTableModel);
        bookingsTable.setEnabled(false);

        bookingsPanel = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                setupDataPanel(),
                getInstructionsPanel());
        bookingsPanel.setResizeWeight(0.5);

        bookingsTable.setAutoCreateRowSorter(true);
        bookingsTable.setFillsViewportHeight(true);
    }

    /**
     * Initialises initial system popup.
     */
    private void initLoadPopUp() {
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

    /**
     * Initialises the file selector to load data.
     */
    private void loadSelector() {
        // restrict files to JSON
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

    /**
     * Draws the top GUI label.
     */
    private void setupInfo() {
        infoPanel = new JPanel();
        JLabel nameInfo = new JLabel("<html>Welcome to the Room Booking System!\n");
        infoPanel.add(nameInfo);
    }

    /**
     * Draws the Intruction Pane.
     * 
     * @return JPanel
     */
    private JPanel getInstructionsPanel() {
        JPanel subInfoPanel = new JPanel();
        String usageIntructions = """
                <html>
                The current bookings will appear in the table on the left hand side.<br>
                You can sort them bu clicking on the column headers.<br>
                <h2>Use Instructions</h2>
                1. (Optional) Load data from a previous session.<br>
                2. Add and remove buildings, rooms, people, and bookings.<br>
                3. (Optional) Save your session. <br>
                <br>
                <br>
                <h3>Notes</h3>
                - Dates and times follow a dd/mm/yyyy and HH:mm format.<br>
                - You have to have Buildings, Rooms, and People before adding bookings.
                """;
        JLabel useInfo = new JLabel(usageIntructions);
        subInfoPanel.add(useInfo);
        return subInfoPanel;
    }

    /**
     * Updates the Bookings Table.
     */
    private void updateTable() {
        String[][] data = controller.controlGetBookings();
        String[][] body = Arrays.copyOfRange(data, 1, data.length);
        bookingsTableModel.setDataVector(body, data[0]);
    }

    /**
     * Draws the Bookings Table.
     * 
     * @return JComponent
     */
    private JComponent setupDataPanel() {
        updateTable();
        bookingsTableModel.addTableModelListener(this);
        JScrollPane scrollTable = new JScrollPane(bookingsTable);
        return scrollTable;
    }

    /**
     * Adds action listeners to all GUI buttons.
     * 
     * @param al
     */
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
    }

    /**
     * Action to do in response to a PropertyChangeEvent.
     * 
     * @param e event
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        // only have to update the view if bookings change
        // since not displaying other data
        if (e.getPropertyName().equals("bookings")) {
            updateTable();

            SwingUtilities.invokeLater(
                    new Runnable() {
                        public void run() {
                            updateTable();
                        }
                    });
        }
    }

    /**
     * Action to do in response to a ActionEvent.
     * 
     * @param e event
     */
    public void actionPerformed(ActionEvent e) {
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

    /**
     * Action to do in response to a TableModelEvent.
     * 
     * @param e event
     */
    @Override
    public void tableChanged(TableModelEvent e) {
        bookingsTable.repaint();
    }
}
