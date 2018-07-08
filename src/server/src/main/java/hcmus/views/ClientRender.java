package hcmus.views;

import hcmus.data.Client;

import javax.swing.*;
import java.awt.*;

public class ClientRender extends JPanel implements ListCellRenderer<Client> {
    private JLabel lbClientName = new JLabel();
    @Override
    public Component getListCellRendererComponent(JList<? extends Client> list, Client value, int index, boolean isSelected, boolean cellHasFocus) {
        lbClientName.setText(value.getName());
        return this;
    }
}
