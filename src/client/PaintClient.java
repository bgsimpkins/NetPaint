/*
 * PaintClient.java
 *
 * Created on June 29, 2008, 4:59 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package client;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import message.ChatMessage;



/**
 *
 * @author Ben
 */
public class PaintClient extends ObjectClient{
    private PaintPanel pc;
    private JPanel buttonPanel = new JPanel();
    private JButton clear;
    private JRadioButton drawButton, fillButton;
    private ColorButton color;

    private JComboBox thick;

    private JFrame frame;
    private JTextPane chatArea;
    private StyledDocument doc;
    private JTextArea respArea;

    private String name = "Person";

    private Cursor drawCursor, fillCursor, dropperCursor;
    
    /** Creates a new instance of PaintClient */
    public PaintClient(int port, String host) {
        
        super(port,host);        

        String resp = JOptionPane.showInputDialog("Please enter your name:");
        this.name = resp.trim();

        frame = new JFrame("Paint!");
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                System.out.println("Link to server closed");
                closeLink();
            }
        });

        

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,600);

        pc = new PaintPanel(objectIn,objectOut,this);



        createMenu();

        Container contentPane = frame.getContentPane();                 
    
        setupButtonPanel();

        setupCursors();

        JPanel leftPanel = new JPanel();
        leftPanel.setMinimumSize(new Dimension(700,570));
        
        leftPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = .9;
        
        leftPanel.add(pc,c);
        
        c.weighty = .1;
        c.gridy = 1;
        
        leftPanel.add(buttonPanel,c);

        JPanel chatPanel = new JPanel();
        chatPanel.setPreferredSize(new Dimension(250,570));
        setupChatPanel(chatPanel);

        JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftPanel,chatPanel);
        splitter.setDividerLocation(.8);
        contentPane.add(splitter);
        //contentPane.setLayout(new GridBagLayout());
        
        //c = new GridBagConstraints();
        
        //c.fill = GridBagConstraints.BOTH;
        //c.weightx = .7;
        //c.weighty = 1;
        
        //contentPane.add(leftPanel,c);
        //contentPane.add(leftPanel);
        
        //c.gridx = 1;
        //c.weightx = .3;
        
        //contentPane.add(chatPanel,c);
        //contentPane.add(chatPanel);
        
        pc.setColorButton(color);

        frame.setVisible(true);

        
    }

    private void createMenu()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("File");

        JMenuItem item = new JMenuItem("Export Image");
        item.setActionCommand("Export Image");
        item.addActionListener(pc);
        menu.add(item);

        menuBar.add(menu);

        frame.setResizable(false);
        frame.setJMenuBar(menuBar);
        
    }

    private void setupChatPanel(JPanel chatPanel)
    {
        chatPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        
        chatArea = new JTextPane();
        chatArea.setBackground(Color.LIGHT_GRAY);
        chatArea.setEditable(false);
        JScrollPane scroller = new JScrollPane(chatArea);
        scroller.setPreferredSize(new Dimension(220,400));
        scroller.setMinimumSize(new Dimension(220,400));

        chatPanel.setLayout(new BorderLayout());
        //GridBagConstraints c = new GridBagConstraints();
        //c.fill = GridBagConstraints.BOTH;
        //c.weightx = .1;
        //c.weighty = .9;

        //chatPanel.add(scroller,c);
        //chatPanel.add(scroller);
        doc = chatArea.getStyledDocument();

        addStylesToDoc(doc);
        /*try {
            doc.insertString(0, "Here's some shit", doc.getStyle("font1"));
            doc.insertString(doc.getLength(), "\nHere's some bold shit", doc.getStyle("font1_bold"));
            doc.insertString(doc.getLength(), "\nHere's some red shit", doc.getStyle("font2"));
        } catch (BadLocationException ex) {
            Logger.getLogger(PaintClient.class.getName()).log(Level.SEVERE, null, ex);
        }*/

        respArea = new JTextArea("Type some shit in here");
        respArea.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                if (respArea.getText().equals("Type some shit in here"))
                {
                    respArea.setText("");
                }
            }
        });
        respArea.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode()==KeyEvent.VK_ENTER) submitMessage();
            }
        });

        respArea.setLineWrap(true);
        respArea.setWrapStyleWord(true);
        respArea.setBackground(new Color(220,220,220));
        respArea.setMinimumSize(new Dimension(220,100));

        //c.gridy = 1;
        //c.weighty = .1;
        //chatPanel.add(respArea,c);
        //chatPanel.add(respArea,c);

        JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT,scroller,respArea);
        splitter.setDividerLocation(.8);
        chatPanel.add(splitter,BorderLayout.CENTER);

    }

    private void setupButtonPanel(){
        buttonPanel.setBorder(BorderFactory.createEtchedBorder());
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(0,10,0,10);
        clear = new JButton("Clear");
        //clear.setMinimumSize(new Dimension(100,50));
        
        clear.addActionListener(pc);
        buttonPanel.add(clear,c);

        c.gridx = 1;
        color = new ColorButton();
        //color.setMinimumSize(new Dimension(50,50));
        color.setPreferredSize(new Dimension(30,30));
        color.addActionListener(pc);
        buttonPanel.add(color,c);

        c.gridx = 2;
        thick = new JComboBox(new String[]{"1","2","3","4","5","6","7","8"});
        //thick.setPreferredSize(new Dimension(30,30));
        //thick.setMinimumSize(new Dimension(100,50));
        thick.addActionListener(pc);
        buttonPanel.add(thick,c);

        c.gridx = 3;
        JPanel modePanel = new JPanel();
        modePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        modePanel.setPreferredSize(new Dimension(110,30));
        modePanel.setLayout(new GridLayout(1,3,10,10));

        ButtonGroup bg = new ButtonGroup();
        /*drawButton = new JRadioButton("Draw");
        drawButton.setSelected(true);
        drawButton.addActionListener(pc);
        bg.add(drawButton);
        modePanel.add(drawButton);

        fillButton = new JRadioButton("Fill");
        fillButton.addActionListener(pc);
        bg.add(fillButton);
        modePanel.add(fillButton);*/

        JToggleButton drawButton = new JToggleButton();
        drawButton.setActionCommand("Draw");
        drawButton.setSelected(true);
        drawButton.addActionListener(pc);
        bg.add(drawButton);
        drawButton.setIcon(new ImageIcon(this.getClass().getResource("/icons/pencil.png")));
        modePanel.add(drawButton);
        
        JToggleButton fillButton = new JToggleButton();
        fillButton.setActionCommand("Fill");
        bg.add(fillButton);
        fillButton.addActionListener(pc);
        fillButton.setIcon(new ImageIcon(this.getClass().getResource("/icons/bucketFill.png")));
        modePanel.add(fillButton);
        
        JToggleButton dropperButton = new JToggleButton();
        dropperButton.setActionCommand("Dropper");
        dropperButton.addActionListener(pc);
        bg.add(dropperButton);
        dropperButton.setIcon(new ImageIcon(this.getClass().getResource("/icons/dropper.png")));
        modePanel.add(dropperButton);

        //fill.setIcon(new ImageIcon(this.getClass().getResource("/icons/bucketFill.png")));
        //fill.setMinimumSize(new Dimension(30,30));
       //fill.setPreferredSize(new Dimension(30,30));
        //fill.addActionListener(pc);
        buttonPanel.add(modePanel,c);
      
    }

    private void setupCursors()
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        try {
            BufferedImage dIm = ImageIO.read(this.getClass().getResource("/icons/pencil.png"));
            drawCursor = toolkit.createCustomCursor(dIm, new Point(0,19), "drawCursor");

            BufferedImage fIm = ImageIO.read(this.getClass().getResource("/icons/bucketFill.png"));
            fillCursor = toolkit.createCustomCursor(fIm, new Point(27,27), "fillCursor");

            BufferedImage drIm = ImageIO.read(this.getClass().getResource("/icons/dropper.png"));
            dropperCursor = toolkit.createCustomCursor(drIm, new Point(0,19), "dropperCursor");

            pc.setCursor(drawCursor);
        } catch (IOException ex) {
            System.err.println("COuld not load cursor!");
        }
        
    }

    private void addStylesToDoc(StyledDocument doc)
    {
        Style def = StyleContext.getDefaultStyleContext().
                        getStyle(StyleContext.DEFAULT_STYLE);

        Style font1 = doc.addStyle("font1", def);
        StyleConstants.setForeground(font1, Color.BLACK);

        font1 = doc.addStyle("font1_bold", font1);
        StyleConstants.setBold(font1, true);

        Style font2 = doc.addStyle("font2", def);
        StyleConstants.setForeground(font2, Color.RED);

        font2 = doc.addStyle("font2_bold",font2);
        StyleConstants.setBold(font2, true);


    }

    public void setMode(int mode)

    {
        if (mode == PaintPanel.DRAW_MODE)
        {
            pc.setCursor(drawCursor);
        }
        else if (mode == PaintPanel.FILL_MODE)
        {
            pc.setCursor(fillCursor);
        }else if (mode == PaintPanel.DROPPER_MODE)
        {
            pc.setCursor(dropperCursor);
        }
    }

    private void submitMessage()
    {
        try {
            doc.insertString(doc.getLength(), "Me:\n", doc.getStyle("font1_bold"));
            doc.insertString(doc.getLength(), respArea.getText().trim()+"\n\n", doc.getStyle("font1"));
        } catch (BadLocationException ex) {}

        pc.sendMessage(new ChatMessage(name,"otherPerson",respArea.getText().trim()));
        respArea.setText("");
    }

    public void receiveMessage(ChatMessage message)
    {
        String sender = message.getSender();
        String text = message.getMessage();
        try {
            doc.insertString(doc.getLength(), sender+":\n", doc.getStyle("font2_bold"));
            doc.insertString(doc.getLength(), text+"\n\n", doc.getStyle("font2"));
        } catch (BadLocationException ex) {}
        chatArea.selectAll();
    }

    public static void main(String[] args){
        try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Couldn't use system look and feel.");
        }

        if (args.length == 2){
            System.out.println("Port: "+args[0]);
            System.out.println("Address: "+args[1]);
            new PaintClient(Integer.parseInt(args[0]),args[1]);
        }
        else {
            System.out.print("Beginning with Default: \n Port: 1234\n Address: 127.0.0.1\n"+
                    "Else, restart with usage: server.PaintServer <Port No.> <Host Address>\n");
            new PaintClient(1234,"localhost");
        }
    }
    
}
