package Main.CapaPresentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Vector;

public class SeleccioDocument {

    private JComboBox autor;
    private JComboBox titol;
    private JButton select;
    private JPanel popUp;
    private JFrame marc;
    private boolean primer;
    private String aux;
    public String autor_obtingut;
    public String titol_obtingut;

    private void inicialitzarPopUp(){
        marc = new JFrame("Selecció de document");
        marc.getContentPane().setBackground(Color.orange);
        marc.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        marc.setSize(500, 500);
        marc.add(popUp, BorderLayout.CENTER);
        marc.setVisible(true);
    }
    private void inicialitzarComboBox(CtrlPresentacio ctrlPresentacio, String accio){
        if(accio .equals("recuperar")) {
                for (String a : ctrlPresentacio.llistatAutorsEliminats()) {
                    autor.addItem(a);
                }
                String at = (String) autor.getSelectedItem();
                for (String ti : ctrlPresentacio.llistarPerTitolEliminats(at)) {
                    titol.addItem(ti);
                }
        }
        else{
                for (String a : ctrlPresentacio.llistatAutors()) {
                    autor.addItem(a);
                }
                String at = (String) autor.getSelectedItem();
                for (String ti : ctrlPresentacio.llistarPerTitol(at)) {
                    titol.addItem(ti);
                }
        }
    }

    public void popUp(CtrlPresentacio ctrlPresentacio, JTextArea textArea, String accio){
        if(ctrlPresentacio.llistatAutorsEliminats().isEmpty() && ctrlPresentacio.llistatAutors().isEmpty()){
            String missatge = "No hi ha documents en el sistema";
            JOptionPane.showMessageDialog(new JFrame(), missatge, "DOCUMENT NO ELIMINAT",
                    JOptionPane.ERROR_MESSAGE);
            marc.dispatchEvent(new WindowEvent(marc, WindowEvent.WINDOW_CLOSING));
        }
        else {
            inicialitzarComboBox(ctrlPresentacio, accio);
            inicialitzarPopUp();
            //per no poder seleccionar un titol que no sigui de l'autor seleccionat
            autor.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (primer) {
                        aux = (String) autor.getSelectedItem();
                    }
                    String a = (String) autor.getSelectedItem();
                    if (!a.equals(aux) || primer) {
                        titol.removeAllItems();
                        for (String t : ctrlPresentacio.llistarPerTitol(a)) {
                            titol.addItem(t);
                        }
                    }
                    aux = (String) autor.getSelectedItem();
                    autor_obtingut = (String) autor.getSelectedItem();
                    titol_obtingut = (String) titol.getSelectedItem();
                    primer = false;
                }
            });

            select.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (accio.equals("obrir")) {
                        vistaPrincipal.autorObert = (String) autor.getSelectedItem();
                        vistaPrincipal.titolObert = (String) titol.getSelectedItem();
                        String contingut = ctrlPresentacio.llistarContingut((String) autor.getSelectedItem(), (String) titol.getSelectedItem());
                        textArea.setText(contingut);
                    } else if (accio.equals("eliminar")) {
                        try {
                            ctrlPresentacio.eliminarDocument((String) autor.getSelectedItem(), (String) titol.getSelectedItem());
                            String missatge = "Document: " + (String) titol.getSelectedItem() + ", " + autor.getSelectedItem() + " eliminat";
                            JOptionPane.showMessageDialog(new JFrame(), missatge, "DOCUMENT NO ELIMINAT",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            String missatge = "Error: Document " + (String) titol.getSelectedItem() + ", " + autor.getSelectedItem() + " no eliminat";
                            JOptionPane.showMessageDialog(new JFrame(), missatge, "DOCUMENT NO ELIMINAT",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (accio.equals("recuperar")) {
                        try {
                            ctrlPresentacio.recuperarDocument((String) autor.getSelectedItem(), (String) titol.getSelectedItem());
                            String missatge = "Document: " + (String) titol.getSelectedItem() + ", " + autor.getSelectedItem() + " recuperat";
                            JOptionPane.showMessageDialog(new JFrame(), missatge, "DOCUMENT RECUPERAT",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            String missatge = "Error: Document " + (String) titol.getSelectedItem() + ", " + autor.getSelectedItem() + " no recuperat";
                            JOptionPane.showMessageDialog(new JFrame(), missatge, "DOCUMENT NO RECUPERAT",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    marc.dispose();
                }
            });
        }
    }
}
