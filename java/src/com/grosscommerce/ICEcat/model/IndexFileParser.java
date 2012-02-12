/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */
package com.grosscommerce.ICEcat.model;

import com.grosscommerce.ICEcat.utilities.XmlUtil;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Used for parsing index files.
 * @author Anykey Skovorodkin
 */
public class IndexFileParser extends XmlObjectsListBase<ProductFileRef>
{
    public static final String ROOT_NODE_NAME = "files.index";

    public IndexFileParser()
    {
    }

    public void parse(InputStream is) throws Throwable
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = dbf.newDocumentBuilder().parse(is);

        Element rootElem = doc.getDocumentElement();

        Element objectElem = XmlUtil.selectSingleElement(rootElem,
                                                         this.getRootNodeName());

        if (objectElem == null)
        {
            throw new IllegalStateException(
                    "Element with name: " + this.getRootNodeName()
                    + " is not found!");
        }
        else
        {
            this.parseFromElement(objectElem);
        }
    }

    @Override
    protected ProductFileRef loadFromElement(Element objElement) throws
            Throwable
    {
        ProductFileRef ref = new ProductFileRef();
        ref.parseFromElement(objElement);
        return ref;
    }

    @Override
    protected void saveObject(ProductFileRef object)
    {
    }

    // <editor-fold defaultstate="collapsed" desc="XmlObjectBase implementation">
    @Override
    public String getRootNodeName()
    {
        return ROOT_NODE_NAME;
    }

    @Override
    protected String getChildNodesName()
    {
        return ProductFileRef.ROOT_NODE_NAME;
    }

    @Override
    public ProductFileRef[] getAll()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    // </editor-fold>
}
