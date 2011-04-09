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

import org.w3c.dom.Element;

/**
 * Base interface for all objects, which can be parsed from xml files.
 * @author Anykey Skovorodkin
 */
public interface IXmlObject
{
    public static final String ID_PROP = "ID";
    public static final String CATID_PROP = "Catid";
    public static final String HIGHPICHEIGHT_PROP = "HighPicHeight";
    public static final String HIGHPICSIZE_PROP = "HighPicSize";
    public static final String HIGHPICWIDTH_PROP = "HighPicWidth";
    public static final String HIGHPIC_PROP = "HighPic";
    public static final String MODEL_NAME_PROP = "Model_Name";
    public static final String ON_MARKET_PROP = "On_Market";
    public static final String PRODUCT_ID_PROP = "Product_ID";
    public static final String PRODUCT_VIEW_PROP = "Product_View";
    public static final String PROD_ID_PROP = "Prod_ID";
    public static final String QUALITY_PROP = "Quality";
    public static final String PATH_PROP = "path";
    public static final String SUPPLIER_ID_PROP = "Supplier_id";
    public static final String UPDATED_PROP = "Updated";
    public static final String LOWPIC_PROP = "LowPic";
    public static final String LOWPICHEIGHT_PROP = "LowPicHeight";
    public static final String LOWPICWIDTH_PROP = "LowPicWidth";
    public static final String LOWPICSIZE_PROP = "LowPicSize";
    public static final String NAME_PROP = "Name";
    public static final String LANGID_PROP = "langid";

    /**
     * Parses object from element.
     * @param thisObjectElement xml element, which present THIS object.
     */
    public boolean parseFromElement(Element thisObjectElement) throws IllegalArgumentException;
    /**
     * Will append new xml node which stores model of this object.
     * @param parentElement
     */
    public void saveToElement(Element parentElement);
}
