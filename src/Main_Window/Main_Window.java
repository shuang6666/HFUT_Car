/*
 * Created by JFormDesigner on Sat Dec 25 18:58:38 CST 2021
 */

package Main_Window;

import About_Page.About_Page;
import Car_Util.HexProcess;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.*;

import Car_Util.Test;
import HFUT_Spider.HFUT_Spider;
import PID_Tool.PID_Tool;
import TCP_Client.TCP_Client;
import TCP_Server.TCP_Server;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.chart.*;

import javax.swing.border.*;

/**
 * @author Milchigs Tom
 */
public class Main_Window extends JFrame {

    public static DataInputStream dataInputStream;
    public static DataOutputStream dataOutputStream;
    public static Socket socket;
    public static boolean Motor_Select = true;
    public static boolean Link_Flag = false;
    public static boolean KeyPress_Flag = false;


    public Main_Window() {
        initComponents();
    }

    private void TCP_Link_ButtonMouseClicked(MouseEvent e) {
        // TODO add your code here
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!Link_Flag) {
                    try {
                        socket = new Socket(IP_TextField.getText(), Integer.parseInt(Port_TextField.getText()));
                    } catch (IOException ex) {
                        Log_Pane.setText(Log_Pane.getText() + "套接字错误，程序若有问题请联系作者田亦爽\n");
                        return;
                    }

                    try {
                        dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        dataInputStream = new DataInputStream(socket.getInputStream());
                    } catch (IOException ex) {
                        Log_Pane.setText(Log_Pane.getText() + "获取输入输出流错误，程序若有问题请联系作者田亦爽\n");
                        return;
                    }

                    Link_State.setEnabled(true);
                    Link_State.doClick();
                    Link_State.setText("LINK");

                    TCP_Link_Button.setText("UNLINK");

                    Link_Flag = true;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Byte[] temp = new Byte[8];
                            while (true) {
                                try {
                                    for (int i = 0; i < 8; i++) {
                                        temp[i] = dataInputStream.readByte();
                                    }
                                    if (Motor_Select) {
                                        Test.timeSeries.addOrUpdate(new Millisecond(), (temp[1].intValue() + temp[0].intValue() * 128));
                                    } else {
                                        Test.timeSeries.addOrUpdate(new Millisecond(), (temp[3].intValue() + temp[2].intValue() * 128));
                                    }
                                    Speed_TextField.setText("Speed_A:" + (temp[1].intValue() + temp[0].intValue() * 128) +
                                            "    Speed_B:" + (temp[3].intValue() + temp[2].intValue() * 128));
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }).start();
                } else {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        Log_Pane.setText(Log_Pane.getText() + "套接字关闭错误，程序若有问题请联系作者田亦爽\n");
                        return;
                    }

                    dataInputStream = null;
                    dataOutputStream = null;
                    socket = null;
                    Link_Flag = false;
                    TCP_Link_Button.setText("LINK");

