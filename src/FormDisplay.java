import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FormDisplay extends JPanel {

    FormDisplay(ArrayList<Component> components){
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridy = 0;
        c.insets = new Insets(0,0,20,0);
        for(Component component: components){
            c.gridy += 1;
            this.add(component, c);
        }
    }
}
