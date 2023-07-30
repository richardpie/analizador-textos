package Main.CapaDomini;
import java.util.*;

public class Expressions {
    public Vector<ExpressioBooleana> expr = new Vector<ExpressioBooleana>();

    public Expressions(){};

    public  void altaExpressio(String exp) throws Exception{
        ExpressioBooleana x = new ExpressioBooleana(exp);
        boolean tr = false;
        for(int i = 0; i < expr.size(); ++i){
            String word = expr.get(i).get_expre_String();
            if(word.equals(exp)) tr = true;
        }
        if(!tr){
            expr.add(x);
        }
        else{
            throw new Exception("L'expressió ja existeix");
        }
    }


    public void baixaExpressio(String exp) throws Exception{
        ExpressioBooleana x = new ExpressioBooleana(exp);
        boolean tr = false;
        for(int i = 0; i < expr.size(); ++i){
            String word = expr.get(i).get_expre_String();
            if(word.equals(exp)) {
                expr.remove(i);
                tr = true;
            }
        }
        if(!tr) throw new Exception("L'expressió no existeix");
    }

    public void modifExpressio(String vell, String nou) throws Exception{
        ExpressioBooleana v = new ExpressioBooleana(vell);
        ExpressioBooleana n = new ExpressioBooleana(nou);
        boolean trobat_vell = false;
        boolean trobat_nou  = false;
        for(int i = 0; i < expr.size(); ++i){
            String word = expr.get(i).get_expre_String();
            if(word.equals(vell)) trobat_vell = true;
            if(word.equals(nou)) trobat_nou = true;
        }

        if(trobat_vell & !trobat_nou){
            altaExpressio(nou);
            baixaExpressio(vell);
        }
        if(!trobat_vell) throw new Exception("L'expressio que es vol modificar no existeix");
        else if(trobat_nou) throw new Exception("La nova expressio ja existeix");
    }

    Vector<ExpressioBooleana>  getAll(){
        return expr;
    }

    public  Vector<String> get_llista(){
        Vector<String> llista = new Vector<String>();
        for(int i = 0; i < expr.size(); ++i){
            ArrayList llista_paraules = new ArrayList<>();
            for(int j = 0; j < expr.get(i).size(); ++j){
                llista_paraules.add(expr.get(i).get_word(j));
            }
            String expressio = String.join(" ",llista_paraules);
            llista.add(expressio);
        }
        return llista;
    }

    Set<Document> juntadoc(Boolean and, Boolean or,Set<Document> contingut, Set<Document>fin){
        if(and){
            fin.retainAll(contingut);
        }
        else if(or){
            fin.addAll(contingut);
        }
        else{
            fin =contingut;
        }
        return fin;
    }
    public Set<Document> doc_expressions(String exp,Carpeta carpeta) {
        ExpressioBooleana x = new ExpressioBooleana(exp);
        Algoritme algo = new Algoritme();
        boolean and = false;
        boolean or = false;
        Set<Document> doc_contenir = new HashSet<>();
        Set<Document> doc_nocontenir = new HashSet<>();
        Set<Document> result = new HashSet<>();
        Set<Document> inter = new HashSet<>();

        for (int i = 0; i < x.size(); ++i) {
            String word = x.get_word(i);
            if (word.startsWith("!")) {
                String p = word.replace("!", "");
                doc_nocontenir = algo.expresions_boleanes(new String[]{p}, carpeta);
                if(result.isEmpty()) result = carpeta.get_tots_docs();

                if (and) {
                    inter = doc_nocontenir;
                    result.removeAll(inter);
                    and = false;
                } else if (or) {
                    result.addAll(doc_nocontenir);
                    or = false;
                } else {
                    result = doc_contenir;
                }
            } else if (word.startsWith("&")) {
                and = true;
                or = false;
            } else if (word.startsWith("|")) {
                and = false;
                or = true;
            } else {
                doc_contenir = algo.expresions_boleanes(new String[]{word}, carpeta);

                result = juntadoc(and, or, doc_contenir, result);
                and = false;
                or = false;
            }

        }
        return result;
    }
    boolean isEmpty(){
        return expr.isEmpty();
    }

}
