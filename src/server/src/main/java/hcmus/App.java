package hcmus;

import javax.swing.*;
import java.awt.*;

/**
 * Hello world!
 *
 */
@SuppressWarnings("Duplicates")
public class App
{
    public static void main( String[] args ) {
        System.out.println("Start Server");
        final ServerFrm serverFrm = new ServerFrm();
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Server Form");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setContentPane(serverFrm.vPanelMain);
            frame.setUndecorated(true);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
