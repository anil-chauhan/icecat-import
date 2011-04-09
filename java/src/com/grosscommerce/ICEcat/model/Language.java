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
import com.grosscommerce.ICEcat.model.annotations.XmlLocalizedFieldAnnotation;
import com.grosscommerce.ICEcat.model.annotations.XmlFieldAnnotation;
import org.w3c.dom.Element;

/**
 * Used for storing information about language.
 * @author Anykey Skovorodkin
 */
public class Language extends XmlObjectBase {

    public static final String ROOT_NODE_NAME = "Language";
    public static final String CODE_PARAM = "Code";
    public static final String SHORTCODE_PARAM = "ShortCode";
    public static final String SID_PARAM = "Sid";
    @XmlFieldAnnotation(propertyName = CODE_PARAM, valueType = ValueTypeEnum.String)
    private String code;
    @XmlFieldAnnotation(propertyName = ID_PROP, valueType = ValueTypeEnum.Int)
    private int id;
    @XmlFieldAnnotation(propertyName = SHORTCODE_PARAM, valueType = ValueTypeEnum.String)
    private String shortCode;
    @XmlFieldAnnotation(propertyName = SID_PARAM, valueType = ValueTypeEnum.Int)
    private int sid;
    @XmlLocalizedFieldAnnotation(nodeName = NAME_PROP)
    private LocalizedValue name = new LocalizedValue();

    public Language() {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getCode() {
        return code;
    }

    public int getId() {
        return id;
    }

    public LocalizedValue getName() {
        return name;
    }

    public String getShortCode() {
        return shortCode;
    }

    public int getSid() {
        return sid;
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="XmlObjectBase implementation">
    @Override
    public String getRootNodeName() {
        return ROOT_NODE_NAME;
    }

    @Override
    protected boolean parseFromElementInternal(Element thisObjectElement) {
        return true;
    }

    @Override
    protected void saveToElementInternal(Element parentElement) {
    }

    // </editor-fold>
    @Override
    public String toString() {
        return "Language{" + "code=" + code + "id=" + id + "shortCode=" + shortCode + '}';
    }
}
