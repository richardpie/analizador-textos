package Main.CapaDomini;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.lang.model.util.Elements;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

import java.util.*;

public class Document {
    String path;
    String titol;
    String autor;
    String contingut;
    HashMap<String, Integer> tf_hash = new HashMap<>();

    public Document(){}

    public Document(String t, String a, String c, String p, HashMap<String, Integer> tf){

        this.titol = t;
        this.autor = a;
        this.contingut = c;
        this.path = p;
        tf_hash = tf;
    }

    public String getPath(){
        return path;
    }
    //geter de Títol
    public String getTitol(){
        return titol;
    }
    //geter Autor
    public String getAutor(){
        return autor;
    }
    //geter de contingut
    public String getContingut(){
        return contingut;
    }
    //geter dSe tf_hash
    /*HashMap<String, Integer> getTf_hash() {
        return tf_hash;
    }*/

    //llegiex l'arxiu amb el path "path" i crea atributs objecte
    public void carregar_doc(String path_doc) throws FileNotFoundException {
        File arxiu = null;
        BufferedReader buffer_lectura = null;
        try {
            arxiu = new File(path_doc); //path de on es troba fitxer a llegir
            FileReader arxiu_lectura = new FileReader(arxiu);
            String nomFile= arxiu.getName();
            path = "EXE/Carpeta_Estandar/"+nomFile;
            buffer_lectura = new BufferedReader(arxiu_lectura);

            //Procedim a llegir per linies
            String linia;
            Integer i = 0;
            while ((linia = buffer_lectura.readLine()) != null) {
                if (!linia.isEmpty()) {
                    if (i == 0) autor = linia;
                    if (i == 1) titol = linia;
                    if (i == 2) contingut = linia;
                    else contingut = contingut +"\n"+ linia;
                    ++i;
                }
            }
        }catch(Exception exc) {
            exc.printStackTrace();
        }
    }

    public void lectura_xml (String path_file){
        try {
            File filename= new File(path_file);
            String nomFile= filename.getName();
            path = "EXE/Carpeta_Estandar/"+nomFile;

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            //Lo tenemos que llamar cómo la librería porque "Document" genera conflictos de clase.
            org.w3c.dom.Document aux = builder.parse(new File(path_file));
            aux.getDocumentElement().normalize();
            Node root = aux.getDocumentElement();
            NodeList elementos_documento = root.getChildNodes();

            for(int i = 0; i < elementos_documento.getLength(); ++i) {

                if (elementos_documento.item(i).getNodeName() == "autor"){
                    autor = (elementos_documento.item(i).getTextContent()).trim();
                }
                else if (elementos_documento.item(i).getNodeName() == "titol") {
                    titol = (elementos_documento.item(i).getTextContent()).trim();
                }
                else if (elementos_documento.item(i).getNodeName() == "contingut") {
                    contingut = elementos_documento.item(i).getTextContent();
                }
            }
        }
        catch (ParserConfigurationException e){
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }



    }

    //PER TF-IDF
    private List<String> llegir_info(String path_dades){
        File arxiu = null;
        BufferedReader buffer_lectura = null;
        List<String> info = new ArrayList<>();
        try {
            arxiu = new File(path_dades); //path de on es troba fitxer a llegir
            FileReader arxiu_lectura = new FileReader(arxiu);
            buffer_lectura = new BufferedReader(arxiu_lectura);
            //Procedim a llegir per linies
            String linia;
            while ((linia = buffer_lectura.readLine()) != null) {
                info.add(linia);
            }
        }catch(Exception exc) {
            exc.printStackTrace();
        }
        return info;
    }
    //Sort HashMap per valor (de menys freq a més)
    private HashMap<String,Integer> sort_hashmap(HashMap<String,Integer> hashMap){
        ArrayList<Integer> sorted = new ArrayList<>();
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            sorted.add(entry.getValue());
        }
        Collections.sort(sorted , new Comparator<Integer>() {
            public int compare(Integer freq1, Integer freq2) {
                return (freq1).compareTo(freq2);
            }
        });
        for (Integer freq : sorted) {
            for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
                if (entry.getValue().equals(freq)) {
                    sortedMap.put(entry.getKey(), freq);
                }
            }
        }
        return sortedMap;
    }

    //Creació Hashmap tf del document
    public void omple_TFHash(HashMap<String, List<Document>> pos_list, Carpeta carpeta) {
        String contingut_aux;
        tf_hash.clear();
        contingut_aux = contingut.toLowerCase(); //passem tot a minúscules, per no produir paraules types diferenciats per una majúscula
        contingut_aux = contingut_aux.replaceAll("\\p{Punct}|¿", ""); //treiem els signes de puntutació pq no ens interessen
        contingut_aux = contingut_aux.replaceAll("[\\d]", ""); //treiem els dígits (numeros)
        StringTokenizer tokenizer = new StringTokenizer(contingut_aux);
        List<String> stopwords = llegir_info("EXE/stopwords.txt");//treiem stopwors pq a l'hora de fer el tfidf no agafi paraules no rellevants amb freq molt elevada
        while (tokenizer.hasMoreTokens()) {
            String paraula = tokenizer.nextToken();
            if (!stopwords.contains(paraula)) {
                if (tf_hash.containsKey(paraula)) {
                    tf_hash.put(paraula, tf_hash.get(paraula) + 1);
                } else {
                    tf_hash.put(paraula, 1);
                }
                afegir_PostingList(pos_list, paraula, titol, autor, carpeta); //afegim la paraula i el doc a la posting list global
            }
        }
        tf_hash = sort_hashmap(tf_hash);
    }


    //POSTING LIST, creem una posting list amb paraula i documents en els quals es troba aquesta paraula per tal de fer la cerca per paraules més ràpida
    private void afegir_PostingList(HashMap<String, List<Document>> posting_list, String paraula, String nom, String autor, Carpeta carpet){
        if(posting_list.containsKey(paraula)){ //si ja es triba dins el hashmap només cal afegir el doc a la llista de doc

            if(!posting_list.get(paraula).contains(carpet.listado_documento.get(autor).get(nom)))
                posting_list.get(paraula).add(carpet.listado_documento.get(autor).get(nom));
        }
        else{  //si no existeix la paraula l'afegim i afegim el doc en conccret al vector de la paraula
            List<Document> documents= new ArrayList<Document>();
            documents.add(carpet.listado_documento.get(autor).get(nom));
            posting_list.put(paraula, documents);
        }
    }

    public void modificacio_document(String str){
        boolean modified = false;
        this.contingut = str;
        modified = true;
    }
}
