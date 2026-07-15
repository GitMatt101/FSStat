package fsstat;

import fsstat.controller.Controller;
import fsstat.view.FSStatFrame;

import javax.swing.*;

public class Main {

    static void main(String[] args) throws InterruptedException {
        if (args.length != 3) {
            System.err.println("Wrong numbers of argument, please input (in order): directory path, max file size, number of bands");
            System.exit(-1);
        }
        final String directory = args[0];
        final long maxSize = Long.parseLong(args[1]);
        final int nBands = Integer.parseInt(args[2]);
        final Controller controller = new Controller(maxSize, nBands);
        final JFrame frame = new FSStatFrame(nBands + 1, controller);
        frame.setVisible(true);
        Thread.sleep(2000);
        controller.startComputing(directory);
    }

}
