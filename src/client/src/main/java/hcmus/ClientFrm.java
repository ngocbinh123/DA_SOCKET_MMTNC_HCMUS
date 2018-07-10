package hcmus;

import hcmus.data.Client;
import hcmus.views.VerticalPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ClientFrm extends BaseFrm implements IClientContract.View {
    private JPanel vPanelMain;
    private JButton vActConnect;
    private ClientController mController;

    static void start() {
        JFrame frame = new JFrame("Client Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new ClientFrm().vPanelMain);
        frame.pack();
        frame.setVisible(true);
    }

    public ClientFrm() {
        super();
        mController = new ClientController();
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

        JLabel lblInfo = new JLabel("Client Information:");
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
    public void updateDataOnUI(Client client) {
        EventQueue.invokeLater(() -> {
            vLblStatus.setText("Status: " + SOCKET_STATUS.CONNECT.getValue());
            vActConnect.setText(SOCKET_STATUS.DISCONNECT.getValue());
//            vLblFileSize.setText(String.format("Files: %d", node.getFileSize()));
//            for (File file : node.getFiles()) {
//                mFileNamesModel.addElement(file.getName());
//            }
            vLblNodeName.setText("Name: " + client.getName());
            vLblLocalPort.setText("Local port: " + String.valueOf(client.getLocalPort()));
//            vLblFileSize.setText(String.format("Files: %d", node.getFileSize()));
        });
    }
}
