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
import com.grosscommerce.ICEcatAPI.model.annotations.XmlFieldAnnotation;
import com.grosscommerce.ICEcatAPI.utilities.XmlUtil;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;

/**
 * Product object.
 * @author Anykey Skovorodkin
 */
public class Product extends XmlObjectBase
{

    public static final String ROOT_NODE_NAME = "Product";
    public static final String PRODID_PROP = "Prod_id";
    public static final String RELEASEDATE_PROP = "ReleaseDate";
    public static final String THUMBPIC_PROP = "ThumbPic";
    public static final String THUMBPICSIZE_PROP = "ThumbPicSize";
    public static final String TITLE_PROP = "Title";
    private static final String CATEGORY_NODE_NAME = "Category";
    /**
     * Url to high product picture.
     */
    @XmlFieldAnnotation (propertyName = HIGHPIC_PROP, valueType = ValueTypeEnum.String)
    private String highPicUrl;
    @XmlFieldAnnotation (propertyName = HIGHPICWIDTH_PROP, valueType = ValueTypeEnum.Int)
    private int highPicWidth;
    @XmlFieldAnnotation (propertyName = HIGHPICHEIGHT_PROP, valueType = ValueTypeEnum.Int)
    private int highPicHeight;
    /**
     * Size of picture in bytes.
     */
    @XmlFieldAnnotation (propertyName = HIGHPICSIZE_PROP, valueType = ValueTypeEnum.Int)
    private int highPicSize;
    @XmlFieldAnnotation (propertyName = ID_PROP, valueType = ValueTypeEnum.Int)
    private int id;
    @XmlFieldAnnotation (propertyName = LOWPIC_PROP, valueType = ValueTypeEnum.String)
    private String lowPicUrl;
    @XmlFieldAnnotation (propertyName = LOWPICHEIGHT_PROP, valueType = ValueTypeEnum.Int)
    private int lowPicHeight;
    @XmlFieldAnnotation (propertyName = LOWPICWIDTH_PROP, valueType = ValueTypeEnum.Int)
    private int lowPicWidth;
    @XmlFieldAnnotation (propertyName = LOWPICSIZE_PROP, valueType = ValueTypeEnum.Int)
    private int lowPicSize;
    @XmlFieldAnnotation (propertyName = NAME_PROP, valueType = ValueTypeEnum.String)
    private String name;
    @XmlFieldAnnotation (propertyName = PRODID_PROP, valueType = ValueTypeEnum.String)
    private String prodID;
    @XmlFieldAnnotation (propertyName = RELEASEDATE_PROP, valueType = ValueTypeEnum.String)
    private String releaseDate;
    @XmlFieldAnnotation (propertyName = THUMBPIC_PROP, valueType = ValueTypeEnum.String)
    private String thumbPicUrl;
    @XmlFieldAnnotation (propertyName = THUMBPICSIZE_PROP, valueType = ValueTypeEnum.Int)
    private int thumbPicSize;
    @XmlFieldAnnotation (propertyName = TITLE_PROP, valueType = ValueTypeEnum.String)
    private String title;
    /**
     * Possible only 3 values:
     * SUPPLIER The content is received from a supplier CMS, but not standardized by an Icecat editor. The
     *  language-specific directories are likely to contain the full (unstandardized) data-sheet.
     * ICECAT The content is entered or standardized by ICECAT editors. The standardized data can be found in the
     *  INT directory and the language-specific directories.
     * NOEDITOR The content is received from a merchant (in most cases one of the 100s of distributors we are daily
     *  “polling”) and may be parsed. Editors haven’t described this product yet. The NOEDITOR data is not
     *  exported in XML to 3rd parties
     *
     * May be better, if this fild will be pressed as enum in future, but now we store it as String.
     */
    @XmlFieldAnnotation (propertyName = QUALITY_PROP, valueType = ValueTypeEnum.String)
    private String quality;
    /**
     * Parrent category id.
     */
    private int catId;
    private HashMap<Integer, CategoryFeatureGroup> featuresGroups =
                                                   new HashMap<Integer, CategoryFeatureGroup>();
    
    private SummaryDescription summaryDescription = new SummaryDescription();

