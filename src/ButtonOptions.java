import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ButtonOptions extends JPanel {

    ButtonOptions(ArrayList<Component> options){
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        for(Component component: options){
            c.gridx += 1;
            c.gridy = 0;
            c.insets = new Insets(0,10,0,0);
            this.add(component, c);
        }
    }
}
