package gui;

import javax.swing.*;
import java.awt.*;

public class EndingFrame extends JFrame {

    public EndingFrame() {

        setTitle("Goodbye");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        ImageIcon gif = new ImageIcon(
                getClass().getResource("/images/end.gif")
        );

        JLabel label = new JLabel(gif);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        add(label, BorderLayout.CENTER);

        Timer timer = new Timer(10000, e -> {
            System.exit(0);
        });

        timer.setRepeats(false);
        timer.start();
    }
}