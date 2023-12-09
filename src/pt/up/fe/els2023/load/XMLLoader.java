package pt.up.fe.els2023.load;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class XMLLoader implements Loader {
    @Override
    public Map<String, Object> load(File file) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            // Make sure the XML is processed securely
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            Map<String, Object> xmlData = new HashMap<>();
            parseNode(xmlData, doc.getDocumentElement());

            return xmlData;
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void parseNode(Map<String, Object> map, Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            NodeList children = element.getChildNodes();

            if (children.getLength() == 1 && children.item(0).getNodeType() == Node.TEXT_NODE) {
                // Numeric value
                try {
                    Double d = Double.parseDouble(element.getTextContent());

                    map.put(element.getNodeName(), d);
                } 
                
                // String value
                catch (NumberFormatException nfe) {
                    map.put(element.getNodeName(), element.getTextContent());
                }
            }
            
            else {
                Map<String, Object> childMap = new HashMap<>();

                for (int i = 0; i < children.getLength(); i++)
                    parseNode(childMap, children.item(i));

                map.put(element.getNodeName(), childMap);
            }
        }
    }
}
