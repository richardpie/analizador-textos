package Main.CapaPresentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class TitolsAutors {
    private JComboBox autors;
    private JButton cerca;
    private JPanel titolsAutor;
    private JList llistatTitols;
    private JFrame marc;

    private void llistaTitols(Set<String> titolsAutor){
        DefaultListModel<String> model = new DefaultListModel<String>();
        for(String t : titolsAutor){
            System.out.println(t);
            model.addElement(t);
        }
        llistatTitols.setModel(model);
    }

    void inicialitzarPopUp(CtrlPresentacio ctrlPresentacio){
        boolean entra = false;
        for(String a : ctrlPresentacio.llistatAutors()){
            entra = true;
            autors.addItem(a);
        }
        if(!entra){
            JOptionPane.showMessageDialog(new JFrame(), "No hi ha documents amb autor indexats" , "ERROR AUTOR",
                    JOptionPane.ERROR_MESSAGE);
        }
        else {
            marc = new JFrame("Cercar per prefix Autor");
            marc.setSize(500, 500);
            marc.add(titolsAutor, BorderLayout.CENTER);
            marc.setVisible(true);
        }
    }

    void popUp(CtrlPresentacio ctrlPresentacio){
        cerca.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String a = (String) autors.getSelectedItem();
                Set<String> titols = ctrlPresentacio.llistarPerTitol(a);
                llistaTitols(titols);
            }
        });

        inicialitzarPopUp(ctrlPresentacio);
    }
}
