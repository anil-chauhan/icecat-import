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
import java.util.ArrayList;
import java.util.logging.Logger;
import org.w3c.dom.Element;

/**
 * Used for storing description of one product feature group.
 * For example for stroring Display Properties features.
 * @author Anykey Skovorodkin
 */
public class CategoryFeatureGroup extends XmlObjectBase
{
    public static final String ROOT_NODE_NAME = "CategoryFeatureGroup";
    private static final String NO_PROP = "No";

    @XmlFieldAnnotation(propertyName=ID_PROP,valueType=ValueTypeEnum.Int)
    private int id;
    @XmlFieldAnnotation(propertyName=NO_PROP,valueType=ValueTypeEnum.Int)
    private int no;

    private FeatureGroup featureGroup = new FeatureGroup();

    private ArrayList<ProductFeature> productFeatures = new ArrayList<ProductFeature>();

    public CategoryFeatureGroup()
    {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    
    public String getName(int langId)
    {
        return this.featureGroup.getName(langId);
    }

    public int getId()
    {
        return id;
    }

    public int getNo()
    {
        return no;
    }

    // </editor-fold>

    public void addProductFeature(ProductFeature productFeature)
    {
        this.productFeatures.add(productFeature);
    }

    public ProductFeature[] getAllProductFeatures()
    {
        return this.productFeatures.toArray(new ProductFeature[0]);
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
        // parse feature group elem
        Element featureGroupElem = XmlUtil.selectSingleElement(thisObjectElement,
                                                           FeatureGroup.ROOT_NODE_NAME);
        
        if(featureGroupElem == null)
        {
            Logger.getLogger(CategoryFeatureGroup.class.getName()).severe(
                    "FeatureGroup element is not found");
            
            return false;
        }
        
        return this.featureGroup.parseFromElement(featureGroupElem);
    }

    @Override
    protected void saveToElementInternal(Element parentElement)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // </editor-fold>
}
