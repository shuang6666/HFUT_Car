package TCP_Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCP_Client extends JFrame implements ActionListener {
    Socket socket;
    Client_Sub_Read_Thread client_sub_read_thread;
    //Client_Sub_Write_Thread client_sub_write_thread;
    DataInputStream dis;//输入流
    DataOutputStream dos;//输出流
    Panel panel_north = new Panel();//北面
    Panel panel_south = new Panel();//南面
    Panel panel_east = new Panel();//东面
    Panel panel_west = new Panel();//西面
    JFrame jFrame = new JFrame("Tcp_2019211551.Client");//窗口
    JButton jButton_01 = new JButton("Link");//北面：连接按钮
    JButton jButton_02 = new JButton("Send");//南面：发送按钮
    JTextField jTextField_01 = new JTextField("127.0.0.1", 10);//北面：IP地址
    JTextField jTextField_02 = new JTextField("10086", 10);//北面：端口
    JTextField jTextField_03 = new JTextField("Hello World!", 30);//南面：消息编辑框
    JTextArea jTextArea_01 = new JTextArea("Welcome To My Program\n");//中部：消息框
    JLabel jLabel_01 = new JLabel("IP:");//北面：端口提示
    JLabel jLabel_02 = new JLabel("Port:");//北面：端口提示
    JLabel jLabel_03 = new JLabel("Say:");//南面：消息提示

    public TCP_Client() {
        //注册监听器
        jButton_01.addActionListener(this);
        jButton_02.addActionListener(this);
        jTextField_03.addActionListener(this);
        //调整组件
        //给每个面添加组件
        panel_north.add(jLabel_01);//给北面添加组件
        panel_north.add(jTextField_01);//给北面添加组件
        panel_north.add(jLabel_02);//给北面添加组件
        panel_north.add(jTextField_02);//给北面添加组件
        panel_north.add(jButton_01);//给北面添加组件
        panel_south.add(jLabel_03);//给南面添加组件
        panel_south.add(jTextField_03);//给南面添加组件
        panel_south.add(jButton_02);//给南面添加组件
        //添加东南西北中至窗口
        jFrame.add(panel_north, "North");
        jFrame.add(panel_south, "South");
        jFrame.add(panel_east, "East");
        jFrame.add(panel_west, "West");
        jFrame.add(jTextArea_01, "Center");
        //调整窗口参数
        jFrame.setBounds(100, 100, 600, 400);
        //jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object object = e.getSource();
        try {
            if (object == jButton_01) {
                Client_Thread client_thread = new Client_Thread();//防止JFrame卡死,引用新线程
                client_thread.start();
            } else if (object == jButton_02 || object == jTextField_03) {
                try {
                    jTextArea_01.append(jTextField_03.getText() + "\n");//发送框的内容写入消息框
                    char[] array=jTextField_03.getText().toCharArray();     //将用户输入转化为数组
                    //对数组中的每个元素进行异或
//                    for(int i=0;i<array.length;i++) {
//                        array[i]=(char)(array[i]^20000);
//                    }
                    dos.writeUTF(new String(array));//发送框的内容网络流流出
                    jTextField_03.setText("");//清空发送框
                } catch (Exception exception) {
                    exception.getMessage();
                }
            }
        } catch (Exception exception) {
            exception.getMessage();
        }
    }

    class Client_Thread extends Thread {
        @Override
        public void run() {
            String ip = jTextField_01.getText();//获得IP地址
            int port = Integer.parseInt(jTextField_02.getText());//获得端口号
            try {
                socket = new Socket(ip, port);
                jTextArea_01.append("Link Tcp_2019211551.Server Successfully With IP:" + ip + "/Port:" + port + "\n");
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                client_sub_read_thread = new Client_Sub_Read_Thread();
                //client_sub_write_thread = new Client_Sub_Write_Thread();
                client_sub_read_thread.start();//开启网络输入流监听线程
            } catch (IOException e) {
                jTextArea_01.append("Link Failed" + "\n");
            }
        }
    }

    class Client_Sub_Read_Thread extends Thread {
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
