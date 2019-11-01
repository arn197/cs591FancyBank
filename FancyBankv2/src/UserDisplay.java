import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UserDisplay extends JPanel {

    UserDisplay(String welcome, ArrayList<JPanel> jPanels, ButtonOptions buttonOptions) {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        JPanel top = new JPanel(new BorderLayout());

        JLabel welcome_text = new JLabel(welcome);
        welcome_text.setName(welcome);
        top.add(welcome_text, BorderLayout.LINE_START);

        top.add(buttonOptions, BorderLayout.LINE_END);

        c.anchor = GridBagConstraints.PAGE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.insets = new Insets(0,0,50,0);
        this.add(top, c);

        c.gridx = -1;
        c.gridy = 0;
        c.insets = new Insets(10,0,0,0);
        c.gridwidth = 1;

        for (JPanel jPanel: jPanels) {
            c.gridx += 1;
            c.gridx = c.gridx % 3;
            if (c.gridx == 0) c.gridy += 1;
            if (c.gridx == 0) c.insets = new Insets(10, 0, 0, 0);
            else c.insets = new Insets(10, 20, 0, 0);
            jPanel.setMinimumSize(new Dimension(200,200));
            this.add(jPanel, c);
        }
    }
}
