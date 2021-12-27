package TCP_Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCP_Server extends JFrame implements ActionListener {
    ServerSocket serverSocket;
    Socket socket;
    Server_Sub_Read_Thread server_sub_read_thread;
    //Server_Sub_Write_Thread server_sub_write_thread;
    DataInputStream dis;
    DataOutputStream dos;
    Panel panel_north = new Panel();//北面
    Panel panel_south = new Panel();//南面
    Panel panel_east = new Panel();//东面
    Panel panel_west = new Panel();//西面
    JFrame jFrame = new JFrame("Tcp_2019211551.Server");//窗口
    JButton jButton_01 = new JButton("Boot");//北面：启动按钮
    JButton jButton_02 = new JButton("Send");//南面：发送按钮
    JTextField jTextField_01 = new JTextField("10086", 30);//北面：端口
    JTextField jTextField_02 = new JTextField("Hello World!", 30);//南面：消息编辑框
    JTextArea jTextArea_01 = new JTextArea("Welcome To My Program\n");//中部：消息框
    JLabel jLabel_01 = new JLabel("Port:");//北面：端口提示
    JLabel jLabel_02 = new JLabel("Say:");//南面：消息提示

    public TCP_Server() {
        //注册监听器
        jButton_01.addActionListener(this);
        jButton_02.addActionListener(this);
        jTextField_02.addActionListener(this);
        //调整组件
        //给每个面添加组件
        panel_north.add(jLabel_01);//给北面添加组件
        panel_north.add(jTextField_01);//给北面添加组件
        panel_north.add(jButton_01);//给北面添加组件
        panel_south.add(jLabel_02);//给南面添加组件
        panel_south.add(jTextField_02);//给南面添加组件
        panel_south.add(jButton_02);//给南面添加组件
        //添加东南西北中至窗口
        jFrame.add(panel_north, "North");
        jFrame.add(panel_south, "South");
        jFrame.add(panel_east, "East");
        jFrame.add(panel_west, "West");
        jFrame.add(jTextArea_01, "Center");
        //调整窗口参数
        jFrame.setBounds(700, 100, 600, 400);
        //jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object object = e.getSource();
        try {
            if (object == jButton_01) {
                int port = Integer.parseInt(jTextField_01.getText());
                serverSocket = new ServerSocket(port);
                jTextArea_01.append("Your Tcp_2019211551.Server Booted Successfully With Port:" + port + "\n");
                Server_Thread server_thread = new Server_Thread();
                server_thread.start();
            } else if (object == jButton_02 || object == jTextField_02) {

                try {
                    jTextArea_01.append(jTextField_02.getText() + "\n");
                    char[] array=jTextField_02.getText().toCharArray();     //将用户输入转化为数组
                    //对数组中的每个元素进行异或
//                    for(int i=0;i<array.length;i++) {
//                        array[i]=(char)(array[i]^20000);
//                    }
                    dos.writeUTF(new String(array));
                    jTextField_02.setText("");
                } catch (Exception exception) {
                    exception.getMessage();
                }
            }
        } catch (Exception exception) {
            exception.getMessage();
        }
    }

    class Server_Thread extends Thread {
        @Override
        public void run() {
            try {
                socket = serverSocket.accept();
                jTextArea_01.append("A New Tcp_2019211551.Client Linked" + "\n");
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                server_sub_read_thread = new Server_Sub_Read_Thread();
                //server_sub_write_thread = new Server_Sub_Write_Thread();
                server_sub_read_thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Server_Sub_Read_Thread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    String message = new String();
                    message = dis.readUTF();
                    char[] array=message.toCharArray();     //将用户输入转化为数组
                    //对数组中的每个元素进行异或
//                    for(int i=0;i<array.length;i++) {
//                        array[i]=(char)(array[i]^20000);
//                    }
//                    jTextArea_01.append(new String(array) + "\n" + message + "\n");
                    jTextArea_01.append(message + "\n");
                }
            } catch (IOException e) {
                e.getMessage();
            }
        }
    }
}