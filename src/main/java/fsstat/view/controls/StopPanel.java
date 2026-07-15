package fsstat.view.controls;

import fsstat.controller.Controller;

import javax.swing.*;
import java.awt.*;

public class StopPanel extends JPanel {

    public StopPanel(final Controller controller) {
        super(new FlowLayout(FlowLayout.CENTER));
        final JButton btnStop = new JButton("■");
        btnStop.setFont(new Font("Monospaced", Font.BOLD, 16));
        btnStop.setForeground(Color.RED);
        btnStop.addActionListener(_ -> controller.stop());
        this.add(btnStop);
    }

}
