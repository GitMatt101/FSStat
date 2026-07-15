package fsstat.view.controls;

import fsstat.controller.Controller;

import javax.swing.*;
import java.awt.*;

public class PlayPausePanel extends JPanel {

    private final JButton button;
    private boolean isPaused = false;

    public PlayPausePanel(final Controller controller) {
        super(new FlowLayout(FlowLayout.CENTER));
        this.button = new JButton("▶");
        this.button.setFont(new Font("Monospaced", Font.BOLD, 16));
        this.button.addActionListener(_ -> {
            controller.toggleExecutionPause();
            this.togglePause();
        });
        this.add(button);
    }

    public void togglePause() {
        isPaused = !isPaused;
        this.button.setText(isPaused ? "||" : "▶");
    }

    @Override
    public void setEnabled(final boolean b) {
        super.setEnabled(b);
        this.button.setEnabled(false);
    }

}
