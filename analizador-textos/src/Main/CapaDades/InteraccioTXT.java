package Main.CapaDades;

import java.io.*;

public class InteraccioTXT extends InteraccioFitxers{

    @Override
    protected void modificaDocument(String path, String contenido){
        File arxiu = null;
        BufferedReader buffer_lectura = null;
        String titol="";
        String autor="";
        try {
            arxiu = new File(path); //path de on es troba fitxer a llegir
            FileReader arxiu_lectura = new FileReader(arxiu);
            String nomFile= arxiu.getName();
            buffer_lectura = new BufferedReader(arxiu_lectura);

            //Procedim a llegir per linies
            String linia;

            Integer i = 0;
            while ((linia = buffer_lectura.readLine()) != null) {
                if (!linia.isEmpty()) {
                    if (i == 0) autor=linia; //la primera linia del document correspon a l'autor
                    System.out.println(autor);
                    if (i == 1) titol=linia; //la segona linia del document correspon al titol
                    System.out.println(titol);
                    ++i;
                } //ara tenim tota la informacio del document per escriure que no s'ha modificat
            }
            BufferedWriter writer= new BufferedWriter(new FileWriter(path));
            for(int j = 0; j < 3; ++j){ //el 3 és de autor, titol, contingut
                if (j == 0){
                    writer.write(autor);
                    writer.newLine();
                }
               else if (j == 1){
                    writer.write(titol);
                    writer.newLine();
                }
                else{
                    writer.write(contenido);
                    writer.newLine();
                }
            }
            writer.close();
        }catch(Exception exc) {
            exc.printStackTrace();
        }

    }

}
