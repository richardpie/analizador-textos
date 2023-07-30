package Main.CapaPresentacio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CercaAutors {
    private JFrame main;
    private JPanel cercaAutors;
    private JTextField autor;
    private JButton cerca;
    private JTextArea llistatAutors;

    private void llistaAutors(String autors){
        llistatAutors.setEditable(false);
        llistatAutors.setText(autors);
    }
    void inicialitzarPopUp(){
        main = new JFrame("Cercar per prefix Autor");
        main.setSize(500, 500);
        main.add(cercaAutors, BorderLayout.CENTER);
        main.setVisible(true);
    }
    void run(CtrlPresentacio ctrlPresentacio){
        cerca.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String a = autor.getText();
                    String autors = ctrlPresentacio.llistarAutorPrefix(a);
                    llistaAutors(autors);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(new JFrame(), ex , "ERROR AUTOR",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        inicialitzarPopUp();
    }
}
