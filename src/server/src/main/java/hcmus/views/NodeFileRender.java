package hcmus.views;

import hcmus.data.NodeFile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NodeFileRender extends JPanel implements ListCellRenderer<NodeFile> {
    private JLabel lbFileName = new JLabel();
    private JLabel lbNodeName = new JLabel();
    public NodeFileRender() {
        EmptyBorder border = new EmptyBorder(5, 5, 1,5);
        lbFileName.setBorder(border);
        lbNodeName.setBorder(border);
        setLayout(new BorderLayout(5, 5));
        add(lbFileName, BorderLayout.WEST);
        add(lbNodeName, BorderLayout.EAST);

    }

    @Override
    public Component getListCellRendererComponent(JList<? extends NodeFile> list, NodeFile value, int index, boolean isSelected, boolean cellHasFocus) {
        lbFileName.setText(value.getName());
        lbNodeName.setText(value.getNodeName());
        lbNodeName.setForeground(Color.blue);
        return this;
    }
}
