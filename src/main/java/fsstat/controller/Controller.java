package fsstat.controller;

import fsstat.Report;
import fsstat.asynchronous.EventLoop;
import fsstat.view.ChartPanel;
import fsstat.view.DirectoryItem;
import fsstat.view.DirectoryPanel;
import fsstat.view.controls.ControlsPanel;

public class Controller {

    private final EventLoop eventLoop;
    private ChartPanel chartPanel;
    private DirectoryPanel directoryPanel;
    private ControlsPanel controlsPanel;

    public Controller(final long maxSize, final int nBands) {
        this.eventLoop = new EventLoop(maxSize, nBands, this);
    }

    public void setChartPanel(final ChartPanel panel) {
        this.chartPanel = panel;
    }

    public void setDirectoryPanel(final DirectoryPanel panel) {
        this.directoryPanel = panel;
    }

    public void setControlsPanel(final ControlsPanel panel) {
        this.controlsPanel = panel;
    }

    public void toggleExecutionPause() {
        this.eventLoop.togglePause();
    }

    public void toggleViewPause() {
        this.controlsPanel.togglePause();
    }

    public void addReport(final Report report) {
        this.chartPanel.addData(report.bands());
        this.directoryPanel.markAsDone(report.directory(), report.nFiles());
    }

    public void addDirectory(final String directory) {
        this.directoryPanel.addDirectory(new DirectoryItem(directory));
    }

    public void startComputing(final String directory) {
        eventLoop.addDirectoryToQueue(directory);
        eventLoop.start();
    }

    public void toggleExecutionMode() {
        this.eventLoop.toggleMode();
    }

    public void stop() {
        this.eventLoop.shutdown();
        this.controlsPanel.disableAll();
    }
}
