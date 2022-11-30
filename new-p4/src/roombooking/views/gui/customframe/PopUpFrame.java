package roombooking.views.gui.customframe;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fasterxml.jackson.databind.JsonSerializable.Base;

import roombooking.controller.BookingSystemController;
import roombooking.model.BookingSystemModel;
import roombooking.views.gui.BookingSystemGui;

public class PopUpFrame implements ActionListener, PropertyChangeListener {
    private BookingSystemModel model;

    public PopUpFrame(BookingSystemModel model) {
        this.model = model;
        this.model.addListener(this);
    }

    public static void newBooking(BookingSystemController controller) {
        String action = "New Booking";
        Map<String, Component> map = new HashMap<>();
        String[] labels = { "Date", "Start Time", "End Time" };
        int numPairs = labels.length;

        JPanel p = new JPanel(new GridLayout(7, 2, 5, 5));
        for (int i = 0; i < numPairs; i++) {
            JLabel l = new JLabel(labels[i], JLabel.LEADING);
            p.add(l);
            JTextField textField = new JTextField(10);
            l.setLabelFor(textField);
            p.add(textField);
            map.put(labels[i], textField);
        }

        // // Room & Building
        // String[][] data = controller.controlGetRooms();
        // String[] dataFormatted = Arrays.asList(data).stream().map(x -> x[0] + " (" +
        // x[1] + ")").toArray(String[]::new);
        // JLabel l = new JLabel("Room (Building)", JLabel.LEADING);
        // p.add(l);
        // JComboBox<String> c = new JComboBox<>(controller.controlGetBuildingList());
        // l.setLabelFor(c);
        // p.add(c);
        // map.put("Building", c);

        /// BUILDING DEPENDEDNT DROPDOWN
        // Building
        JLabel l = new JLabel("Building", JLabel.LEADING);
        p.add(l);
        JComboBox<String> c = new JComboBox<>(controller.controlGetBuildingList());
        l.setLabelFor(c);
        p.add(c);
        map.put("Building", c);

        // Room
        JLabel l2 = new JLabel("Room", JLabel.LEADING);
        p.add(l2);

        JComboBox<String> c2 = new JComboBox<>(controller.controlGetRoomList());
        // c2.setEnabled(false);
        l2.setLabelFor(c2);
        p.add(c2);
        map.put("Room", c2);

        // c.addActionListener(new ActionListener() {
        // public void actionPerformed(ActionEvent e) {
        // System.out.println((String) c.getSelectedItem());
        // System.out.println(controller.controlGetRoomList((String)
        // c.getSelectedItem()).toString());
        // c2.setModel(new DefaultComboBoxModel<>(controller.controlGetRoomList((String)
        // c.getSelectedItem())));
        // c2.setEnabled(true);

        // }
        // });

        // Owner
        JLabel l3 = new JLabel("Owner", JLabel.LEADING);
        p.add(l3);
        JComboBox<String> c3 = new JComboBox<>(controller.controlGetPersonList());
        l3.setLabelFor(c3);
        p.add(c3);
        map.put("Owner", c3);

        BaseForm newBookingFrame = new BaseForm(action, 300, 400, p);
        JButton saveButton = newBookingFrame.getSaveButton();

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // get inputs
                JTextField date = (JTextField) map.get("Date");
                JTextField st = (JTextField) map.get("Start Time");
                JTextField et = (JTextField) map.get("End Time");
                JComboBox<String> building = (JComboBox) map.get("Building");
                JComboBox<String> room = (JComboBox) map.get("Room");
                JComboBox<String> owner = (JComboBox) map.get("Owner");
                // pass to controller
                String msg = controller.controlAddBooking(
                        date.getText(),
                        st.getText(),
                        et.getText(),
                        (String) building.getSelectedItem(),
                        (String) room.getSelectedItem(),
                        (String) owner.getSelectedItem());
                JOptionPane.showMessageDialog(newBookingFrame, msg);
                newBookingFrame.dispose();
            }
        });
        // newBookingFrame.paintAll(newBookingFrame.getGraphics());
        // newBookingFrame.pack();
    }

    public static void newPerson(BookingSystemController controller) {
        System.out.println("Running new person");
        String action = "New Person";
        Map<String, Component> map = new HashMap<>();

        String[] labels = { "Name", "Email" };
        int numPairs = labels.length;

        JPanel p = new JPanel(new GridLayout(2, 2, 5, 5));
        for (int i = 0; i < numPairs; i++) {
            JLabel l = new JLabel(labels[i], JLabel.LEADING);
            p.add(l);
            JTextField textField = new JTextField(10);
            map.put(labels[i], textField);
            l.setLabelFor(textField);
            p.add(textField);
        }

        BaseForm newFrame = new BaseForm(action, 300, 400, p);
        JButton saveButton = newFrame.getSaveButton();

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Running new person action");
                // get inputs
                JTextField name = (JTextField) map.get("Name");
                JTextField email = (JTextField) map.get("Email");
                // pass to controller
                String msg = controller.controlAddPerson(
                        name.getText(),
                        email.getText());
                JOptionPane.showMessageDialog(newFrame, msg);
                newFrame.dispose();
            }
        });
        // newFrame.paintAll(newFrame.getGraphics());
        // newFrame.pack();

    }

    public static void newRoom(BookingSystemController controller) {
        String action = "New Room";

        JPanel p = new JPanel(new GridLayout(2, 2, 5, 5));
        JLabel l = new JLabel("Name", JLabel.LEADING);
        p.add(l);
        JTextField textField = new JTextField(10);
        l.setLabelFor(textField);
        p.add(textField);

        JLabel l2 = new JLabel("Building", JLabel.LEADING);
        p.add(l2);
        JComboBox<String> c = new JComboBox<>(controller.controlGetBuildingList());
        l2.setLabelFor(c);
        p.add(c);

        BaseForm newFrame = new BaseForm(action, 300, 400, p);
        JButton saveButton = newFrame.getSaveButton();
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String msg = controller.controlAddRoom(
                        textField.getText(),
                        (String) c.getSelectedItem());
                JOptionPane.showMessageDialog(newFrame, msg);
                newFrame.dispose();
            }
        });
        // newFrame.paintAll(newFrame.getGraphics());
        // newFrame.pack();
    }

    public static void newBuilding(BookingSystemController controller) {
        String action = "New Building";
        Map<String, Component> map = new HashMap<>();

        String[] labels = { "Name", "Address" };
        int numPairs = labels.length;

        JPanel p = new JPanel(new GridLayout(2, 2, 5, 5));
        for (int i = 0; i < numPairs; i++) {
            JLabel l = new JLabel(labels[i], JLabel.LEADING);
            p.add(l);
            JTextField textField = new JTextField(10);
            map.put(labels[i], textField);
            l.setLabelFor(textField);
            p.add(textField);
        }

        BaseForm newFrame = new BaseForm(action, 300, 400, p);
        JButton saveButton = newFrame.getSaveButton();
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JTextField name = (JTextField) map.get("Name");
                JTextField bld = (JTextField) map.get("Address");
                String msg = controller.controlAddBuilding(
                        name.getText(),
                        bld.getText());
                JOptionPane.showMessageDialog(newFrame, msg);
                newFrame.dispose();
            }
        });

    }

    public static void removeBooking(BookingSystemController controller) {
        String action = "Remove Booking";
        Map<String, Component> map = new HashMap<>();
        String[] labels = { "Date", "Start Time", "End Time" };
        int numPairs = labels.length;

        JPanel p = new JPanel(new GridLayout(6, 2, 5, 5));
        for (int i = 0; i < numPairs; i++) {
            JLabel l = new JLabel(labels[i], JLabel.LEADING);
            p.add(l);
            JTextField textField = new JTextField(10);
            l.setLabelFor(textField);
            p.add(textField);
            map.put(labels[i], textField);
        }

        // JPanel p = new JPanel(new GridLayout(7, 2, 5, 5));
        // Date
        // JLabel l = new JLabel("Date", JLabel.LEADING);
        // p.add(l);
        // JComboBox<String> c = new JComboBox<>(controller.controlGetDateList());
        // l.setLabelFor(c);
        // p.add(c);

        // DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        // Building
        JLabel l1 = new JLabel("Building", JLabel.LEADING);
        p.add(l1);
        JComboBox<String> c1 = new JComboBox<>(controller.controlGetBuildingList());
        // c1.setModel(model);
        // c1.setEnabled(false);
        l1.setLabelFor(c1);
        p.add(c1);
        map.put("Building", c1);

        // Room
        JLabel l2 = new JLabel("Room", JLabel.LEADING);
        p.add(l2);
        JComboBox<String> c2 = new JComboBox<>(controller.controlGetRoomList());
        // c2.setModel(model);
        // c2.setEnabled(false);
        l2.setLabelFor(c2);
        p.add(c2);
        map.put("Room", c2);

        JLabel l3 = new JLabel("Owner", JLabel.LEADING);
        p.add(l3);
        JComboBox<String> c3 = new JComboBox<>(controller.controlGetPersonList());
        l3.setLabelFor(c3);
        p.add(c3);
        map.put("Owner", c3);

        // JLabel l1 = new JLabel("Building", JLabel.LEADING);
        // p.add(l1);
        // JComboBox c1 = new JComboBox<>(controller.controlGetBuildingList());
        // l.setLabelFor(c1);
        // p.add(c1);
        // map.put("Building", c1);

        // JLabel l2 = new JLabel("Room", JLabel.LEADING);
        // p.add(l2);
        // JComboBox c2 = new JComboBox<>(controller.controlGetRoomList());
        // l2.setLabelFor(c2);
        // p.add(c2);
        // map.put("Room", c2);

        // // Owner
        // JLabel l3 = new JLabel("Owner", JLabel.LEADING);
        // p.add(l3);
        // JComboBox c3 = new JComboBox<>(controller.controlGetPersonList());
        // l3.setLabelFor(c3);
        // p.add(c3);
        // map.put("Owner", c3);

        BaseForm removeBookingFrame = new BaseForm(action, 300, 400, p);
        JButton saveButton = removeBookingFrame.getSaveButton();
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JTextField date = (JTextField) map.get("Date");
                JTextField st = (JTextField) map.get("Start Time");
                JTextField et = (JTextField) map.get("End Time");
                JComboBox<String> building = (JComboBox) map.get("Building");
                JComboBox<String> room = (JComboBox) map.get("Room");
                JComboBox<String> owner = (JComboBox) map.get("Owner");
                // pass to controller
                String msg = controller.controlRemoveBooking(
                        date.getText(),
                        st.getText(),
                        et.getText(),
                        (String) building.getSelectedItem(),
                        (String) room.getSelectedItem(),
                        (String) owner.getSelectedItem());
                JOptionPane.showMessageDialog(removeBookingFrame, msg);
                removeBookingFrame.dispose();
            }
        });
    }

    public static void removePerson(BookingSystemController controller) {
        String action = "Remove Person";

        JPanel p = new JPanel(new GridLayout(2, 2, 5, 5));

        // Name
        JLabel l = new JLabel("Name", JLabel.LEADING);
        p.add(l);
        JComboBox<String> c = new JComboBox<>(controller.controlGetPersonList());
        l.setLabelFor(c);
        p.add(c);

        // Email
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        JLabel l2 = new JLabel("Email", JLabel.LEADING);
        p.add(l2);
        JComboBox<String> c2 = new JComboBox<>();
        c2.setModel(model);
        c2.setEnabled(false);
        l2.setLabelFor(c2);
        p.add(c2);

        c.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String person = (String) c.getSelectedItem();
                c2.setModel(new DefaultComboBoxModel<>(controller.controlGetEmailList(person)));
                c2.setEnabled(true);
            }
        });

        BaseForm newFrame = new BaseForm(action, 300, 400, p);
        JButton saveButton = newFrame.getSaveButton();

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String person = (String) c.getSelectedItem();
                String email = (String) c2.getSelectedItem();
                controller.controlRemovePerson(person, email);
                newFrame.dispose();
            }
        });

    }

    public static void removeRoom(BookingSystemController controller) {
        String action = "Remove Room";

        JPanel p = new JPanel(new GridLayout(2, 2, 5, 5));

        // Name
        JLabel l = new JLabel("Building", JLabel.LEADING);
        p.add(l);
        JComboBox<String> c = new JComboBox<>(controller.controlGetBuildingList());
        l.setLabelFor(c);
        p.add(c);

        // Room
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(controller.controlGetRoomList());
        JLabel l2 = new JLabel("Room", JLabel.LEADING);
        p.add(l2);
        JComboBox<String> c2 = new JComboBox<>();
        c2.setModel(model);
        c2.setEnabled(false);
        l2.setLabelFor(c2);
        p.add(c2);

        c.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c2.setEnabled(true);
                String builString = (String) c.getSelectedItem();
                String[] roomStrings = controller.controlGetRoomList(builString);
                System.out.printf("Rooms: %s\n", roomStrings.toString());
                c2.setModel(new DefaultComboBoxModel<>());
                c2.setEnabled(true);

            }
        });

        BaseForm newFrame = new BaseForm(action, 300, 400, p);
        JButton saveButton = newFrame.getSaveButton();
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String building = (String) c.getSelectedItem();
                String room = (String) c2.getSelectedItem();
                controller.controlRemoveRoom(room, building);
                newFrame.dispose();
            }
        });

    }

    public static void removeBuilding(BookingSystemController controller) {
        String action = "Remove Building";
        JPanel p = new JPanel(new GridLayout(1, 2, 5, 5));

        // Name
        JLabel l = new JLabel("Name", JLabel.LEADING);
        p.add(l);
        JComboBox<String> c = new JComboBox<>(controller.controlGetBuildingList());
        l.setLabelFor(c);
        p.add(c);

        BaseForm newFrame = new BaseForm(action, 300, 400, p);
        JButton saveButton = newFrame.getSaveButton();

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String msg = controller.controlRemoveBuilding(
                        (String) c.getSelectedItem());
                JOptionPane.showMessageDialog(newFrame, msg);
                newFrame.dispose();
            }
        });

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

}
