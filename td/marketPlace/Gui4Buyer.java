package td.marketPlace;

import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * a simple window for a Jade GuiAgent with two texts areas to display
 * information
 *
 * @author emmanuel adam
 * @version 1
 */
public class Gui4Buyer extends JFrame implements ActionListener {
    /**
     * code associated to the Quit button
     */
    public static final int QUITCODE = -1;
    /**
     * code associated to the "send to lobby" button
     */
    private static final String QUITCMD = "-1";
    /**
     * string associated to the send to lobby button
     */
    private static final String SENDCMDLOBBY = "1";

    public static final int SENDBEGIN = 1;

    private static final String BEGINCMD = "1";
    /**
     * nb of windows created
     */
    static int nb = 0;
    /**
     * Low Text area
     */

    public JTextArea mainTextArea;
    /**
     * monAgent linked to this frame
     */
    GuiAgent myAgent;
    /**
     * no of the window
     */
    int no;

    /**
     * creates a window and displays it in a free space of the screen
     */
    public Gui4Buyer() {
        final int preferedWidth = 500;
        final int preferedHeight = 300;
        no = nb++;

        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int x = (no * preferedWidth) % screenWidth;
        int y = (((no * preferedWidth) / screenWidth) * preferedHeight) % screenHeight;

        setBounds(x, y, preferedWidth, preferedHeight);
        buildGui();
        setVisible(true);
    }

    public Gui4Buyer(GuiAgent agent) {
        this();
        myAgent = agent;
        setTitle(myAgent.getLocalName());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    /**
     * build the gui : a text area in the center of the window, with scroll bars
     */
    private void buildGui() {
        getContentPane().setLayout(new BorderLayout());
        mainTextArea = new JTextArea();
        mainTextArea.setRows(5);
        JScrollPane jScrollPane = new JScrollPane(mainTextArea);
        getContentPane().add(BorderLayout.CENTER, jScrollPane);

        JPanel jpanel = new JPanel();
        jpanel.setLayout(new GridLayout(0, 3));
        // (just add columns to add button, or other thing...
        JButton button = new JButton("--- QUIT ---");
        button.addActionListener(this);
        button.setActionCommand(QUITCMD);
        jpanel.add(button);
        button = new JButton("BEGIN");
        button.addActionListener(this);
        button.setActionCommand(BEGINCMD);
        jpanel.add(button);


        getContentPane().add(BorderLayout.NORTH, jpanel);
    }

    /**
     * add a string to the main text area
     */
    public void println(final String chaine) {
        String texte = mainTextArea.getText();
        texte = texte + chaine + "\n";
        mainTextArea.setText(texte);
        mainTextArea.setCaretPosition(texte.length());
    }

    /**
     * add a string to a text area  (main parameter is no more used)
     * @param chaine text to add
     * @param main if true text is added to the main text area, if false, text is set in the small text area
     */
    public void println(final String chaine, final boolean main) {
        if(main)println(chaine);

    }

    /**
     * reaction to the button event and communication with the agent
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        final String source = evt.getActionCommand();
        if (source.equals(QUITCMD) || source.equals(BEGINCMD)) {
            GuiEvent ev = new GuiEvent(this, Integer.parseInt(source));
            myAgent.postGuiEvent(ev);
        }
    }

}
