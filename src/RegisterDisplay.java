import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RegisterDisplay {
    private ArrayList<Component> components;
    private String name;
    private ButtonOptions buttonOptions;

    public ArrayList<Component> getComponents() {
        return components;
    }

    public String getName() {
        return name;
    }

    public ButtonOptions getButtonOptions() {
        return buttonOptions;
    }

    RegisterDisplay(ArrayList<String> account_types, Customer customer){
        components = new ArrayList<>();

        JLabel jLabel = new JLabel("Create new account");
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);

        components.add(jLabel);

        JTextField customer_name = new JTextField(20);
        customer_name.setName(customer.getCustomer_name());
        customer_name.setText(customer_name.getName());
        customer_name.setEditable(false);
        customer_name.setFocusable(false);

        components.add(customer_name);

        JTextField user_name = new JTextField(20);
        user_name.setName(customer.getCustomer_username());
        user_name.setText(user_name.getName());
        user_name.setEditable(false);
        user_name.setFocusable(false);

        components.add(user_name);

        JFormattedTextField balance = new JFormattedTextField();
        balance.setName("Please enter the starting balance");
        balance.setText(balance.getName());
        balance.addFocusListener(BankDisplay.focusListenerText);

        components.add(balance);

        ButtonGroup type = new ButtonGroup();
        int count = 0;
        for(String s: account_types){
            JRadioButton jRadioButton = new JRadioButton(s);
            jRadioButton.setActionCommand(s);
            if(count == 0){
                jRadioButton.setSelected(true);
            }
            components.add(jRadioButton);
            type.add(jRadioButton);
            count += 1;
        }

        ActionListener actionListener = actionEvent -> {
            String command = actionEvent.getActionCommand();
            jLabel.setText("Create new account");
            if (command.equals("Back")) {
                BankSystem.customer_interface(-1);
            } else {
                double bal = 0;
                try {
                    bal = Double.parseDouble(balance.getText());
                } catch (Exception e) {
                    bal = -1;
                }
                String t = type.getSelection().getActionCommand();
                String response = BankSystem.newAccount(bal, t);
                if (response.equals("success")) {
                    BankSystem.customer_interface(-1);
                } else {
                    jLabel.setText(response);
                }
            }
        };

        JButton submit = new JButton("Submit");
        submit.addActionListener(actionListener);

        components.add(submit);

        JButton back = new JButton("Back");
        back.addActionListener(actionListener);

        components.add(back);
    }

    RegisterDisplay(int manager){
        components = new ArrayList<>();

        JLabel jLabel = new JLabel("Login");
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);

        components.add(jLabel);

        JTextField user_name = new JTextField(20);
        user_name.setName("Please enter your username");
        user_name.setText(user_name.getName());
        user_name.addFocusListener(BankDisplay.focusListenerText);

        components.add(user_name);

        JPasswordField jPasswordField = new JPasswordField(20);
        jPasswordField.addFocusListener(BankDisplay.focusListenerPassword);
        jPasswordField.setName("Please enter your password");
        jPasswordField.setEchoChar((char)0);
        jPasswordField.setText(jPasswordField.getName());

        components.add(jPasswordField);

        ActionListener actionListener = actionEvent -> {
            if(actionEvent.getActionCommand().equals("Back")){
                BankSystem.home_interface();
            }
            int flag = 0;
            String username = user_name.getText();
            String password = String.valueOf(jPasswordField.getPassword());
            jLabel.setText("Login");
            String error = "Login";
            if(username.length() == 0){
                flag = 1;
                error = "Username cannot be empty";
            }
            else{
                boolean response = BankSystem.login(username, password, manager);
                if(!response){
                    flag = 1;
                    error = "Wrong details";
                }
            }
            if(flag == 1){
                jLabel.setText(error);
            }
        };

        JButton jButton = new JButton("Submit");
        jButton.setName("0");
        jButton.addActionListener(actionListener);

        components.add(jButton);

        jButton = new JButton("Back");
        jButton.addActionListener(actionListener);

        components.add(jButton);

    }

    RegisterDisplay(ArrayList<String> account_types){
        components = new ArrayList<>();

        JLabel jLabel = new JLabel("Register");
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);

        components.add(jLabel);

        JTextField customer_name = new JTextField(20);
        customer_name.setName("Please enter your full name");
        customer_name.setText(customer_name.getName());
        customer_name.addFocusListener(BankDisplay.focusListenerText);

        components.add(customer_name);

        JTextField user_name = new JTextField(20);
        user_name.setName("Please enter a username");
        user_name.setText(user_name.getName());
        user_name.addFocusListener(BankDisplay.focusListenerText);

        components.add(user_name);

        JPasswordField jPasswordField = new JPasswordField(20);
        jPasswordField.addFocusListener(BankDisplay.focusListenerPassword);
        jPasswordField.setName("Please enter a password");
        jPasswordField.setEchoChar((char)0);
        jPasswordField.setText(jPasswordField.getName());

        components.add(jPasswordField);

        JPasswordField jPasswordFieldAgain = new JPasswordField(20);
        jPasswordFieldAgain.setName("Please re-enter the password");
        jPasswordFieldAgain.addFocusListener(BankDisplay.focusListenerPassword);
        jPasswordFieldAgain.setEchoChar((char)0);
        jPasswordFieldAgain.setText(jPasswordFieldAgain.getName());

        components.add(jPasswordFieldAgain);

        JFormattedTextField balance = new JFormattedTextField();
        balance.setName("Please enter the starting balance");
        balance.setText(balance.getName());
        balance.addFocusListener(BankDisplay.focusListenerText);

        components.add(balance);

        ButtonGroup type = new ButtonGroup();
        int count = 0;
        for(String s: account_types){
            JRadioButton jRadioButton = new JRadioButton(s);
            jRadioButton.setActionCommand(s);
            if(count == 0){
                jRadioButton.setSelected(true);
            }
            components.add(jRadioButton);
            type.add(jRadioButton);
            count += 1;
        }

        ActionListener actionListener = actionEvent -> {
            if(actionEvent.getActionCommand().equals("Back")){
                BankSystem.home_interface();
            }
            int flag = 0;
            double bal = 0;
            try{
                bal = Double.parseDouble(balance.getText());
            }catch(Exception e){
                bal = -1;
            }

            String t = type.getSelection().getActionCommand();
            String username = user_name.getText();
            String customerName = customer_name.getText();
            String password = String.valueOf(jPasswordField.getPassword());
            String password_again = String.valueOf(jPasswordFieldAgain.getPassword());
            jLabel.setText("Register");
            String error = "Register";

            if(customerName.length() == 0 || customerName.equals(customer_name.getName())){
                flag = 1;
                error = "Please enter your full name";
            }
            else if(username.length() == 0 || username.equals(user_name.getName())){
                flag = 1;
                error = "Username cannot be empty";
            }
            else if(!password.equals(password_again)){
                flag = 1;
                error = "Your passwords do not match";
            }
            else{
                String response = BankSystem.registerNewUser(customerName, username, password, bal, t);
                if(response.equals("success")){
                    BankSystem.login_interface(1);
                }
                else {
                    flag = 1;
                    error = response;
                }
            }
            if(flag == 1){
                jLabel.setText(error);
            }
        };

        JButton submit = new JButton("Submit");
        submit.addActionListener(actionListener);

        components.add(submit);

        JButton back = new JButton("Back");
        back.addActionListener(actionListener);

        components.add(back);
    }
}
