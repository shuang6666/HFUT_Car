package HFUT_Spider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class HFUT_Spider {
    public HFUT_Spider() {
        initComponents();
    }

    private void Cancel_ButtonMouseReleased(MouseEvent e) {
        // TODO add your code here
        Main_Window.dispose();
    }

    private void Get_ButtonMouseReleased(MouseEvent e) throws IOException {
        // TODO add your code here
        StringBuilder info = new StringBuilder();
        info.append(HFUT_INFO.Get_HFUTMain()).append('\n');
        info.append(HFUT_INFO.Get_ComputerAndInformation()).append('\n');
        info.append(HFUT_INFO.Get_MechanicalEngineerin()).append('\n');
        info.append(HFUT_INFO.Get_MaterialsScience()).append('\n');
        info.append(HFUT_INFO.Get_ElectricalEngineering()).append('\n');
        info.append(HFUT_INFO.Get_CivilEngineering()).append('\n');
        info.append(HFUT_INFO.Get_ChemicalEngineering()).append('\n');
        Text_Panel.setText(info.toString());
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Milchigs Tom
        Main_Window = new JFrame();
        scrollPane1 = new JScrollPane();
        Text_Panel = new JTextPane();
        Get_Button = new JButton();
        Cancel_Button = new JButton();

        //======== Main_Window ========
        {
            Main_Window.setVisible(true);
            Main_Window.setResizable(false);
            //Main_Window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            Container Main_WindowContentPane = Main_Window.getContentPane();
            Main_WindowContentPane.setLayout(null);

            //======== scrollPane1 ========
            {

                //---- Text_Panel ----
                Text_Panel.setEditable(false);
                scrollPane1.setViewportView(Text_Panel);
            }
            Main_WindowContentPane.add(scrollPane1);
            scrollPane1.setBounds(5, 5, 945, 430);

            //---- Get_Button ----
            Get_Button.setText("Get");
            Get_Button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    try {
                        Get_ButtonMouseReleased(e);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            Main_WindowContentPane.add(Get_Button);
            Get_Button.setBounds(new Rectangle(new Point(760, 445), Get_Button.getPreferredSize()));

            //---- Cancel_Button ----
            Cancel_Button.setText("Cancel");
            Cancel_Button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    Cancel_ButtonMouseReleased(e);
                }
            });
            Main_WindowContentPane.add(Cancel_Button);
            Cancel_Button.setBounds(new Rectangle(new Point(860, 445), Cancel_Button.getPreferredSize()));

            Main_WindowContentPane.setPreferredSize(new Dimension(960, 505));
            Main_Window.pack();
            Main_Window.setLocationRelativeTo(Main_Window.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Milchigs Tom
    private JFrame Main_Window;
    private JScrollPane scrollPane1;
    private JTextPane Text_Panel;
    private JButton Get_Button;
    private JButton Cancel_Button;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}