                    Link_State.setEnabled(false);
                    Link_State.setText("UNLINK");
                }
            }
        }).start();
    }


    private void radioButton1KeyPressed(KeyEvent e) {
        // TODO add your code here
        if (Link_Flag && !KeyPress_Flag) {
            byte[] buff = new byte[]{0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7F};
            buff[1] = (byte) (Speed_Slider.getValue() / 128);
            buff[2] = (byte) (Speed_Slider.getValue() % 128);
            buff[3] = (byte) (Speed_Slider.getValue() / 128);
            buff[4] = (byte) (Speed_Slider.getValue() % 128);
            switch (e.getKeyChar()) {
                case 'w':
                    buff[0] = 0x01;
                    try {
                        dataOutputStream.write(buff);
                        dataOutputStream.flush();
                    } catch (IOException ex) {
                        Log_Pane.setText(Log_Pane.getText() + "TCP发送错误，程序若有问题请联系作者田亦爽\n");
                        return;
                    }
                    break;
                case 'a':
                    buff[0] = 0x02;
                    try {
                        dataOutputStream.write(buff);
                        dataOutputStream.flush();
                    } catch (IOException ex) {
                        Log_Pane.setText(Log_Pane.getText() + "TCP发送错误，程序若有问题请联系作者田亦爽\n");
                        return;
                    }
                    break;
                case 'd':
                    buff[0] = 0x03;
                    try {
                        dataOutputStream.write(buff);
                        dataOutputStream.flush();
                    } catch (IOException ex) {
                        Log_Pane.setText(Log_Pane.getText() + "TCP发送错误，程序若有问题请联系作者田亦爽\n");
                        return;
                    }
                    break;
                case 's':
                    buff[0] = 0x04;
                    try {
                        dataOutputStream.write(buff);
                        dataOutputStream.flush();
                    } catch (IOException ex) {
                        Log_Pane.setText(Log_Pane.getText() + "TCP发送错误，程序若有问题请联系作者田亦爽\n");
                        return;
                    }
                    break;
            }
            KeyPress_Flag = true;
        }
    }

    private void radioButton1KeyReleased(KeyEvent e) {
        // TODO add your code here
        KeyPress_Flag = false;
        if (Link_Flag) {
            switch (e.getKeyChar()) {
                case 'w':
                case 'a':
                case 's':
                case 'd':
                    byte[] buff = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7F};
                    try {
                        dataOutputStream.write(buff);
                        dataOutputStream.flush();
                    } catch (IOException ex) {
                    }
                    break;
            }
        }
    }

    private void Fore_ButtonMousePressed(MouseEvent e) {
        // TODO add your code here
        if (Link_Flag) {
            byte[] buff = new byte[]{0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7F};
            buff[1] = (byte) (Speed_Slider.getValue() / 128);
            buff[2] = (byte) (Speed_Slider.getValue() % 128);
            buff[3] = (byte) (Speed_Slider.getValue() / 128);
            buff[4] = (byte) (Speed_Slider.getValue() % 128);
            try {
                dataOutputStream.write(buff);
                dataOutputStream.flush();
            } catch (IOException ex) {
                Log_Pane.setText(Log_Pane.getText() + "TCP发送错误，程序若有问题请联系作者田亦爽\n");
                return;
            }
        }
    }

    private void Fore_ButtonMouseReleased(MouseEvent e) {
        // TODO add your code here
        if (Link_Flag) {
            byte[] buff = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7F};
            try {
                dataOutputStream.write(buff);
                dataOutputStream.flush();
            } catch (IOException ex) {
                Log_Pane.setText(Log_Pane.getText() + "TCP发送错误，程序若有问题请联系作者田亦爽\n");
                return;
            }
        }
    }

    private void RIGHT_ButtonMousePressed(MouseEvent e) {
        // TODO add your code here
        if (Link_Flag) {
            byte[] buff = new byte[]{0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7F};
            buff[1] = (byte) (Speed_Slider.getValue() / 128);
            buff[2] = (byte) (Speed_Slider.getValue() % 128);
            buff[3] = (byte) (Speed_Slider.getValue() / 128);
            buff[4] = (byte) (Speed_Slider.getValue() % 128);
            try {
                dataOutputStream.write(buff);
                dataOutputStream.flush();
            } catch (IOException ex) {
                Log_Pane.setText(Log_Pane.getText() + "TCP发送错误，程序若有问题请联系作者田亦爽\n");
                return;
            }
        }
    }

    private void RIGHT_ButtonMouseReleased(MouseEvent e) {
        // TODO add your code here
        if (Link_Flag) {
            byte[] buff = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7F};
            try {
                dataOutputStream.write(buff);
                dataOutputStream.flush();
            } catch (IOException ex) {
                Log_Pane.setText(Log_Pane.getText() + "TCP发送错误，程序若有问题请联系作者田亦爽\n");
                return;
            }
        }
    }

    private void DOWN_ButtonMousePressed(MouseEvent e) {
        // TODO add your code here
        if (Link_Flag) {
            byte[] buff = new byte[]{0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7F};
            buff[1] = (byte) (Speed_Slider.getValue() / 128);
            buff[2] = (byte) (Speed_Slider.getValue() % 128);
            buff[3] = (byte) (Speed_Slider.getValue() / 128);
            buff[4] = (byte) (Speed_Slider.getValue() % 128);
            try {
                dataOutputStream.write(buff);
                dataOutputStream.flush();
            } catch (IOException ex) {
                Log_Pane.setText(Log_Pane.getText() + "TCP发送错误，程序若有问题请联系作者田亦爽\n");
                return;
            }
        }
    }

    private void DOWN_ButtonMouseReleased(MouseEvent e) {
        // TODO add your code here
        if (Link_Flag) {
            byte[] buff = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7F};
            try {
                dataOutputStream.write(buff);
                dataOutputStream.flush();
            } catch (IOException ex) {
                Log_Pane.setText(Log_Pane.getText() + "TCP发送错误，程序若有问题请联系作者田亦爽\n");
                return;
            }
        }
    }

    private void LEFT_ButtonMousePressed(MouseEvent e) {
        // TODO add your code here
        if (Link_Flag) {
            byte[] buff = new byte[]{0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7F};
            buff[1] = (byte) (Speed_Slider.getValue() / 128);
            buff[2] = (byte) (Speed_Slider.getValue() % 128);
            buff[3] = (byte) (Speed_Slider.getValue() / 128);
            buff[4] = (byte) (Speed_Slider.getValue() % 128);
            try {
                dataOutputStream.write(buff);
                dataOutputStream.flush();
            } catch (IOException ex) {
                Log_Pane.setText(Log_Pane.getText() + "TCP发送错误，程序若有问题请联系作者田亦爽\n");
                return;
            }
        }
    }

    private void LEFT_ButtonMouseReleased(MouseEvent e) {
        // TODO add your code here
        if (Link_Flag) {
            byte[] buff = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7F};
            try {
                dataOutputStream.write(buff);
                dataOutputStream.flush();
            } catch (IOException ex) {
                Log_Pane.setText(Log_Pane.getText() + "TCP发送错误，程序若有问题请联系作者田亦爽\n");
                return;
            }
        }
    }

    private void AboutMousePressed(MouseEvent e) {
        // TODO add your code here
        About_Page about_page = new About_Page();
    }

    private void menuItem3MousePressed(MouseEvent e) {
        // TODO add your code here
        TCP_Client tcp_client = new TCP_Client();
    }

    private void menuItem4MousePressed(MouseEvent e) {
        // TODO add your code here
        TCP_Server tcp_server = new TCP_Server();
    }

    private void menuItem20MousePressed(MouseEvent e) {
        // TODO add your code here
        HFUT_Spider hfut_spider = new HFUT_Spider();
    }

    private void menuItem24MousePressed(MouseEvent e) {
        // TODO add your code here
        try {
            URI uri = new URI("http://www.milchigs.natapp1.cc");
            Desktop.getDesktop().browse(uri);
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    private void menuItem32MousePressed(MouseEvent e) {
        // TODO add your code here
        PID_Tool pid_tool = new PID_Tool();
    }

    private void Motor_SwitchMouseClicked(MouseEvent e) {
        // TODO add your code here
        Motor_Select = !Motor_Select;
    }

    private void Speed_SliderMouseDragged(MouseEvent e) {
        // TODO add your code here
        Speed_Num.setText(String.valueOf(Speed_Slider.getValue()));
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Milchigs Tom
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        About = new JMenuItem();
        menuItem11 = new JMenuItem();
        Github = new JMenuItem();
        menuItem1 = new JMenuItem();
        menu2 = new JMenu();
        menu6 = new JMenu();
        menuItem14 = new JMenuItem();
        menuItem13 = new JMenuItem();
        menu7 = new JMenu();
        menuItem15 = new JMenuItem();
        menuItem16 = new JMenuItem();
        menu8 = new JMenu();
        menuItem17 = new JMenuItem();
        menuItem18 = new JMenuItem();
        menu3 = new JMenu();
        menuItem2 = new JMenuItem();
        menuItem23 = new JMenuItem();
        menuItem32 = new JMenuItem();
        menuItem3 = new JMenuItem();
        menuItem4 = new JMenuItem();
        menuItem21 = new JMenuItem();
        menuItem22 = new JMenuItem();
        menuItem5 = new JMenuItem();
        menuItem19 = new JMenuItem();
        menuItem20 = new JMenuItem();
        menu4 = new JMenu();
        menuItem6 = new JMenuItem();
        menuItem7 = new JMenuItem();
        menuItem8 = new JMenuItem();
        menuItem9 = new JMenuItem();
        menu5 = new JMenu();
        menuItem10 = new JMenuItem();
        menu9 = new JMenu();
        menuItem24 = new JMenuItem();
        menuItem25 = new JMenuItem();
        menuItem26 = new JMenuItem();
        menuItem27 = new JMenuItem();
        menu10 = new JMenu();
        menuItem12 = new JMenuItem();
        menuItem28 = new JMenuItem();
        menuItem29 = new JMenuItem();
        menuItem31 = new JMenuItem();
        menu11 = new JMenu();
        menuItem30 = new JMenuItem();
        TCP_Link_Button = new JButton();
        Speed_Slider = new JSlider();
        IP_TextField = new JTextField();
        Port_TextField = new JTextField();
        Link_State = new JRadioButton();
        DrawArea = new JPanel();
        test1 = new Test();
        Fore_Button = new JButton();
        LEFT_Button = new JButton();
        RIGHT_Button = new JButton();
        DOWN_Button = new JButton();
        scrollPane1 = new JScrollPane();
        Log_Pane = new JTextPane();
        Speed_Label = new JLabel();
        Author = new JLabel();
        radioButton1 = new JRadioButton();
        Speed_TextField = new JTextField();
        Motor_Switch = new JToggleButton();
        Speed_Num = new JLabel();

        //======== this ========
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== menuBar1 ========
        {

            //======== menu1 ========
            {
                menu1.setText("Milchigs");

                //---- About ----
                About.setText("About");
                About.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        AboutMousePressed(e);
                    }
                });
                menu1.add(About);

                //---- menuItem11 ----
                menuItem11.setText("Author");
                menu1.add(menuItem11);

                //---- Github ----
                Github.setText("Github");
                menu1.add(Github);

                //---- menuItem1 ----
                menuItem1.setText("Thanks");
                menu1.add(menuItem1);
            }
            menuBar1.add(menu1);

            //======== menu2 ========
            {
                menu2.setText("Preference");

                //======== menu6 ========
                {
                    menu6.setText("TCP Link");

                    //---- menuItem14 ----
                    menuItem14.setText("LINK");
                    menu6.add(menuItem14);

                    //---- menuItem13 ----
                    menuItem13.setText("UNLINK");
                    menu6.add(menuItem13);
                }
                menu2.add(menu6);

                //======== menu7 ========
                {
                    menu7.setText("Motor Select");

                    //---- menuItem15 ----
                    menuItem15.setText("Motor A");
                    menu7.add(menuItem15);

                    //---- menuItem16 ----
                    menuItem16.setText("Motor B");
                    menu7.add(menuItem16);
                }
                menu2.add(menu7);

                //======== menu8 ========
                {
                    menu8.setText("Speed Setup");

                    //---- menuItem17 ----
                    menuItem17.setText("Speed UP");
                    menu8.add(menuItem17);

                    //---- menuItem18 ----
                    menuItem18.setText("Speed DOWN");
                    menu8.add(menuItem18);
                }
                menu2.add(menu8);
            }
            menuBar1.add(menu2);

            //======== menu3 ========
            {
                menu3.setText("Tools");

                //---- menuItem2 ----
                menuItem2.setText("System State");
                menu3.add(menuItem2);

                //---- menuItem23 ----
                menuItem23.setText("Thread Pool");
                menu3.add(menuItem23);

                //---- menuItem32 ----
                menuItem32.setText("PID Tool");
                menuItem32.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        menuItem32MousePressed(e);
                    }
                });
                menu3.add(menuItem32);

                //---- menuItem3 ----
                menuItem3.setText("TCP Client");
                menuItem3.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        menuItem3MousePressed(e);
                    }
                });
                menu3.add(menuItem3);

                //---- menuItem4 ----
                menuItem4.setText("TCP Server");
                menuItem4.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        menuItem4MousePressed(e);
                    }
                });
                menu3.add(menuItem4);

                //---- menuItem21 ----
                menuItem21.setText("UDP Client");
                menu3.add(menuItem21);

                //---- menuItem22 ----
                menuItem22.setText("UDP Server");
                menu3.add(menuItem22);

                //---- menuItem5 ----
                menuItem5.setText("Motion Processer");
                menu3.add(menuItem5);

                //---- menuItem19 ----
                menuItem19.setText("Keyboard Control");
                menu3.add(menuItem19);

                //---- menuItem20 ----
                menuItem20.setText("HFUT Spider(Credit cookie QAQ)");
                menuItem20.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        menuItem20MousePressed(e);
                    }
                });
                menu3.add(menuItem20);
            }
            menuBar1.add(menu3);

            //======== menu4 ========
            {
                menu4.setText("Help");

                //---- menuItem6 ----
                menuItem6.setText("How to use");
                menu4.add(menuItem6);

                //---- menuItem7 ----
                menuItem7.setText("Document");
                menu4.add(menuItem7);

                //---- menuItem8 ----
                menuItem8.setText("Website");
                menu4.add(menuItem8);

                //---- menuItem9 ----
                menuItem9.setText("Wiki");
                menu4.add(menuItem9);
            }
            menuBar1.add(menu4);

            //======== menu5 ========
            {
                menu5.setText("SLAM");

                //---- menuItem10 ----
                menuItem10.setText("Under development...");
                menu5.add(menuItem10);
            }
            menuBar1.add(menu5);

            //======== menu9 ========
            {
                menu9.setText("Hardware");

                //---- menuItem24 ----
                menuItem24.setText("PCB Layout");
                menuItem24.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        menuItem24MousePressed(e);
                    }
                });
                menu9.add(menuItem24);

                //---- menuItem25 ----
                menuItem25.setText("MCU Layout");
                menu9.add(menuItem25);

                //---- menuItem26 ----
                menuItem26.setText("Hardware Document");
                menu9.add(menuItem26);

                //---- menuItem27 ----
                menuItem27.setText("Bom Table");
                menu9.add(menuItem27);
            }
            menuBar1.add(menu9);

            //======== menu10 ========
            {
                menu10.setText("Document");

                //---- menuItem12 ----
                menuItem12.setText("Begin Document");
                menu10.add(menuItem12);

                //---- menuItem28 ----
                menuItem28.setText("Hardware Document");
                menu10.add(menuItem28);

                //---- menuItem29 ----
                menuItem29.setText("Software Document");
                menu10.add(menuItem29);

                //---- menuItem31 ----
                menuItem31.setText("Make Follow Document");
                menu10.add(menuItem31);

                //======== menu11 ========
                {
                    menu11.setText("Module Document");
                }
                menu10.add(menu11);

                //---- menuItem30 ----
                menuItem30.setText("Use Guide");
                menu10.add(menuItem30);
            }
            menuBar1.add(menu10);
        }
        setJMenuBar(menuBar1);

        //---- TCP_Link_Button ----
        TCP_Link_Button.setText("Link");
        TCP_Link_Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TCP_Link_ButtonMouseClicked(e);
            }
        });
        contentPane.add(TCP_Link_Button);
        TCP_Link_Button.setBounds(new Rectangle(new Point(725, 535), TCP_Link_Button.getPreferredSize()));

        //---- Speed_Slider ----
        Speed_Slider.setMaximum(400);
        Speed_Slider.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Speed_SliderMouseDragged(e);
            }
        });
        contentPane.add(Speed_Slider);
        Speed_Slider.setBounds(430, 540, 265, 24);

        //---- IP_TextField ----
        IP_TextField.setText("192.168.0.1");
        IP_TextField.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(IP_TextField);
        IP_TextField.setBounds(815, 535, 160, IP_TextField.getPreferredSize().height);

        //---- Port_TextField ----
        Port_TextField.setText("8888");
        Port_TextField.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(Port_TextField);
        Port_TextField.setBounds(985, 535, 76, Port_TextField.getPreferredSize().height);

        //---- Link_State ----
        Link_State.setText("UNLINK");
        Link_State.setFocusable(false);
        contentPane.add(Link_State);
        Link_State.setBounds(new Rectangle(new Point(1070, 540), Link_State.getPreferredSize()));

        //======== DrawArea ========
        {
            DrawArea.setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing. border
            .EmptyBorder ( 0, 0 ,0 , 0) ,  "JF\u006frmDes\u0069gner \u0045valua\u0074ion" , javax. swing .border . TitledBorder. CENTER ,javax
            . swing. border .TitledBorder . BOTTOM, new java. awt .Font ( "D\u0069alog", java .awt . Font. BOLD ,
            12 ) ,java . awt. Color .red ) ,DrawArea. getBorder () ) ); DrawArea. addPropertyChangeListener( new java. beans
            .PropertyChangeListener ( ){ @Override public void propertyChange (java . beans. PropertyChangeEvent e) { if( "\u0062order" .equals ( e.
            getPropertyName () ) )throw new RuntimeException( ) ;} } );
            DrawArea.setLayout(new BorderLayout());
            DrawArea.add(test1, BorderLayout.CENTER);
        }
        contentPane.add(DrawArea);
        DrawArea.setBounds(425, 25, 710, 495);

        //---- Fore_Button ----
        Fore_Button.setText("UP");
        Fore_Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Fore_ButtonMousePressed(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                Fore_ButtonMouseReleased(e);
            }
        });
        contentPane.add(Fore_Button);
        Fore_Button.setBounds(new Rectangle(new Point(170, 485), Fore_Button.getPreferredSize()));

        //---- LEFT_Button ----
        LEFT_Button.setText("LEFT");
        LEFT_Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                LEFT_ButtonMousePressed(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                LEFT_ButtonMouseReleased(e);
            }
        });
        contentPane.add(LEFT_Button);
        LEFT_Button.setBounds(new Rectangle(new Point(60, 535), LEFT_Button.getPreferredSize()));

        //---- RIGHT_Button ----
        RIGHT_Button.setText("RIGHT");
        RIGHT_Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                RIGHT_ButtonMousePressed(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                RIGHT_ButtonMouseReleased(e);
            }
        });
        contentPane.add(RIGHT_Button);
        RIGHT_Button.setBounds(new Rectangle(new Point(280, 535), RIGHT_Button.getPreferredSize()));

        //---- DOWN_Button ----
        DOWN_Button.setText("DOWN");
        DOWN_Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                DOWN_ButtonMousePressed(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                DOWN_ButtonMouseReleased(e);
            }
        });
        contentPane.add(DOWN_Button);
        DOWN_Button.setBounds(165, 535, 88, DOWN_Button.getPreferredSize().height);

        //======== scrollPane1 ========
        {

            //---- Log_Pane ----
            Log_Pane.setText("Log Area");
            Log_Pane.setEditable(false);
            Log_Pane.setFocusable(false);
            scrollPane1.setViewportView(Log_Pane);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(30, 25, 370, 420);

        //---- Speed_Label ----
        Speed_Label.setText("Speed:");
        contentPane.add(Speed_Label);
        Speed_Label.setBounds(390, 540, 50, 25);

        //---- Author ----
        Author.setText("Hello, my name is Yishuang Tian. This opensource software is made for everyone. I hope you can enjoy it.");
        contentPane.add(Author);
        Author.setBounds(30, 0, Author.getPreferredSize().width, 20);

        //---- radioButton1 ----
        radioButton1.setText("Keyboard Control");
        radioButton1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                radioButton1KeyPressed(e);
            }
            @Override
            public void keyReleased(KeyEvent e) {
                radioButton1KeyReleased(e);
            }
        });
        contentPane.add(radioButton1);
        radioButton1.setBounds(260, 490, 145, radioButton1.getPreferredSize().height);

        //---- Speed_TextField ----
        Speed_TextField.setText("Speed Display");
        Speed_TextField.setHorizontalAlignment(SwingConstants.CENTER);
        Speed_TextField.setFocusable(false);
        Speed_TextField.setEditable(false);
        contentPane.add(Speed_TextField);
        Speed_TextField.setBounds(30, 450, 370, Speed_TextField.getPreferredSize().height);

        //---- Motor_Switch ----
        Motor_Switch.setText("A/B");
        Motor_Switch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Motor_SwitchMouseClicked(e);
            }
        });
        contentPane.add(Motor_Switch);
        Motor_Switch.setBounds(new Rectangle(new Point(60, 485), Motor_Switch.getPreferredSize()));

        //---- Speed_Num ----
        Speed_Num.setText("0");
        contentPane.add(Speed_Num);
        Speed_Num.setBounds(695, 540, 30, 25);

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Milchigs Tom
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenuItem About;
    private JMenuItem menuItem11;
    private JMenuItem Github;
    private JMenuItem menuItem1;
    private JMenu menu2;
    private JMenu menu6;
    private JMenuItem menuItem14;
    private JMenuItem menuItem13;
    private JMenu menu7;
    private JMenuItem menuItem15;
    private JMenuItem menuItem16;
    private JMenu menu8;
    private JMenuItem menuItem17;
    private JMenuItem menuItem18;
    private JMenu menu3;
    private JMenuItem menuItem2;
    private JMenuItem menuItem23;
    private JMenuItem menuItem32;
    private JMenuItem menuItem3;
    private JMenuItem menuItem4;
    private JMenuItem menuItem21;
    private JMenuItem menuItem22;
    private JMenuItem menuItem5;
    private JMenuItem menuItem19;
    private JMenuItem menuItem20;
    private JMenu menu4;
    private JMenuItem menuItem6;
    private JMenuItem menuItem7;
    private JMenuItem menuItem8;
    private JMenuItem menuItem9;
    private JMenu menu5;
    private JMenuItem menuItem10;
    private JMenu menu9;
    private JMenuItem menuItem24;
    private JMenuItem menuItem25;
    private JMenuItem menuItem26;
    private JMenuItem menuItem27;
    private JMenu menu10;
    private JMenuItem menuItem12;
    private JMenuItem menuItem28;
    private JMenuItem menuItem29;
    private JMenuItem menuItem31;
    private JMenu menu11;
    private JMenuItem menuItem30;
    private JButton TCP_Link_Button;
    private JSlider Speed_Slider;
    private JTextField IP_TextField;
    private JTextField Port_TextField;
    private JRadioButton Link_State;
    private JPanel DrawArea;
    private Test test1;
    private JButton Fore_Button;
    private JButton LEFT_Button;
    private JButton RIGHT_Button;
    private JButton DOWN_Button;
    private JScrollPane scrollPane1;
    private JTextPane Log_Pane;
    private JLabel Speed_Label;
    private JLabel Author;
    private JRadioButton radioButton1;
    private JTextField Speed_TextField;
    private JToggleButton Motor_Switch;
    private JLabel Speed_Num;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
