import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.util.DnsSrv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.*;
import java.sql.*;

//extends JDialog
public class LoginForm extends JDialog{
    private JTextField lfEmail;
    private JPasswordField lfPassword;
    private JButton lfLoginbutton;
    private JButton lfcancelButton;
    private JPanel loginPanel;

    public LoginForm(JFrame parent){
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setModal(true);
        setMinimumSize(new Dimension(450,470));
        setLocationRelativeTo(parent);//set in the middle of the frame
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);//terminate if click the close button

        lfLoginbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = lfEmail.getText();
                String password = String.valueOf(lfPassword.getPassword());

                user = getAuthenticator(email,password);//method to check if this credentials is correct or not
                if (user != null){
                    dispose();//close login form if valid
                }
                else{
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Email or Password Invalid!",
                            "Try Again",
                            JOptionPane.ERROR_MESSAGE);// if not error
                }
            }
        });

        lfcancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);//make the dialog visible
    }
    public User user;
    private User getAuthenticator(String email, String Password){// returns a valid user if the user is found in database
        User user = null;

        final String DB_Url = "jdbc:mysql://localhost:3306/myguest"; //"jdbc:mysql://localhost:port/nameofsql"
        final String Username = "root";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection(DB_Url, Username, PASSWORD);
            //Connect to database!

            //query command
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,Password);

            ResultSet resultset = preparedStatement.executeQuery();// statement to execuite sql query

            if(resultset.next()){
                user = new User();
                user.name = resultset.getString("name");
                user.email = resultset.getString("email");
                user.phone = resultset.getString("phone");
                user.address = resultset.getString("address");
                user.password = resultset.getString("password");

            }
            //close database connection
            stmt.close();
            conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return user;


    }






    //run the login form
    public static void main(String[] args){
        LoginForm loginForm = new LoginForm(null);
        User user = loginForm.user;// if this user is a valid user , the user is authenticated
        if (user != null){
            System.out.println("Successfull Authentication of: " + user.name);
            System.out.println("Email: " + user.email);
            System.out.println("Phone: " + user.phone);
            System.out.println("Address: " + user.address);
        }
        else{
            System.out.println("Authentication Canceled!");
        }
    }
}
