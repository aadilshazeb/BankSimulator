package bank.management.system;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class Pin extends JFrame implements ActionListener {

    JPasswordField t1,t2;
    JButton b1,b2;
    JLabel l1,l2,l3;
    String cardno;   // IMPORTANT: store card number

    Pin(String cardno){
        this.cardno = cardno;

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1000,1180,Image.SCALE_DEFAULT);
        JLabel bg = new JLabel(new ImageIcon(i2));
        bg.setBounds(0,0,960,1080);
        add(bg);

        l1 = new JLabel("CHANGE YOUR PIN");
        l1.setForeground(Color.WHITE);
        l1.setBounds(280,330,400,35);
        bg.add(l1);

        l2 = new JLabel("New PIN:");
        l2.setForeground(Color.WHITE);
        l2.setBounds(180,390,150,35);
        bg.add(l2);

        l3 = new JLabel("Re-Enter New PIN:");
        l3.setForeground(Color.WHITE);
        l3.setBounds(180,440,200,35);
        bg.add(l3);

        t1 = new JPasswordField();
        t1.setBounds(350,390,180,25);
        bg.add(t1);

        t2 = new JPasswordField();
        t2.setBounds(350,440,180,25);
        bg.add(t2);

        b1 = new JButton("CHANGE");
        b1.setBounds(390,588,150,35);
        bg.add(b1);

        b2 = new JButton("BACK");
        b2.setBounds(390,633,150,35);
        bg.add(b2);

        b1.addActionListener(this);
        b2.addActionListener(this);

        setSize(960,1080);
        setLocation(500,0);
        setUndecorated(true);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae){

        try{

            String npin = new String(t1.getPassword());
            String rpin = new String(t2.getPassword());

            if(ae.getSource()==b1){

                if(npin.isEmpty() || rpin.isEmpty()){
                    JOptionPane.showMessageDialog(null,"Please fill both fields");
                    return;
                }

                if(!npin.equals(rpin)){
                    JOptionPane.showMessageDialog(null,"PIN does not match");
                    return;
                }

                if(!npin.matches("\\d{4}")){
                    JOptionPane.showMessageDialog(null,"PIN must be 4 digits");
                    return;
                }

                Conn c = new Conn();

                // UPDATE ONLY LOGIN TABLE
                String q = "update login set pin='"+rpin+"' where cardnumber='"+cardno+"'";
                c.s.executeUpdate(q);

                JOptionPane.showMessageDialog(null,"PIN Changed Successfully");

                setVisible(false);
                new Transactions(cardno);

            } else if(ae.getSource()==b2){

                setVisible(false);
                new Transactions(cardno);
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        new Pin("12345678");
    }
}