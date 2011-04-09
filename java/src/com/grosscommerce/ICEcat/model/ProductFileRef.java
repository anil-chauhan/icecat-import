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
import com.grosscommerce.ICEcat.model.annotations.XmlNodeType;
import com.grosscommerce.ICEcat.utilities.DateUtil;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;

/**
 * Used as object, which present file object in all Index Files.
 * @author Anykey Skovorodkin
 */
public class ProductFileRef extends XmlObjectBase
{

    public static final String ROOT_NODE_NAME = "file";
    /**
     * Relative path to document, which describe product.
     */
    @XmlFieldAnnotation (nullable = false, propertyName = PATH_PROP, valueType = ValueTypeEnum.String)
    private String path;
    /**
     * ICEcat product id.
     */
    @XmlFieldAnnotation (propertyName = PRODUCT_ID_PROP, valueType = ValueTypeEnum.Int)
    private int productId;
    /**
     * It is a manufacturer’s unique identifier for a product.
     */
    @XmlFieldAnnotation (propertyName = PROD_ID_PROP, valueType = ValueTypeEnum.String)
    private String prodId;
    /**
     * Last update time.
     */
    @XmlFieldAnnotation (propertyName = UPDATED_PROP, valueType = ValueTypeEnum.DateTime)
    private Date updated;
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
     * Stores id of supplier.
     */
    @XmlFieldAnnotation (propertyName = SUPPLIER_ID_PROP, valueType = ValueTypeEnum.Int)
    private int supplierId;
    /**
     * Id of product's category.
     */
    @XmlFieldAnnotation (nullable = false, propertyName = CATID_PROP, valueType = ValueTypeEnum.Int)
    private int catId;
    /**
     * Indicates whether a product is somewhere seen on the market by ICEcat.
     */
    @XmlFieldAnnotation (propertyName = ON_MARKET_PROP, valueType = ValueTypeEnum.Boolean)
    private boolean onMarket;
    @XmlFieldAnnotation (propertyName = MODEL_NAME_PROP, valueType = ValueTypeEnum.String)
    private String modelName;
    /**
     * Indicates how many times the current product was requested.
     */
    @XmlFieldAnnotation (propertyName = PRODUCT_VIEW_PROP, valueType = ValueTypeEnum.Int)
    private int productView;
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

    public ProductFileRef()
    {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public int getCatId()
    {
        return catId;
    }

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

    public String getModelName()
    {
        return modelName;
    }

    public boolean isOnMarket()
    {
        return onMarket;
    }

    public String getPath()
    {
        return path;
    }

    public String getProdId()
    {
        return prodId;
    }

    public int getProductId()
    {
        return productId;
    }

    public int getProductView()
    {
        return productView;
    }

    public String getQuality()
    {
        return quality;
    }

    public int getSupplierId()
    {
        return supplierId;
    }

    public Date getUpdated()
    {
        return updated;
    }// </editor-fold>

    @Override
    public String toString()
    {
        return "ProductFileRef{"
               + "path=" + path
               + ", productId=" + productId
               + ", prodId=" + prodId
               + ", updated=" + updated
               + ", quality=" + quality
               + ", supplierId=" + supplierId
               + ", catId=" + catId
               + ", onMarket=" + onMarket
               + ", modelName=" + modelName
               + ", productView=" + productView
               + ", highPicUrl=" + highPicUrl
               + ", highPicWidth=" + highPicWidth
               + ", highPicHeight=" + highPicHeight
               + ", highPicSize=" + highPicSize + '}';
    }

    // <editor-fold defaultstate="collapsed" desc="XmlObjectBase implementation">
    @Override
    public String getRootNodeName()
    {
        return ROOT_NODE_NAME;
    }

    protected Object getValueFromAttributes(Attributes attributes, XmlFieldAnnotation annotation) throws NumberFormatException
    {
        String value = attributes.getValue(annotation.propertyName());
        Object objValue = null;
        if (value != null && ! value.isEmpty())
        {
            switch (annotation.valueType())
            {
                case Boolean:
                    objValue = Boolean.valueOf(value);
                    break;
                case DateTime:
                    try
                    {
                        objValue = DateUtil.stringToDate(value);
                    }
                    catch (ParseException ex)
                    {
                        Logger.getLogger(ProductFileRef.class.getName()).log(Level.SEVERE,
                                                                             null,
                                                                             ex);
                    }
                    break;
                case Int:
                    objValue = Integer.valueOf(value);
                    break;
                case String:
                    objValue = value;
                    break;
            }
        }
        return objValue;
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
    public void loadFromAttributes(Attributes attributes)
    {
        List<Field> fields = this.getAllFields();

        for (Field field : fields)
        {
            if (field.isAnnotationPresent(XmlFieldAnnotation.class))
            {
                XmlFieldAnnotation annotation = field.getAnnotation(
                        XmlFieldAnnotation.class);
                if (annotation.nodeType() == XmlNodeType.XmlAttribute)
                {
                    Object objValue = this.getValueFromAttributes(attributes,
                                                             annotation);
                    if(objValue != null)
                    {
                        this.setFieldValue(field, objValue);
                    }
                }
            }
        }
    }

    protected void setFieldValue(Field field, Object objValue) throws SecurityException
    {
        boolean restoreAccessible = false;
        if ( ! field.isAccessible())
        {
            field.setAccessible(true);
            restoreAccessible = true;
        }
        
        try
        {
            field.set(this, objValue);
        }
        catch (IllegalArgumentException ex)
        {
            Logger.getLogger(ProductFileRef.class.getName()).log(Level.SEVERE,
                                                                 null, ex);
        }
        catch (IllegalAccessException ex)
        {
            Logger.getLogger(ProductFileRef.class.getName()).log(Level.SEVERE,
                                                                 null, ex);
        }
        
        if (restoreAccessible)
        {
            field.setAccessible(false);
        }
    }
}
