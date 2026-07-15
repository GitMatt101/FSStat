package fsstat.view;

import javax.swing.*;
import java.awt.*;

public class ChartPanel extends JPanel {

    private int[] data;

    public ChartPanel(final int numberOfBins) {
        this.data = new int[numberOfBins];
        setBorder(BorderFactory.createTitledBorder("Files distribution"));
        setBackground(Color.WHITE);
    }

    public void addData(final int[] newData) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < data.length; i++) {
                this.data[i] += newData[i];
            }
            this.repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final int[] currentData = this.data;

        if (currentData == null || currentData.length == 0)
            return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth() - 40;
        int height = getHeight() - 60;
        int paddingLeft = 20;
        int paddingTop = 30;

        int max = 0;
        for (int val : currentData) {
            if (val > max)
                max = val;
        }
        if (max == 0)
            max = 1;

        int barWidth = width / currentData.length - 5;

        for (int i = 0; i < currentData.length; i++) {
            int barHeight = (int) (((double) currentData[i] / max) * height);
            int x = paddingLeft + i * (barWidth + 5);
            int y = paddingTop + height - barHeight;

            g2d.setColor(new Color(70, 130, 180));
            g2d.fillRect(x, y, barWidth, barHeight);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawRect(x, y, barWidth, barHeight);
            String label = String.valueOf(currentData[i]);
            int stringWidth = g2d.getFontMetrics().stringWidth(label);
            int textX = x + (barWidth - stringWidth) / 2;
            g2d.drawString(label, textX, y - 5);
        }
    }
}