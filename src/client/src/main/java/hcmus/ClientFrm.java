package hcmus;

import hcmus.data.Client;
import hcmus.data.NodeFile;
import hcmus.views.NodeFileRender;
import hcmus.views.VerticalPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
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
    private JLabel vLblClientName;
    private JLabel vLblLocalPort;
    private JLabel vLblFileSize;
    private void createUIComponents() {
        vPanelMain = new JPanel(new BorderLayout());
        vPanelBody = new JPanel(new GridLayout(0,2));
        vPanelBody.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelLeftContainer = new VerticalPanel();
        panelRightContainer = new VerticalPanel();

        vLblStatus = new JLabel("Status: ");
        vLblClientName = new JLabel("Name: ");
        vLblLocalPort = new JLabel("Local port: ");
        vLblFileSize = new JLabel("Files: ");

        JLabel lblInfo = new JLabel("Client Information:");
        Font labelFont = lblInfo.getFont();
        lblInfo.setForeground(Color.BLUE);
        lblInfo.setFont(new Font(labelFont.getName(), Font.BOLD, 14));
        panelLeftContainer.add(lblInfo);

        panelLeftContainer.add(vLblStatus);
        panelLeftContainer.add(vLblClientName);
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
                clearInfo();
                mController.disconnect();
            }else {
                mController.requestConnectToServer();
            }
        });

        vPanelMain.add(vPanelBody, BorderLayout.CENTER);
        vPanelMain.add(vActConnect, BorderLayout.SOUTH);
    }

    @Override
    public void updateDataOnUI(Client client) {
        vLblStatus.setText("Status: " + SOCKET_STATUS.CONNECT.getValue());
        vActConnect.setText(SOCKET_STATUS.DISCONNECT.getValue());
        vLblClientName.setText("Name: " + client.getName());
        vLblLocalPort.setText("Local port: " + String.valueOf(client.getLocalPort()));
    }

    private ArrayList<NodeFile> fileList = new ArrayList<>();
    @Override
    public void showFilesOnUI(List<NodeFile> ls) {
        EventQueue.invokeLater(() -> {
            System.out.println(String.format("ls: %d", ls.size()));
            fileList.addAll(ls);
            loadFileOnListView();
        });
    }

    public void loadFileOnListView() {
        fileList.sort(Comparator.comparing(NodeFile::getName));
        System.out.println(String.format("ls: %d", fileList.size()));
        mFileNamesModel.clear();
        for (NodeFile file: fileList) {
            mFileNamesModel.addElement(file);
        }
    }

    @Override
    public void removeFilesByNodeId(String nodeId) {
        for (int i = fileList.size() - 1; i >= 0;i--) {
            NodeFile item = fileList.get(i);
            if (item.getNodeId().contains(nodeId)) {
                fileList.remove(i);
            }
        }
        loadFileOnListView();
    }

    @Override
    public void downloadFailure(String err) {
        JOptionPane.showMessageDialog(vPanelMain,err);
    }

    @Override
    public void downloadSuccessful(int index, NodeFile file) {
        String msg = MSG_DOWNLOAD_SUCCESSFUL;
        if (file != null) {
            msg = String.format("%s\n - %s",MSG_DOWNLOAD_SUCCESSFUL, file.getLocalFile().getAbsolutePath());
        }
        JOptionPane.showMessageDialog(vPanelMain,msg);
    }

    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        boolean adjust = listSelectionEvent.getValueIsAdjusting();
        if (!adjust) {
            JList list = (JList) listSelectionEvent.getSource();
            int selections[] = list.getSelectedIndices();
            Object selectionValues[] = list.getSelectedValues();
            for (int i = 0, n = selections.length; i < n; i++) {
                NodeFile selected = (NodeFile) selectionValues[i];
                chooseDir(selections[i], selected);
            }
        }
    }

    private File storedDir = null;
    private void chooseDir(int position, NodeFile nodeFile) {
        // parent component of the dialog
        JFrame parentFrame = new JFrame();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save as");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        String path = String.format("%s/%s", storedDir == null ? "": storedDir.getAbsolutePath(), nodeFile.getName());
        fileChooser.setSelectedFile(new File(path));
        fileChooser.setSelectedFile(new File(path));
        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            storedDir = fileToSave.getParentFile();
//            mController.requestDownload(position, nodeFile, fileToSave.getAbsolutePath());
            mController.requestNodeSendFileByUDPReliable(position, nodeFile, fileToSave.getAbsolutePath());
        }
    }

    private void clearInfo() {
        vLblClientName.setText("Name:");
        vLblStatus.setText("Status:");
        vActConnect.setText("Connect To Server");
        vLblLocalPort.setText("Local port:");
        mFileNamesModel.clear();
        fileList.clear();
    }
}
