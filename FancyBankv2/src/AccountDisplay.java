import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AccountDisplay extends JPanel {
    private JLabel account_number;
    private JLabel balance;
    private JLabel warning;
    private TableView current_stocks;
    private TableView avail_stocks;
    private JTextField buy_amount;
    private JTextField sell_amount;
    private JComboBox<String> paying;
    private JComboBox<String> receiving;
    private JComboBox<String> currencies;
    private TableView transactions_table;
    private ArrayList<JPanel> jPanels;
    private JTextField amount;
    private Account account;
    private ButtonOptions buttonOptions;
    private String name;

    public ArrayList<JPanel> getjPanels() {
        return jPanels;
    }

    public void setjPanels(ArrayList<JPanel> jPanels) {
        this.jPanels = jPanels;
    }

    public ButtonOptions getButtonOptions() {
        return buttonOptions;
    }

    public void setButtonOptions(ButtonOptions buttonOptions) {
        this.buttonOptions = buttonOptions;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    private ActionListener actionListenerSecurity = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(actionEvent.getActionCommand().equals("Back")){
                BankSystem.customer_interface(-1);
            }
            else{
                current_stocks.getTitle().setText(current_stocks.getTitle().getName());
                avail_stocks.getTitle().setText(avail_stocks.getTitle().getName());

                String t = actionEvent.getActionCommand();
                if(t.equals("Sell")){
                    double bal = 0;
                    try{
                        bal = Double.parseDouble(sell_amount.getText());
                    }catch(Exception e){
                        bal = -1;
                    }

                    if(bal <= 0){
                        current_stocks.getTitle().setText("Please enter an integer amount above 0");
                        sell_amount.setText(sell_amount.getName());
                        return;
                    }
                    int pos = current_stocks.getjTable().getSelectedRow();
                    if(pos == -1){
                        current_stocks.getTitle().setText("Please select a row");
                        return;
                    }
                    if(!BankSystem.sellStock(bal, account.getAccount_number(), pos, paying.getSelectedIndex())){
                        current_stocks.getTitle().setText("Not enough stock");
                        return;
                    }
                    BankSystem.viewAccount(account);
                }
                else{
                    double bal = 0;
                    try{
                        bal = Double.parseDouble(buy_amount.getText());
                    }catch(Exception e){
                        bal = -1;
                    }
                    if(bal <= 0){
                        avail_stocks.getTitle().setText("Please enter an integer amount above 0");
                        buy_amount.setText(buy_amount.getName());
                        return;
                    }
                    int pos = avail_stocks.getjTable().getSelectedRow();
                    if(pos == -1){
                        avail_stocks.getTitle().setText("Please select a row");
                        return;
                    }
                    if(!BankSystem.buyStock(bal, account.getAccount_number(), pos, receiving.getSelectedIndex())){
                        avail_stocks.getTitle().setText("Not enough stock available / Low acc balance");
                        return;
                    }
                    BankSystem.viewAccount(account);
                }
            }
            buy_amount.setText(buy_amount.getName());
            sell_amount.setText(sell_amount.getName());
        }
    };

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

    AccountDisplay(SecurityAccount account, ArrayList<StockTransaction> transactions, Customer customer, ArrayList<Stock> available_stocks, ArrayList<Account> accounts){
        this.account = account;
        ArrayList<JPanel> jPanels = new ArrayList<>();

        String[][] options = new String[][]{null,null,null};

        String[][] data = new String[account.getStocks().size()][4];
        int i = 0;
        for(Stock stock: account.getStocks()){
            data[i][0] = stock.getCode();
            data[i][1] = String.valueOf(stock.getValue());
            data[i][2] = String.valueOf(stock.getN_stocks());
            data[i][3] = String.valueOf(stock.getValue() * stock.getN_stocks());
            i += 1;
        }

        String[] columnNames = {"Stock code", "Value", "Number", "Total Value"};
        current_stocks = new TableView(data, columnNames, "Bought stocks", options[0]);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        jPanel.add(current_stocks, c);

        JPanel bottom = new JPanel(new FlowLayout());
        sell_amount = new JTextField("Please enter amount");
        sell_amount.setName("Please enter amount");
        sell_amount.addFocusListener(BankDisplay.focusListenerText);
        bottom.add(sell_amount);

        ArrayList<String> paying_accs = new ArrayList<>();
        for(Account account1: accounts){
            if(account1.getType().equals("Security")) continue;
            paying_accs.add(account1.getType() + " " + account1.getBalance());
        }

        String[] accs = paying_accs.toArray(new String[0]);
        paying = new JComboBox<>(accs);
        bottom.add(paying);

        JButton jButton = new JButton("Sell");
        jButton.addActionListener(actionListenerSecurity);

        bottom.add(jButton);

        c.gridx = 0;
        c.gridy += 1;
        jPanel.add(bottom, c);

        jPanels.add(jPanel);

        data = new String[available_stocks.size()][3];
        i = 0;
        for(Stock stock: available_stocks){
            data[i][0] = stock.getCode();
            data[i][1] = String.valueOf(stock.getValue());
            data[i][2] = String.valueOf(stock.getN_stocks());
            i += 1;
        }

        columnNames = new String[]{"Stock code", "Value", "Number"};
        avail_stocks = new TableView(data, columnNames, "Available Stocks", options[1]);

        jPanel = new JPanel();
        jPanel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        jPanel.add(avail_stocks, c);

        bottom = new JPanel(new FlowLayout());
        buy_amount = new JTextField("Please enter amount");
        buy_amount.setName("Please enter amount");
        buy_amount.addFocusListener(BankDisplay.focusListenerText);
        bottom.add(buy_amount);

        receiving = new JComboBox<>(accs);
        bottom.add(receiving);

        jButton = new JButton("Buy");
        jButton.addActionListener(actionListenerSecurity);

        bottom.add(jButton);

        c.gridx = 0;
        c.gridy += 1;
        jPanel.add(bottom, c);

        jPanels.add(jPanel);

        data = new String[transactions.size()][4];
        i = 0;
        for(StockTransaction transaction: transactions){
            if(transaction.getSending_user_id() == customer.getCustomer_id()){
                data[i][0] = "Buy";
            }
            else{
                data[i][0] = "Sell";
            }
            data[i][1] = transaction.getCode();
            data[i][2] = String.valueOf(transaction.getN_stock());
            data[i][3] = String.valueOf(transaction.getAmount());
            i += 1;
        }
        columnNames = new String[]{"Type", "Code", "Number", "Amount"};

        TableView transactions_table = new TableView(data, columnNames, "Transactions", options[2]);
        jPanels.add(transactions_table);

        this.name = customer.getCustomer_name();
        this.jPanels = jPanels;

        String[] top_buttons = new String[]{"Back"};
        ActionListener actionListener = actionEvent -> BankSystem.buttonPress(actionEvent.getActionCommand());
        ArrayList<Component> components = new ArrayList<>();
        for(String s: top_buttons){
            jButton = new JButton(s);
            jButton.addActionListener(actionListenerSecurity);
            components.add(jButton);
        }

        ButtonOptions buttonOptions = new ButtonOptions(components);

        this.buttonOptions = buttonOptions;
    }

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
                else if(transaction.getReceiving_account_id() == -3) data[i][1] = "Stock Purchase";
                else if(transaction.getReceiving_account_id() == -4) data[i][1] = "Trading Fees";
                else data[i][1] = String.valueOf(transaction.getReceiving_account_id());
                data[i][2] = String.valueOf(transaction.getSending_account_id());
            }
            else{
                data[i][0] = "Credit";
                if(transaction.getSending_account_id() == -1) data[i][1] = "Cash Deposit";
                else if(transaction.getSending_account_id() == -2) data[i][1] = "Bank Deposit";
                else if(transaction.getSending_account_id() == -3) data[i][1] = "Stock Purchase";
                else if(transaction.getSending_account_id() == -4) data[i][1] = "Trading Fees";
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
