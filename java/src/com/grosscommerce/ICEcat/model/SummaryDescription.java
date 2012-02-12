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
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.Element;

/**
 * Product's summary description.
 * @author Anykey Skovorodkin
 */
public class SummaryDescription extends XmlObjectBase
{
    public static final String ROOT_NODE_NAME = "SummaryDescription";
    public static final String LONG_SUMMARY_DESCRIPTION = "LongSummaryDescription";
    public static final String SHORT_SUMMARY_DESCRIPTION = "ShortSummaryDescription";
    /**
     * key - language's id, value - description
     */
    private HashMap<Integer, String> shortSummaryDescription =
            new HashMap<Integer, String>();
    /**
     * key - language's id, value - description
     */
    private HashMap<Integer, String> longSummaryDescription =
            new HashMap<Integer, String>();

    public SummaryDescription()
    {
    }

    public String getLongDescription(int langId)
    {
        return this.longSummaryDescription.get(langId);
    }

    public String getShortDescription(int langId)
    {
        return this.shortSummaryDescription.get(langId);
    }

    // <editor-fold defaultstate="collapsed" desc="XmlObjectBase implementation">
    @Override
    public String getRootNodeName()
    {
        return ROOT_NODE_NAME;
    }

    @Override
    protected boolean parseFromElementInternal(Element thisObjectElement)
    {
        // parse short summary descriptions
        List<Element> elements = XmlUtil.selectElementsByName(thisObjectElement,
                                                              SHORT_SUMMARY_DESCRIPTION);
        this.parseDescriptions(elements, this.shortSummaryDescription);

        // parse long summary description
        elements = XmlUtil.selectElementsByName(thisObjectElement,
                                                LONG_SUMMARY_DESCRIPTION);
        this.parseDescriptions(elements, this.longSummaryDescription);

        return true;
    }

    @Override
    protected void saveToElementInternal(Element parentElement)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // </editor-fold>
    private void parseDescriptions(List<Element> elements,
                                   HashMap<Integer, String> summaryDescription)
    {
        for (Element elem : elements)
        {
            int langId = XmlUtil.selectIntAttribute(elem, LANGID_PROP);
            String value = elem.getTextContent();

            summaryDescription.put(langId, value);
        }
    }
}
