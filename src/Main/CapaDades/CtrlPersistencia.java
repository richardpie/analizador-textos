package Main.CapaDades;

import Main.CapaDomini.Carpeta;

import java.io.*;
import java.util.*;
import java.io.IOException;

public class CtrlPersistencia {
    GestorED gestor = new GestorED();
    public CtrlPersistencia(){

    };
    public void inicialitza() throws IOException {

        gestor.inicialitza();

    };
    public void escriurePostingList(String autor, String titol, String paraula){
        gestor.escriurePostingList(autor, titol, paraula);
    }
    public void borrarPostingList() {
        gestor.borrarPostingList();
    }
    /*public ArrayList<String>getPostingWords(){
        return gestor.getPostingPalabras();
    }
    public ArrayList<String>getPostingAutorTitols(){
        return gestor.getPostingAutors_titols();
    }*/

    public void actualitzarDocument(String autor, String titol, String nou_contingut, String path,
                                    HashMap <String, Integer> tf_hash_to_save) {
        gestor.actualitzarDocument(autor, titol, nou_contingut, path, tf_hash_to_save);
    }


    public void afegirDocument(String path){

        try {
            InteraccioFitxers.importaDocument(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    };

    public void baixaDocument(String path) throws IOException {
        InteraccioFitxers.eliminaDocument(path);

    };

    public void recuperar_document(String path) throws IOException {

        InteraccioFitxers.recuperaDocument(path);


    }

    public void modificarDocument(String path, String contenido){
        if(path.endsWith("txt")){
            InteraccioFitxers interaccioFitxerstxt = new InteraccioTXT();
            interaccioFitxerstxt.modificaDocument(path, contenido);}

        if(path.endsWith("xml")){
            InteraccioFitxers interaccioFitxersxml = new InteraccioXML();
            interaccioFitxersxml.modificaDocument(path, contenido);
        }
    }

    public Vector<String> getPaths() {
        return gestor.getPaths();
    }
    public Vector<String> getAutors() {
        return gestor.getAutors();
    }
    public Vector<String> getTitols() {
        return gestor.getTitols();
    }
    public Vector<String> getContinguts() {
        return gestor.getContinguts();
    }
    public List<HashMap<String, Integer>> getTf_dades() {
        return gestor.getTf_dades();
    }

    public void ExtractWordsPosting() throws IOException{
        //gestor.ExtractWordsPosting();
    };
}
