import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;




public class ConInfo {

public static void main(String[] args){

//JFrame Instance
JFrame frame = new JFrame();
frame.setTitle("CONTACT");
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.setResizable(false);
frame.setSize(400,500);
frame.setLayout(null);


//ImageIcon for the JFrame
ImageIcon cont = new ImageIcon("phone.png");
frame.setIconImage(cont.getImage());
frame.getContentPane().setBackground(new Color(0x56365c));


//Contact Us Label
JLabel profile = new JLabel();
profile.setText("CONTACT US");
profile.setForeground(new Color(0xffffff));
profile.setFont(new Font("Arial",Font.BOLD, 33));
profile.setBackground(new Color(0x56365c));
profile.setOpaque(true);
profile.setHorizontalAlignment(JLabel.CENTER);
profile.setVerticalTextPosition(JLabel.CENTER);
profile.setBounds(20,50,300,40);


// Name Entry Point
JLabel name = new JLabel();
name.setText("NAME");
name.setForeground(new Color(0xE6D9F2));
name.setFont(new Font("Arial", Font.BOLD, 12));
name.setBackground(new Color(0x56365c));
name.setOpaque(true);
name.setBounds(40, 110, 80, 20);


//Name TextField Entry
JTextField name1 = new JTextField();
name1.setText("Enter Your Full Name");
name1.setForeground(Color.GRAY);
name1.setFont(new Font("Arial", Font.BOLD, 12));
name1.setBackground(new Color(0xffffff));
name1.setOpaque(true);
name1.setBounds(40, 128, 230, 35);

//Name TextField Placeholder
name1.addFocusListener(new FocusListener() {


 @Override
            public void focusGained(FocusEvent e) {
                if (name1.getText().equals("Enter Your Full Name")) {
                    name1.setText("");
                    name1.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (name1.getText().isEmpty()) {
                    name1.setText("Enter Your Full Name");
                    name1.setForeground(Color.GRAY );
                }
            }
        });



// Email Entry Point
JLabel email = new JLabel();
email.setText("EMAIL");
email.setForeground(new Color(0xE6D9F2));
email.setFont(new Font("Arial", Font.BOLD, 12));
email.setBackground(new Color(0x56365c));
email.setOpaque(true);
email.setBounds(40, 167, 80, 35);

//Email TextField Entry
JTextField email1 = new JTextField();
email1.setText("Enter Your Email");
email1.setForeground(Color.GRAY);
email1.setFont(new Font("Arial", Font.BOLD, 12));
email1.setBackground(new Color(0xFFFFFF));
email1.setOpaque(true);
email1.setBounds(40, 194, 230, 35);

email1.addFocusListener(new FocusListener() {


 @Override
            public void focusGained(FocusEvent e) {
                if (email1.getText().equals("Enter Your Email")) {
                    email1.setText("");
                    email1.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (email1.getText().isEmpty()) {
                    email1.setText("Enter Your Email");
                    email1.setForeground(Color.GRAY );
                }
            }
        });

// Phone Entry Point
JLabel phone = new JLabel();
phone.setText("PHONE");
phone.setForeground(new Color(0xE6D9F2));
phone.setFont(new Font("Arial", Font.BOLD, 12));
phone.setBackground(new Color(0x56365c));
phone.setOpaque(true);
phone.setBounds(40,235, 80, 35);


//Phone TextField Entry
JTextField phnField = new JTextField();
 phnField.setText("Enter Your Phone Number");
 phnField .setForeground(Color.GRAY);
 phnField .setFont(new Font("Arial", Font.BOLD, 12));
 phnField .setBackground(new Color(0xFFFFFF));
 phnField .setOpaque(true);
 phnField .setBounds(40, 260, 230, 35);

 phnField.addKeyListener(new KeyAdapter() {
    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();

        // Block non-digits
        if (!Character.isDigit(c)) {
            e.consume();
            return;
        }

        // Limit to 10 digits
        if (phnField.getText().length() >= 10) {
            e.consume();
        }
    }
});


phnField.addFocusListener(new FocusListener() {


 @Override
            public void focusGained(FocusEvent e) {
                if (phnField.getText().equals("Enter Your Phone Number")) {
                    phnField.setText("");
                    phnField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (phnField.getText().isEmpty()) {
                    phnField.setText("Enter Your Phone Number");
                    phnField.setForeground(Color.GRAY );
                }
            }
        });


// Address Entry Point
JLabel address = new JLabel();
address.setText("ADDRESS");
address.setForeground(new Color(0xE6D9F2));
address.setFont(new Font("Arial", Font.BOLD, 12));
address.setBackground(new Color(0x56365c));
address.setOpaque(true);
address.setBounds(40,297, 80, 35);


//address TextField Entry
JTextField addr = new JTextField();
 addr.setText("Enter Your Home/Work Address");
 addr.setForeground(Color.GRAY);
 addr.setFont(new Font("Arial", Font.BOLD, 12));
 addr.setBackground(new Color(0xFFFFFF));
 addr.setOpaque(true);
 addr.setBounds(40,325, 230, 35);


 addr.addFocusListener(new FocusListener() {


 @Override
            public void focusGained(FocusEvent e) {
                if (addr.getText().equals("Enter Your Home/Work Address")) {
                    addr.setText("");
                    addr.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (addr.getText().isEmpty()) {
                    addr.setText("Enter Your Home/Work Address");
                    addr.setForeground(Color.GRAY );
                }
            }
        });




//send Button
JButton button = new JButton("SEND");
button.setForeground(new Color(0x2b0071));
button.setBackground(new Color(0xdcddff));
button.setBounds(90, 370, 80, 35);

button.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {

        String nameText  = name1.getText();
        String emailText = email1.getText();
        String phoneText = phnField.getText();
        String addressText = addr.getText();
        
        
        // Remove placeholders
        if (nameText.equals("Enter Your Full Name")) nameText = "";
        if (emailText.equals("Enter Your Email")) emailText = "";
        if (phoneText.equals("Enter Your Phone Number")) phoneText = "";
        if(addressText.equals("Enter Your Home/Work Address")) addressText="";

        // Validate empty fields
        if (nameText.isEmpty() || emailText.isEmpty() || phoneText.isEmpty()|| addressText.isEmpty()) {
            JOptionPane.showMessageDialog(
                frame,
                "Please fill in all fields",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Validate email
        if (!isValidEmail(emailText)) {
            JOptionPane.showMessageDialog(
                frame,
                "Invalid email – does not exist",
                "Email Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Validate phone number
        if (phoneText.length() != 10) {
            JOptionPane.showMessageDialog(
                frame,
                "Phone number must be exactly 10 digits",
                "Phone Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
       
    

        // Success message
        JOptionPane.showMessageDialog(
            frame,
            "Message sent successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
});



//frame Visibility
frame.add(addr);
frame.add(address);
frame.add(button);
frame.add(phnField);
frame.add(phone);
frame.add(email1);
frame.add(email);
frame.add(name1);
frame.add(name);
frame.add(profile);
frame.setVisible(true);
frame.requestFocusInWindow();

    }
       
    static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(regex);
    }
}
