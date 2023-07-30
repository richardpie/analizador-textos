package Main.CapaDomini;

import Main.CapaDades.CtrlPersistencia;

import javax.print.Doc;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CtrlDomini {

    private static Integer iterador;
    private static Integer index;
    private static Integer numDocuments;
    private static Object[][] dades;
    public static Carpeta carpeta_estandard = new Carpeta("carpeta", "/." );
    static Carpeta carpeta_eliminats = new Carpeta("eliminats", "/." );
    static Expressions expressions = new Expressions();

    CtrlPersistencia ctrlPersistencia= new CtrlPersistencia();
    public CtrlDomini() throws Exception {
        try {
            ctrlPersistencia.inicialitza();
            posarDades();
        }catch (Exception e){
            throw e;
        }
    };


    private void posarDades() throws Exception{
        Vector<String> autors = ctrlPersistencia.getAutors();
        Vector<String> titols = ctrlPersistencia.getTitols();
        Vector<String> continguts = ctrlPersistencia.getContinguts();
        Vector<String> paths = ctrlPersistencia.getPaths();
        List<HashMap<String,Integer>> tf_dades = ctrlPersistencia.getTf_dades();

        for(int i = 0; i < autors.size(); ++i){
            Document doc = new Document();
            doc.autor = autors.get(i);
            doc.titol = titols.get(i);
            doc.contingut = continguts.get(i);
            doc.path = paths.get(i);
            System.out.println(tf_dades);
            doc.tf_hash = tf_dades.get(i);
            System.out.println(tf_dades.get(i));
            carpeta_estandard.doc_en_listado(doc);
        }
        try {
            //ctrlPersistencia.ExtractWordsPosting();
        } catch (Exception e) {
            throw e;
        }
        //carrega_postinglist();
    }





    public void carregar_un_document(){
        Document doc = new Document();
        carga_document(doc);
    }

    public Set<String> llistarAutorsEliminats(){
        return carpeta_eliminats.get_tots_autors();
    }
    public Set<String> get_tots_autors(){
        return carpeta_estandard.get_tots_autors();
    }
    public Set<String> get_tots_titols(){
        return carpeta_estandard.get_tots_titols();
    }

    public void donar_de_baixa_document(String autor, String obra)throws Exception{
        try {
            if(!carpeta_estandard.listado_documento.containsKey(autor))
                throw new Exception("Autor no existent en el sistema.");


            if(!carpeta_estandard.listado_documento.get(autor).containsKey(obra))
                throw new Exception("El documento no existe para este autor.");

            String path = carpeta_estandard.getPath();
            Document docu= carpeta_estandard.listado_documento.get(autor).get(obra);

            String docupath= docu.getPath();
            carpeta_eliminats.importa_document(docu, docupath);

            ctrlPersistencia.baixaDocument(docupath);

            carpeta_estandard.baixa_Document(autor,obra);


        }catch (Exception e){
            throw e;
        }
    }

    public void alta_document(String path_document) throws FileNotFoundException {
        Document new_doc = new Document();
        try {
            ctrlPersistencia.afegirDocument(path_document);
            importar_document(new_doc, path_document);

        }
        catch (FileNotFoundException e){
            throw e;
        }
    }

    public void recuperar_un_document(String autor, String titol) throws Exception{
        try {

            Document docu = carpeta_eliminats.listado_documento.get(autor).get(titol);
            String docupath = docu.getPath();
            ctrlPersistencia.recuperar_document(docupath);
            carpeta_estandard.importa_document(docu, docupath);
            carpeta_eliminats.baixa_Document(autor, titol);
        }
        catch(Exception e){
            throw e;
        }
    }

    public void carga_document(Document doc){

        String path = carpeta_estandard.getPath();
        lecturacarpeta(path);

    }

    void lecturacarpeta(String path){
        File carpeta = new File(path);
        archivos_carpeta(carpeta);
    }

    void archivos_carpeta(File carpet){
        for (File arxiu : Objects.requireNonNull(carpet.listFiles())) {
            if (!arxiu.isDirectory()) {

            } /*else{
                archivos_carpeta(arxiu); //en el cas que hi hagués una carpeta dins una altra carpeta entraria a buscar els arxius
            }*/
        }
    }

    void importar_document(Document doc, String old_path) throws FileNotFoundException {
        try {
            carpeta_estandard.importa_document(doc, old_path);
        }
        catch(FileNotFoundException e){
            throw e;


        }
        doc.omple_TFHash(carpeta_estandard.posting_list, carpeta_estandard);
    }

    public void alta_expressio(String exp) throws Exception{
        try{
            expressions.altaExpressio(exp);
        }
        catch(Exception e){
            throw e;
        }
    }

    public void baixa_expressio(String exp) throws Exception{
        try{
            expressions.baixaExpressio(exp);
        }
        catch(Exception e){
            throw e;
        }

    }

    public void modificar_expresio(String vell, String nou) throws Exception{
        try{
            expressions.modifExpressio(vell,nou);
        }
        catch(Exception e){
            throw e;
        }

    }
    public static Vector<String> llistar_expressions() {
        return expressions.get_llista();
    }

    void modificacion_de_documento(String autor, String titol, String str) throws Exception{
       try{
            carpeta_estandard.modifica_document_en_carpeta(autor, titol, str);
            carpeta_estandard.listado_documento.get(autor).get(titol).omple_TFHash(carpeta_estandard.posting_list,carpeta_estandard);
            String path= carpeta_estandard.listado_documento.get(autor).get(titol).getPath();
            ctrlPersistencia.modificarDocument(path, str);
            ctrlPersistencia.actualitzarDocument(autor, titol, str, carpeta_estandard.listado_documento.get(autor).get(titol).path, carpeta_estandard.listado_documento.get(autor).get(titol).tf_hash);

           ctrlPersistencia.borrarPostingList();
           for(String paraula: carpeta_estandard.posting_list.keySet()){
               for (Document doc : carpeta_estandard.posting_list.get(paraula)) {
                       ctrlPersistencia.escriurePostingList(doc.autor,doc.titol, paraula);
               }
           }
        }
        catch(Exception e){
            throw e;
        }

    }
    public Set<String> llistar_per_titol(String autor){
        return carpeta_estandard.get_titols(autor);
    }
    public Set<String> llistarPerTitolEliminats(String autor){
        return carpeta_eliminats.get_titols(autor);
    }

    public String llistar_autor_per_prefix(String autor_prefix) throws Exception{
        try {
            String aux = carpeta_estandard.get_autors(autor_prefix);
            return aux;
        }
        catch(Exception e){
            throw e;

        }
    }

    public String llistar_contingut(String autor, String titol){
            String carry = carpeta_estandard.get_contenido(autor, titol);
            return carry;
    }

    private Object[][] tractamentdades(TreeMap<Double, List<Document>> semblants, Integer kBox){
        iterador = 0;
        index = 0;
        numDocuments = 0;
        dades = new Object[kBox][3];
        for (Double d : semblants.keySet()) {
            semblants.get(d).forEach((temp) -> {
                ++numDocuments;
            });
        }
        for (Double d : semblants.keySet()) {
            semblants.get(d).forEach((temp) -> {
                if (iterador > (numDocuments - kBox)-1) { //com està tot ordenat de menor a major agafem els últims k documents ja que esran els més semblants
                    for (int i = 0; i < 3; ++i) { //el3 fa referència a similitud, autor i títol
                        if (i == 0) {
                            dades[index][i] = d;
                        } else if (i == 1) {
                            dades[index][i] = temp.getTitol();
                        } else {
                            dades[index][i] = temp.getAutor();
                        }
                    }
                    ++index;
                }
                ++iterador;
            });
        }
        return dades;
    }

    public Object[][] llistar_k_documents(String autor1, String titol1, Integer k){
            Algoritme alg = new Algoritme();
            Document doc = carpeta_estandard.listado_documento.get(autor1).get(titol1);
            TreeMap<Double, List<Document>> semblants = alg.similitud_doc(carpeta_estandard, doc); //ens retorna tots els documents ordenats per similitud de menor a major
            Object[][] dadesTractades = tractamentdades(semblants, k);
            return dadesTractades;
    }

    public Set<String> documents_per_booleans(String exp){
        //Algoritme alg = new Algoritme
        Set<String> llistat = new HashSet<>();
        Set<Document> docs = expressions.doc_expressions(exp,carpeta_estandard);
        for(Document doc: docs){
            ArrayList id_doc = new ArrayList<>();
            id_doc.add(doc.getAutor());
            id_doc.add(doc.getTitol());

            String document  = doc.getAutor()+ " "+doc.getTitol();
            llistat.add(document);
        }
        return llistat;
    }

    Carpeta get_carpeta() {
        return carpeta_estandard;
    }
    public String getContingutPath(String path){
        for (String autor : carpeta_estandard.listado_documento.keySet()) {
            for(String titol : carpeta_estandard.listado_documento.get(autor).keySet()){
                if(carpeta_estandard.listado_documento.get(autor).get(titol).getPath().equals(path)) return carpeta_estandard.listado_documento.get(autor).get(titol).getContingut();
            }
        }
        return  "";
    }

    public void guardarDocumet(String autor, String titol, String contingut) throws Exception {
        //capaPersistencia.guardarDocumet;
        try {
            modificacion_de_documento(autor, titol, contingut);
        }catch (Exception e){
            throw e;
        }
    }




    public void actualitzaInfo(){
        try (FileWriter fileWriter = new FileWriter("output.txt")) {

            for (Map.Entry<String,TreeMap<String,Document>> externalEntry: carpeta_estandard.listado_documento.entrySet()){

                TreeMap<String,Document> internalMap= externalEntry.getValue();

                for(Map.Entry<String, Document> internalEntry:internalMap.entrySet() ){

                    fileWriter.write(internalEntry.getValue().titol);
                    fileWriter.write(internalEntry.getValue().autor);

                    for(Map.Entry<String,Integer>entryTf:internalEntry.getValue().tf_hash.entrySet()){
                        fileWriter.write(entryTf.getKey()+":"+entryTf.getValue());
                    }

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
