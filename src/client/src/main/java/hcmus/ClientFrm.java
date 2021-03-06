package hcmus;

import hcmus.data.Client;
import hcmus.data.NodeFile;
import hcmus.views.NodeFileRender;
import hcmus.views.VerticalPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

import static hcmus.Constant.MSG_DOWNLOAD_SUCCESSFUL;

public class ClientFrm extends BaseFrm implements IClientContract.View, ListSelectionListener {
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
    private DefaultListModel<NodeFile> mFileNamesModel;
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
        JList<NodeFile> nodeFilesJList = new JList<>();
        nodeFilesJList.setModel(mFileNamesModel);
        nodeFilesJList.setCellRenderer(new NodeFileRender());
        nodeFilesJList.addListSelectionListener(this);
        panelRightContainer.add(new JScrollPane(nodeFilesJList));

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

    @Override
    public void showFilesOnUI(List<NodeFile> ls) {
        ls.sort(Comparator.comparing(NodeFile::getName));
        for (NodeFile file: ls) {
            mFileNamesModel.addElement(file);
        }
    }

    @Override
    public void downloadFailure(String err) {
        JOptionPane.showMessageDialog(vPanelMain,err);
    }

    @Override
    public void downloadSuccessful(int index, NodeFile file) {
        String msg = String.format("%s\n - %s",MSG_DOWNLOAD_SUCCESSFUL, file.getLocalFile().getAbsolutePath());
        JOptionPane.showMessageDialog(vPanelMain,msg);
    }

    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        System.out.println("First index: " + listSelectionEvent.getFirstIndex());
        System.out.println(", Last index: " + listSelectionEvent.getLastIndex());
        boolean adjust = listSelectionEvent.getValueIsAdjusting();
        System.out.println(", Adjusting? " + adjust);
        if (!adjust) {
            JList list = (JList) listSelectionEvent.getSource();
            int selections[] = list.getSelectedIndices();
            Object selectionValues[] = list.getSelectedValues();
            for (int i = 0, n = selections.length; i < n; i++) {
                if (i == 0) {
                    System.out.println(" Selections: ");
                }
                System.out.println(selections[i] + "/" + selectionValues[i] + " ");

                NodeFile selected = (NodeFile) selectionValues[i];
                mController.requestDownload(selections[i], selected);
            }
        }
    }
}
