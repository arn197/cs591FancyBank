import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

public class BankDisplay extends JFrame {
    private int width;
    private int height;
    private JPanel rootPanel;

    public static FocusListener focusListenerText = new FocusListener() {
        @Override
        public void focusGained(FocusEvent focusEvent) {
            JTextField jTextField = (JTextField) focusEvent.getSource();
            if(jTextField.getText().equals(jTextField.getName())) jTextField.setText("");
        }

        @Override
        public void focusLost(FocusEvent focusEvent) {
            JTextField jTextField = (JTextField) focusEvent.getSource();
            if(jTextField.getText().length() == 0){
                jTextField.setText(jTextField.getName());
            }
        }
    };

    public static FocusListener focusListenerPassword = new FocusListener() {
        @Override
        public void focusGained(FocusEvent focusEvent) {
            JPasswordField jPasswordField = (JPasswordField) focusEvent.getSource();
            String pass = String.valueOf(jPasswordField.getPassword());
            if(pass.equals(jPasswordField.getName())){
                jPasswordField.setEchoChar('*');
                jPasswordField.setText("");
            }
        }

        @Override
        public void focusLost(FocusEvent focusEvent) {
            JPasswordField jPasswordField = (JPasswordField) focusEvent.getSource();
            String pass = String.valueOf(jPasswordField.getPassword());
            if(pass.length() == 0){
                jPasswordField.setEchoChar((char)0);
                jPasswordField.setText(jPasswordField.getName());
            }
        }
    };

    BankDisplay(Bank bank, int width, int height) {
        this.width = width;
        this.height = height;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(bank.getBank_name());
        this.setVisible(true);
        this.setSize(width, height);
        this.rootPanel = new JPanel();
        this.rootPanel.setSize(width, height);
        this.rootPanel.setLayout(new BorderLayout());
        this.add(rootPanel);

        this.welcome();
    }

    public void welcome(){

        ArrayList<Component> components = new ArrayList<>();

        JLabel jLabel = new JLabel("Welcome!");
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        components.add(jLabel);

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(actionEvent.getActionCommand().equals("Login")){
                    BankSystem.login_interface(1);
                }
                else if(actionEvent.getActionCommand().equals("Create a new account")){
                    BankSystem.register_interface();
                }
                else if(actionEvent.getActionCommand().equals("Manager Login")){
                    BankSystem.login_interface(0);
                }
            }
        };

        String[] options = new String[]{"Login","Create a new account","Manager Login"};
        for(int i = 0;i<options.length;i++){
            JButton jButton = new JButton(options[i]);
            jButton.addActionListener(actionListener);
            components.add(jButton);
        }

        FormDisplay formDisplay = new FormDisplay(components);
        this.changeDisplay(formDisplay);
    }


    public void report(ArrayList<Transaction> transactions){
        ManagerDisplay reportDisplay = new ManagerDisplay(transactions);

        FormDisplay formDisplay = new FormDisplay(reportDisplay.getComponents());
        this.changeDisplay(formDisplay);
    }

    public void newAccount(ArrayList<String> account_types, Customer customer){
        RegisterDisplay newAccountDisplay = new RegisterDisplay(account_types, customer);

        FormDisplay formDisplay = new FormDisplay(newAccountDisplay.getComponents());
        this.changeDisplay(formDisplay);
    }

    public void login(int manager){
        RegisterDisplay loginDisplay = new RegisterDisplay(manager);

        FormDisplay formDisplay = new FormDisplay(loginDisplay.getComponents());
        this.changeDisplay(formDisplay);
    }

    public void register(ArrayList<String> account_types){
        RegisterDisplay registerDisplay = new RegisterDisplay(account_types);

        FormDisplay formDisplay = new FormDisplay(registerDisplay.getComponents());
        this.changeDisplay(formDisplay);
    }

    public void customer(Customer customer, ArrayList<Transaction> transactions, int flag){
        CustomerDisplay customerDisplay = new CustomerDisplay(customer, transactions, flag);
        UserDisplay userDisplay = new UserDisplay(customerDisplay.getName(), customerDisplay.getjPanels(), customerDisplay.getButtonOptions());
        this.changeDisplay(userDisplay);
    }

    public void managerSettings(double min_balance, double interest_rate, double loan_interest_rate, double service_charge, double high_balance){
        ManagerDisplay managerSettingsDisplay = new ManagerDisplay(min_balance, interest_rate, loan_interest_rate, service_charge, high_balance);

        FormDisplay formDisplay = new FormDisplay(managerSettingsDisplay.getComponents());
        this.changeDisplay(formDisplay);
    }

    public void viewLoan(Customer customer, Loan loan, double interest_rate){
        LoanDisplay loanViewDisplay = new LoanDisplay(customer, loan, interest_rate);

        FormDisplay formDisplay = new FormDisplay(loanViewDisplay.getComponents());
        this.changeDisplay(formDisplay);
    }

    public void alert(String msg){
        JOptionPane.showMessageDialog(this.rootPanel, msg);
    }

    public void transfer(Customer customer){
        TransferDisplay transferDisplay = new TransferDisplay(customer);
        UserDisplay userDisplay = new UserDisplay(transferDisplay.getName(), transferDisplay.getjPanels(), transferDisplay.getButtonOptions());
        this.changeDisplay(userDisplay);
    }

    public void viewAccount(Account account, ArrayList<Transaction> transactions, Customer customer){
        AccountDisplay accountDisplay = new AccountDisplay(account, transactions, customer);
        this.changeDisplay(accountDisplay);
    }

    public void loan(Customer customer){
        LoanDisplay loanDisplay = new LoanDisplay(customer);

        FormDisplay formDisplay = new FormDisplay(loanDisplay.getComponents());
        this.changeDisplay(formDisplay);
    }

    public void manager(Manager manager, ArrayList<Customer> customers, ArrayList<Transaction> transactions, ArrayList<Loan> loans){
        ManagerDisplay managerDisplay = new ManagerDisplay(manager, customers, transactions, loans);
        UserDisplay userDisplay = new UserDisplay(managerDisplay.getName(), managerDisplay.getjPanels(), managerDisplay.getButtonOptions());
        this.changeDisplay(userDisplay);
    }

    public void changeDisplay(JPanel jPanel){
        this.rootPanel.removeAll();
        this.rootPanel.add(jPanel, BorderLayout.CENTER);
        this.rootPanel.revalidate();
        this.rootPanel.repaint();
    }
}
