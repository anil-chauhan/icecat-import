/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */
package com.grosscommerce.core;

import com.grosscommerce.ICEcat.model.XmlObjectBase;
import com.grosscommerce.ICEcat.model.annotations.ValueTypeEnum;
import com.grosscommerce.ICEcat.model.annotations.XmlFieldAnnotation;
import org.w3c.dom.Element;

/**
 * Used for accessing to web service.
 * @author Anykey Skovorodkin
 */
public class Token extends XmlObjectBase {

    public static final String ROOT_NODE_NAME = "Token";
    private static final String CATEGORY_NAME_PROP = "CatName";
    private static final String VALUE_PROP = "Value";
    private static final String THREADS_COUNT_PROP = "Threads_Count";
    @XmlFieldAnnotation(propertyName = CATID_PROP, valueType = ValueTypeEnum.Int)
    private int catId;
    @XmlFieldAnnotation(propertyName = CATEGORY_NAME_PROP, valueType = ValueTypeEnum.String)
    private String catName;
    @XmlFieldAnnotation(propertyName = THREADS_COUNT_PROP, valueType = ValueTypeEnum.Int)
    private int threadsCount = 1;
    @XmlFieldAnnotation(propertyName = VALUE_PROP, valueType = ValueTypeEnum.String)
    private String value;

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getThreadsCount() {
        return threadsCount;
    }

    public void setThreadsCount(int threadsCount) {
        this.threadsCount = threadsCount;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="XmlObjectBase">
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
}
