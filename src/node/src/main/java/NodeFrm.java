import data.Node;
import hcmus.BaseFrm;
import hcmus.Constant;
import hcmus.SOCKET_STATUS;
import hcmus.views.VerticalPanel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    private JPanel vPanelHeader;
    private JPanel vPanelBody;
    private VerticalPanel panelLeftContainer;
    private VerticalPanel panelRightContainer;
    private DefaultListModel<String> mFileNamesModel;
    private  JTextField txtServerIP;
    private JLabel vLblStatus;
    private JLabel vLblNodeName;
    private JLabel vLblLocalPort;
    private JLabel vLblFileSize;

    private void createUIComponents() {

        vPanelMain = new JPanel(new BorderLayout());
        vPanelHeader = new JPanel(new GridLayout(0,2));

        vPanelBody = new JPanel(new GridLayout(0,2));
        vPanelBody.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelLeftContainer = new VerticalPanel();
        panelRightContainer = new VerticalPanel();

//        header
        JLabel lblServerHost = new JLabel("Server IP: ", SwingConstants.RIGHT);
        vPanelHeader.add(lblServerHost);

        txtServerIP = new JTextField(Constant.SERVER_IP);
        vPanelHeader.add(txtServerIP);

        vLblStatus = new JLabel("Status: ");
        vLblNodeName = new JLabel("Name: ");
        vLblLocalPort = new JLabel("Local port: ");
        vLblFileSize = new JLabel("Files: ");

        JLabel lblInfo = new JLabel("Node Information:");
        Font labelFont = lblInfo.getFont();
        lblInfo.setForeground(Color.BLUE);
        lblInfo.setFont(new Font(labelFont.getName(), Font.BOLD, 14));
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
                clearInfo();
            }else {
                String serverIP = txtServerIP.getText().trim();
                if (!serverIP.isEmpty()) {
                    mController.requestConnectToServer(serverIP);
                }else {
                    showMessage("Server IP should't be null");
                }
            }
        });

        vPanelMain.add(vPanelHeader, BorderLayout.NORTH);
        vPanelMain.add(vPanelBody, BorderLayout.CENTER);
        vPanelMain.add(vActConnect, BorderLayout.SOUTH);
    }

    @Override
    public void updateDataOnUI(Node node) {
        vLblStatus.setText("Status: " + SOCKET_STATUS.CONNECT.getValue());
        vActConnect.setText(SOCKET_STATUS.DISCONNECT.getValue());
        vLblFileSize.setText(String.format("Files: %d", node.getFileSize()));
        for (File file : node.getFiles()) {
            mFileNamesModel.addElement(file.getName());
        }
        vLblNodeName.setText("Name: " + node.getName());
        vLblLocalPort.setText("Local port: " + String.valueOf(node.getLocalPort()));
        vLblFileSize.setText(String.format("Files: %d", node.getFileSize()));
    }

    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(vPanelMain,message);
    }

    @Override
    public void clearInfo() {
        vLblNodeName.setText("Name:");
        vLblStatus.setText("Status:");
        vActConnect.setText("Connect To Server");
        vLblLocalPort.setText("Local port:");
        mFileNamesModel.clear();
    }
}
