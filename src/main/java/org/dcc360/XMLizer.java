//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.dcc360;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.dcc360.Services.Crypta;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLizer {
    private static String source = SetupKurwanner.geDBDir();
    private static String installDir = SetupKurwanner.getInstallDir();
    private static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    public XMLizer() {
    }

    public static void initDBDataToXML() {
        try {
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document dom = documentBuilder.newDocument();
            Element root = dom.createElement("Databases");
            dom.appendChild(root);

            SaveDBXML(dom);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }

    public static void writeDBDataToXML() {
        initDBDataToXML();

        createDB(new Database("DB1", "localhost:1521/XEPDB1", "DIMON", "Q1w2e3r4t5y6", true));
        createDB(new Database("DB2", "localhost:1521/XEPDB2", "TECHUSER", "Q1w2e3r4t5y6", true));
        createDB(new Database("DB3", "localhost:1521/XEPDB3", "VIEWERUSER", "Q1w2e3r4t5y6", true));

    }

    //    public static void createDB(String dbName, String dbConnection, String dbUser, String dbPassword){
    public static void createDB(Database database) {
        Element db;
        Element nested;
        try {
            Document dom = dbf.newDocumentBuilder().parse(new File(source));

            db = dom.createElement("database");
            db.setAttribute("name", database.getName());
            db.setAttribute("isActive", database.getActive().toString());
            nested = dom.createElement("connection");
            nested.setTextContent(database.getConnection());
            db.appendChild(nested);
            nested = dom.createElement("username");
            nested.setTextContent(database.getUsername());
            db.appendChild(nested);
            nested = dom.createElement("password");
            nested.setTextContent(Crypta.encrypt(database.getPassword()));
            db.appendChild(nested);
            dom.getElementsByTagName("Databases").item(0).appendChild(db);
            dom.getDocumentElement().normalize();
            SaveDBXML(dom);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteDB(Database database) {
        try {
            Document document = dbf.newDocumentBuilder().parse(new File(source));
            NodeList matchedElementList = document.getElementsByTagName("database");
            for (int temp = 0; temp < matchedElementList.getLength(); temp++) {
                Node nNode = matchedElementList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if (database.getName().equals(eElement.getAttribute("name")))
                        nNode.getParentNode().removeChild(nNode);
                }
            }
            SaveDBXML(document);
        } catch (IOException | ParserConfigurationException | SAXException ioException) {
            ioException.printStackTrace();
        }
    }

    private static void SaveDBXML(Document dom) {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty("method", "xml");
            tr.setOutputProperty("encoding", "UTF-8");
            if (!(new File(SetupKurwanner.geDBDir())).exists()) {
                (new File(source)).getParentFile().mkdirs();
                (new File(source)).createNewFile();
            }

            tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(source)));
        } catch (TransformerException | IOException te) {
            te.printStackTrace();
        }

    }

    public static List<Database> getDBList() {
        try {
            List<Database> dbList = new ArrayList();
            Document document = dbf.newDocumentBuilder().parse(new File(source));
            NodeList matchedElementList = document.getElementsByTagName("database");

            for (int temp = 0; temp < matchedElementList.getLength(); temp++) {
                Node nNode = matchedElementList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    File dbFolder = new File(installDir + eElement.getAttribute("name"));
                    Boolean dbActive = Boolean.parseBoolean(eElement.getAttribute("isActive"));
                    String dbConnection = eElement.getElementsByTagName("connection").item(0).getTextContent();
                    String dbUsername = eElement.getElementsByTagName("username").item(0).getTextContent();
                    String dbPassword = Crypta.decrypt(eElement.getElementsByTagName("password").item(0).getTextContent());

                    dbList.add(new Database(dbFolder, dbConnection, dbUsername, dbPassword, dbActive));
                }
            }
            return dbList;
        } catch (Exception e) {
            System.out.println("Error read DB's list " + e.getMessage());
            return null;
        }
    }

    public static void setIsActiveAttribute(Database db, String value) {
        try {
            if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false"))
                return;
            Document document = dbf.newDocumentBuilder().parse(new File(source));
            NodeList matchedElementList = document.getElementsByTagName("database");
            for (int i = 0; i < matchedElementList.getLength(); i++) {
                Node xmlDB = matchedElementList.item(i);
                if (xmlDB.getNodeType() == Node.ELEMENT_NODE) {
                    Node element = xmlDB.getAttributes().getNamedItem("isActive");
                    if (xmlDB.getAttributes().getNamedItem("name").getNodeValue().equals(db.getName()))
                        element.setNodeValue(value.toLowerCase());
                }
            }
            SaveDBXML(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
