package Main.CapaDades;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.*;


public abstract class InteraccioFitxers {

   public static void importaDocument(String path){

        String old_path = path;
        File filename= new File(old_path);
       String nomFile= filename.getName();
       String newPath= "EXE/Carpeta_Estandar";
        File newFile = new File(newPath+"/"+nomFile);
        Path old = filename.toPath();
        Path imported= newFile.toPath();
       try{
            Files.copy(old,imported);

       } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static void eliminaDocument(String path) throws IOException {

        String old_path = path;
        System.out.println(old_path);
        File filename= new File(old_path);
        String nomFile= filename.getName();
        System.out.println(nomFile);

        File archivo = new File("EXE/Carpeta_Estandar/"+nomFile);
        File destino = new File("EXE/Carpeta_Eliminats/"+nomFile);

        archivo.renameTo(destino);


    }

    static void recuperaDocument(String path) throws IOException{

        String old_path = path;
        File filename= new File(old_path);
        String nomFile= filename.getName();

        File archivo = new File("EXE/Carpeta_Eliminats/"+nomFile);
        File destino = new File("EXE/Carpeta_Estandar/"+nomFile);

        archivo.renameTo(destino);
    }
    void modificaDocument(String path,String contenido){

       if (path.endsWith("txt")){
           modificaDocument(path, contenido);
       }

    }


}