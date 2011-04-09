/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */
package gc.configfile;

import gc.ICEcatAPI.common.Constants;
import gc.ICEcatAPI.model.XmlObjectBase;
import gc.ICEcatAPI.model.annotations.ValueTypeEnum;
import gc.ICEcatAPI.model.annotations.XmlFieldAnnotation;
import gc.ICEcatAPI.model.annotations.XmlNodeType;
import gc.ICEcatAPI.utilities.XmlUtil;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Used for reading and writing config file.
 * @author Anykey Skovorodkin
 */
public class ConfigFile extends XmlObjectBase
{
    public static final String ROOT_NODE_NAME = "Config";

    private static final String LANGUAGE_PROP = "language";
    
    @XmlFieldAnnotation (nodeType = XmlNodeType.XmlNode, propertyName = LANGUAGE_PROP, valueType = ValueTypeEnum.String)
    private String languageShortCode = Constants.DEFAULT_LANGUAGE_SHORT_CODE;
    
    private ICECatSettings iCECatSettings = new ICECatSettings();
    private ImporterSettings importerSettings = new ImporterSettings();

    public ConfigFile()
    {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    
    public ICECatSettings getiCECatSettings()
    {
        return iCECatSettings;
    }

    public ImporterSettings getImporterSettings()
    {
        return importerSettings;
    }

    public String getLanguageShortCode()
    {
        return languageShortCode;
    }

    public void setLanguageShortCode(String languageShortCode)
    {
        this.languageShortCode = languageShortCode;
    }

    // </editor-fold>
    
    public void saveToFile(File file) throws TransformerException, ParserConfigurationException
    {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        // create root element
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement(ROOT_NODE_NAME);
        doc.appendChild(rootElement);
        
        // save model
        this.saveToThisElement(rootElement);

        // save to file
        XmlUtil.saveDocumentToFile(doc, file);
    }

    public void loadFromFile(File file) throws IOException, IllegalAccessException, ParserConfigurationException, SAXException
    {
        Document doc = XmlUtil.loadFromFile(file);
        this.parseFromElement(doc.getDocumentElement());
    }

    // <editor-fold defaultstate="collapsed" desc="XmlObjectBase">
    @Override
    public String getRootNodeName()
    {
        return ROOT_NODE_NAME;
    }

    @Override
    protected boolean parseFromElementInternal(Element thisObjectElement)
    {
        return true;
    }

    @Override
    protected void saveToElementInternal(Element parentElement)
    {
    }
    // </editor-fold>
}
