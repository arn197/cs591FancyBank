import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CustomerDisplay {

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

    CustomerDisplay(Customer customer, ArrayList<Transaction> transactions, int flag) {
        ArrayList<JPanel> jPanels = new ArrayList<>();

        String[][] options = new String[][]{{"Close Account","View Account"},null,{"Request Loan","View Loan"}};
        if(flag == 0){
            options = new String[][]{null,null,null};
        }

        ArrayList<Account> accounts = customer.getAccounts();
        String[][] data = new String[accounts.size()][3];
        int i = 0;
        for(Account account: accounts){
            if(!account.isActive()) continue;
            data[i][0] = String.valueOf(account.getAccount_number());
            data[i][1] = account.getType();
            data[i][2] = String.valueOf(account.getBalance());
            i += 1;
        }

        String[] columnNames = {"Account Number", "Type", "Balance"};
        TableView accounts_table = new TableView(data, columnNames, "Accounts", options[0]);

        jPanels.add(accounts_table);

        data = new String[transactions.size()][4];
        i = 0;
        for(Transaction transaction: transactions){
            if(transaction.getSending_user_id() == customer.getCustomer_id()){
                data[i][0] = "Debit";
                if(transaction.getReceiving_account_id() == -1) data[i][1] = "Cash Withdrawal";
                else if(transaction.getReceiving_account_id() == -2) data[i][1] = "Bank Charge";
                else data[i][1] = String.valueOf(transaction.getReceiving_account_id());
                data[i][2] = String.valueOf(transaction.getSending_account_id());
            }
            else{
                data[i][0] = "Credit";
                if(transaction.getSending_account_id() == -1) data[i][1] = "Cash Deposit";
                else if(transaction.getSending_account_id() == -2) data[i][1] = "Bank Deposit";
                else data[i][1] = String.valueOf(transaction.getSending_account_id());
                data[i][2] = String.valueOf(transaction.getReceiving_account_id());
            }
            data[i][3] = String.valueOf(transaction.getAmount());
            i += 1;
        }
        columnNames = new String[]{"Type", "Src/Dest", "Account", "Amount"};
        TableView transactions_table = new TableView(data, columnNames, "Transactions", options[1]);
        jPanels.add(transactions_table);

        data = new String[customer.getLoans().size()][4];
        i = 0;
        for(Loan loan: customer.getLoans()){
            data[i][0] = String.valueOf(loan.getInitial_amount());
            data[i][1] = String.valueOf(loan.getDuration());
            data[i][2] = loan.getCollateral_name();
            data[i][3] = loan.getStatus();
            i+=1;
        }
        columnNames = new String[]{"Amount", "Duration", "Collateral", "Status"};
        TableView loans_table = new TableView(data, columnNames, "Loans", options[2]);
        jPanels.add(loans_table);

        String[] top_buttons;
        if(flag == 1) top_buttons = new String[]{"New Account","Transfer","Logout"};
        else top_buttons = new String[]{"Back"};

        ActionListener actionListener = actionEvent -> BankSystem.buttonPress(actionEvent.getActionCommand());

        ArrayList<Component> components = new ArrayList<>();
        for(String s: top_buttons){
            JButton jButton = new JButton(s);
            jButton.addActionListener(actionListener);
            components.add(jButton);
        }

        ButtonOptions buttonOptions = new ButtonOptions(components);

        this.name = customer.getCustomer_name();
        this.jPanels = jPanels;
        this.buttonOptions = buttonOptions;
    }
}
