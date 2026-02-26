package bank.management.system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame {

    JTextField cardField;
    JPasswordField pinField;

    public Login() {

        setTitle("National Trust Bank - Secure ATM");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Light Gradient Background
        JPanel background = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(220, 235, 255),
                        0, getHeight(), Color.WHITE);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        background.setLayout(new GridBagLayout());
        setContentPane(background);

        // Rounded Card Panel
        JPanel cardPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow
                g2.setColor(new Color(0,0,0,40));
                g2.fillRoundRect(8,8,getWidth()-16,getHeight()-16,25,25);

                // Card
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0,0,getWidth()-16,getHeight()-16,25,25);
            }
        };

        cardPanel.setOpaque(false);
        cardPanel.setPreferredSize(new Dimension(420, 450));
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(new EmptyBorder(40,40,40,40));

        // Logo
        ImageIcon logoIcon = new ImageIcon(
                ClassLoader.getSystemResource("icons/logo.jpg"));
        Image logoImg = logoIcon.getImage()
                .getScaledInstance(90,90,Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(logoImg));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel bankName = new JLabel("NATIONAL TRUST BANK");
        bankName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        bankName.setForeground(new Color(0,70,140));
        bankName.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardPanel.add(logo);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(bankName);
        cardPanel.add(Box.createVerticalStrut(30));

        // Card Number
        JLabel cardLabel = new JLabel("Card Number");
        cardLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cardPanel.add(cardLabel);

        cardField = new JTextField();
        cardField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cardPanel.add(cardField);

        // Auto Format XXXX XXXX XXXX XXXX
        cardField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = cardField.getText().replaceAll("\\s","");
                if(text.length() > 16) text = text.substring(0,16);

                StringBuilder formatted = new StringBuilder();
                for(int i=0;i<text.length();i++){
                    if(i>0 && i%4==0) formatted.append(" ");
                    formatted.append(text.charAt(i));
                }
                cardField.setText(formatted.toString());
            }
        });

        cardPanel.add(Box.createVerticalStrut(20));

        // PIN
        JLabel pinLabel = new JLabel("PIN");
        pinLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cardPanel.add(pinLabel);

        pinField = new JPasswordField();
        pinField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cardPanel.add(pinField);

        cardPanel.add(Box.createVerticalStrut(30));

        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBackground(new Color(0, 120, 215));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        cardPanel.add(loginBtn);

        cardPanel.add(Box.createVerticalStrut(15));

        JButton signupBtn = new JButton("Create Account");
        signupBtn.setBorderPainted(false);
        signupBtn.setContentAreaFilled(false);
        signupBtn.setForeground(new Color(0,120,215));
        signupBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardPanel.add(signupBtn);

        cardPanel.add(Box.createVerticalStrut(20));

        JLabel secure = new JLabel("🔒 Secured by 256-bit encryption");
        secure.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        secure.setForeground(Color.GRAY);
        secure.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(secure);

        background.add(cardPanel);

        loginBtn.addActionListener(e -> authenticate());
        signupBtn.addActionListener(e -> {
            setVisible(false);
            new Signup();
        });

        setVisible(true);
    }

    private void authenticate() {

        String card = cardField.getText().replaceAll("\\s","");
        String pin = new String(pinField.getPassword());

        if(card.isEmpty() || pin.isEmpty()){
            JOptionPane.showMessageDialog(this,"All fields required");
            return;
        }

        try {
            Conn c = new Conn();
            PreparedStatement ps =
                    c.c.prepareStatement(
                            "SELECT * FROM login WHERE cardnumber=? AND pin=?");

            ps.setString(1, card);
            ps.setString(2, pin);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                setVisible(false);
                new Transactions(card);
            } else {
                JOptionPane.showMessageDialog(this,"Invalid credentials");
            }

        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}