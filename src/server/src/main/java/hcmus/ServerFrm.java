package hcmus;

import hcmus.data.Client;
import hcmus.data.Node;
import hcmus.data.NodeFile;
import hcmus.views.ClientRender;
import hcmus.views.NodeFileRender;
import hcmus.views.NodeRender;
import hcmus.views.VerticalPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class ServerFrm extends BaseFrm implements ISocketServerContract.View {
    private ServerController mController;
    public JPanel vPanelMain;
    private JPanel vPanelBody;
    private JPanel vPanelFooter;
    private JList vFilesList;
    private DefaultListModel<NodeFile> mFilesModel;
    private DefaultListModel<Node> mNodesModel;
    private DefaultListModel<Client> mClientsModel;

    public ServerFrm() {
        super();
        mController = new ServerController();
        mController.attachView(this);
    }

    public void startListenConnections() {
        mController.startListenConnections();
    }


    private void createUIComponents() {
        vPanelBody = new JPanel(new GridLayout(0,2));
        vPanelFooter = new JPanel(new GridLayout(0,1));

        VerticalPanel panelLeftContainer = new VerticalPanel();
        VerticalPanel panelRightContainer = new VerticalPanel();

        mClientsModel = new DefaultListModel<>();
        panelLeftContainer.add(new JLabel("Clients:"));
        panelRightContainer.add(new JLabel("Nodes:"));

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

        vPanelMain = new JPanel(new GridLayout(2,1));
        vPanelMain.add(vPanelBody);
        vPanelMain.add(vPanelFooter);
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
        EventQueue.invokeLater(() -> {
            mFilesModel.addElement(file);
        });
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
