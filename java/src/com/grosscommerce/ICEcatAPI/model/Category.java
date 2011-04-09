/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */
package com.grosscommerce.ICEcatAPI.model;

import com.grosscommerce.ICEcatAPI.model.annotations.ValueTypeEnum;
import com.grosscommerce.ICEcatAPI.model.annotations.XmlLocalizedFieldAnnotation;
import com.grosscommerce.ICEcatAPI.model.annotations.XmlFieldAnnotation;
import com.grosscommerce.ICEcatAPI.utilities.XmlUtil;
import org.w3c.dom.Element;

/**
 * Used for storing one category.
 * @author Anykey Skovorodkin
 */
public class Category extends XmlObjectBase
{
    public static final String ROOT_NODE_NAME       = "Category";
    public static final String PARENT_CATEGORY_NODE = "ParentCategory";

    @XmlFieldAnnotation (propertyName = ID_PROP, valueType = ValueTypeEnum.Int)
    private int id;
    @XmlFieldAnnotation (propertyName = "LowPic", valueType = ValueTypeEnum.String)
    private String lowPic;
    /**
     * "Score" attribute in the response reflects the category usage statistic.
     * The higher number means the higher usage level.
     */
    @XmlFieldAnnotation (propertyName = "Score", valueType = ValueTypeEnum.Int)
    private int score;
    /**
     * 1 This category may be used for product lookup in product list lookup request
     * 0 This category is not made searchable (in our own product finder tools)
     */
    @XmlFieldAnnotation (propertyName = "Searchable", valueType = ValueTypeEnum.Boolean)
    private boolean searchable;
    @XmlFieldAnnotation (propertyName = "ThumbPic", valueType = ValueTypeEnum.String)
    private String thumbnailPic;
    @XmlFieldAnnotation (propertyName = "UNCATID", valueType = ValueTypeEnum.String)
    private String uncatid;
    @XmlFieldAnnotation (propertyName = "Visible", valueType = ValueTypeEnum.Boolean)
    private boolean visible;
    @XmlLocalizedFieldAnnotation(nodeName="Description")
    private LocalizedValue description = new LocalizedValue();
    @XmlLocalizedFieldAnnotation(nodeName="Name")
    private LocalizedValue name  = new LocalizedValue();
    private int parentCategoryId = 0;
    private int level = -1;

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getLowPic()
    {
        return lowPic;
    }

    public void setLowPic(String lowPic)
    {
        this.lowPic = lowPic;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public boolean isSearchable()
    {
        return searchable;
    }

    public void setSearchable(boolean searchable)
    {
        this.searchable = searchable;
    }

    public String getThumbnailPic()
    {
        return thumbnailPic;
    }

    public void setThumbnailPic(String thumbnailPic)
    {
        this.thumbnailPic = thumbnailPic;
    }

    public String getUncatid()
    {
        return uncatid;
    }

    public void setUncatid(String uncatid)
    {
        this.uncatid = uncatid;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public String getDescription(int langId)
    {
        return this.description.getValue(langId);
    }

    public String getName(int langId)
    {
        return this.name.getValue(langId);
    }

    public int getParentCategoryId()
    {
        return this.parentCategoryId;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
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
        Element parentCategoryNode = XmlUtil.selectSingleElement(
                thisObjectElement, PARENT_CATEGORY_NODE);

        if(parentCategoryNode != null)
        {
            this.parentCategoryId = XmlUtil.selectIntAttribute(
                    parentCategoryNode, ID_PROP);
        }

        return true;
    }

    @Override
    protected void saveToElementInternal(Element parentElement)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // </editor-fold>

    @Override
    public String toString()
    {
        return "Category{" +
                "id=" + id +
                " parentId=" + parentCategoryId +
                " lowPic=" + lowPic +
                " score=" + score +
                " searchable=" + searchable +
                " thumbnailPic=" + thumbnailPic +
                " uncatid=" + uncatid +
                " visible=" + visible + '}';
    }
}
