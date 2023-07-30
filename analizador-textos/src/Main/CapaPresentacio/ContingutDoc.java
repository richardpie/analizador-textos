package Main.CapaPresentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContingutDoc {
    private JPanel popUp;
    private JTextArea text;
    private JComboBox titol;
    private JComboBox autor;
    private JButton cerca;
    private JFrame marcC;
    private boolean primer;
    private String aux;
    private void marc(){
        marcC = new JFrame("Contingut Documents");
        marcC.getContentPane().setBackground(Color.orange);
        marcC.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        marcC.setSize(500, 500);
        marcC.add(popUp, BorderLayout.CENTER);
        marcC.setVisible(true);
    }
    private void iniEscena(CtrlPresentacio ctrlPresentacio){
        for(String a : ctrlPresentacio.llistatAutors()){
            autor.addItem(a);
        }
        String at = (String)autor.getSelectedItem();
        for(String ti: ctrlPresentacio.llistarPerTitol(at)){
            titol.addItem(ti);
        }
        //per no poder seleccionar un titol que no sigui de l'autor seleccionat
        autor.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(primer){
                    aux = (String)autor.getSelectedItem();
                }
                String a = (String)autor.getSelectedItem();
                if(!a.equals(aux) || primer) {
                    titol.removeAllItems();
                    for (String t : ctrlPresentacio.llistarPerTitol(a)) {
                        titol.addItem(t);
                    }
                }
                aux = (String)autor.getSelectedItem();
                primer = false;
            }
        });
        text.setEditable(false);
    }
    public void popUp(CtrlPresentacio ctrlPresentacio){
        iniEscena(ctrlPresentacio);
        cerca.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String autorBox = (String) autor.getSelectedItem();
                String titolBox = (String) titol.getSelectedItem();
                String contigut = ctrlPresentacio.llistarContingut(autorBox, titolBox);
                text.setText(contigut);
            }
        });
        marc();
    }
}
