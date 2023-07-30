package Main.CapaDades;

import Main.CapaDomini.Carpeta;
import Main.CapaDomini.Document;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class GestorED {
    private Vector<String> autors = new Vector<String>();
    private Vector<String> titols = new Vector<String>();
    private Vector<String> continguts = new Vector<String>();
    private Vector<String> paths = new Vector<String>();
    private Vector<HashMap<String,Integer>> tf_dades = new Vector<HashMap<String, Integer>>();
    private ArrayList <String> palabras = new ArrayList<>(); //ATRIBUTO GESTOR
    private ArrayList <String> autor_titol = new ArrayList<>(); //ATRIBUTO GESTOR

    public GestorED(){}

    public void escriurePostingList(String autor, String titol, String paraula){
        try {

            StringBuilder path_doc= new StringBuilder(Paths.get("").toAbsolutePath().toString());
            path_doc.append("/EXE/posting_list.txt");
            BufferedReader check_content = new BufferedReader(new FileReader(path_doc.toString()));
            String to_save = "";
            String aux = "";
            boolean trobat = false;
            while ((aux = check_content.readLine()) != null) {
                if(!aux.equals(paraula)){
                    to_save += (aux+"\n");
                }
                else{
                    trobat = true;
                    to_save += (aux+"\n"+ autor+"$"+titol+"\n");
                }
            }
            if (!trobat){
                to_save += ("|"+"\n");
                to_save += (paraula + "\n");
                to_save += (autor+"$"+titol+"\n");
            }
            check_content.close();
            BufferedWriter to_write = new BufferedWriter(new FileWriter(path_doc.toString()));
            to_write.write(to_save);

            to_write.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void borrarPostingList() {
        try {
            StringBuilder path_doc= new StringBuilder(Paths.get("").toAbsolutePath().toString());
            path_doc.append("/EXE/posting_list.txt");
            BufferedWriter to_write = new BufferedWriter(new FileWriter(path_doc.toString()));
            to_write.write("");
            to_write.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void actualitzarDocument(String autor, String titol, String nou_contingut, String path,
                                    HashMap <String, Integer> tf_hash_to_save) {

        try{
            String hash_to_string = "";
            for(Map.Entry<String, Integer> set : tf_hash_to_save.entrySet()){
                hash_to_string += set.getKey() + " " + set.getValue() + "\n";
            }

            StringBuilder path_doc= new StringBuilder(Paths.get("").toAbsolutePath().toString());
            path_doc.append("/EXE/dades.txt");
            BufferedReader check_content = new BufferedReader (new FileReader(path_doc.toString()));
            String to_save = "";
            String aux = "";
            boolean trobat = false;
            while((aux = check_content.readLine()) != null){
                if(!aux.equals(autor+"$"+titol) && !trobat){
                    to_save += (aux + "\n");
                }
                else{
                    trobat = true;
                }
                if(aux.equals("|")){
                    trobat = false;
                }
            }
            check_content.close();
            if(to_save.contains(autor + "\n" + titol)){ //NO ENTIENDO ESTOOOOOO
            }
            to_save += autor + "$" + titol + "\n" + nou_contingut + "\n" +"$"+ "\n"+ path + "\n" + hash_to_string + "|"; // | -> centinela
            BufferedWriter to_write = new BufferedWriter(new FileWriter(path_doc.toString()));
            to_write.write(to_save);

            to_write.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void inicialitza() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("EXE/dades.txt"));
        String linea;
        String autor = "";
        String titol = "";
        StringBuilder contenido = new StringBuilder();
        String path = "";
        HashMap<String, Integer> tf = new HashMap<String, Integer>();
        int estado = 0; // 0: autor y título, 1: contenido, 2: path, 3: clave-valor
        while ((linea = reader.readLine()) != null) {
            switch (estado) {
                case 0:
                    // leyendo autor y título
                    System.out.println(linea);
                    int separador = linea.indexOf("$");
                    System.out.println(linea);
                    autors.add(linea.substring(0, separador));
                    titols.add(linea.substring(separador + 1));
                    estado = 1;
                    System.out.println(autor);
                    System.out.println(titol);
                    break;
                case 1:
                    // leyendo contenido
                    if (linea.equals("$")) {
                        continguts.add(String.valueOf(contenido));
                        // marca de fin de contenido
                        estado = 2;
                    } else {
                        contenido.append(linea);
                    }
                    break;
                case 2:
                    // leyendo path
                    path = linea;
                    paths.add(path);
                    estado = 3;
                    break;
                case 3:
                    // leyendo clave-valor
                    if (linea.startsWith("|")) {
                        tf_dades.add(tf);
                        // marca de fin del HashMap y del documento
                        estado = 0; // se vuelve al estado 0 para leer el siguiente documento
                    } else {
                        int separadorhash = linea.indexOf(" ");
                        String clave = linea.substring(0, separadorhash);
                        int valor = Integer.parseInt(linea.substring(separadorhash + 1));
                        tf.put(clave, valor);
                    }
                    break;
            }
        }
        reader.close();
    };

    public void ExtractWordsPosting() throws IOException{
        try {
            StringBuilder path_doc= new StringBuilder(Paths.get("").toAbsolutePath().toString());
            path_doc.append("/EXE/posting_list.txt");
            BufferedReader buffer_aux = new BufferedReader(new FileReader(path_doc.toString()));
            String line;
            //int counter = 0;

            String palabra_carry = "";
            while ((line = buffer_aux.readLine()) != null) {
                if (!line.contains("|")) {

                    if (!line.contains("$"))
                        palabra_carry = line;
                    else {
                        palabras.add(palabra_carry);
                        autor_titol.add(line);
                    }
                }
            }
            buffer_aux.close();
        }catch (Exception e) {
            throw e;
        }
    };
    public ArrayList<String> getPostingPalabras(){

        return palabras;
    };
    public ArrayList<String> getPostingAutors_titols(){

        return autor_titol;
    };

    public Vector<String> getPaths() {
        return paths;
    }
    public Vector<String> getAutors() {
        return autors;
    }
    public Vector<String> getTitols() {
        return titols;
    }
    public Vector<String> getContinguts() {
        return continguts;
    }
    public Vector<HashMap<String, Integer>> getTf_dades() {
        return tf_dades;
    }

}
