import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AccountDisplay extends JPanel {
    private JLabel account_number;
    private JLabel balance;
    private JLabel warning;
    private JComboBox<String> currencies;
    private TableView transactions_table;
    private JTextField amount;
    private Account account;

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(actionEvent.getActionCommand().equals("Back")){
                BankSystem.customer_interface(-1);
            }
            else{
                warning.setText("");
                double bal = 0;
                try{
                    bal = Double.parseDouble(amount.getText());
                }catch(Exception e){
                    bal = -1;
                }
                if(bal <= 0){
                    warning.setText("Please enter a balance above 0");
                    amount.setText(amount.getName());
                    return;
                }

                bal = BankSystem.currency_rate(currencies.getSelectedIndex(), bal);

                String t = actionEvent.getActionCommand();
                if(t.equals("Deposit")){
                    if(!BankSystem.deposit(bal, account.getAccount_number())) warning.setText("Not enough balance");
                    else BankSystem.viewAccount(account);
                }else{
                    if(!BankSystem.withdraw(bal, account.getAccount_number())) warning.setText("Not enough balance");
                    else BankSystem.viewAccount(account);
                }
                amount.setText(amount.getName());
            }
        }
    };

    AccountDisplay(Account account, ArrayList<Transaction> transactions, Customer customer){
        this.account = account;
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        JPanel top = new JPanel(new BorderLayout());
        this.account_number = new JLabel("Account number - " + account.getAccount_number());
        top.add(account_number, BorderLayout.PAGE_START);
        this.balance = new JLabel("Balance - " + account.getBalance());
        this.warning = new JLabel();
        top.add(balance, BorderLayout.CENTER);
        top.add(warning, BorderLayout.PAGE_END);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        this.add(top, c);

        String[][] data = new String[transactions.size()][4];
        int i = 0;
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
        String[] columnNames = new String[]{"Type", "Src/Dest", "Account", "Amount"};

        currencies = new JComboBox<>(BankSystem.currencies());

        c.gridx = 0;
        c.gridy = 2;
        this.add(currencies, c);

        transactions_table = new TableView(data, columnNames, "Transactions", null);
        c.gridx = 0;
        c.gridy += 1;
        c.gridwidth = 3;
        this.add(transactions_table, c);
        c.gridwidth = 1;

        JPanel bottom = new JPanel(new FlowLayout());
        amount = new JTextField("Please enter amount");
        amount.setName("Please enter amount");
        amount.addFocusListener(BankDisplay.focusListenerText);
        bottom.add(amount);


        c.gridx = 0;
        c.gridy += 1;
        this.add(bottom, c);

        JButton jButton = new JButton("Deposit");
        jButton.addActionListener(actionListener);
        c.gridx = 1;
        this.add(jButton, c);

        jButton = new JButton("Withdraw");
        jButton.addActionListener(actionListener);
        c.gridx = 2;
        this.add(jButton, c);

        jButton = new JButton("Back");
        jButton.addActionListener(actionListener);
        c.gridx = 0;
        c.gridy += 1;
        c.gridwidth = 3;
        this.add(jButton, c);
    }
}
