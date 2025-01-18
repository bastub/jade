package td.timeTable;

import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * a simple GUI interface to watch teacher agents deciding their timetables
 *
 * @author bastienubassy
 * @version 1
 */
public class Gui4Teacher extends JFrame implements ActionListener {
    /**
     * code associated to the "begin" button
     */
    public static final int SENDBEGIN = 1;

    /**
     * action associated to the "begin" button
     */
    private static final String BEGINCMD = "1";

    /**
     * main text area
     */
    public JTextArea mainTextArea;

    /**
     * myAgent linked to this frame
     */
    GuiAgent myAgent;

    /**
     * counter of windows created
     */
    static int nb = 0;

    /**
     * no of the window
     */
    int no;

    /**
     * creates a window and displays it in a free space of the screen
     */
    public Gui4Teacher() {
        final int preferredWidth = 500;
        final int preferredHeight = 800;
        no = nb++;

        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int x = (no * preferredWidth) % screenWidth;
        int y = (((no * preferredWidth) / screenWidth) * preferredHeight) % screenHeight;

        setBounds(x, y, preferredWidth, preferredHeight);
        buildGui();
        setVisible(true);
    }

    public Gui4Teacher(GuiAgent agent) {
        this();
        myAgent = agent;
        setTitle(myAgent.getLocalName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        JButton button = new JButton("BEGIN");
        button.addActionListener(this);
        button.setActionCommand(BEGINCMD);
        jpanel.add(button);
        getContentPane().add(BorderLayout.NORTH, jpanel);
    }

    /**
     * add a string to the main text area
     */
    public void println(final String newText) {
        String text = mainTextArea.getText();
        text += newText + "\n";
        mainTextArea.setText(text);
        mainTextArea.setCaretPosition(text.length());
    }

    /**
     * add a string to a text area (main parameter is no more used)
     * @param newText text to add
     * @param main if true text is added to the main text area, if false, text is set in the small text area
     */
    public void println(final String newText, final boolean main) {
        if(main)println(newText);
    }

    /**
     * reaction to the button event and communication with the agent
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        final String source = evt.getActionCommand();
        if (source.equals(BEGINCMD)) {
            GuiEvent ev = new GuiEvent(this, Integer.parseInt(source));
            myAgent.postGuiEvent(ev);
        }
    }
}
