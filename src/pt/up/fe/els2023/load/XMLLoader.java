package pt.up.fe.els2023.load;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pt.up.fe.els2023.utils.FileUtils;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class XMLLoader implements Loader{
    @Override
    public Map<String, Object> load(File file) {
        file = new File(FileUtils.getFilePathWithRootSource(file));
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            // Make sure the XML is processed securely
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            Map<String, Object> xmlData = new HashMap<>();
            parseNode(xmlData, doc.getDocumentElement());

            return xmlData;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseNode(Map<String, Object> map, Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            NodeList children = element.getChildNodes();

            if (children.getLength() == 1 && children.item(0).getNodeType() == Node.TEXT_NODE) {
                map.put(element.getNodeName(), element.getTextContent());
            } else {
                Map<String, Object> childMap = new HashMap<>();
                for (int i = 0; i < children.getLength(); i++) {
                    parseNode(childMap, children.item(i));
                }

                map.put(element.getNodeName(), childMap);
            }
        }
    }
}
