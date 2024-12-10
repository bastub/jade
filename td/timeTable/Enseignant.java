package td.timeTable;

import jade.core.behaviours.ReceiverBehaviour;
import jade.gui.AgentWindowed;
import jade.gui.SimpleWindow4Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import static java.lang.System.out;

/**
 * Agent class to represent a teacher
 *
 * @author bastienubassy
 */
public class Enseignant extends AgentWindowed {

    int[][][] matEdt;

    /**
     * agent setup, adds its behaviours
     */
    @Override
    protected void setup() {
        window = new SimpleWindow4Agent(getLocalName(),this);
        reset();

        //wait for a "PROPOSE" msg
        var modele = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);

        addBehaviour(new ReceiverBehaviour(this, -1, modele, true,(a,msg)->{
            ACLMessage myAnswer = msg.createReply();
            myAnswer.setPerformative(ACLMessage.PROPOSE);
            myAnswer.setContent("Test" + getLocalName());
            a.send(myAnswer);
        }));

        //wait for an accept proposal msg
        modele = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        addBehaviour(new ReceiverBehaviour(this, -1, modele, true,(a,msg)-> {println(getLocalName() + " -> seller accept !!!");reset();}));

        //wait for a reject proposal msg
        modele = MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL);
        addBehaviour(new ReceiverBehaviour(this, -1, modele, (a,msg)-> {println(getLocalName() + " -> seller reject !!!");reset();}));

    }

    private void reset() {
        println("X".repeat(30));
        println("X".repeat(30));
        println("-> I am ready.");
        println("~".repeat(20));
    }

    /**I inform the user when I leave the platform*/
    @Override
    protected void takeDown() {
        out.println(getLocalName() + " -> I leave the plateform ! ");
    }


}
