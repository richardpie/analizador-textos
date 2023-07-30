package Main.CapaDomini;

import java.util.*;

public class Algoritme {
    private static double N_documents;
    //normmalitzem els pesos del document
    HashMap<String, Double> normalitzar(HashMap<String, Double> tw){
        HashMap<String, Double> tw_normalit = new HashMap<String, Double>();
        double suma = 0.;
        for(String p : tw.keySet()) {
            suma += (tw.get(p)*tw.get(p));
        }
        Double resultat = Math.sqrt(suma);
        for(String paraula : tw.keySet()) tw_normalit.put(paraula, (tw.get(paraula)/resultat));
        return tw_normalit;
    }

    //caàlcul del TF-IDF
    HashMap<String, Double> tf_idf(Document doc, Carpeta carpeta){
        HashMap<String, Double> tw_doc = new HashMap<String,Double>();
        Double max_ferq = 0.;
        Iterator<Map.Entry<String, Integer>> Iterador
                = doc.tf_hash.entrySet().iterator();
        while(Iterador.hasNext()){
            Map.Entry<String, Integer> new_Map = (Map.Entry<String, Integer>) Iterador.next();
            max_ferq = Double.valueOf(new_Map.getValue()); //agafem l'últim element del hashmap[paraula, freq] de cada doc ja que està ordenat freq_min->freq_màx
        }
        N_documents = 0.0;
        for (String autor : carpeta.listado_documento.keySet()) {
            for(String titol : carpeta.listado_documento.get(autor).keySet()){
                ++N_documents;
            }
        }
        double tf;
        double idf;
        double df;
        for(String paraula : carpeta.posting_list.keySet()){
            df = (double) carpeta.posting_list.get(paraula).size();
            if (doc.tf_hash.containsKey(paraula)) {
                tf = Double.valueOf(doc.tf_hash.get(paraula))/max_ferq;
            }
            else {
                tf = 0.0/max_ferq;
            }
            idf = N_documents/df;
            idf = Math.log(idf)/Math.log(2);
            Double wdi = tf*idf;
            tw_doc.put(paraula,wdi);
        }
        return normalitzar(tw_doc);
    }

    Double similitud_per_cosinus(HashMap<String, Double> tw1, HashMap<String, Double> tw2){
        int i = 0;
        int j = 0;
        double similitud= 0.;
        String[] tw1_set = tw1.keySet().toArray(new String[0]);
        String[] tw2_set = tw2.keySet().toArray(new String[0]);
        while(i < tw1.size() && j < tw2.size()){
            int comparar = tw1_set[i].compareTo(tw2_set[j]);
            if (tw1_set[i] == tw2_set[j]){
                similitud += tw1.get(tw1_set[i])*tw2.get(tw2_set[j]);
                ++i;
                ++j;
            }
            else if(comparar > 0) ++j;
            else ++i;
        }
        return similitud;
    }

    TreeMap<Double, List<Document>> similitud_doc(Carpeta carpet, Document d){
        TreeMap<Double, List<Document>> ranquing_similitud = new TreeMap<Double, List<Document>>();
        HashMap<String, Double> tw_doc1 = tf_idf(d, carpet);
        //per cada document que es troba a la carpeta fem el tf_ide i comparem el seu vector amb tw_doc1 per trobar la seva similitud
        for(Map.Entry <String, TreeMap<String, Document>> EntradaExterior: carpet.listado_documento.entrySet()){
            for(Map.Entry <String, Document> EntradaInterior : EntradaExterior.getValue().entrySet()){
                Document doc2 = carpet.listado_documento.get(EntradaExterior.getKey()).get(EntradaInterior.getKey());
                if(doc2 != d) {
                    HashMap<String, Double> tw_doc2 = tf_idf(doc2, carpet);
                    double similitud = similitud_per_cosinus(tw_doc1, tw_doc2);
                    //si dos documents "doc2" tenen la mateixa similitud amb el "d" es posen els dos en la llista ld sinó serà una llista d'un sol element
                    if(!ranquing_similitud.containsKey(similitud))
                    {
                        List<Document> ld = new ArrayList<Document>();
                        ld.add(doc2);
                        ranquing_similitud.put(similitud, ld);
                    }
                    else{
                        ranquing_similitud.get(similitud).add(doc2);
                    }
                }
            }
        }

        return ranquing_similitud;
    }

    //comprobació expresions boleanes en el contingut del document
    Set<Document> expresions_boleanes(String[] paraules, Carpeta carpeta){
        //creació de dos sets(una per doocuments amb parauls que volem i l'altre per paraules que no volem)
        Set<Document> documents = new HashSet<>();
        //búsqueda del docs que contenen paraules que
        int i = 0;

        for(String paraula : paraules) { //per cada paraula del array paraules
            Set<Document> aux = new HashSet<>();
            if (carpeta.posting_list.containsKey(paraula)) { //mirem si està a la posting list
                for (Document d : carpeta.posting_list.get(paraula)) //agafem tots els documents que contenen la paraula
                    aux.add(d);
            }
            if (i == 0) documents = aux; //si és le primer cop afegim els documents de "aux" a "documente"
            else{
                documents.retainAll(aux); //si no és el primer cop fem la intersecció per assegurar que totes les paraules estiguin en els documents resultants
            }
            ++i;
        }
        return documents;
    }
}
