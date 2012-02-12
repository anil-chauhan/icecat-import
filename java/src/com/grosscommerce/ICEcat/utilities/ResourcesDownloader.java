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

import com.grosscommerce.ICEcat.common.Constants;
import com.grosscommerce.ICEcat.model.Categories;
import com.grosscommerce.ICEcat.model.Languages;
import com.grosscommerce.ICEcat.model.Product;
import com.grosscommerce.ICEcat.model.XmlObjectBase;
import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Used for downloading resources from icecat.
 * @author Anykey Skovorodkin
 */
public class ResourcesDownloader
{
    public static Categories downloadCategories(String username, String password)
            throws Throwable
    {
        return downloadObject(new Categories(),
                              Constants.CATEGORIES_LIST_FILE_NAME, 
                              username,
                              password);
    }

    public static Languages downloadLanguares(String username, String password)
            throws Throwable
    {
        return downloadObject(new Languages(), 
                              Constants.LANGUAGE_LIST_FILE_NAME,
                              username, 
                              password);
    }

    public static Product downloadProduct(String relativeUrl, String username,
                                          String password) throws Throwable
    {
        return downloadObject(new Product(),
                              Constants.ICEGAT_DATA_URL + relativeUrl, 
                              username,
                              password);
    }

    public static <T extends XmlObjectBase> T downloadObject(
            T objectInstance,
            String url,
            String username,
            String password) throws Throwable
    {
        byte[] data = Downloader.download(url,
                                          username, 
                                          password);

        if (data == null)
        {
            return null;
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(
                data));

        Element rootElem = doc.getDocumentElement();

        Element objectElem = XmlUtil.selectSingleElement(rootElem,
                                                         objectInstance.
                getRootNodeName());

        if (objectElem == null)
        {
            throw new IllegalStateException(
                    "Element with name: " + objectInstance.getRootNodeName()
                    + " is not found!");
        }

        if (objectInstance.parseFromElement(objectElem))
        {
            return objectInstance;
        }

        return null; // something bad happened
    }
}