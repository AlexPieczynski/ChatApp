
import static java.awt.event.InputEvent.SHIFT_DOWN_MASK;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_SHIFT;
import java.awt.event.KeyListener;
import java.util.Objects;
import javax.swing.JOptionPane;
import java.util.Vector;
import javax.swing.JCheckBox;
import java.awt.event.WindowEvent;
import javax.swing.text.DefaultCaret;

/*
This is the ClientGUI class which works in tandem with the Client and Message classes.
It allows communication with a server to transmit messages between other users running an
instance if that instance is connected to the same server. 
*/

public class ClientGUI extends javax.swing.JFrame implements KeyListener {

    public ClientGUI() {
        initComponents();
        //Build checkBox vector and populate for ease of navigation
        boxes = new Vector<>();
        boxes.add(checkBox1);
        boxes.add(checkBox2);
        boxes.add(checkBox3);
        boxes.add(checkBox4);
        boxes.add(checkBox5);
        boxes.add(checkBox6);
        userName = null;    //triggers message if user hasn't setup the connection  
        user = null;
        displayArea.setText("Connect to server from the menu.\n"
                + "Check the boxes to select recipients. Press Enter to send message.\n");
    }

    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jDialog1 = new javax.swing.JDialog();
        jDialog2 = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        displayArea = new javax.swing.JTextArea();
        checkBox1 = new javax.swing.JCheckBox();
        sendBtn = new javax.swing.JButton();
        checkBox2 = new javax.swing.JCheckBox();
        checkBox3 = new javax.swing.JCheckBox();
        checkBox4 = new javax.swing.JCheckBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        writeArea = new javax.swing.JTextArea();
        checkBox5 = new javax.swing.JCheckBox();
        checkBox6 = new javax.swing.JCheckBox();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        serverMenu = new javax.swing.JMenu();
        connectMenuItem = new javax.swing.JMenuItem();
        disconnectMenuItem = new javax.swing.JMenuItem();
        
        //Set auto-scrolling for displayArea
        DefaultCaret caret = (DefaultCaret)displayArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        //-------------Layout-------------//

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        //Configure displayArea to wrap text
        displayArea.setEditable(false);
        displayArea.setColumns(20);
        displayArea.setRows(5);
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        jScrollPane1.setViewportView(displayArea);

        sendBtn.setText("Send to Selected Users");
        sendBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendBtnActionPerformed(evt);
            }
        });

        //The checkboxes are initialized as disabled
        checkBox1.setEnabled(false);
        checkBox2.setEnabled(false);
        checkBox3.setEnabled(false);
        checkBox4.setEnabled(false);
        checkBox5.setEnabled(false);
        checkBox6.setEnabled(false);

        //Configure writeArea to wrap text
        writeArea.setColumns(20);
        writeArea.setRows(5);
        writeArea.setLineWrap(true);
        writeArea.setWrapStyleWord(true);
        jScrollPane3.setViewportView(writeArea);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(sendBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(checkBox2)
                                    .addComponent(checkBox1))
                                .addGap(123, 123, 123)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(checkBox4)
                                    .addComponent(checkBox3))))
                        .addGap(113, 113, 113)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(checkBox5)
                            .addComponent(checkBox6))
                        .addGap(117, 117, 117)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(checkBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkBox2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(checkBox3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkBox4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(checkBox5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkBox6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sendBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        fileMenu.setText("File");

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        serverMenu.setText("Server");

        connectMenuItem.setText("Connect");
        connectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectMenuItemActionPerformed(evt);
            }
        });
        serverMenu.add(connectMenuItem);

        disconnectMenuItem.setText("Disconnect");
        disconnectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectMenuItemActionPerformed(evt);
            }
        });
        serverMenu.add(disconnectMenuItem);

        menuBar.add(serverMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        
        writeArea.addKeyListener(this);
        
        pack();
    }

