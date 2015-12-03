package Server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sensor
 */

public class ChatServer extends javax.swing.JFrame {

    private SettingChat settingchat = new SettingChat();
    /**
     * Creates new form Server
     */
    public ChatServer() throws IOException{
        initComponents();
        setVisible(true);
        settingchat.setGui(this);
        settingchat.set();
        
    }
    
    public void appendMsg(String msg)
    {
        SLog.append(msg);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        SLog = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Monitor");
        setAlwaysOnTop(true);

        SLog.setEditable(false);
        SLog.setColumns(20);
        SLog.setRows(5);
        jScrollPane1.setViewportView(SLog);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>                        

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws IOException{
        new ChatServer();
    }

    // Variables declaration - do not modify                     
    private javax.swing.JTextArea SLog;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration                   
}
