package Server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer extends javax.swing.JFrame {

    private SettingGame settinggame = new SettingGame();
    /**
     * Creates new form Server
     */
    public GameServer() throws IOException{
        initComponents();
        setVisible(true);
        settinggame.setGui(this);
        settinggame.setGame();       
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>                        


    public static void main(String args[]) throws IOException{
        new GameServer();
    }

    // Variables declaration - do not modify                     
    private javax.swing.JTextArea SLog;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration                   
}