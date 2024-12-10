package td.marketPlace;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;

import java.util.*;

public class LaunchAgents {
    public static void main(String[] args) {
        // prepare arguments for the Jade container
        Properties prop = new ExtendedProperties();
        // -- add a control/debug window
        prop.setProperty(Profile.GUI, "true");
        // -- add the agents
        StringBuilder sb = new StringBuilder();
        List<String> names = Arrays.asList("Diego", "Valerie", "Rodriguez", "Sylvie", "Patoche", "Shanon", "Robin", "Liam", "Dominique", "Gégé", "Fabrice", "Véro", "Hortense", "Marine", "Alexia", "Laeticia");
        for (int i = 1; i < 4; i++) {
            Random r = new Random(System.currentTimeMillis() + (i * 1000));
            String nomAgent = names.get(r.nextInt(names.size()));
            String typeAgent = ":td.marketPlace.Buyer("+i+");";
            sb.append(nomAgent).append(typeAgent);
        }
        prop.setProperty(Profile.AGENTS, sb.toString());
        // create the jade profile
        ProfileImpl profMain = new ProfileImpl(prop);
        // launch the main jade container
        Runtime rt = Runtime.instance();
        rt.createMainContainer(profMain);
    }
}
