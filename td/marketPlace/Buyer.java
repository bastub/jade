package td.marketPlace;

import td.marketPlace.Gui4Buyer;
import jade.core.AID;
import jade.core.AgentServicesTools;
import jade.core.behaviours.ReceiverBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * class for an agent that registers to a service and that messages to agents according to the service they belong
 *
 * @author eadam
 */
public class Buyer extends GuiAgent {

    /**
     * little gui to display and send messages
     */
    Gui4Buyer window;

    /**
     * address (aid) of the other agents
     */
    AID[] neighbourgs;

    /**
     * msg to send
     */
    String helloMsg;

    int id = -1;




    /**
     * agent set-up.
     * - Register the agent in the yellow pages for a service chosen between cordiality-lobby or cordiality-reception.
     * - Add a behavior that listens and displays the received message
     */
    @Override
    protected void setup() {
        
        String[] args = (String[]) this.getArguments();
        id = ((args != null && args.length > 0) ? Integer.parseInt(args[0]) : -1);
        window = new Gui4Buyer(this);
        window.println(helloMsg, false);
        //choose to register to lobby or reception-desk

        //Register as a lobby agent in the cordiality service
        AgentServicesTools.register(this, "cordiality", "buyer");
        window.mainTextArea.setBackground(Color.white);
        window.println("Bienvenue au marché!");
        window.println("-".repeat(30));
        window.println("Nous avons plusieurs choix de restaurant. Choisissez votre favori!\nWelsh\nNachos\nPoke\nSushi\nPizza");
        window.println("\n\nForum\n--------\n");
        //Stay continuously attentive to messages received of all types, without limit of duration
        addBehaviour(new ReceiverBehaviour(this, -1, null, true, (a,msg)->{
            window.println(msg.getSender().getLocalName() + ": " + msg.getContent(),
                    true);
        }));
    }

    /**
     * Send a message to the agents registered under a given service
     */
    private void sendMessage() throws IOException {
        String Welsh = "Welsh=" + new Random(System.currentTimeMillis() + (id * 10000L) + 1000).nextInt(11);
        String Nachos = ";Nachos=" + new Random(System.currentTimeMillis() + (id * 10000L) + 2000).nextInt(11);
        String Poke = ";Poke=" + new Random(System.currentTimeMillis() + (id * 10000L) + 3000).nextInt(11);
        String Sushi = ";Sushi=" + new Random(System.currentTimeMillis() + (id * 10000L) + 4000).nextInt(11);
        String Pizza = ";Pizza=" + new Random(System.currentTimeMillis() + (id * 10000L) + 5000).nextInt(11);

        String message = Welsh + Nachos + Poke + Sushi + Pizza;

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent(message);
        neighbourgs = AgentServicesTools.searchAgents(this, "cordiality", "buyer");
        msg.addReceivers(neighbourgs);
        msg.addReceivers(this.getAID());
        send(msg);
    }


    /**
     * Reaction to the event transmitted by the window
     *
     * @param ev evenement
     */
    protected void onGuiEvent(GuiEvent ev) {
        switch (ev.getType()) {
            case Gui4Buyer.SENDBEGIN -> {
                try {
                    sendMessage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case Gui4Buyer.QUITCODE -> doDelete();
        }
    }

    protected ArrayList<Integer> score(String message) {
        ArrayList<Integer> meanRest = new ArrayList<Integer>();
        String[] notes = message.split("=");
        return meanRest;
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
        System.err.println("Agent : " + getAID().getName() + " quitte le marché.");
    }

}
