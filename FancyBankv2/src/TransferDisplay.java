import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TransferDisplay {
    private ArrayList<JPanel> jPanels;
    private String name;
    private ButtonOptions buttonOptions;

    public ArrayList<JPanel> getjPanels() {
        return jPanels;
    }

    public String getName() {
        return name;
    }

    public ButtonOptions getButtonOptions() {
        return buttonOptions;
    }

    TransferDisplay(Customer customer){
        ArrayList<JPanel> components = new ArrayList<>();

        ArrayList<Account> accounts = customer.getAccounts();
        int count = 0;
        for(Account account: accounts){
            if(account.getType().equals("Security")) continue;
            count++;
        }
        String[][] data = new String[count][3];
        int i = 0;
        for(Account account: accounts){
            if(account.getType().equals("Security")) continue;
            data[i][0] = String.valueOf(account.getAccount_number());
            data[i][1] = account.getType();
            data[i][2] = String.valueOf(account.getBalance());
            i += 1;
        }

        String[] columnNames = { "Account Number", "Type", "Balance" };

        JLabel warning = new JLabel("");

        TableView source = new TableView(data, columnNames, "Source Account", null);

        components.add(source);

        TableView dest = new TableView(data, columnNames, "Destination Account", null);

        components.add(dest);

        JFormattedTextField balance = new JFormattedTextField();
        balance.setName("Please enter transfer amount");
        balance.setText(balance.getName());
        balance.addFocusListener(BankDisplay.focusListenerText);

        String[] options = new String[]{"Transfer","Back"};
        ActionListener actionListener = actionEvent -> {
            if(actionEvent.getActionCommand().equals("Back")){
                BankSystem.customer_interface(-1);
            }
            else{
                warning.setText("");
                double bal = 0;
                try{
                    bal = Double.parseDouble(balance.getText());
                }catch(Exception e){
                    bal = -1;
                }
                int d = dest.getjTable().getSelectedRow();
                int s = source.getjTable().getSelectedRow();
                if(s < 0){
                    warning.setText("Please select a source account");
                }
                else if(d < 0){
                    warning.setText("Please select a destination account");
                }
                else if(s == d){
                    warning.setText("Accounts are same");
                }
                else if(bal <= 0){
                    warning.setText("Amount must be > 0");
                }
                else if(BankSystem.transfer(s, d, bal)){
                    warning.setText("Success");
                    BankSystem.customer_interface(-1);
                }
                else warning.setText("Not enough balance");
            }
        };

        ArrayList<Component> top_options = new ArrayList<>();

        top_options.add(warning);
        top_options.add(balance);
        for(String s: options){
            JButton jButton = new JButton(s);
            jButton.addActionListener(actionListener);
            top_options.add(jButton);
        }

        ButtonOptions buttonOptions = new ButtonOptions(top_options);

        this.name = customer.getCustomer_name();
        this.jPanels = components;
        this.buttonOptions = buttonOptions;
    }
}
