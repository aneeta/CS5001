package roombooking.views.gui.customframe;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import roombooking.controller.BookingSystemController;
import roombooking.model.BookingSystemModel;
<<<<<<< HEAD:CS5001-p4-room-booking/src/roombooking/views/gui/customframe/PopUpFrame.java
import roombooking.views.gui2.BookingSystemGui;
=======
>>>>>>> refs/rewritten/fix-names-p4:new-p4/src/roombooking/views/gui/customframe/PopUpFrame.java

/**
 * Class drawing a pop up frame with an appropriate from for add/remove
 * functionality.
 * Draws a frame with appropriate fields.
 * Listens to the Model to populate these fields.
 * Takes data in from the fields and passes it to the Controller.
 */
public class PopUpFrame implements ActionListener, PropertyChangeListener {
    private BookingSystemModel model;
    private JComboBox<String> roomBlg;
    private JComboBox<String> peopleList;

    public PopUpFrame(BookingSystemModel model) {
        this.model = model;
        this.model.addListener(this);
    }

    public void newBooking(BookingSystemController controller) {
        String action = "New Booking";
        Map<String, Component> map = new HashMap<>();
        String[] labels = { "Date", "Start Time", "End Time" };
        int numPairs = labels.length;

        // Date, Start, End (Freeform text fields)
        JPanel p = new JPanel(new GridLayout(7, 2, 5, 5));
        for (int i = 0; i < numPairs; i++) {
            JLabel l = new JLabel(labels[i], JLabel.LEADING);
            p.add(l);
            JTextField textField = new JTextField(10);
            l.setLabelFor(textField);
            p.add(textField);
            map.put(labels[i], textField);
        }

<<<<<<< HEAD:CS5001-p4-room-booking/src/roombooking/views/gui/customframe/PopUpFrame.java
        // Room & Building
        String[][] data = controller.controlGetRooms();
        String[] dataFormatted = Arrays.asList(Arrays.copyOfRange(data, 1, data.length)).stream()
                .map(x -> x[0] + " - " + x[1]).toArray(String[]::new);
        JLabel l = new JLabel("Room (Building)", JLabel.LEADING);
=======
        // Building (Dropdown)
        JLabel l = new JLabel("Building", JLabel.LEADING);
>>>>>>> refs/rewritten/fix-names-p4:new-p4/src/roombooking/views/gui/customframe/PopUpFrame.java
        p.add(l);
        roomBlg = new JComboBox<>(dataFormatted);
        l.setLabelFor(roomBlg);
        p.add(roomBlg);
        // map.put("Room (Building)", c);

<<<<<<< HEAD:CS5001-p4-room-booking/src/roombooking/views/gui/customframe/PopUpFrame.java
        /// BUILDING DEPENDEDNT DROPDOWN
        // // Building
        // JLabel l = new JLabel("Building", JLabel.LEADING);
        // p.add(l);
        // JComboBox<String> c = new JComboBox<>(controller.controlGetBuildingList());
        // l.setLabelFor(c);
        // p.add(c);
        // map.put("Building", c);

        // // Room
        // JLabel l2 = new JLabel("Room", JLabel.LEADING);
        // p.add(l2);

        // JComboBox<String> c2 = new JComboBox<>();
        // c2.setEnabled(false);
        // l2.setLabelFor(c2);
        // p.add(c2);
        // map.put("Room", c2);

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
        JLabel owner = new JLabel("Owner", JLabel.LEADING);
        p.add(owner);
        peopleList = new JComboBox<>(controller.controlGetPersonList());
        owner.setLabelFor(peopleList);
        p.add(peopleList);
        // map.put("Owner", c3);
=======
        // Room (Dropdown)
        JLabel l2 = new JLabel("Room", JLabel.LEADING);
        p.add(l2);
        JComboBox<String> c2 = new JComboBox<>(controller.controlGetRoomList());
        l2.setLabelFor(c2);
        p.add(c2);
        map.put("Room", c2);

        // Owner (Dropdown)
        JLabel l3 = new JLabel("Owner", JLabel.LEADING);
        p.add(l3);
        JComboBox<String> c3 = new JComboBox<>(controller.controlGetPersonList());
        l3.setLabelFor(c3);
        p.add(c3);
        map.put("Owner", c3);
>>>>>>> refs/rewritten/fix-names-p4:new-p4/src/roombooking/views/gui/customframe/PopUpFrame.java

        BaseForm newBookingFrame = new BaseForm(action, 300, 400, p);
        JButton saveButton = newBookingFrame.getSaveButton();
        // action upon clicking save
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // get inputs
                String[] roomAndBuidling = roomBlg.getSelectedItem().toString().split(" ");
                JTextField date = (JTextField) map.get("Date");
                JTextField st = (JTextField) map.get("Start Time");
                JTextField et = (JTextField) map.get("End Time");
                String room = roomAndBuidling[0];
                String building = roomAndBuidling[2];
                // JComboBox<String> room = (JComboBox) map.get("Room");
                // JComboBox<String> owner = (JComboBox) map.get("Owner");
                // pass to controller
                String msg = controller.controlAddBooking(
                        date.getText(),
                        st.getText(),
                        et.getText(),
                        building,
                        room,
                        (String) peopleList.getSelectedItem());
                JOptionPane.showMessageDialog(newBookingFrame, msg);
                newBookingFrame.dispose();
            }
        });
    }

