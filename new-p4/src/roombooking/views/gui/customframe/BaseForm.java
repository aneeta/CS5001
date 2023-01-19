package roombooking.views.gui.customframe;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Base frame for add/remove room/booking/person/building pop ups.
 */
public class BaseForm extends JFrame {
    private JButton saveButton;
    private JButton resetButton;

    public BaseForm(String action, int w, int h, JPanel form) {
        final JFrame newFrame = new JFrame(action);
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newFrame.setSize(w, h); // set frame size

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        this.saveButton = new JButton("OK");
        this.resetButton = new JButton("Reset");

        this.resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (Component comp : form.getComponents()) {
                    if (comp instanceof JTextField) {
                        ((JTextField) comp).setText("");
                    }
                }
            }
        });

        panel.add(this.resetButton);
        panel.add(Box.createHorizontalGlue());
        panel.add(this.saveButton);

        this.getContentPane().add(form, BorderLayout.CENTER);
        this.getContentPane().add(panel, BorderLayout.SOUTH);
        this.setVisible(true);
        this.paintAll(this.getGraphics());
        this.pack();
    }

    /**
     * Getter method for the Save button.
     * 
     * @return JButton
     */
    public JButton getSaveButton() {
        return this.saveButton;
    }
}
