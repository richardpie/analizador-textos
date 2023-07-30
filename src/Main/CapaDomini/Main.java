package Main.CapaDomini;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class Main {
    static CtrlDomini ctrl;

    static {
        try {
            ctrl = new CtrlDomini();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static Integer iterador;

    public static void main(String[] args) throws Exception {

        //CapaDomini.Domini.Main.registre(args[0], args[1], args[2]);
        //**** CapaDomini.Domini.Main.lectura_docs(args[0]);
        System.out.println("PROP MENU TESTING:" +
                "\n1.Carrega Document" +
                "\n2.Baixa Document" +
                "\n3.Importar Document" +
                "\n4.Recuperar Document" +
                "\n5.Consultar" +
                "\n6.Alta Expressio" +
                "\n7.Baixa Expressio" +
                "\n8.Modificar Expressio" +
                "\n9.Modificar Document" +
                "\n10.Exit");
        int opcion = 0;
        String seleccion;

        do {
            seleccion = System.console().readLine();
            if (seleccion.length() != 0)
                opcion = Integer.parseInt(seleccion);
            else
                opcion = 0;

            switch (opcion) {
                case 1:
                    try {
                        ctrl.carregar_un_document();
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    try {
                        System.out.println("Para eliminar un documento, primero introduzca el autor");
                        String autor_de_obra = System.console().readLine();

                        System.out.println("Ahora introduzca el titulo de la obra");
                        String titulo_de_obra = System.console().readLine();

                        ctrl.donar_de_baixa_document(autor_de_obra, titulo_de_obra);

                        System.out.println("El documento se ha dado de baja satisfactoriamente");


                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    try {
                        System.out.println("Ahora introduzca la ruta del archivo");
                        String path_obra = System.console().readLine();

                        ctrl.alta_document(path_obra);

                        System.out.println("Documento importado correctamente");
                    }

                    catch (FileNotFoundException e){

                        System.out.println("Ruta del documento incorrecta");
                    }
                    break;

                case 4:
                    try {
                        System.out.println("Para recuperar un documento, primero introduzca el autor");
                        String autor_de_obra = System.console().readLine();

                        System.out.println("Ahora introduzca el titulo de la obra");
                        String titulo_de_obra = System.console().readLine();


                        ctrl.recuperar_un_document(autor_de_obra, titulo_de_obra);

                        System.out.println("Documento recuperado correctamente");
                    }

                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }

                    break;

                case 5:
                    consultar();

                    break;

                case 6:
                    try {
                        System.out.println("Introduzca la expresion");
                        String exp = System.console().readLine();
                        ctrl.alta_expressio(exp);

                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }                        break;

                case 7:
                    try {
                        System.out.println("Introduzca la expresion");
                        String exp = System.console().readLine();
                        ctrl.baixa_expressio(exp);
                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }                        break;

                case 8:
                    try {
                        System.out.println("Introduzca la expresion a modificar");
                        String vell = System.console().readLine();
                        System.out.println("Introduzca la nueva expresion");
                        String nou = System.console().readLine();
                        ctrl.modificar_expresio(vell,nou);
                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }                        break;

                case 9:
                    try {

                        System.out.println("Introduzca el autor del documento a modificar");
                        String autor = System.console().readLine();

                        System.out.println("Introduzca el titulo del documento a modificar");
                        String titulo = System.console().readLine();

                        System.out.println("Introduzca el contenido de la modificacion");
                        String contenido = System.console().readLine();


                        ctrl.modificacion_de_documento(autor, titulo, contenido);

                        System.out.println("El documento se ha modificado satisfactoriamente");

                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 10:
                    System.out.println("SALIENDO");
                    break;

                default:
                    System.out.println("Opcio no valida");
                    break;
            }
        } while (opcion != 10);
    }

    static void consultar(){
        //Pre:
        //Post: Muestra por pantalla la informacion solicitada sobre los documentos listados
        System.out.println("TIPUS DE CONSULTA:" +
                "\n1.Llistar títols del autor" +
                "\n2.Llistar autors" +
                "\n3.Mostrar contingut" +
                "\n4.Llistar documents" +
                "\n10.Exit");
        Integer opcio_cercador = Integer.parseInt(System.console().readLine());

        switch (opcio_cercador) {
            case 1:
                try {
                    System.out.println("Introduzca el autor sobre el que quiere obtener los titulos");
                    String autor = System.console().readLine();
                    System.out.println (ctrl.llistar_per_titol(autor));
                    System.out.println("Titulos del autor consultados correctamente");

                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
                break;
            case 2:
                try {
                    System.out.println("Introduzca el prefijo para obtener los autores que coinciden");

                    String autor_prefix = System.console().readLine();
                    System.out.println(ctrl.llistar_autor_per_prefix(autor_prefix));
                    System.out.println("Autores consultados correctamente");
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
                break;

            case 3:
                try {
                    System.out.println("Introduzca el autor " +
                            "del documento que desea obtener");
                    String autor = System.console().readLine();

                    System.out.println("Introduzca el titulo " +
                            "del documento que desea obtener");

                    String titol = System.console().readLine();
                    System.out.println(ctrl.llistar_contingut(autor, titol));

                    System.out.println("Documento consultado correctamente");
                }

                catch (Exception e){
                    System.out.println(e.getMessage());
                }
                break;
            case 4:
                consultar_submenu();

                break;
            case 10:
                break;
        }
    };

    static void consultar_submenu(){
        //Pre:
        //Post: Muestra por pantalla los documentos solicitados que cumplen con los filtros de busqueda
        System.out.println("TIPUS DE CONSULTA:" +
                "\n1.Llistar K documents similars" +
                "\n2.Llistar documents per booleans" +
                "\n10.Exit");
        Integer opcio_cerca;
        opcio_cerca = Integer.parseInt(System.console().readLine());

        switch (opcio_cerca) {
            /*case 1:
                //es mostren tots els documents que tenim a el sistema (carregats al treemap)
                for(String autor_doc: ctrl.get_carpeta().listado_documento.keySet()) {
                    System.out.println(autor_doc);
                    for (String titol_doc : ctrl.get_carpeta().listado_documento.get(autor_doc).keySet()) {
                        System.out.println(titol_doc);
                    }
                }

                //entrada d'arguments (autor i títol del doc a comparar i n de documents rellevants)
                String autor1 = System.console().readLine();
                String titol1 = System.console().readLine();
                Integer k = Integer.parseInt(System.console().readLine());
                try {
                    TreeMap<Double, List<Document>> semblants = ctrl.llistar_k_documents(autor1, titol1,k);
                    iterador = 0;
                    for (Double d : semblants.keySet()) {
                        semblants.get(d).forEach((temp) -> {
                            ++iterador;
                            if (iterador > (semblants.size() - k)) //com està tot ordenat per de m enor a major agafem els últims k documents ja que esran els més semblants
                                System.out.println("similitud = " + d + " Titol: " + temp.titol + "  Autor: " + temp.autor);
                        });
                    }
                } catch (Exception e){
                    System.out.println(e);
                }
                break;*/
            /*case 2:
                System.out.println("Introduzca la expression:");
                String expresio = System.console().readLine();
                try {
                    Set<Document> docs= ctrl.documents_per_booleans(expresio);
                    for(Document doc: docs){
                        String autor = doc.getAutor();
                        String titol = doc.getTitol();
                        System.out.println(autor+" "+titol);
                    }


                    //ctrl.documents_per_booleans();

                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
                break;*/
        }
    }
}
