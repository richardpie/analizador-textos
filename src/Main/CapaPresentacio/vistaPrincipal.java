package Main.CapaPresentacio;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class vistaPrincipal extends JFrame {
    private JFrame marc = new JFrame("GESTOR DE TEXTOS");
    private CtrlPresentacio controlador;
    private JPanel PanelPrincipal;
    private JTextArea TextPrincipal;
    private JPanel PanelText;
    private JButton guardar;
    private JMenuBar barraMenu;
    private JMenu arxiu, cerques;
    private JMenuItem expressionsBooleanes, contingutDoc, similitudDoc, titolsAutor, prefixAutor, carregar, obrir, eliminarDocument, recuperarDocument;
    static String autorObert;
    static String titolObert;
    static boolean activat = false;

    private void afegirItems(JMenu menu, List<JMenuItem> items){
        for (JMenuItem item : items){
            menu.add(item);
        }
    }

    private void inicialitzarMarc(){
        marc.setSize(500, 500);
        marc.setLayout(new GridLayout(1, 1));
        marc.add(PanelPrincipal, BorderLayout.CENTER);
    }

    private void inicialitzarArxiu(){
        arxiu = new JMenu("Arxiu");
        List arxius = new ArrayList<JMenuItem>();
        carregar = new JMenuItem("Carregar Document");
        arxius.add(carregar);
        obrir = new JMenuItem("Obrir Document");
        arxius.add(obrir);
        eliminarDocument = new JMenuItem("Eliminar Document");
        eliminarDocument.setBackground(Color.RED);
        arxius.add(eliminarDocument);
        recuperarDocument = new JMenuItem("Recuperar Document");
        arxius.add(recuperarDocument);
        afegirItems(arxiu, arxius);
    }

    private void inicialitzarCerca(){
        cerques = new JMenu("Cerques");
        List buscar = new ArrayList<JMenuItem>();
        expressionsBooleanes = new JMenuItem("Expressions Booleanes");
        buscar.add(expressionsBooleanes);
        contingutDoc = new JMenuItem("Contingut Document");
        buscar.add(contingutDoc);
        similitudDoc = new JMenuItem("K Documents Semblants");
        buscar.add(similitudDoc);
        titolsAutor = new JMenuItem("Títols de l'autor");
        buscar.add(titolsAutor);
        prefixAutor = new JMenuItem("Autors per prefix");
        buscar.add(prefixAutor);
        afegirItems(cerques, buscar);
    }

    private void iniVista(){
        inicialitzarMarc();
        //crea Barmenu
        barraMenu = new JMenuBar();
        //Apartat arxiu
        inicialitzarArxiu();
        //Apartat Cerca
        inicialitzarCerca();

        //afegir a la barra de menu
        barraMenu.add(arxiu);
        barraMenu.add(cerques);

        marc.setJMenuBar(barraMenu);
    }

    public void menuPrincipal(){
        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controlador.guardarDocumet(autorObert, titolObert, TextPrincipal.getText());
                    String missatge = "Document guardat correctement";
                    JOptionPane.showMessageDialog(new JFrame(), missatge, "DOCUMENT GUARDAT CORRECTEMENT",
                            JOptionPane.INFORMATION_MESSAGE);
                }catch (Exception ex) {
                    String missatge = "Error: Document no guardat correctement";
                    JOptionPane.showMessageDialog(new JFrame(), missatge, "DOCUMENT NO GUARDAT",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        eliminarDocument.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SeleccioDocument seleccioDocument = new SeleccioDocument();
                seleccioDocument.popUp(controlador, TextPrincipal, "eliminar");
            }
        });

        recuperarDocument.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SeleccioDocument select = new SeleccioDocument();
                select.popUp(controlador, TextPrincipal, "recuperar");
            }
        });

        obrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SeleccioDocument select = new SeleccioDocument();
                select.popUp(controlador, TextPrincipal, "obrir");
            }
        });

        carregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fixer = new JFileChooser();
                FileNameExtensionFilter filtre = new FileNameExtensionFilter("txt i xml", "txt", "xml");
                fixer.setFileFilter(filtre);
                fixer.setAcceptAllFileFilterUsed(false);
                fixer.setMultiSelectionEnabled(true);
                if (fixer.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    boolean correcte = true;
                    File[] docs = fixer.getSelectedFiles();
                    for (File doc : docs) {
                        String path = doc.getAbsolutePath();
                        try {
                            controlador.altaDocument(path);
                        } catch (Exception ex) {
                            String missatge = "Error: El document no és correcte o ja existeix en el sistema";
                            JOptionPane.showMessageDialog(new JFrame(), missatge, "DOCUMENT NO CARREGAT", JOptionPane.ERROR_MESSAGE);
                            correcte = false;
                        }
                    }
                    if (correcte) {
                        JOptionPane.showMessageDialog(null, "Documents carregat/s correctement");
                    }
                }
            }
        });

        expressionsBooleanes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExpressionsBooleanes bools = new ExpressionsBooleanes();
                bools.search(controlador);
            }
        });

        similitudDoc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!controlador.llistatAutors().isEmpty()) {
                    KDocs kDocs = new KDocs();
                    kDocs.popUp(controlador);
                }
                else {
                    String missatge = "Error: No hi ha documents al sistema";
                    JOptionPane.showMessageDialog(new JFrame(), missatge, "NO HI HA DOCUMENTS", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        contingutDoc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ContingutDoc cDoc = new ContingutDoc();
                if (!controlador.llistatAutors().isEmpty()) {
                    cDoc.popUp(controlador);
                }
                else {
                    String missatge = "Error: No hi ha documents al sistema";
                    JOptionPane.showMessageDialog(new JFrame(), missatge, "NO HI HA DOCUMENTS", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        prefixAutor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!controlador.llistatAutors().isEmpty()) {
                    CercaAutors autors = new CercaAutors();
                    autors.run(controlador);
                }
                else{
                    String missatge = "Error: No hi ha documents al sistema";
                    JOptionPane.showMessageDialog(new JFrame(), missatge, "NO HI HA DOCUMENTS", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        titolsAutor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!controlador.llistatAutors().isEmpty()) {
                    TitolsAutors titols = new TitolsAutors();
                    titols.popUp(controlador);
                }
                else{
                    String missatge = "Error: No hi ha documents al sistema";
                    JOptionPane.showMessageDialog(new JFrame(), missatge, "NO HI HA DOCUMENTS", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public vistaPrincipal(CtrlPresentacio controladorP){
        controlador = controladorP;
        iniVista();
    }

    public void ferVisible(){
        menuPrincipal();
        marc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marc.setVisible(true);
    }
}
