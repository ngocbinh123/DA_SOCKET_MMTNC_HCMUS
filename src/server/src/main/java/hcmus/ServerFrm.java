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
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                mNodesModel.addElement(node);
            }
        });
    }

    @Override
    public void showClientOnUI(final Client client) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                mClientsModel.addElement(client);
            }
        });
    }

    @Override
    public void showFileOnUI(final NodeFile file) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                mFilesModel.addElement(file);
            }
        });
    }

    private void removeFiles(int nodeId) {
        for (int i =  mFilesModel.size() - 1; i >= 0; i--) {
            if (mFilesModel.get(i).getNodeId() == nodeId) {
                mFilesModel.remove(i);
            }
        }
    }

    @Override
    public void closeNode(Node node) {
        for (int i =0; i < mNodesModel.size(); i++) {
            if (node.getId() == node.getId()) {
                mNodesModel.remove(i);
                removeFiles(node.getId());
                break;
            }
        }
    }

    @Override
    public void closeClient(Client client) {
    }
}
