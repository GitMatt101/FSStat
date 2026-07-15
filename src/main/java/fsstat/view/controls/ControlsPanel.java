package fsstat.view.controls;

import fsstat.controller.Controller;

import javax.swing.*;
import java.awt.*;

public class ControlsPanel extends JPanel {

    private final PlayPausePanel playPausePanel;
    private final ModePanel modePanel;

    public ControlsPanel(final Controller controller) {
        super(new GridLayout(1, 3, 10, 0));
        controller.setControlsPanel(this);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.playPausePanel = new PlayPausePanel(controller);
        this.modePanel = new ModePanel(controller);
        this.add(this.modePanel);
        this.add(this.playPausePanel);
        this.add(new StopPanel(controller));
    }

    public void togglePause() {
        this.playPausePanel.togglePause();
    }

    public void disableAll() {
        this.playPausePanel.setEnabled(false);
        this.modePanel.setEnabled(false);
    }
}
