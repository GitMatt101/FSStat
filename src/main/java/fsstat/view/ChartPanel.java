package fsstat.view;

import javax.swing.*;
import java.awt.*;

public class ChartPanel extends JPanel {

    private final int[] data;
    private final String[] labels;

    public ChartPanel(final long maxSize, final int nBands) {
        this.labels = new String[nBands + 1];
        this.data = new int[nBands + 1];
        final long step = maxSize / nBands;
        for (int i = 0; i < nBands + 1; i++) {
            if (i == nBands) {
                this.labels[i] = ">" + (i * step);
            } else {
                this.labels[i] = (i * step) + "-" + (i+1) * step;
            }
        }
        setBorder(BorderFactory.createTitledBorder("Files distribution"));
        setBackground(Color.WHITE);
    }

    public void addData(final int[] newData) {
        SwingUtilities.invokeLater(() -> {
            int limit = Math.min(data.length, newData.length);
            for (int i = 0; i < limit; i++) {
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
        int totalFiles = 0;
        int max = 0;
        for (int val : currentData) {
            totalFiles += val;
            if (val > max) max = val;
        }
        if (max == 0)
            max = 1;
        g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2d.setColor(Color.DARK_GRAY);

        final String totalText = "Total files: " + totalFiles;
        final int totalTextWidth = g2d.getFontMetrics().stringWidth(totalText);
        g2d.drawString(totalText, getWidth() - totalTextWidth - 20, 25);

        final double rad = Math.toRadians(45);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 10));
        int maxLabelWidth = 0;
        if (labels != null) {
            for (String label : labels) {
                int w = g2d.getFontMetrics().stringWidth(label);
                if (w > maxLabelWidth) {
                    maxLabelWidth = w;
                }
            }
        }

        final int bottomPadding = (int) (maxLabelWidth * Math.sin(rad)) + 15;
        final int paddingLeft = 20;
        final int paddingTop = 40;
        final int width = getWidth() - 40;
        int height = getHeight() - paddingTop - bottomPadding;
        if (height < 50) {
            height = 50;
        }
        int barWidth = width / currentData.length - 5;
        for (int i = 0; i < currentData.length; i++) {
            final int barHeight = (int) (((double) currentData[i] / max) * height);
            final int x = paddingLeft + i * (barWidth + 5);
            final int y = paddingTop + height - barHeight;
            g2d.setColor(new Color(70, 130, 180));
            g2d.fillRect(x, y, barWidth, barHeight);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawRect(x, y, barWidth, barHeight);

            g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g2d.setColor(Color.BLACK);
            final String valLabel = String.valueOf(currentData[i]);
            final int valWidth = g2d.getFontMetrics().stringWidth(valLabel);
            final int valX = x + (barWidth - valWidth) / 2;
            g2d.drawString(valLabel, valX, y - 5);
            if (labels != null && i < labels.length) {
                g2d.setFont(new Font("SansSerif", Font.BOLD, 10));
                g2d.setColor(Color.BLACK);
                final String rangeLabel = labels[i];
                final int labelY = paddingTop + height + 10;
                final java.awt.geom.AffineTransform originalTransform = g2d.getTransform();
                g2d.translate(x + 5, labelY);
                g2d.rotate(rad);
                g2d.drawString(rangeLabel, 0, 5);
                g2d.setTransform(originalTransform);
            }
        }
    }
}