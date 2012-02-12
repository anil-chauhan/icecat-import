/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */
package com.grosscommerce.ICEcat.utilities;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Used for working with xml.
 * @author Anykey Skovorodkin
 */
public abstract class XmlUtil
{
    public static String selectStringAttribute(Element parent, String attrName,
                                               String defaultVal)
    {
        String result = parent.getAttribute(attrName);

        if (result == null || result.isEmpty())
        {
//            Logger.getLogger(XmlUtil.class.getName()).log(Level.INFO,
//                    "Attribute {0} is not found", attrName);
            return defaultVal;
        }

        return result;
    }

    public static boolean selectBooleanAttribute(Element parent, String attrName)
    {
        String result = selectStringAttribute(parent, attrName, "0");

        if (result.equalsIgnoreCase("1")
                || result.equalsIgnoreCase("true"))
        {
            return true;
        }

        return false;
    }

    public static int selectIntAttribute(Element parent, String attrName)
    {
        String result = selectStringAttribute(parent, attrName, "0");
        return Integer.valueOf(result);
    }

    public static Date selectDateAttribute(Element parent, String attrName)
            throws ParseException
    {
        String result = selectStringAttribute(parent, attrName, DateUtil.
                dateToString(new Date()));
        return DateUtil.stringToDate(result);
    }

    public static Element selectSingleElement(Element parent, String elementName)
    {
        NodeList list = parent.getElementsByTagName(elementName);

        if (list.getLength() > 0)
        {
            return (Element) list.item(0);
        }

        return null;
    }

    public static List<Element> selectElementsByName(Element parent,
                                                     String elementName)
    {
        ArrayList<Element> result = new ArrayList<Element>();

        NodeList list = parent.getChildNodes();

        for (int i = 0; i < list.getLength(); i++)
        {
            Node node = list.item(i);

            if ((node instanceof Element) && node.getNodeName().equals(
                    elementName))
            {
                result.add((Element) node);
            }
        }

        return result;
    }

    public static String selectStringElement(Element parent, String elementName,
                                             String defaultVal)
    {
        Element element = selectSingleElement(parent, elementName);

        if (element == null)
        {
            return defaultVal;
        }
        else
        {
            return element.getTextContent();
        }
    }

    public static Date selectDateElement(Element parent, String elementName)
            throws ParseException
    {
        String result = selectStringElement(parent, elementName, DateUtil.
                dateToString(new Date()));
        return DateUtil.stringToDate(result);
    }

    public static int selectIntElement(Element parent, String elementName)
    {
        String result = selectStringElement(parent, elementName, "0");
        return Integer.valueOf(result);
    }

    public static boolean selectBooleanElement(Element parent,
                                               String elementName)
    {
        String result = selectStringElement(parent, elementName, "0");

        if (result.equalsIgnoreCase("1")
                || result.equalsIgnoreCase("true"))
        {
            return true;
        }

        return false;
    }

    public static void appendStringAttribute(Element thisElem, String attrName,
                                             String attrValue)
    {
        Attr attr = thisElem.getOwnerDocument().createAttribute(attrName);

        attr.setTextContent(attrValue);
        thisElem.setAttributeNode(attr);

    }

    public static void appendStringElement(Element parentElement,
                                           String nodeName, String nodeValue)
    {
        // check for exists
        Element elem = selectSingleElement(parentElement, nodeName);

        if (elem == null)
        {
            elem = parentElement.getOwnerDocument().createElement(nodeName);
            parentElement.appendChild(elem);
        }

        elem.setTextContent(nodeValue);
    }

    public static void appendBooleanAttribute(Element thisElement,
                                              String attrName, boolean value)
    {
        appendStringAttribute(thisElement, attrName, String.valueOf(value));
    }

    public static void appendIntAttribute(Element thisElement, String attrName,
                                          int value)
    {
        appendStringAttribute(thisElement, attrName, String.valueOf(value));
    }

    public static void appendDateAttribute(Element thisElement, String attrName,
                                           Date value)
    {
        appendStringAttribute(thisElement, attrName,
                              DateUtil.dateToString(value));
    }

    public static void appendBooleanElement(Element parentElement,
                                            String nodeName, boolean value)
    {
        appendStringElement(parentElement, nodeName, String.valueOf(value));
    }

    public static void appendIntElement(Element parentElement, String nodeName,
                                        int value)
    {
        appendStringElement(parentElement, nodeName, String.valueOf(value));
    }

    public static void appendDateElement(Element parentElement, String nodeName,
                                         Date value)
    {
        appendStringElement(parentElement, nodeName,
                            DateUtil.dateToString(value));
    }

    public static void saveDocumentToFile(Document xmlDoc, File file) throws
            TransformerConfigurationException, TransformerException
    {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(
                "{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(xmlDoc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    public static Document loadFromFile(File file) throws IllegalAccessException,
                                                          ParserConfigurationException,
                                                          SAXException,
                                                          IOException
    {
        if (!file.exists())
        {
            throw new IllegalAccessException("File is not exists: " + file.
                    getAbsolutePath());
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(file);
    }
}
