package fsstat.view.controls;

import fsstat.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModePanel extends JPanel {

    private final JButton button;

    public ModePanel(final Controller controller) {
        super(new FlowLayout(FlowLayout.CENTER));
        this.button = new JButton("|▶");
        this.button.setToolTipText("Pause after reading a directory. Click to switch to continuous reading.");
        this.button.setFont(new Font("Monospaced", Font.BOLD, 16));
        this.button.addActionListener(new ActionListener() {
            private boolean stepByStep = true;
            @Override
            public void actionPerformed(final ActionEvent e) {
                stepByStep = !stepByStep;
                if (stepByStep) {
                    button.setText("|▶");
                    button.setToolTipText("Pause after reading a directory. Click to switch to continuous reading.");
                } else {
                    button.setText(">>");
                    button.setToolTipText("Continuous reading. Click to switch to pause after each reading.");
                }
                controller.toggleExecutionMode();
            }
        });
        this.add(this.button);
    }

    @Override
    public void setEnabled(final boolean b) {
        super.setEnabled(b);
        this.button.setEnabled(false);
    }

}
