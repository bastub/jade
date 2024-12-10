package td.timeTable;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import java.util.Properties;

/**
 * Agent class
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
        prop.setProperty(Profile.AGENTS, "Teacher1:td.timeTable.Enseignant(0-3/1-2);Teacher3:td.timeTable.Enseignant(0-3/1-2);Teacher3:td.timeTable.Enseignant");
        // create the ain container
        ProfileImpl profMain = new ProfileImpl(prop);
        // launch it !
        Runtime rt = Runtime.instance();
        rt.createMainContainer(profMain);
    }
}
