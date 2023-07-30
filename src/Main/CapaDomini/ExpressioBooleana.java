package Main.CapaDomini;

import java.util.ArrayList;
import java.util.Vector;

public class ExpressioBooleana {
    public Vector<String> expr = new Vector<String>();

    public ExpressioBooleana(String x){

        for(int i = 0; i< x.length();++i){
            if(x. startsWith("{")){
                int indexParenetesis = x.indexOf("}");
                String in = x.substring(1,indexParenetesis);
                String[] words = in.split(" ");
                for(String f:words){
                    String paraulf = f.toLowerCase();
                    paraulf = paraulf.trim();
                    expr.add(paraulf);
                }
                String borra = x.substring(0,indexParenetesis+1);
                String fi = x.replaceFirst(borra, "");
                x = fi.trim();
            }

            if(x.startsWith("&")){
                expr.add("&");
                String v = x.replaceFirst("&", "");
                x = v.trim();

            }

            if(x.startsWith("|")){
                expr.add("|");
                String v = x.replaceFirst("[|]","");
                x = v.trim();

            }

            if(x.startsWith("(")) {
                int indexParenetesis = x.indexOf(")");
                String in = x.substring(1, indexParenetesis);
                String[] words = x.split("\\s");
                for (String f : words) {

                    String paraulf = f.toLowerCase();
                    paraulf = paraulf.trim();
                    f = paraulf;
                    if (f == "&") expr.add("&");
                    if (f == "|") expr.add("|");
                    else {
                        expr.add(paraulf);
                    }
                    String borra = x.substring(0, indexParenetesis + 1);
                    String fi = x.replaceFirst(borra, "");
                    x = fi.trim();

                }
            }
            else{
                int acabaparaula = x.indexOf(" ");
                if (acabaparaula == -1) acabaparaula = x.length();
                String p = x.substring(0,acabaparaula);

                String paraula = p.toLowerCase();
                paraula = paraula.trim();
                expr.add(paraula);
                String borra = x.substring(0,acabaparaula);
                String fi = x.replaceFirst(borra, "");
                x = fi.trim();
            }
        }

        //passar de string a vector*/
    }

    public int size(){
        return expr.size();
    }

    public String get_word(int i){
        return expr.get(i);
    }

    public String get_expre_String(){
        ArrayList llista_paraules = new ArrayList();
        for(int j = 0; j < expr.size(); ++j){
            llista_paraules.add(get_word(j));
        }
        String expressio = String.join(" ",llista_paraules);
        return expressio;
    }

}
