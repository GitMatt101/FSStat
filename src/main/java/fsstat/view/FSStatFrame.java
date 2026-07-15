package fsstat.view;

import fsstat.controller.Controller;
import fsstat.view.controls.ControlsPanel;

import javax.swing.*;
import java.awt.*;

public class FSStatFrame extends JFrame {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    private final ChartPanel chartPanel;
    private final DirectoryPanel directoryPanel;

    public FSStatFrame(final int nBins, final Controller controller) {
        this.setTitle("FSStat");
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        this.directoryPanel = new DirectoryPanel();
        this.chartPanel = new ChartPanel(nBins);

        controller.setDirectoryPanel(this.directoryPanel);
        controller.setChartPanel(this.chartPanel);

        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.directoryPanel, this.chartPanel);
        splitPane.setDividerLocation(350);
        splitPane.setResizeWeight(0.3);
        this.add(splitPane, BorderLayout.CENTER);
        this.add(new ControlsPanel(controller), BorderLayout.SOUTH);

//        final List<DirectoryItem> items = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            items.add(new DirectoryItem("C:\\path\\to\\directory" + (i + 1)));
//        }
//
//        new Thread(() -> {
//            for (final DirectoryItem item : items) {
//                directoryPanel.addDirectory(item);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException _) {}
//            }
//        }).start();
//
//
//        final Random rand = new Random();
//        new Thread(() -> {
//            while (true) {
//                if (rand.nextBoolean()) {
//                    items.get(rand.nextInt(items.size())).markAsExplored();
//                    directoryPanel.refreshList();
//                }
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException _) {}
//            }
//        }).start();
//
//        new Thread(() -> {
//            while (true) {
//                final int[] data = IntStream.range(0, nBins).map(_ -> rand.nextInt(10)).toArray();
//                chartPanel.addData(data);
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException _) {}
//            }
//        }).start();
    }

    public ChartPanel getChartPanel() {
        return this.chartPanel;
    }

    public DirectoryPanel getDirectoryPanel() {
        return  this.directoryPanel;
    }

}
