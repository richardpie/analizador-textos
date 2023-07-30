package Main.CapaPresentacio;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.Set;
import java.util.Vector;

public class ExpressionsBooleanes {
    private JButton buscar;
    private JButton guardar;
    private JButton eliminar;
    private JButton modificar;
    private JPanel inici;
    private JTextField input;
    private JFrame  marc;
    private JList expressions;
    private JPanel PanellExpressions;
    private JList LlistatDocuments;

    private void inicialitzarPopUp(CtrlPresentacio ctrlPresentacio){
        marc = new JFrame("Sel·lecció de document");
        marc.getContentPane().setBackground(Color.orange);
        marc.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        marc.setSize(500, 500);
        llistaExpressions(ctrlPresentacio.llistat_expresions());
        marc.add(PanellExpressions, BorderLayout.CENTER);
        marc.setVisible(true);
    }

    private void llistaExpressions(Vector<String> expre){
        DefaultListModel<String> model = new DefaultListModel<String>();
        for(String t : expre){
            model.addElement(t);
        }
        expressions.setModel(model);
    }

    public void search(CtrlPresentacio ctrlPresentacio) {
        inicialitzarPopUp(ctrlPresentacio);
        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String expressio = input.getText();
                if(expressio != null) {
                    LlistatDocuments.setListData(buscar_docs(expressio, ctrlPresentacio));
                }

            }
        });

        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String exp = input.getText();
                try {
                    ctrlPresentacio.altaExpressio(exp);
                    expressions.setListData(CtrlPresentacio.llistat_expresions());
                    input.setText(null);

                } catch (Exception ex) {
                    String missatge = "Error: L'expressió ja existeix";
                    JOptionPane.showMessageDialog(new JFrame(), missatge, "ERROR",
                            JOptionPane.ERROR_MESSAGE);

                }
            }
        });

        expressions.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()){
                    String expressio_seleccionada = (String) expressions.getSelectedValue();
                    if(expressio_seleccionada != null){
                        netejar_llista_doc();
                        selecciona_expre(expressio_seleccionada, ctrlPresentacio);
                    }
                }
            }
        });
    }

    void selecciona_expre(String expressio, CtrlPresentacio ctrlPresentacio){
        JFrame seleccio = new JFrame();
        eliminar = new JButton("Eliminar");
        modificar = new JButton("Modificar");
        JButton buscar2 = new JButton("Buscar");
        JLabel text = new JLabel(expressio);
        seleccio.add(text);
        seleccio.add(buscar2);
        seleccio.add(modificar);
        seleccio.add(eliminar);

        seleccio.setLayout(new GridBagLayout());
        seleccio.setSize(500,200);
        seleccio.setVisible(true);

        modificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                modificar_expressio(expressio,ctrlPresentacio);
                netejar_llista_doc();
            }
        });

        eliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ctrlPresentacio.baixaExpressio((String) expressions.getSelectedValue());
                    expressions.setListData(ctrlPresentacio.llistat_expresions());
                    seleccio.dispatchEvent(new WindowEvent(seleccio, WindowEvent.WINDOW_CLOSING));
                    netejar_llista_doc();

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        buscar2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LlistatDocuments.setListData(buscar_docs(expressio, ctrlPresentacio));
                seleccio.dispatchEvent(new WindowEvent(seleccio, WindowEvent.WINDOW_CLOSING));
            }
        });
    }

    void modificar_expressio(String expressio,CtrlPresentacio ctrlPresentacio){
        JFrame modif = new JFrame("Modificar");
        JPanel panel = new JPanel();
        JTextField nova_expre = new JTextField(null,20);
        JButton canviar = new JButton("Guardar");
        panel.add(nova_expre);
        panel.add(canviar);
        modif.add(panel);
        modif.setSize(200,200);
        modif.setLayout(new GridLayout(1,2));
        modif.setVisible(true);

        canviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String novaexpressio = nova_expre.getText();
                try {
                    ctrlPresentacio.modificarExpressio(expressio,novaexpressio);
                    expressions.setListData(ctrlPresentacio.llistat_expresions());
                    modif.setVisible(false);
                    modif.dispatchEvent(new WindowEvent(modif, WindowEvent.WINDOW_CLOSING));

                } catch (Exception ex) {
                    String missatge = "Error: La nova expressió ja existeix";
                    JOptionPane.showMessageDialog(new JFrame(), missatge, "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    Vector<String> buscar_docs(String expressio,CtrlPresentacio ctrlPresentacio){
        Set<String> docs = ctrlPresentacio.llistatDocumentsBooleans(expressio);
        Vector<String> vec =  new Vector<>();
        for(String doc : docs) {
            vec.add(doc);
        }
        return vec;
    }

    void netejar_llista_doc(){
        LlistatDocuments.setListData(new Vector<>());
    }
}
