package td.timeTable;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;

import java.util.Properties;

/**
 *
 * @author bastienubassy
 */
public class Main  {

    public static void main(String[] args) {
        // prepare argument for the JADE container
        Properties prop = new ExtendedProperties();
        // display a control/debug window
        prop.setProperty(Profile.GUI, "true");
        // declare the agents
        StringBuilder sb = new StringBuilder();
        String nomAgent = "Teacher ";
        for (int i = 1; i < 4; i++) {
            String typeAgent = ":td.timeTable.TeacherAgent("+i+");";
            sb.append(nomAgent).append(i).append(typeAgent);
        }
        prop.setProperty(Profile.AGENTS, sb.toString());
        // create the main container
        ProfileImpl profMain = new ProfileImpl(prop);
        // launch it !
        Runtime rt = Runtime.instance();
        rt.createMainContainer(profMain);
    }
}