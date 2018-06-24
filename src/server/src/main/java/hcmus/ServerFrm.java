package hcmus;

import hcmus.base.BaseFrm;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerFrm extends BaseFrm implements IServerContract.View {
    private ServerController mController;
    private JPanel panelMain;
    private JButton actStart;

    static void start() {
        JFrame frame = new JFrame("Server Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new ServerFrm().panelMain);
        frame.pack();
        frame.setVisible(true);
    }

    public ServerFrm() {
        super();
        actStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mController.startListenConnections();
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mController = new ServerController();
        mController.attachView(this);
    }
}
