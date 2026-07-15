package fsstat.view;

import javax.swing.*;
import java.awt.*;

public class DirectoryPanel extends JPanel {

    private final DefaultListModel<DirectoryItem> listModel;

    public DirectoryPanel() {
        super(new BorderLayout());
        this.setBorder(BorderFactory.createTitledBorder("Exploration status"));
        this.listModel = new DefaultListModel<>();
        final JList<DirectoryItem> directoryList = getDirectoryItemJList(listModel);
        this.add(new JScrollPane(directoryList), BorderLayout.CENTER);
    }

    public void addDirectory(final DirectoryItem item) {
        SwingUtilities.invokeLater(() -> this.listModel.addElement(item));
        this.refreshList();
    }

    private void refreshList() {
        SwingUtilities.invokeLater(this::repaint);
    }

    public void markAsDone(final String directory, final int nFiles) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < this.listModel.size(); i++) {
                if (this.listModel.get(i).toString().equals(directory)) {
                    this.listModel.get(i).markAsExplored(nFiles);
                }
            }
            this.refreshList();
        });
    }

    private static JList<DirectoryItem> getDirectoryItemJList(final DefaultListModel<DirectoryItem> listModel) {
        final JList<DirectoryItem> directoryList = new JList<>(listModel);
        directoryList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                final JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof final DirectoryItem item) {
                    if (!isSelected) {
                        label.setBackground(item.isExplored() ? new Color(100, 255, 100) : new Color(200, 200, 200));
                    }
                    label.setForeground(Color.BLACK);
                    label.setOpaque(true);
                }
                return label;
            }
        });
        return directoryList;
    }
}