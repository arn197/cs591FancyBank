import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LoanDisplay {
    ArrayList<Component> components;

    public ArrayList<Component> getComponents() {
        return components;
    }

    //For the view loan function
    LoanDisplay(Customer customer, Loan loan, double interest_rate){
        components = new ArrayList<>();
        JLabel jLabel = new JLabel("View Loan");
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);

        components.add(jLabel);

        JLabel initial = new JLabel("Initial amount is " + loan.getInitial_amount());
        initial.setHorizontalAlignment(SwingConstants.CENTER);

        components.add(initial);

        JLabel balance = new JLabel("Loan balance is " + loan.getBalance());
        balance.setHorizontalAlignment(SwingConstants.CENTER);

        components.add(balance);

        JLabel interest = new JLabel("Loan interest rate is " + interest_rate);
        interest.setHorizontalAlignment(SwingConstants.CENTER);

        components.add(interest);

        JLabel collateral_name = new JLabel("Your collateral is " + loan.getCollateral_name());
        collateral_name.setHorizontalAlignment(SwingConstants.CENTER);

        components.add(collateral_name);

        JLabel collateral_amount = new JLabel("Value of your collateral " + loan.getCollateral_amount());
        collateral_amount.setHorizontalAlignment(SwingConstants.CENTER);

        components.add(collateral_amount);

        double p = loan.getInitial_amount() * loan.getInterest_rate()/100.0;
        if(p > loan.getBalance()) p = loan.getBalance();
        JLabel next_payment;
        if(p == 0) next_payment = new JLabel("No payment due");
        else next_payment = new JLabel("Next payment due is " + p);
        next_payment.setHorizontalAlignment(SwingConstants.CENTER);

        if(loan.getStatus().equals("Active")) components.add(next_payment);

        String[] options = new String[customer.getAccounts().size()];
        int i = 0;
        for(Account account: customer.getAccounts()){
            options[i] = account.getType() + " " + account.getAccount_number() + " [Balance " + account.getBalance() + "]";
            i += 1;
        }
        JComboBox<String> jComboBox = new JComboBox<>(options);

        if(loan.getStatus().equals("Active")) components.add(jComboBox);

        double finalP = p;
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                jLabel.setText("View Loan");
                if(actionEvent.getActionCommand().equals("Back")){
                    BankSystem.customer_interface(-1);
                }
                else{
                    int ac_no = jComboBox.getSelectedIndex();
                    if(!BankSystem.payLoan(finalP, ac_no, loan)){
                        jLabel.setText("Not enough balance");
                    }
                    else{
                        BankSystem.viewLoan(customer, loan, interest_rate);
                    }
                }
            }
        };

        JButton jButton = new JButton("Make Payment");
        jButton.addActionListener(actionListener);

        if(p > 0 && loan.getStatus().equals("Active")) components.add(jButton);

        jButton = new JButton("Back");
        jButton.addActionListener(actionListener);

        components.add(jButton);

    }

    //For viewing the table of loans
    LoanDisplay(Customer customer){
        ArrayList<Component> components = new ArrayList<>();

        JLabel jLabel = new JLabel("Request loan");
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);

        components.add(jLabel);

        JTextField customer_name = new JTextField();
        customer_name.setName(customer.getCustomer_name());
        customer_name.setText(customer_name.getName());
        customer_name.setEditable(false);
        customer_name.setFocusable(false);

        components.add(customer_name);

        JTextField collateral_name = new JTextField();
        collateral_name.setName("Please enter the name of the collateral (e.g. house)");
        collateral_name.setText(collateral_name.getName());
        collateral_name.addFocusListener(BankDisplay.focusListenerText);

        components.add(collateral_name);

        JFormattedTextField collateral_amount = new JFormattedTextField();
        collateral_amount.setName("Please enter the amount of the collateral");
        collateral_amount.setText(collateral_amount.getName());
        collateral_amount.addFocusListener(BankDisplay.focusListenerText);

        components.add(collateral_amount);

        JFormattedTextField balance = new JFormattedTextField();
        balance.setName("Please enter the loan amount");
        balance.setText(balance.getName());
        balance.addFocusListener(BankDisplay.focusListenerText);

        components.add(balance);

        JFormattedTextField duration = new JFormattedTextField();
        duration.setName("Please enter the loan duration (months)");
        duration.setText(duration.getName());
        duration.addFocusListener(BankDisplay.focusListenerText);

        components.add(duration);

        ActionListener actionListener = actionEvent -> {
            String command = actionEvent.getActionCommand();
            jLabel.setText("Request loan");
            if (command.equals("Back")) {
                BankSystem.customer_interface(-1);
            } else {
                double bal = 0;
                try {
                    bal = Double.parseDouble(balance.getText());
                } catch (Exception e) {
                    bal = -1;
                }
                int dur = 0;
                try {
                    dur = Integer.parseInt(duration.getText());
                } catch (Exception e) {
                    dur = -1;
                }
                double col_amount = 0;
                try{
                    col_amount = Double.parseDouble(collateral_amount.getText());
                }
                catch(Exception e){
                    col_amount = -1;
                }

                if(collateral_name.getText().length() == 0 || collateral_name.getText().equals(collateral_name.getName())){
                    jLabel.setText("Please enter a collateral name");
                }
                else if(col_amount <= 0){
                    jLabel.setText("Please enter a collateral amount");
                }
                else if(bal <= 0){
                    jLabel.setText("Please enter a loan amount above 0");
                }
                else if(dur <= 0){
                    jLabel.setText("Please enter a valid loan duration");
                }
                else{
                    BankSystem.requestLoan(bal, dur, collateral_name.getText(), col_amount);
                    BankSystem.customer_interface(-1);
                }
            }
        };

        JButton submit = new JButton("Submit");
        submit.addActionListener(actionListener);

        components.add(submit);

        JButton back = new JButton("Back");
        back.addActionListener(actionListener);

        components.add(back);

        this.components = components;
    }
}