//-------------Actions-------------//    
    
    //disconnect client from server on window close
    @Override
    protected void processWindowEvent(WindowEvent e)
    {
      if(e.getID() == WindowEvent.WINDOW_CLOSING){
        if (user != null)
          user.disconnect();
        this.dispose();
      }
    }
    
    private void connectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        //handle the case where user is already connected
        try {
            user.disconnect(); 
        } catch(NullPointerException e) {
            // user has not been created. *OKAY*
        }
        
        //Create instance of client class
        user = new Client(this); //Pass reference of GUI for queue listening in Client
        
        //Prompt user for IP
        String ip = JOptionPane.showInputDialog("Please input IP address:");
        while(!user.connectToServer(ip)){
            ip = JOptionPane.showInputDialog("Cannot connect, please try again:");
        }//if ip is connectable then prompt for user name
        
        //Prompt user for desired name
        userName = JOptionPane.showInputDialog("Please input your user name:");
        //Check that user name does not already exist and that user hasn't hit cancel on option
        while(!user.setUsername(userName) && (userName != null)){
            userName = null;    //reset userName
            userName = JOptionPane.showInputDialog("That name is already in use, please try again:");
        }
        recips = new Vector<>();    //Create recipients vector for peer selecting
        
    }

    
    private void sendBtnActionPerformed(java.awt.event.ActionEvent evt) {
        // To send message, populate recipient list, then call method in client 
        populateRecips();
        if(userName != null){   
            user.sendMessage(writeArea.getText(), recips);
        }
        else{   //If user hasn't entered their name, prompt them for it 
            JOptionPane.showMessageDialog(null, "Connect to server and enter user name first.","Error",
                                              JOptionPane.PLAIN_MESSAGE);
        }
        recips.clear(); //clear recips list
        writeArea.setText("");
    }

    
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        user.disconnect();
        System.exit(0);
    }

    
    private void disconnectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        user.disconnect();  //Disconnect from current server
        resetPeers();       //Reset peer list
    }

    
    //-------------Methods-------------//    
    
    private void populateRecips(){
        //Iterate across the checkBoxes to see if any are selected by user
        for(JCheckBox box : boxes){
            if(box.isSelected()){   //Add whichever boxes are selected to the recipients list
                recips.add(box.getText());
            }
        }
    }
    
    
    void resetPeers(){  //After disconnecting, we shouldn't have peers listed
        for(JCheckBox box : boxes){
            box.setText("");    //Blank
            box.setSelected(false); //de-select
            box.setEnabled(false);  //non-selectable now
        }
        recips.clear(); //clear recipients list
    }
    
    
    void updatePeers(Vector<String> peers){
        int i = 0;  //i checks peer index
        //Iterate accross the checkboxes
        for(JCheckBox box : boxes){
            if(i < peers.size()){   //If we aren't out of bounds in the vector
                if(Objects.equals(peers.get(i), box.getText())){//Check if name is already in the box
                    //Then take no action...
                    //We don't want to mess with boxes the user may have selected already
                }
                else{//We need to add the peer to the checkBox...
                    box.setEnabled(true);
                    box.setText(peers.get(i));
                    box.setSelected(false); //Don't check box by default
                    if(userName.equals(peers.get(i))){//if name is the user
                        box.setSelected(true);
                        box.setEnabled(false);   //We don't want it selectable
                    }
                }
            }
            else{   //There are no peers in list, so clear and disable checkbox
                box.setText("");        //Blank
                box.setSelected(false); //de-select
                box.setEnabled(false);  //non-selectable now
            }
            i++;    //increment i to see next peer
        }
        this.pack();    //Resize window to adjust for added content
    }
    
    
    void updateMessages(Message m){
        //Appends new inbound message to displayArea
        displayArea.append(m.getSender() + ":  " + m.getMsg() + "\n");
    }
    
    //------------Key events-----------//
    
    @Override
    public void keyPressed(KeyEvent e) {
        //Same function as button press without the set text method
        if(e.getKeyCode() == VK_ENTER){
            populateRecips();
            if (userName != null) {
                user.sendMessage(writeArea.getText(), recips);
            } else {   //If user hasn't entered their name, prompt them for it 
                JOptionPane.showMessageDialog(null, "Connect to server and enter user name first.", "Error",
                        JOptionPane.PLAIN_MESSAGE);
            }
            recips.clear(); //clear recips list
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        //DO nothing
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //set text on release of key so that cursor returns to top
        if(e.getKeyCode() == VK_ENTER) writeArea.setText("");
    }
    
    //-------------Main-------------//
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientGUI().setVisible(true);
            }
        });
    }

    // Variables declaration
    private javax.swing.JCheckBox checkBox1;
    private javax.swing.JCheckBox checkBox2;
    private javax.swing.JCheckBox checkBox3;
    private javax.swing.JCheckBox checkBox4;
    private javax.swing.JCheckBox checkBox5;
    private javax.swing.JCheckBox checkBox6;
    private javax.swing.JMenuItem connectMenuItem;
    private javax.swing.JMenuItem disconnectMenuItem;
    private javax.swing.JTextArea displayArea;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton sendBtn;
    private javax.swing.JMenu serverMenu;
    private javax.swing.JTextArea writeArea;
    private String userName;
    private Client user;
    private Vector<JCheckBox> boxes;
    private Vector<String> recips;

}

