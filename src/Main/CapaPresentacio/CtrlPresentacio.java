package Main.CapaPresentacio;

import Main.CapaDomini.CtrlDomini;

import java.io.FileNotFoundException;
import java.util.Set;
import java.util.Vector;

public class CtrlPresentacio {
    private static CtrlDomini ctrlDomini;
    private Main.CapaPresentacio.vistaPrincipal vistaPrincipal;

    public CtrlPresentacio(){
        try {
            ctrlDomini = new CtrlDomini();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        vistaPrincipal = new vistaPrincipal(this);
    }

    public void inicialitzarPresentacio(){
        vistaPrincipal.ferVisible();
    }

    public Set<String> llistatAutors(){
        return ctrlDomini.get_tots_autors();
    }

    public Set<String> llistatTitols(){
        return ctrlDomini.get_tots_titols();
    }

    public Set<String> llistatAutorsEliminats(){
        return ctrlDomini.llistarAutorsEliminats();
    }

    public Set<String> llistarPerTitolEliminats(String autor){
        return ctrlDomini.llistarPerTitolEliminats(autor);
    }

    public void altaDocument(String path) throws FileNotFoundException {
        ctrlDomini.alta_document(path);

    }

    public Object[][] kDocs(String autor, String titol, Integer k){
        return ctrlDomini.llistar_k_documents(autor, titol, k);
    }

    public String llistarContingut(String autor, String titol){
        return ctrlDomini.llistar_contingut(autor, titol);
    }

    public String llistarAutorPrefix(String autor) throws Exception {
        try {
            return ctrlDomini.llistar_autor_per_prefix(autor);
        }catch(Exception e){
            throw e;
        }
    }

    public void altaExpressio(String expressio) throws Exception{
        ctrlDomini.alta_expressio(expressio);
    }

    public void baixaExpressio(String exp) throws Exception {
        ctrlDomini.baixa_expressio(exp);
    }

    public void modificarExpressio(String vell, String nou) throws Exception{
        ctrlDomini.modificar_expresio(vell,nou);
    }

    public static Vector<String> llistat_expresions(){
        return ctrlDomini.llistar_expressions();
    }

    public Set<String> llistatDocumentsBooleans(String exp){
        return ctrlDomini.documents_per_booleans(exp);

    }

    public Set<String> llistarPerTitol(String autor){
        return ctrlDomini.llistar_per_titol(autor);

    }

    public void guardarDocumet(String autor, String titol, String contingut) throws Exception {
        try {
            ctrlDomini.guardarDocumet(autor, titol, contingut);
        }catch (Exception e){
            throw e;
        }
    }

    public void eliminarDocument(String autor, String titol) throws Exception {
        try {
            ctrlDomini.donar_de_baixa_document(autor, titol);
         }catch (Exception e){
            throw e;
        }
    }

    public void recuperarDocument(String autor, String titol) throws Exception {
        try {
            System.out.println("autor: "+autor+" titol: "+titol);
            ctrlDomini.recuperar_un_document(autor, titol);
        }catch (Exception e) {
            throw e;
        }
    }
}
