package hcmus;

import hcmus.data.Client;
import hcmus.data.Node;
import hcmus.data.NodeFile;
import hcmus.views.ClientRender;
import hcmus.views.NodeFileRender;
import hcmus.views.NodeRender;
import hcmus.views.VerticalPanel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class ServerFrm extends BaseFrm implements ISocketServerContract.View {
    private ServerController mController;
    public JPanel vPanelMain;
    private JPanel vPanelHeader;
    private JPanel vPanelBody;
    private VerticalPanel vPanelFooter;
    private JList vFilesList;
    private DefaultListModel<NodeFile> mFilesModel;
    private DefaultListModel<Node> mNodesModel;
    private DefaultListModel<Client> mClientsModel;

    public ServerFrm() {
        super();
        mController = new ServerController();
        mController.attachView(this);
    }

    private JButton vActListen;
    private void createUIComponents() {
        vPanelHeader= new JPanel(new FlowLayout());
        vPanelBody = new JPanel(new GridLayout(0,2));
        vPanelBody.setBorder(new EmptyBorder(10, 10, 10, 10));
        vPanelFooter = new VerticalPanel();
        vPanelFooter.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblServerIPTitle = new JLabel("Server IP: ");
        vPanelHeader.add(lblServerIPTitle);

        JLabel lblServerIP = new JLabel(getRemoteIP());
        lblServerIP.setForeground(Color.RED);
        vPanelHeader.add(lblServerIP);

        vActListen = new JButton("Start Listen");
        vActListen.addActionListener(e -> {
            if (vActListen.getText().contains("Start")) {
                vActListen.setText("Stop");
                new Thread(() -> mController.startListenConnections()).start();
            }else {
                vActListen.setText("Start Listen");
                mClientsModel.clear();
                mNodesModel.clear();
                mFilesModel.clear();
                new Thread(() -> mController.stopListen()).start();
            }
        });
        vPanelHeader.add(vActListen);

        JLabel lblNodeFiles = new JLabel("Node files:");
        lblNodeFiles.setForeground(Color.BLUE);
        vPanelFooter.add(lblNodeFiles);
        VerticalPanel panelLeftContainer = new VerticalPanel();
        VerticalPanel panelRightContainer = new VerticalPanel();

        mClientsModel = new DefaultListModel<>();
        JLabel lblClient = new JLabel("Clients:");
        lblClient.setForeground(Color.BLUE);
        panelLeftContainer.add(lblClient);
        panelLeftContainer.setBorder(new EmptyBorder(0, 0, 0, 5));
        JLabel lblNodes = new JLabel("Nodes:");
        lblNodes.setForeground(Color.BLUE);
        panelRightContainer.add(lblNodes);
        panelRightContainer.setBorder(new EmptyBorder(0, 5, 0, 0));


        JList<Client> clientJList = new JList<>();
        clientJList.setCellRenderer(new ClientRender());
        clientJList.setModel(mClientsModel);

        JScrollPane clientScrollPanel = new JScrollPane(clientJList);
        panelLeftContainer.add(clientScrollPanel);

        mNodesModel = new DefaultListModel<>();
        JList<Node> nodeJList = new JList<>();
        nodeJList.setCellRenderer(new NodeRender());
        nodeJList.setModel(mNodesModel);
        JScrollPane nodeScrollPanel = new JScrollPane(nodeJList);
        panelRightContainer.add(nodeScrollPanel);

        vPanelBody.add(panelLeftContainer);
        vPanelBody.add(panelRightContainer);

        mFilesModel = new DefaultListModel<>();
        vFilesList = new JList(mFilesModel);
        vFilesList.setCellRenderer(new NodeFileRender());
        JScrollPane scrollPane = new JScrollPane(vFilesList);
        vPanelFooter.add(scrollPane);
        vPanelMain = new JPanel(new GridLayout(3,1));

        vPanelMain.add(vPanelBody);
        vPanelMain.add(vPanelFooter);
        vPanelMain.add(vPanelHeader);
    }

    @Override
    public void showNodeOnUI(final Node node) {
        EventQueue.invokeLater(() -> {
            ArrayList<Node> nodes = new ArrayList<>();
            nodes.add(node);
            ArrayList<String> nodeNames = new ArrayList<>();
            nodeNames.add(node.getName());
            for (int i = 0; i < mNodesModel.size(); i++) {
                Node item = mNodesModel.get(i);
                if (!nodeNames.contains(item.getName())) {
                    nodeNames.add(item.getName());
                    nodes.add(item);
                }
            }

            nodes.sort(Comparator.comparing(Node::getName));
            mNodesModel.clear();
            for (Node item: nodes) {
                mNodesModel.addElement(item);
            }
        });
    }

    @Override
    public void showClientOnUI(final Client client) {
        EventQueue.invokeLater(() -> {
            ArrayList<Client> clients = new ArrayList<>();
            clients.add(client);
            ArrayList<String> clientNames = new ArrayList<>();
            clientNames.add(client.getName());
            for (int i = 0; i < mClientsModel.size(); i++) {
                Client item = mClientsModel.get(i);
                if (!clientNames.contains(item.getName())) {
                    clientNames.add(item.getName());
                    clients.add(item);
                }
            }

            clients.sort(Comparator.comparing(Client::getName));
            mClientsModel.clear();
            for (Client item : clients) {
                mClientsModel.addElement(item);
            }
        });
    }

    @Override
    public void showFileOnUI(final NodeFile file) {
        EventQueue.invokeLater(() -> mFilesModel.addElement(file));
    }

    private ArrayList<NodeFile> fileList = new ArrayList<>();
    @Override
    public void showFilesOnUI(ArrayList<NodeFile> files) {
        fileList.addAll(files);
        loadFileOnListView();
    }

    private void loadFileOnListView() {
        fileList.sort(Comparator.comparing(NodeFile::getName));
        mFilesModel.clear();
        for (NodeFile file: fileList) {
            mFilesModel.addElement(file);
        }
    }

    private void removeFiles(int nodeId) {
        for (int i =  fileList.size() - 1; i >= 0; i--) {
            if (fileList.get(i).getNodeId() == nodeId) {
                fileList.remove(i);
            }
        }
        loadFileOnListView();
    }

    @Override
    public void closeNode(Node node) {
        for (int i =0; i < mNodesModel.size(); i++) {
            Node item = mNodesModel.get(i);
            if (node.getId() == item.getId()) {
                mNodesModel.remove(i);
                removeFiles(node.getId());
                break;
            }
        }
    }

    @Override
    public void closeClient(Client client) {
        for (int i = 0; i < mClientsModel.size(); i++) {
            Client item = mClientsModel.get(i);
            if (client.getId() == item.getId()) {
                mClientsModel.remove(i);
            }
        }
    }
}
