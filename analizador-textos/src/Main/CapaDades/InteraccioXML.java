package Main.CapaDades;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class InteraccioXML extends InteraccioFitxers {


    @Override
    protected void modificaDocument(String path, String contingut){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(new File(path));

            document.getDocumentElement().normalize();

            Node root = document.getDocumentElement();
            NodeList elementos_documento = root.getChildNodes();

            String autor = "", titol = "";
            for(int i = 0; i < elementos_documento.getLength(); ++i) {


                if(elementos_documento.item(i).getNodeType() == Node.ELEMENT_NODE){

                    if(elementos_documento.item(i).getNodeName() == "autor")
                        autor = elementos_documento.item(i).getTextContent();
                    else if(elementos_documento.item(i).getNodeName() == "titol")
                        titol = elementos_documento.item(i).getTextContent();


                }
            }

            File xml = new File(path);
            FileWriter xmlw = new FileWriter(xml);
            PrintWriter pxml = new PrintWriter(xmlw);
            pxml.println("<?xml version = \"1.0\" encoding = \"utf-8\"?>\n" +
                    "\n" +
                    "<document>");

            pxml.println("\t<autor>\n" +
                    autor +
                    "</autor>\n");

            pxml.println("\t<titol>\n" +
                    titol +
                    "</titol>\n");

            pxml.println("\t<contingut>\n" +
                    contingut +
                    "</contingut>\n");

            pxml.println("</document>");

            pxml.close();
            System.out.println("TXT a XML completado");

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

}
