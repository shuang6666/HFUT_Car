/*
 * Created by JFormDesigner on Mon Dec 27 00:49:47 CST 2021
 */

package PID_Tool;

import Main_Window.Main_Window;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author Milchigs Tom
 */
public class PID_Tool extends JFrame {
    public PID_Tool() {
        initComponents();
    }

    private void EXITMousePressed(MouseEvent e) {
        // TODO add your code here
        this.dispose();
    }

    private void slider1MouseDragged(MouseEvent e) {
        // TODO add your code here
        P_Label.setText(String.valueOf(slider1.getValue()));
    }

    private void slider3MouseDragged(MouseEvent e) {
        // TODO add your code here
        I_Label.setText(String.valueOf(slider3.getValue()));
    }

    private void slider2MouseDragged(MouseEvent e) {
        // TODO add your code here
        D_Label.setText(String.valueOf(slider2.getValue()));
    }

    private void button1MouseClicked(MouseEvent e) {
        // TODO add your code here
        if (Main_Window.Link_Flag) {
            byte[] buff = new byte[]{0x05, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7F};
            buff[1] = (byte) (slider1.getValue() / 128);
            buff[2] = (byte) (slider1.getValue() % 128);
            buff[3] = (byte) (slider3.getValue() / 128);
            buff[4] = (byte) (slider3.getValue() % 128);
            buff[5] = (byte) (slider2.getValue() / 128);
            buff[6] = (byte) (slider2.getValue() % 128);
            try {
                Main_Window.dataOutputStream.write(buff);
                Main_Window.dataOutputStream.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Milchigs Tom
        button1 = new JButton();
        EXIT = new JButton();
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        slider1 = new JSlider();
        slider2 = new JSlider();
        slider3 = new JSlider();
        P_Label = new JLabel();
        D_Label = new JLabel();
        I_Label = new JLabel();

        //======== this ========
        setVisible(true);
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- button1 ----
        button1.setText("SEND");
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                button1MouseClicked(e);
            }
        });
        contentPane.add(button1);
        button1.setBounds(new Rectangle(new Point(130, 130), button1.getPreferredSize()));

        //---- EXIT ----
        EXIT.setText("EXIT");
        EXIT.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EXITMousePressed(e);
            }
        });
        contentPane.add(EXIT);
        EXIT.setBounds(new Rectangle(new Point(240, 130), EXIT.getPreferredSize()));

        //---- label1 ----
        label1.setText("Argument_P:");
        contentPane.add(label1);
        label1.setBounds(new Rectangle(new Point(45, 30), label1.getPreferredSize()));

        //---- label2 ----
        label2.setText("Argument_I:");
        contentPane.add(label2);
        label2.setBounds(new Rectangle(new Point(45, 60), label2.getPreferredSize()));

        //---- label3 ----
        label3.setText("Argument_D:");
        contentPane.add(label3);
        label3.setBounds(new Rectangle(new Point(45, 90), label3.getPreferredSize()));

        //---- slider1 ----
        slider1.setMaximum(3000);
        slider1.setValue(0);
        slider1.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                slider1MouseDragged(e);
            }
        });
        contentPane.add(slider1);
        slider1.setBounds(130, 30, 240, slider1.getPreferredSize().height);

        //---- slider2 ----
        slider2.setMaximum(3000);
        slider2.setValue(0);
        slider2.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                slider2MouseDragged(e);
            }
        });
        contentPane.add(slider2);
        slider2.setBounds(130, 90, 240, 24);

        //---- slider3 ----
        slider3.setMaximum(3000);
        slider3.setValue(0);
        slider3.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                slider3MouseDragged(e);
            }
        });
        contentPane.add(slider3);
        slider3.setBounds(130, 60, 240, 24);

        //---- P_Label ----
        P_Label.setText("0");
        contentPane.add(P_Label);
        P_Label.setBounds(375, 30, 80, P_Label.getPreferredSize().height);

        //---- D_Label ----
        D_Label.setText("0");
        contentPane.add(D_Label);
        D_Label.setBounds(375, 90, 80, D_Label.getPreferredSize().height);

        //---- I_Label ----
        I_Label.setText("0");
        contentPane.add(I_Label);
        I_Label.setBounds(375, 60, 80, I_Label.getPreferredSize().height);

        contentPane.setPreferredSize(new Dimension(460, 185));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Milchigs Tom
    private JButton button1;
    private JButton EXIT;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JSlider slider1;
    private JSlider slider2;
    private JSlider slider3;
    private JLabel P_Label;
    private JLabel D_Label;
    private JLabel I_Label;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
