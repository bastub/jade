package td.Morpion;

import jade.core.AID;
import jade.core.AgentServicesTools;
import jade.core.behaviours.ReceiverBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.awt.*;
import java.io.IOException;

/**
 * class for an agent that registers to a service and that messages to agents according to the service they belong
 *
 * @author eadam
 */
public class PlayerAgent extends GuiAgent {

    /**
     * little gui to display and send messages
     */
    Gui4Morpion window;

    /**
     * address (aid) of the other agents
     */
    AID[] neighbourgs;

    /**
     * msg to send
     */
    String helloMsg;

    int id = -1;

    Boolean canPlay = false;

    MorpionGame M;

    /**
     * agent set-up.
     * - Register the agent in the yellow pages for a service chosen between cordiality-lobby or cordiality-reception.
     * - Add a behavior that listens and displays the received message
     */
    @Override
    protected void setup() {
        M = new MorpionGame();
        String[] args = (String[]) this.getArguments();
        id = ((args != null && args.length > 0) ? Integer.parseInt(args[0]) : -1);
        window = new Gui4Morpion(this);
        window.println(helloMsg, false);
        //choose to register to lobby or reception-desk

        //Register as a lobby agent in the cordiality service
        AgentServicesTools.register(this, "cordiality", String.valueOf(id));
        window.mainTextArea.setBackground(Color.white);
        if (id == 1) {
            canPlay = true;
            window.println("You are ready to play!");
            window.println(M.toString());
        } else {
            window.println("Player 1 is going to play!");
        }

        //Stay continuously attentive to messages received of all types, without limit of duration
        addBehaviour(new ReceiverBehaviour(this, -1, null, true, (a,msg)->{
            window.println(msg.getSender().getLocalName() + " played, it's your turn",
                    true);
            try {
                M = (MorpionGame) msg.getContentObject();
            } catch (UnreadableException e) {
                throw new RuntimeException(e);
            }
            window.println(M.toString(), true);
            window.println("-".repeat(30));
            canPlay = true;
        }));

    }

    /**
     * Send a message to the agents registered under a given service
     * @param text text to send
     * @param id ID of the player to which the recipients of the message belong
     */
    private void sendMessage(String text, int id) throws IOException {
        if (!canPlay) {
            return;
        }

        if (!M.play(text, id)) {
            window.println("Impossible move, try again.");
            return;
        }

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContentObject(M);
        neighbourgs = AgentServicesTools.searchAgents(this, "cordiality", String.valueOf(id));
        msg.addReceivers(neighbourgs);
        send(msg);
        canPlay = false;
    }


    /**
     * Reaction to the event transmitted by the window
     *
     * @param ev evenement
     */
    protected void onGuiEvent(GuiEvent ev) {
        switch (ev.getType()) {
            case Gui4Morpion.SENDLOBBY -> {
                try {
                    sendMessage(window.lowTextArea.getText(),(id % 3) + 1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case Gui4Morpion.QUITCODE -> doDelete();
        }
    }

    /**deregister to the service and close the window before leaving
     * */
    @Override
    protected void takeDown() {
        // S'effacer du service pages jaunes
        AgentServicesTools.deregisterAll(this);
        //fermer la fenetre
        window.dispose();
        //bye
        System.err.println("Agent : " + getAID().getName() + " quitte la plateforme.");
    }

}
