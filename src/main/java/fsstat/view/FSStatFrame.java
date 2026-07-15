package fsstat.view;

import fsstat.controller.Controller;
import fsstat.view.controls.ControlsPanel;

import javax.swing.*;
import java.awt.*;

public class FSStatFrame extends JFrame {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    public FSStatFrame(final long maxSize, final int nBands, final Controller controller) {
        this.setTitle("FSStat");
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        final DirectoryPanel directoryPanel = new DirectoryPanel();
        final ChartPanel chartPanel = new ChartPanel(maxSize, nBands);

        controller.setDirectoryPanel(directoryPanel);
        controller.setChartPanel(chartPanel);

        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, directoryPanel, chartPanel);
        splitPane.setDividerLocation(350);
        splitPane.setResizeWeight(0.3);
        this.add(splitPane, BorderLayout.CENTER);
        this.add(new ControlsPanel(controller), BorderLayout.SOUTH);
    }

}
