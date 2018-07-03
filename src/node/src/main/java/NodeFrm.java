import hcmus.BaseFrm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NodeFrm extends BaseFrm implements INodeContract.View {
    private NodeController mController;
    private JPanel panelMain;
    private JButton actStart;

    static void start() {
        JFrame frame = new JFrame("data.Node Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new NodeFrm().panelMain);
        frame.pack();
        frame.setVisible(true);
    }

    public NodeFrm() {
        super();
        mController = new NodeController();
        mController.attachView(this);
        actStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mController.requestConnectToServer();
            }
        });
    }
}
