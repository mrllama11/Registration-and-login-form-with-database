import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.io.*;
import java.sql.*;

public class RegistrationForm extends JDialog {
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JTextField tfAddress;
    private JPasswordField pfPassword;
    private JPasswordField cpfPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registerPanel;

    public RegistrationForm(JFrame parent){
        super(parent);
        setTitle("Create a new account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450,400));
        setModal((true));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void registerUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String address = tfAddress.getText();
        String password = String.valueOf(pfPassword.getPassword());
        String confirm_password = String.valueOf((cpfPassword.getPassword()));

        //check if all the fields is answered
        if(name.isEmpty()||email.isEmpty()||phone.isEmpty()||address.isEmpty()||address.isEmpty()||password.isEmpty()||confirm_password.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Please enter all the required fields!",
                    "Try Again",JOptionPane.ERROR_MESSAGE);
            return;

        //check if the password equals confirm password
        }
        if(!password.equals(confirm_password)){
            JOptionPane.showMessageDialog(this,
                    "Confirm password does not match!",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //add user to database
        user = addUserToDatabase(name,email,phone,address,password);

        //if valid user close registration , if not error
        if (user!=null){
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Failed to Register New User",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    public User user;
    //add user to database if succeed returns a valid user to database if not return null
    private User addUserToDatabase(String name, String email, String phone, String address, String password){
        User user = null;
        final String DB_Url = "jdbc:mysql://localhost:3306/myguest"; //"jdbc:mysql://localhost:port/nameofsql"
        final String Username = "root";
        final String Password = "";

        try{
            Connection conn = DriverManager.getConnection(DB_Url,Username,Password);
            //Connect to database!

            //query command
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO users(name,email,phone,address,password)"+"VALUES(?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,phone);
            preparedStatement.setString(4,address);
            preparedStatement.setString(5,password);

            //insert row into table
            int addedRows = preparedStatement.executeUpdate();
            if(addedRows > 0){
                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.address = address;
                user.password = password;
            }
            stmt.close();
            conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String[] args){
        JFrame parent = null;
        RegistrationForm myform = new RegistrationForm(parent);
        User user = myform.user;
        if (user != null){
            System.out.println("Succesfull registration of: "+ user.name);
        }
        else {
            System.out.println("Registration cancelled");
        }
    }
}
