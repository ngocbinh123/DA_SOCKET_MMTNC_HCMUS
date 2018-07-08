package hcmus.views;

import hcmus.data.Node;

import javax.swing.*;
import java.awt.*;

public class NodeRender extends JPanel implements ListCellRenderer<Node> {
    private JLabel lbNodeName = new JLabel();

    public NodeRender() {
        setLayout(new BorderLayout(5, 5));
        add(lbNodeName, BorderLayout.WEST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Node> list, Node value, int index, boolean isSelected, boolean cellHasFocus) {
        lbNodeName.setText(value.getName());
        return this;
    }
}
