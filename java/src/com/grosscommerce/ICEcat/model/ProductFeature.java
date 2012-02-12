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

import com.grosscommerce.ICEcat.model.annotations.ValueTypeEnum;
import com.grosscommerce.ICEcat.model.annotations.XmlFieldAnnotation;
import com.grosscommerce.ICEcat.utilities.XmlUtil;
import java.util.logging.Logger;
import org.w3c.dom.Element;

/**
 *
 * @author Anykey Skovorodkin
 */
public class ProductFeature extends XmlObjectBase
{
    public static final String ROOT_NODE_NAME = "ProductFeature";
    public static final String LOCALIZED_PROP = "Localized";
    public static final String VALUE_PROP = "Value";
    public static final String CATEGORY_FEATURE_GROUP_ID_PROP = "CategoryFeatureGroup_ID";
    public static final String PRESENTATION_VALUE_PROP = "Presentation_Value";
    @XmlFieldAnnotation(propertyName = LOCALIZED_PROP, valueType = ValueTypeEnum.Boolean)
    private boolean localized;
    @XmlFieldAnnotation(propertyName = VALUE_PROP, valueType = ValueTypeEnum.String)
    private String value;
    @XmlFieldAnnotation(propertyName = CATEGORY_FEATURE_GROUP_ID_PROP, valueType = ValueTypeEnum.Int)
    private int categoryFeatureGroupId;
    @XmlFieldAnnotation(propertyName = PRESENTATION_VALUE_PROP, valueType = ValueTypeEnum.String)
    private String presentationValue;
    private Feature feature = new Feature();

    public ProductFeature()
    {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public int getCategoryFeatureGroupId()
    {
        return categoryFeatureGroupId;
    }

    public boolean isLocalized()
    {
        return localized;
    }

    public String getPresentationValue()
    {
        return presentationValue;
    }

    public String getValue()
    {
        return value;
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="XmlObjectBase implementation">
    @Override
    public String getRootNodeName()
    {
        return ROOT_NODE_NAME;
    }

    @Override
    protected boolean parseFromElementInternal(Element thisObjectElement)
    {
        Element elem = XmlUtil.selectSingleElement(thisObjectElement,
                                                   Feature.ROOT_NODE_NAME);

        if (elem == null)
        {
            Logger.getLogger(ProductFeature.class.getName()).severe(
                    "Feature is not found");

            return false;
        }

        return this.feature.parseFromElement(elem);
    }

    @Override
    protected void saveToElementInternal(Element parentElement)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="overrides">
    @Override
    public String toString()
    {
        return "ProductFeature{" + "feature=" + feature + "presentationValue=" + presentationValue + '}';
    }// </editor-fold>
}
