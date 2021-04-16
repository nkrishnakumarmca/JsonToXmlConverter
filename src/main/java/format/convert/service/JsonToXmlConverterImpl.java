package format.convert.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import format.convert.exception.XmlConverterException;
import format.convert.util.AppUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Iterator;
import java.util.Map;

public class JsonToXmlConverterImpl implements Converter {

    @Override
    public void xmlJsonConverter(String inputFilePath, String outputFilePath) {
        try {
            ObjectMapper jsonMapper = new ObjectMapper();
            JsonNode allNodes = jsonMapper.readTree(new File(inputFilePath));

            Iterator<JsonNode> allElements = allNodes.elements();
            Iterator<String> rootTagNames = allNodes.fieldNames();
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = getElement(document, "object");
            //create the xml file transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            if (allNodes.size() <= 0) {
                String s = allNodes.asText();
                Element element = getElement(document, AppUtil.validate(s));
                element.setTextContent(s);
                document.appendChild(element);
            } else {
                document.appendChild(root);
                buildXml(allElements, rootTagNames, document, root, true);
            }
            StreamResult streamResult = new StreamResult(new File(outputFilePath));
            transformer.transform(domSource, streamResult);
        } catch (Exception e) {
            throw new XmlConverterException();
        }
    }

    /**
     * @param allElements
     * @param rootTagNames
     * @param document
     * @param root
     * @param hasAttribute
     */
    private void buildXml(Iterator<JsonNode> allElements, Iterator<String> rootTagNames, Document document, Element root, boolean hasAttribute) {
        while (allElements.hasNext()) {
            JsonNode currJsonNode = allElements.next();
            String rootTagName = null;
            if (hasAttribute) {
                rootTagName = rootTagNames.hasNext() ? rootTagNames.next() : null;
            }
            if (currJsonNode.isObject()) {
                buildObject(rootTagName, document, root, currJsonNode, hasAttribute);

            } else if (currJsonNode.isBoolean()) {
                Element element = getElement(document, getName(currJsonNode));
                if (hasAttribute && rootTagName != null) {
                    setAttribute(rootTagName, element);
                }
                element.setTextContent(currJsonNode.asText());
                root.appendChild(element);
            } else if (currJsonNode.isArray()) {
                buildArray(rootTagName, document, root, currJsonNode, true);
            } else {
                String value = currJsonNode.asText();
                Element element = getElement(document, AppUtil.validate(value));
                if (hasAttribute && rootTagName != null) {
                    setAttribute(rootTagName, element);
                }
                element.setTextContent(value);
                root.appendChild(element);
            }

        }
    }

    /**
     * @param rootTagName
     * @param element
     */
    private void setAttribute(String rootTagName, Element element) {
        element.setAttribute("name", rootTagName);
    }

    /**
     * @param document
     * @param validate
     * @return
     */
    private Element getElement(Document document, String validate) {
        return document.createElement(validate);
    }

    /**
     * @param currJsonNode
     * @return
     */
    private String getName(JsonNode currJsonNode) {
        return currJsonNode.getNodeType().name().toLowerCase();
    }

    /**
     * @param rootTagName
     * @param document
     * @param root
     * @param currJsonNode
     * @param hasAttribute
     */
    private void buildObject(String rootTagName, Document document, Element root, JsonNode currJsonNode, boolean hasAttribute) {
        Iterator<Map.Entry<String, JsonNode>> currJsonNodefields = currJsonNode.fields();
        Element element = getElement(document, getName(currJsonNode));
        setAttribute(rootTagName, hasAttribute, element);
        root.appendChild(element);

        while (currJsonNodefields.hasNext()) {
            Map.Entry<String, JsonNode> field = currJsonNodefields.next();
            String fieldName = field.getKey();
            JsonNode jsonNode = field.getValue();
            if(jsonNode.isArray()){
                //String rootTagName, Document document, Element parentElement, JsonNode currJsonNode, boolean hasAttribute
                buildArray(rootTagName,document,root,jsonNode,false);
            }
            Element newElement = getElement(document, AppUtil.validate(jsonNode.asText()));
            setAttribute(fieldName, newElement);
            newElement.appendChild(document.createTextNode(jsonNode.asText()));
            element.appendChild(newElement);
        }
    }

    /**
     * @param rootTagName
     * @param hasAttribute
     * @param element
     */
    private void setAttribute(String rootTagName, boolean hasAttribute, Element element) {
        if (hasAttribute && rootTagName != null) {
            setAttribute(rootTagName, element);
        }
    }

    /**
     * @param rootTagName
     * @param document
     * @param parentElement
     * @param currJsonNode
     * @param hasAttribute
     */
    private void buildArray(String rootTagName, Document document, Element parentElement, JsonNode currJsonNode, boolean hasAttribute) {
        Element element = getElement(document, getName(currJsonNode));
        if (hasAttribute) {
            setAttribute(rootTagName, element);
        }
        parentElement.appendChild(element);
        Iterator<JsonNode> subElements = currJsonNode.elements();
        while (subElements.hasNext()) {
            JsonNode jsonNode = subElements.next();
            if (jsonNode.isArray() || jsonNode.isObject()) {
                buildXml(jsonNode.elements(), jsonNode.fieldNames(), document, element, true);
            } else {
                String value = jsonNode.asText();
                Element newElement = getElement(document, AppUtil.validate(value));
                newElement.setTextContent(value);
                element.appendChild(newElement);
            }
        }
    }
}
