import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TableView extends JPanel {
    private JLabel title;
    private JTable jTable;
    private JPanel buttons;

    public JTable getjTable() {
        return jTable;
    }

    public void setjTable(JTable jTable) {
        this.jTable = jTable;
    }

    public JLabel getTitle() {
        return title;
    }

    public void setTitle(JLabel title) {
        this.title = title;
    }

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int pos = jTable.getSelectedRow();
            title.setText(title.getName());
            if(pos < 0 && (!actionEvent.getActionCommand().contains("Pay")
                    && !actionEvent.getActionCommand().contains("Request")
                    && !actionEvent.getActionCommand().contains("Get Report")
                    && !actionEvent.getActionCommand().contains("Edit")
                    && !actionEvent.getActionCommand().contains("Delete"))){
                title.setText("Please select a row");
            }
            else BankSystem.tableButtonPress(actionEvent.getActionCommand(), jTable);
        }
    };

    TableView(String[][] data, String[] columns, String title_text, String[] buttonOptions){
        this.setLayout(new BorderLayout());
        this.title = new JLabel(title_text);
        this.title.setName(title_text);
        this.jTable = new JTable(data, columns);
        JPanel temp = new JPanel();
        temp.setLayout(new BorderLayout());
        temp.add(this.title, BorderLayout.PAGE_START);
        temp.add(jTable.getTableHeader(), BorderLayout.CENTER);
        this.add(temp, BorderLayout.PAGE_START);
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jScrollPane.setPreferredSize(new Dimension(400,400));
        this.add(jScrollPane, BorderLayout.CENTER);

        this.buttons = new JPanel();
        this.buttons.setLayout(new FlowLayout());
        if(buttonOptions != null){
            for(String s: buttonOptions){
                JButton jButton = new JButton(s);
                jButton.setActionCommand(s);
                jButton.addActionListener(actionListener);
                this.buttons.add(jButton);
            }
            this.add(this.buttons, BorderLayout.PAGE_END);
        }
    }
}