    public Product()
    {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public int getHighPicHeight()
    {
        return highPicHeight;
    }

    public int getHighPicSize()
    {
        return highPicSize;
    }

    public String getHighPicUrl()
    {
        return highPicUrl;
    }

    public int getHighPicWidth()
    {
        return highPicWidth;
    }

    public int getId()
    {
        return id;
    }

    public int getLowPicHeight()
    {
        return lowPicHeight;
    }

    public int getLowPicSize()
    {
        return lowPicSize;
    }

    public String getLowPicUrl()
    {
        return lowPicUrl;
    }

    public int getLowPicWidth()
    {
        return lowPicWidth;
    }

    public String getName()
    {
        return name;
    }

    public String getProdID()
    {
        return prodID;
    }

    public String getQuality()
    {
        return quality;
    }

    public String getReleaseDate()
    {
        return releaseDate;
    }

    public int getThumbPicSize()
    {
        return thumbPicSize;
    }

    public String getThumbPicUrl()
    {
        return thumbPicUrl;
    }

    public String getTitle()
    {
        return title;
    }

    public int getCatId()
    {
        return catId;
    }

    public SummaryDescription getSummaryDescription()
    {
        return this.summaryDescription;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="XmlObjectBase implementation">
    @Override
    public String getRootNodeName()
    {
        return ROOT_NODE_NAME;
    }

    @Override
    protected boolean parseFromElementInternal(Element thisObjectElement)
    {
        // parse category id
        Element categoryElem = XmlUtil.selectSingleElement(thisObjectElement,
                                                           CATEGORY_NODE_NAME);

        if (categoryElem == null)
        {
            Logger.getLogger(Product.class.getName()).severe(
                    "Category elem is not found.");
            return false;
        }

        this.catId = XmlUtil.selectIntAttribute(categoryElem, ID_PROP);

        this.parseCategoryFeatureGroups(thisObjectElement);

        this.parseProductFeatures(thisObjectElement);

        // parse summary description
        Element summaryDescrptElem = XmlUtil.selectSingleElement(thisObjectElement,
                                                                 SummaryDescription.ROOT_NODE_NAME);
        if(summaryDescrptElem != null)
        {
            this.summaryDescription.parseFromElement(summaryDescrptElem);
        }

        return true;
    }

    @Override
    protected void saveToElementInternal(Element parentElement)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void parseProductFeatures(Element thisObjectElement) throws IllegalArgumentException
    {
        List<Element> productFeatureElems = XmlUtil.selectElementsByName(
                thisObjectElement, ProductFeature.ROOT_NODE_NAME);

        for (Element productFeatureElem : productFeatureElems)
        {
            ProductFeature feature = new ProductFeature();
            if (feature.parseFromElement(productFeatureElem))
            {
                CategoryFeatureGroup group = this.featuresGroups.get(
                        feature.getCategoryFeatureGroupId());

                if (group != null)
                {
                    group.addProductFeature(feature);
                }
                else
                {
                    Logger.getLogger(Product.class.getName()).log(Level.SEVERE,
                                                                  "CategoryFeatureGroup with id: {0} is not found",
                                                                  feature.getCategoryFeatureGroupId());
                }
            }
        }
    }

    private void parseCategoryFeatureGroups(Element thisObjectElement) throws IllegalArgumentException
    {
        // parse feature groups
        List<Element> categoryFeatureGroupsElems = XmlUtil.selectElementsByName(
                thisObjectElement,
                CategoryFeatureGroup.ROOT_NODE_NAME);
        for (Element categoryFeatureGroupElem : categoryFeatureGroupsElems)
        {
            CategoryFeatureGroup group = new CategoryFeatureGroup();
            if (group.parseFromElement(categoryFeatureGroupElem))
            {
                this.featuresGroups.put(group.getId(), group);
            }
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="overrides">
    @Override
    public String toString()
    {
        return "Product{"
               + " highPicUrl=" + highPicUrl
               + " highPicWidth=" + highPicWidth
               + " highPicHeight=" + highPicHeight
               + " highPicSize=" + highPicSize
               + " id=" + id
               + " lowPicUrl=" + lowPicUrl
               + " lowPicHeight=" + lowPicHeight
               + " lowPicWidth=" + lowPicWidth
               + " lowPicSize=" + lowPicSize
               + " name=" + name
               + " prodID=" + prodID
               + " releaseDate=" + releaseDate
               + " thumbPicUrl=" + thumbPicUrl
               + " thumbPicSize=" + thumbPicSize
               + " title=" + title
               + " quality=" + quality
               + " catId=" + catId + '}';
    }// </editor-fold>
}
