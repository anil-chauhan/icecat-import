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

import com.grosscommerce.ICEcat.model.annotations.XmlLocalizedFieldAnnotation;
import org.w3c.dom.Element;

/**
 * Ordinary stores feature group's name
 * @author Anykey Skovorodkin
 */
public class FeatureGroup extends XmlObjectBase
{
    public static final String ROOT_NODE_NAME = "FeatureGroup";

    @XmlLocalizedFieldAnnotation(nodeName=NAME_PROP)
    private LocalizedValue name = new LocalizedValue();

    public FeatureGroup()
    {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    
    public String getName(int langId)
    {
        return this.name.getValue(langId);
    }

    // </editor-fold>

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