<<<<<<< HEAD:CS5001-p4-room-booking/src/roombooking/views/gui/customframe/PopUpFrame.java
    public void newPerson(BookingSystemController controller) {
        System.out.println("Running new person");
=======
    public static void newPerson(BookingSystemController controller) {
>>>>>>> refs/rewritten/fix-names-p4:new-p4/src/roombooking/views/gui/customframe/PopUpFrame.java
        String action = "New Person";
        Map<String, Component> map = new HashMap<>();

        String[] labels = { "Name", "Email" };
        int numPairs = labels.length;

        // Name and Email (Freeform text fields)
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
        // action upon clicking save
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
    }

    public void newRoom(BookingSystemController controller) {
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
        // action upon clicking save
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
    }

    public void newBuilding(BookingSystemController controller) {
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
        // action upon clicking save
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

    public void removeBooking(BookingSystemController controller) {
        String action = "Remove Booking";
        Map<String, Component> map = new HashMap<>();
        String[] labels = { "Date", "Start Time", "End Time" };
        int numPairs = labels.length;

        // Date, Start, End (Freeform text fields)
        JPanel p = new JPanel(new GridLayout(6, 2, 5, 5));
        for (int i = 0; i < numPairs; i++) {
            JLabel l = new JLabel(labels[i], JLabel.LEADING);
            p.add(l);
            JTextField textField = new JTextField(10);
            l.setLabelFor(textField);
            p.add(textField);
            map.put(labels[i], textField);
        }

<<<<<<< HEAD:CS5001-p4-room-booking/src/roombooking/views/gui/customframe/PopUpFrame.java
        DefaultComboBoxModel<String> cmodel = new DefaultComboBoxModel<>();
        // Building
        JLabel l1 = new JLabel("Building", JLabel.LEADING);
        p.add(l1);
        JComboBox<String> c1 = new JComboBox<>();
        c1.setModel(cmodel);
        c1.setEnabled(false);
=======
        JLabel l1 = new JLabel("Building", JLabel.LEADING);
        p.add(l1);
        JComboBox<String> c1 = new JComboBox<>(controller.controlGetBuildingList());
>>>>>>> refs/rewritten/fix-names-p4:new-p4/src/roombooking/views/gui/customframe/PopUpFrame.java
        l1.setLabelFor(c1);
        p.add(c1);
        map.put("Building", c1);

        // Room
        JLabel l2 = new JLabel("Room", JLabel.LEADING);
        p.add(l2);
<<<<<<< HEAD:CS5001-p4-room-booking/src/roombooking/views/gui/customframe/PopUpFrame.java
        JComboBox<String> c2 = new JComboBox<>();
        c2.setModel(cmodel);
        c2.setEnabled(false);
=======
        JComboBox<String> c2 = new JComboBox<>(controller.controlGetRoomList());
>>>>>>> refs/rewritten/fix-names-p4:new-p4/src/roombooking/views/gui/customframe/PopUpFrame.java
        l2.setLabelFor(c2);
        p.add(c2);
        map.put("Room", c2);

        JLabel l3 = new JLabel("Owner", JLabel.LEADING);
        p.add(l3);
        JComboBox<String> c3 = new JComboBox<>(controller.controlGetPersonList());
        l3.setLabelFor(c3);
        p.add(c3);
        map.put("Owner", c3);

        BaseForm removeBookingFrame = new BaseForm(action, 300, 400, p);
        JButton saveButton = removeBookingFrame.getSaveButton();
        // action upon clicking save
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

    public void removePerson(BookingSystemController controller) {
        String action = "Remove Person";

        JPanel p = new JPanel(new GridLayout(2, 2, 5, 5));

        // Name
        JLabel l = new JLabel("Name", JLabel.LEADING);
        p.add(l);
        JComboBox<String> c = new JComboBox<>(controller.controlGetPersonList());
        l.setLabelFor(c);
        p.add(c);

        // Email
<<<<<<< HEAD:CS5001-p4-room-booking/src/roombooking/views/gui/customframe/PopUpFrame.java
        DefaultComboBoxModel<String> cmodel = new DefaultComboBoxModel<>();

=======
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
>>>>>>> refs/rewritten/fix-names-p4:new-p4/src/roombooking/views/gui/customframe/PopUpFrame.java
        JLabel l2 = new JLabel("Email", JLabel.LEADING);
        p.add(l2);
        JComboBox<String> c2 = new JComboBox<>();
        c2.setModel(cmodel);
        c2.setEnabled(false);
        l2.setLabelFor(c2);
        p.add(c2);

        // Email dropdown after selecting Person
        c.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String person = (String) c.getSelectedItem();
                c2.setModel(new DefaultComboBoxModel<>(controller.controlGetEmailList(person)));
                c2.setEnabled(true);
            }
        });

        BaseForm newFrame = new BaseForm(action, 300, 400, p);
        JButton saveButton = newFrame.getSaveButton();
        // action upon clicking save
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String person = (String) c.getSelectedItem();
                String email = (String) c2.getSelectedItem();
                controller.controlRemovePerson(person, email);
                newFrame.dispose();
            }
        });

    }

    public void removeRoom(BookingSystemController controller) {
        String action = "Remove Room";
        JPanel p = new JPanel(new GridLayout(2, 2, 5, 5));

        // Name
        JLabel l = new JLabel("Building", JLabel.LEADING);
        p.add(l);
        JComboBox<String> c = new JComboBox<>(controller.controlGetBuildingList());
        l.setLabelFor(c);
        p.add(c);

<<<<<<< HEAD:CS5001-p4-room-booking/src/roombooking/views/gui/customframe/PopUpFrame.java
        // Email
        DefaultComboBoxModel<String> cmodel = new DefaultComboBoxModel<>();
=======
        // Room
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(controller.controlGetRoomList());
>>>>>>> refs/rewritten/fix-names-p4:new-p4/src/roombooking/views/gui/customframe/PopUpFrame.java
        JLabel l2 = new JLabel("Room", JLabel.LEADING);
        p.add(l2);
        JComboBox<String> c2 = new JComboBox<>();
        c2.setModel(cmodel);
        c2.setEnabled(false);
        l2.setLabelFor(c2);
        p.add(c2);
        // room selection dropdown after building selected
        c.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c2.setEnabled(true);
                String builString = (String) c.getSelectedItem();
                String[] roomStrings = controller.controlGetRoomList(builString);
                c2.setModel(new DefaultComboBoxModel<>(roomStrings));
                c2.setEnabled(true);
            }
        });

        BaseForm newFrame = new BaseForm(action, 300, 400, p);
        JButton saveButton = newFrame.getSaveButton();
        // action upon clicking save
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String building = (String) c.getSelectedItem();
                String room = (String) c2.getSelectedItem();
                controller.controlRemoveRoom(room, building);
                newFrame.dispose();
            }
        });

    }

    public void removeBuilding(BookingSystemController controller) {
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
        // action upon clicking save
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
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

}
