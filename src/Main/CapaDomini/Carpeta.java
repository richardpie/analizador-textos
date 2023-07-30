package Main.CapaDomini;

import javax.print.Doc;
import java.io.FileNotFoundException;
import java.util.*;

public class Carpeta {

    //Atributos
    String nombre_carpeta;
    String path;
    TreeMap<String, TreeMap<String, Document>> listado_documento =
            new TreeMap<String, TreeMap<String, Document>>();
    static HashMap<String, List<Document>> posting_list = new HashMap<String, List<Document>>();

    //Geter path
    public String getPath() {
        return path;
    }

    //Constructora
    public Carpeta(String nombre_asignado, String path_asignado) {


        this.nombre_carpeta = nombre_asignado;
        this.path = path_asignado;

    }
    //Només seerveix per els testos
    public Carpeta(String nombre_asignado, String path_asignado, Document docs[], HashMap<String, List<Document>> posting){
        this.nombre_carpeta = nombre_asignado;
        this.path = path_asignado;

        for(Document d : docs) {
            String autor = d.getAutor();
            String titol = d.getTitol();
            TreeMap<String, Document> aux = new TreeMap<String, Document>();
            if (listado_documento.containsKey((autor))) {
                aux = listado_documento.get(autor);
            }
            aux.put(titol, d);
            listado_documento.put(autor, aux);
        }
        //creem la posting list plena
        posting_list = posting;
    }


    public Set<String> get_tots_autors(){
        return listado_documento.keySet();
    }
    public Set<String> get_tots_titols(){
        Set<String> titols = new HashSet<>();
        for (String autor : listado_documento.keySet()){
            for(String titol : listado_documento.get(autor).keySet()){
                titols.add(titol);
            }
        }
        return titols;
    }

    public Set<Document> get_tots_docs(){
        Set<Document> documents = new HashSet<>();
        for (String autor : listado_documento.keySet()){
            for(String titol : listado_documento.get(autor).keySet()){
                documents.add(listado_documento.get(autor).get(titol));
            }
        }
        return documents;
    }

    private void carrega_doc_extern(Document doc, String path_file) throws FileNotFoundException {
        try {
            if(path_file.endsWith("txt")){

                doc.carregar_doc(path_file);
            }
            else{

                doc.lectura_xml(path_file);
            }
        }
        catch(FileNotFoundException e){
            throw e;
        }
        doc_en_listado(doc);
    }

    public void importa_document(Document doc, String old_string) throws FileNotFoundException {
        try {
            carrega_doc_extern(doc, old_string);
        }
        catch(FileNotFoundException e){
            throw e;
        }

    }

    public void doc_en_listado(Document d){
        String autor = d.getAutor();
        String titol = d.getTitol();
        TreeMap<String, Document> aux = new TreeMap<String, Document>();
        if(listado_documento.containsKey((autor))) {
            aux = listado_documento.get(autor);
        }
        aux.put(titol, d);
        listado_documento.put(autor, aux);
    }

    public void baixa_Document(String autor, String titol){

        Document eliminat = listado_documento.get(autor).get(titol);
        doc_en_listado(eliminat);

        listado_documento.get(autor).remove(titol);
        if (listado_documento.get(autor).size()==0) listado_documento.remove(autor);

    }

    public Set<String> get_titols (String autor){
        Set<String> aux = new HashSet<String>();
        for(String titols : listado_documento.get(autor).keySet()){
            aux.add(titols);
        }
        return aux;
    }

    public String get_autors (String prefix) throws Exception{
        boolean prefix_vacio = false, used = false, leido = false;

        if(prefix.length() == 0)
            prefix_vacio = true;
        String aux = "";

        for(Map.Entry <String, TreeMap<String, Document>> iterador: listado_documento.entrySet()){

            String key = iterador.getKey();
            if(prefix_vacio || key.startsWith(prefix)) {
                used = true;
                if(leido)
                    aux = aux + "\n" + key;
                else
                    aux = aux + key;

                leido = true;
            }
        }
        if (!used) {
            throw new Exception ("La llista d'autors està buida o " +
                    "no existeix cap autor amb el prefix donat.");
        }
        return aux;
    }

    public String get_contenido (String autor, String titol){
        String carry = "";
        if (listado_documento.containsKey(autor)) {
            if (listado_documento.get(autor).containsKey((titol))) {
                carry = listado_documento.get(autor).get(titol).getContingut();
            }
        }
        return carry;
    }
    private void eliminar_paraules_posting(Document d) {
        for (String s : d.tf_hash.keySet()){
            if(posting_list.containsKey(s)){
                posting_list.get(s).remove(d);
            }
        }
    }

    public void modifica_document_en_carpeta (String autor, String titol, String str) throws Exception{

        if(!listado_documento.containsKey((autor)))
            throw new Exception("El autor introducido no existe");
        if(!listado_documento.get(autor).containsKey(titol))
            throw new Exception ("El autor introducido no tiene el documento solicitado");

        listado_documento.get(autor).get(titol).modificacio_document(str);
        eliminar_paraules_posting(listado_documento.get(autor).get(titol));
    }
}
