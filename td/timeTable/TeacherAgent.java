package td.timeTable;

import jade.core.behaviours.ReceiverBehaviour;
import jade.gui.AgentWindowed;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import td.timeTable.Gui4Teacher;

import java.io.IOException;

import static java.lang.System.out;

/**
 * Agent class to represent a discussion between 3 teacher.
 *
 * @author bastienubassy
 */
public class TeacherAgent extends AgentWindowed {
    Gui4Teacher window;

    int c1 = -1000;
    int c2 = -20;
    int nc = 0;
    int vp = 10;
    int nvp = 0;

    int[][] d1RoomsConstraints = {{nc, nc, nc}, {c1, nc, nc}, {nc, nc, c2}, {nc, nc, nc}};
    int[][] d1Teacher1Constraints = {{nc, nc, nc}, {nc, nc, nc}, {nc, nc, nc}, {c1, c1, c1}};
    int[][] d1Teacher2Constraints = {{nc, nc, nc}, {nc, nc, nc}, {nc, nc, nc}, {c2, c2, c2}};
    int[][] d1Teacher3Constraints = {{nc, nc, nc}, {nc, nc, nc}, {c1, c1, c1}, {nc, nc, nc}};
    int[][] d2RoomsConstraints = {{nc, c2, nc}, {nc, nc, nc}, {nc, nc, nc}, {nc, c1, c1}};
    int[][] d2Teacher1Constraints = {{nc, nc, nc}, {nc, nc, nc}, {c2, c2, c2}, {nc, nc, nc}};
    int[][] d2Teacher2Constraints = {{nc, nc, nc}, {c1, c1, c1}, {nc, nc, nc}, {nc, nc, nc}};
    int[][] d2Teacher3Constraints = {{c2, c2, c2}, {nc, nc, nc}, {nc, nc, nc}, {nc, nc, nc}};
    int[][] vpConstraints = {{vp, vp, nvp}, {vp, vp, nvp}, {vp, vp, nvp}, {vp, vp, nvp}};

    int[][][] constraints;

    int id = -1;

    /**
     * agent setup, adds its behaviours
     */
    @Override
    protected void setup() {

        String[] args = (String[]) this.getArguments();
        id = ((args != null && args.length > 0) ? Integer.parseInt(args[0]) : -1);

        window = new Gui4Teacher(this);
        reset();

        //wait for a "PROPOSE" msg
        var model = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);

        addBehaviour(new ReceiverBehaviour(this, -1, model, true,(a,msg)->{
            try {
                int[][][] receivedConstraints = (int[][][]) msg.getContentObject();
                constraints = add3DMatrices(constraints, receivedConstraints);
            } catch (UnreadableException e) {
                throw new RuntimeException(e);
            }
            window.println("/!\\ New TimeTable constraints : ");
            window.println("-".repeat(30));
            displayTimetable(constraints);
            var myAnswer = msg.createReply();
            myAnswer.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            a.send(myAnswer);
        }));

        //wait for an accept proposal msg
        model = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        addBehaviour(new ReceiverBehaviour(this, -1, model, true,(a,msg)-> window.println(getLocalName() + " -> teacher accept !!!")));

        //wait for a reject proposal msg
        model = MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL);
        addBehaviour(new ReceiverBehaviour(this, -1, model, (a,msg)-> window.println(getLocalName() + " -> teacher reject !!!")));

        //wait for an inform msg
        model = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        addBehaviour(new ReceiverBehaviour(this, -1, model, true,(a,msg)->{
            int[][][] newConstraints;
            try {
                newConstraints = (int[][][]) msg.getContentObject();
            } catch (UnreadableException e) {
                throw new RuntimeException(e);
            }
            window.println("Constraints from : " + msg.getSender().getLocalName());
            window.println("-".repeat(30));
            displayTimetable(newConstraints);
        }));

        try {
            sendConstraints();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void reset() {
        constraints = createConstraints();
        assert constraints != null;
        displayTimetable(constraints);
    }

    private int[][] addMatrices(int[][]... matrices) {
        int rows = matrices[0].length;
        int columns = matrices[0][0].length;
        int[][] c = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                c[i][j] = 0;
                for(int[][] matrix:matrices) {
                    c[i][j] += matrix[i][j];
                }
            }
        }
        return c;
    }

    private int[][][] add3DMatrices(int[][][]... matrices) {
        int nb_mat = matrices[0].length;
        int rows = matrices[0][0].length;
        int columns = matrices[0][0][0].length;
        int[][][] c = new int[nb_mat][rows][columns];
        for(int k = 0; k < nb_mat; k++) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    c[k][i][j] = 0;
                    for(int[][][] matrix:matrices) {
                        c[k][i][j] += matrix[k][i][j];
                    }
                }
            }
        }
        return c;
    }

    private void displayTimetable(int[][][] timetable){
        int nb_mat = timetable.length;
        int rows = timetable[0].length;
        int columns = timetable[0][0].length;
        for(int k = 1; k < nb_mat + 1; k++) {
            window.println("Jour " + k + " :");
            window.println("-".repeat(30));
            for (int i = 0; i < rows; i++) {
                StringBuilder row = new StringBuilder();
                for (int j = 0; j < columns; j++) {
                    row.append(timetable[0][i][j]).append("\t");
                }
                window.println(row.toString());
            }
        }
    }

    private int[][][] createConstraints() {
        int[][][] constraints = new int[2][][];
        if (id == 1) {
            int[][] d1Constraints = addMatrices(d1RoomsConstraints, d1Teacher1Constraints, vpConstraints);
            int[][] d2Constraints = addMatrices(d2RoomsConstraints, d2Teacher1Constraints, vpConstraints);
            constraints[0] = d1Constraints;
            constraints[1] = d2Constraints;
            return constraints;
        }
        if (id == 2) {
            int[][] d1Constraints = addMatrices(d1RoomsConstraints, d1Teacher2Constraints, vpConstraints);
            int[][] d2Constraints = addMatrices(d2RoomsConstraints, d2Teacher2Constraints, vpConstraints);
            constraints[0] = d1Constraints;
            constraints[1] = d2Constraints;
            return constraints;
        }
        if (id == 3) {
            int[][] d1Constraints = addMatrices(d1RoomsConstraints, d1Teacher3Constraints, vpConstraints);
            int[][] d2Constraints = addMatrices(d2RoomsConstraints, d2Teacher3Constraints, vpConstraints);
            constraints[0] = d1Constraints;
            constraints[1] = d2Constraints;
            return constraints;
        }
        return null;
    }

    private void sendTimeTable() throws IOException {
        int[][][] newConstraints = {{{c1, c1, 0}, {0, c1, c1}, {0, 0, 0}, {0, 0, 0}}, {{c1, 0, c1}, {c1, c1, 0}, {0, 0, 0}, {0, 0, 0}}};
        ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
        msg.setContentObject(newConstraints);
        msg.addReceiver("Teacher 2");
        send(msg);
    }

    private void sendConstraints() throws IOException {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContentObject(constraints);
        for (int i = 1; i < 4; i++) {
            if (i == id) {
                continue;
            }
            msg.addReceiver("Teacher " + i);
        }
        send(msg);
    }

    protected void onGuiEvent(GuiEvent ev) {
        if (ev.getType() == Gui4Teacher.SENDBEGIN) {
            try {
                sendTimeTable();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}