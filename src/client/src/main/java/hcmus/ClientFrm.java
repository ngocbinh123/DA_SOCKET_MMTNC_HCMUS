package hcmus;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientFrm extends BaseFrm implements IClientContract.View {
    private JPanel panelMain;
    private JButton actStart;
    private ClientController mController;

    static void start() {
        JFrame frame = new JFrame("Client Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new ClientFrm().panelMain);
        frame.pack();
        frame.setVisible(true);
    }

    public ClientFrm() {
        super();
        actStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mController.requestConnectToServer();
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mController = new ClientController();
    }
}
