import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManagerDisplay {
    private ArrayList<JPanel> jPanels;
    private String name;
    private ButtonOptions buttonOptions;
    private ArrayList<Component> components;

    //For report of transactions
    ManagerDisplay(ArrayList<Transaction> transactions) {
        ArrayList<Component> components = new ArrayList<>();
        String[][] data = new String[transactions.size()][3];
        int i = 0;
        for (Transaction transaction : transactions) {
            data[i][0] = String.valueOf(transaction.getAmount());
            data[i][1] = String.valueOf(transaction.getSending_user_id());
            data[i][2] = String.valueOf(transaction.getReceiving_user_id());
            if (data[i][1].equals("-2")) {
                data[i][1] = "Bank";
            } else if (data[i][1].equals("-1")) {
                data[i][1] = "Cash";
            }
            if (data[i][2].equals("-2")) {
                data[i][2] = "Bank";
            } else if (data[i][2].equals("-1")) {
                data[i][2] = "Cash";
            }
            i += 1;
        }
        String[] columnNames = new String[]{"Amount", "Sender", "Receiver"};
        TableView transactions_table = new TableView(data, columnNames, "Transactions", null);
        components.add(transactions_table);

        ActionListener actionListener = actionEvent -> {
            if (actionEvent.getActionCommand().equals("Back")) {
                BankSystem.manager_interface();
            }
        };

        JButton back = new JButton("Back");
        back.addActionListener(actionListener);

        components.add(back);

        this.components = components;
    }

    //Manager settings display
    ManagerDisplay(double min_balance, double interest_rate, double loan_interest_rate, double service_charge, double high_balance) {
        components = new ArrayList<>();
        String[] values = new String[]{"Minimum account opening balance", "Interest rate for accounts", "Interest rate for loans", "Service charge for transactions", "High balance threshold"};
        Double[] d_values = new Double[]{min_balance, interest_rate, loan_interest_rate, service_charge, high_balance};

        JLabel settings = new JLabel("Bank Settings - Enter new values and press Done");
        components.add(settings);

        ArrayList<JFormattedTextField> jFormattedTextFields = new ArrayList<>();

        for (int i = 0; i < values.length; i++) {
            JLabel jLabel = new JLabel(values[i]);
            jLabel.setHorizontalAlignment(SwingConstants.CENTER);

            components.add(jLabel);

            JFormattedTextField val = new JFormattedTextField();
            val.setName(String.valueOf(i));
            val.setText(String.valueOf(d_values[i]));
            val.addFocusListener(BankDisplay.focusListenerText);

            components.add(val);
            jFormattedTextFields.add(val);
        }

        ActionListener actionListener = actionEvent -> {
            if (actionEvent.getActionCommand().equals("Done")) {
                settings.setText("Bank Settings - Enter new values and press Done");
                int i = 0;
                Double[] new_values = new Double[jFormattedTextFields.size()];
                for (; i < jFormattedTextFields.size(); i++) {
                    double bal = 0;
                    try {
                        bal = Double.parseDouble(jFormattedTextFields.get(i).getText());
                    } catch (Exception e) {
                        bal = -1;
                    }
                    if (bal <= 0) {
                        settings.setText("Please select a valid value for \"" + values[i] + "\"");
                        break;
                    } else new_values[i] = bal;
                }
                if (i == jFormattedTextFields.size()) {
                    BankSystem.updateSettings(new_values);
                    BankSystem.manager_interface();
                }
            }
        };

        JButton jButton = new JButton("Done");
        jButton.addActionListener(actionListener);
        components.add(jButton);

    }

    //Main manager display
    ManagerDisplay(Manager manager, ArrayList<Customer> customers, ArrayList<Transaction> transactions, ArrayList<Loan> loans) {
        ArrayList<JPanel> jPanels = new ArrayList<>();

        String[][] options = new String[][]{{"View Customer", "Pay Interest"}, {"Get Report"}, {"Approve Loan", "Decline Loan"}};
        String[][] data = new String[customers.size()][2];
        int i = 0;
        for (Customer customer : customers) {
            data[i][0] = String.valueOf(customer.getCustomer_name());
            int count = 0;
            for (Account account : customer.getAccounts()) {
                count += account.getBalance();
            }
            data[i][1] = String.valueOf(count);
            i += 1;
        }
        String[] columnNames = {"Customer", "Total balance"};
        TableView customer_table = new TableView(data, columnNames, "Customers", options[0]);
        jPanels.add(customer_table);

        data = new String[transactions.size()][3];
        i = 0;
        for (Transaction transaction : transactions) {
            data[i][0] = String.valueOf(transaction.getAmount());
            data[i][1] = String.valueOf(transaction.getSending_user_id());
            data[i][2] = String.valueOf(transaction.getReceiving_user_id());
            if (data[i][1].equals("-2")) {
                data[i][1] = "Bank";
            } else if (data[i][1].equals("-1")) {
                data[i][1] = "Cash";
            } else if (data[i][1].equals("-3")) {
                data[i][1] = "Stock Purchase";
            } else if (data[i][1].equals("-4")) {
                data[i][1] = "Trading Fees";
            }
            if (data[i][2].equals("-2")) {
                data[i][2] = "Bank";
            } else if (data[i][2].equals("-1")) {
                data[i][2] = "Cash";
            } else if (data[i][2].equals("-3")) {
                data[i][2] = "Stock Purchase";
            } else if (data[i][2].equals("-4")) {
                data[i][2] = "Trading Fees";
            }
            i += 1;
        }
        columnNames = new String[]{"Amount", "Sender", "Receiver"};
        TableView transactions_table = new TableView(data, columnNames, "Transactions", options[1]);
        jPanels.add(transactions_table);

        data = new String[loans.size()][3];
        i = 0;
        for (Loan loan : loans) {
            data[i][0] = loan.getCollateral_name() + "(" + loan.getCollateral_amount() + ")";
            data[i][1] = String.valueOf(loan.getInitial_amount());
            data[i][2] = loan.getStatus();
            i += 1;
        }
        columnNames = new String[]{"Collateral(Value)", "Loan Amount", "Status"};
        TableView loans_table = new TableView(data, columnNames, "Loans", options[2]);
        jPanels.add(loans_table);

        String[] top_buttons = new String[]{"Stock Market", "Bank Settings", "Logout"};
        ActionListener actionListener = actionEvent -> {
            try {
                BankSystem.buttonPress(actionEvent.getActionCommand());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        ArrayList<Component> components = new ArrayList<>();
        for (String s : top_buttons) {
            JButton jButton = new JButton(s);
            jButton.addActionListener(actionListener);
            components.add(jButton);
        }

        ButtonOptions buttonOptions = new ButtonOptions(components);
        this.name = manager.getManager_name();
        this.jPanels = jPanels;
        this.buttonOptions = buttonOptions;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public ArrayList<JPanel> getjPanels() {
        return jPanels;
    }

    public String getName() {
        return name;
    }

    public ButtonOptions getButtonOptions() {
        return buttonOptions;
    }
}
