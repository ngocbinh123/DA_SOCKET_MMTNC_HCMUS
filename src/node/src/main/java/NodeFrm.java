import hcmus.BaseFrm;

import javax.swing.*;

public class NodeFrm extends BaseFrm implements INodeContract.View {
    private NodeController mController;
    private JPanel panelMain;
    private JButton actStart;

    static void start() {
        JFrame frame = new JFrame("Node Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new NodeFrm().panelMain);
        frame.pack();
        frame.setVisible(true);
    }

    public NodeFrm() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mController = new NodeController();
        mController.attachView(this);
    }
}
