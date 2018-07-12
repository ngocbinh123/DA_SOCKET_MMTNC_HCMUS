import data.Node;
import hcmus.BaseFrm;
import hcmus.SOCKET_STATUS;
import hcmus.views.VerticalPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class NodeFrm extends BaseFrm implements INodeContract.View {
    private NodeController mController;
    private JPanel vPanelMain;
    private JButton vActConnect;

    static void start() {
        JFrame frame = new JFrame("Node Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new NodeFrm().vPanelMain);
        frame.pack();
        frame.setVisible(true);
    }

    public NodeFrm() {
        super();
        mController = new NodeController();
        mController.attachView(this);
    }

    private JPanel vPanelBody;
    private VerticalPanel panelLeftContainer;
    private VerticalPanel panelRightContainer;
    private DefaultListModel<String> mFileNamesModel;
    private JLabel vLblStatus;
    private JLabel vLblNodeName;
    private JLabel vLblLocalPort;
    private JLabel vLblFileSize;

    private void createUIComponents() {
        vPanelMain = new JPanel(new BorderLayout());
        vPanelBody = new JPanel(new GridLayout(0,2));

        panelLeftContainer = new VerticalPanel();
        panelRightContainer = new VerticalPanel();

        vLblStatus = new JLabel("Status: ");
        vLblNodeName = new JLabel("Name: ");
        vLblLocalPort = new JLabel("Local port: ");
        vLblFileSize = new JLabel("Files: ");

        JLabel lblInfo = new JLabel("Node Information:");
        panelLeftContainer.add(lblInfo);

        panelLeftContainer.add(vLblStatus);
        panelLeftContainer.add(vLblNodeName);
        panelLeftContainer.add(vLblLocalPort);

        panelRightContainer.add(vLblFileSize);
        mFileNamesModel = new DefaultListModel<>();
        JList<String> nodeJList = new JList<>();
        nodeJList.setModel(mFileNamesModel);
        panelRightContainer.add(new JScrollPane(nodeJList));

        vPanelBody.add(panelLeftContainer);
        vPanelBody.add(panelRightContainer);

        vActConnect = new JButton("Connect To Server");
        vActConnect.addActionListener(e -> {
            if (vActConnect.getText().equalsIgnoreCase(SOCKET_STATUS.DISCONNECT.getValue())) {
                mController.disconnect();
                vActConnect.setText("Connect To Server");
            }else {
                mController.requestConnectToServer();
            }
        });

        vPanelMain.add(vPanelBody, BorderLayout.CENTER);
        vPanelMain.add(vActConnect, BorderLayout.SOUTH);
    }

    @Override
    public void updateDataOnUI(boolean isConnect, Node node) {
        EventQueue.invokeLater(() -> {
            if (isConnect) {
                vLblStatus.setText("Status: " + SOCKET_STATUS.CONNECT.getValue());
                vActConnect.setText(SOCKET_STATUS.DISCONNECT.getValue());
                vLblFileSize.setText(String.format("Files: %d", node.getFileSize()));
                for (File file : node.getFiles()) {
                    mFileNamesModel.addElement(file.getName());
                }
                vLblNodeName.setText("Name: " + node.getName());
                vLblLocalPort.setText("Local port: " + String.valueOf(node.getLocalPort()));
                vLblFileSize.setText(String.format("Files: %d", node.getFileSize()));
            }else {
                vLblStatus.setText("Status: " + SOCKET_STATUS.DISCONNECT.getValue());
                vActConnect.setText("Connect To Server");
            }
        });
    }
}
