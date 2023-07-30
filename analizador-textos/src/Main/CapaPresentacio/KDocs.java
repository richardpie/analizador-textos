package Main.CapaPresentacio;

import Main.CapaDomini.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.TreeMap;

public class KDocs {
    private JComboBox autor;
    private JComboBox titol;
    private JButton cerca;
    private JTable documents;
    private JPanel PopUp;
    private JComboBox Ndocs;
    private JFrame marc1;
    private Object[][] dades;
    private boolean primer;
    private String aux;

    private void iniTable(String[] cols, Object[][] data){
        documents.setModel(new DefaultTableModel(data, cols));
    }

    private void marc(){
        marc1 = new JFrame("K Documents Semblants");
        marc1.getContentPane().setBackground(Color.orange);
        marc1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        marc1.setSize(500, 500);
        marc1.add(PopUp, BorderLayout.CENTER);
        marc1.setVisible(true);
    }

    void popUp(CtrlPresentacio ctrlPresentacio){
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

        int nDocuments = -1; //pq mai podem comparar més de tots els docs menys ell mateix
        for(String t : ctrlPresentacio.llistatTitols()){
            ++nDocuments;
        }
        while(nDocuments >= 0) {
            Ndocs.addItem(nDocuments);
            --nDocuments;
        }

        cerca.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String autorBox = (String) autor.getSelectedItem();
                String titolBox = (String) titol.getSelectedItem();
                Integer kBox = (Integer) Ndocs.getSelectedItem();
                dades = ctrlPresentacio.kDocs(autorBox, titolBox, kBox);
                String[] columnes = {"Similitud", "Titol", "Autor"};
                iniTable(columnes, dades);
            }
        });
        marc();
    }
}
