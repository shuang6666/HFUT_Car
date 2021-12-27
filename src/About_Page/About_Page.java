/*
 * Created by JFormDesigner on Sun Dec 26 22:01:19 CST 2021
 */

package About_Page;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author Milchigs Tom
 */
public class About_Page extends JFrame {
    public About_Page() {
        initComponents();
    }

    private void button1MouseClicked(MouseEvent e) {
        // TODO add your code here
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Milchigs Tom
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        button1 = new JButton();
        label5 = new JLabel();

        //======== this ========
        setVisible(true);
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- label1 ----
        label1.setText("Build Platform: MacOS 12.0.1");
        contentPane.add(label1);
        label1.setBounds(new Rectangle(new Point(30, 140), label1.getPreferredSize()));

        //---- label2 ----
        label2.setText("LOGO We Do Not Have Logo Now.");
        contentPane.add(label2);
        label2.setBounds(new Rectangle(new Point(85, 55), label2.getPreferredSize()));

        //---- label3 ----
        label3.setText("Java Edition: Zulu JDK 1.8");
        contentPane.add(label3);
        label3.setBounds(new Rectangle(new Point(30, 160), label3.getPreferredSize()));

        //---- label4 ----
        label4.setText("Architecture: Apple M1 ARM-Arch64");
        contentPane.add(label4);
        label4.setBounds(new Rectangle(new Point(30, 180), label4.getPreferredSize()));

        //---- button1 ----
        button1.setText("OK");
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                button1MouseClicked(e);
            }
        });
        contentPane.add(button1);
        button1.setBounds(new Rectangle(new Point(260, 210), button1.getPreferredSize()));

        //---- label5 ----
        label5.setText("Version: 0.0.1");
        contentPane.add(label5);
        label5.setBounds(new Rectangle(new Point(30, 120), label5.getPreferredSize()));

        contentPane.setPreferredSize(new Dimension(355, 265));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Milchigs Tom
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JButton button1;
    private JLabel label5;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
