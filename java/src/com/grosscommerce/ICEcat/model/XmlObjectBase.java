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
import com.grosscommerce.ICEcat.model.annotations.XmlFieldAnnotation;
import com.grosscommerce.ICEcat.model.annotations.XmlNodeType;
import com.grosscommerce.ICEcat.utilities.XmlUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;

/**
 * Base XmlObject. Provides basic functional for parsing any objects from xml.
 * @author Anykey Skovorodkin
 */
public abstract class XmlObjectBase implements IXmlObject {

    public abstract String getRootNodeName();

    protected abstract boolean parseFromElementInternal(Element thisObjectElement);

    protected abstract void saveToElementInternal(Element parentElement);

    private void checkNodeName(Element thisObjectElement) throws IllegalArgumentException {
        if (!thisObjectElement.getNodeName().equals(this.getRootNodeName())) {
            throw new IllegalArgumentException(
                    "Error! Element name is not valid. Element name: " + thisObjectElement.getNodeName() + " != " + this.getRootNodeName());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="IXmlObject implementation">
    @Override
    public boolean parseFromElement(Element thisObjectElement) throws IllegalArgumentException {
        this.checkNodeName(thisObjectElement);

        List<Field> fields = this.getAllFields();

        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            boolean restoreAccessible = false;
            if (!field.isAccessible()) {
                field.setAccessible(true);
                restoreAccessible = true;
            }

            if (!this.parseField(field, thisObjectElement)) {
                if (!this.parseLocalizedField(field, thisObjectElement)) {
                    if (!this.parseXmlObject(field, thisObjectElement)) {
                        // TODO: add other types here
                    }
                }
            }

            if (restoreAccessible) {
                field.setAccessible(false);
            }
        }

        return this.parseFromElementInternal(thisObjectElement);
    }

    protected List<Field> getAllFields() throws SecurityException {
        List<Field> fields = new ArrayList<Field>();

        fields.addAll(Arrays.asList(this.getClass().getDeclaredFields()));

        Class supertype = this.getClass().getSuperclass();

        while (supertype != null && supertype != XmlObjectBase.class) {
            Field[] inheritedFields = supertype.getDeclaredFields();

            fields.addAll(Arrays.asList(inheritedFields));

            supertype = supertype.getSuperclass();
        }

        return fields;
    }

    // </editor-fold>
    private boolean parseField(Field field, Element thisObjectElement) throws SecurityException, IllegalArgumentException {
        XmlFieldAnnotation xmlProp =
                field.getAnnotation(XmlFieldAnnotation.class);
        Object value = null;
        if (xmlProp == null) {
            return false;
        }

        switch (xmlProp.valueType()) {
            case Boolean:
                if (xmlProp.nodeType() == XmlNodeType.XmlAttribute) {
                    value = XmlUtil.selectBooleanAttribute(thisObjectElement,
                            xmlProp.propertyName());
                } else {
                    value = XmlUtil.selectBooleanElement(thisObjectElement,
                            xmlProp.propertyName());
                }
                break;
            case DateTime:
                try {
                    if (xmlProp.nodeType() == XmlNodeType.XmlAttribute) {
                        value = XmlUtil.selectDateAttribute(thisObjectElement,
                                xmlProp.propertyName());
                    } else {
                        value = XmlUtil.selectDateElement(thisObjectElement,
                                xmlProp.propertyName());
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(XmlObjectBase.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
                break;
            case Int:
                if (xmlProp.nodeType() == XmlNodeType.XmlAttribute) {
                    value = XmlUtil.selectIntAttribute(thisObjectElement,
                            xmlProp.propertyName());
                } else {
                    value = XmlUtil.selectIntElement(thisObjectElement,
                            xmlProp.propertyName());
                }
                break;
            case String:
                if (xmlProp.nodeType() == XmlNodeType.XmlAttribute) {
                    value = XmlUtil.selectStringAttribute(thisObjectElement,
                            xmlProp.propertyName(),
                            "");
                } else {
                    value = XmlUtil.selectStringElement(thisObjectElement,
                            xmlProp.propertyName(),
                            "");
                }
                break;
            default:
                throw new IllegalArgumentException(
                        "Unknown value type: " + xmlProp.valueType());
        }

        if (value != null) {
            try {
                field.set(this, value);
                return true;
            } catch (IllegalAccessException ex) {
                Logger.getLogger(XmlObjectBase.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }

        return false;
    }

    private boolean parseLocalizedField(Field field, Element thisObjectElement) {
        XmlLocalizedFieldAnnotation localizedField = field.getAnnotation(
                XmlLocalizedFieldAnnotation.class);

        if (localizedField == null) {
            return false;
        }

        try {
            if (field.getType() != LocalizedValue.class) {
                throw new IllegalStateException(
                        "XmlLocalizedFieldAnnotation will be used only for LocalizedValue fields");
            }

            LocalizedValue fieldValue = (LocalizedValue) field.get(this);

            List<Element> xmlElems = XmlUtil.selectElementsByName(
                    thisObjectElement, localizedField.nodeName());

            for (Element elem : xmlElems) {
                int langId = XmlUtil.selectIntAttribute(elem,
                        localizedField.langIdAttrName());
                String strValue = XmlUtil.selectStringAttribute(elem,
                        localizedField.valueAttrName(),
                        "");
                fieldValue.putValue(langId, strValue);
            }
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(XmlObjectBase.class.getName()).log(Level.SEVERE,
                    null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(XmlObjectBase.class.getName()).log(Level.SEVERE,
                    null, ex);
        }

        return true;
    }

    private boolean parseXmlObject(Field field, Element thisObjectElement) {
        if (!this.isSuperclass(field.getType(), XmlObjectBase.class)) {
            return false;
        }

        XmlObjectBase xmlObjectBase = null;

        try {
            xmlObjectBase = (XmlObjectBase) field.get(this);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(XmlObjectBase.class.getName()).log(Level.SEVERE,
                    null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(XmlObjectBase.class.getName()).log(Level.SEVERE,
                    null, ex);
        }

        if (xmlObjectBase == null) {
            Logger.getLogger(XmlObjectBase.class.getName()).log(
                    Level.INFO,
                    "XmlObjectBase field is not initialized, name: {0}",
                    field.getName());
            return false;
        }

        Element objectElement = XmlUtil.selectSingleElement(thisObjectElement,
                xmlObjectBase.getRootNodeName());

        if (objectElement == null) {
            Logger.getLogger(XmlObjectBase.class.getName()).log(
                    Level.INFO,
                    "Elements is not found, field name: {0}, element name: {1}",
                    new Object[]{
                        field.getName(),
                        xmlObjectBase.getRootNodeName()
                    });

            return false;
        }

        xmlObjectBase.parseFromElement(objectElement);

        return true;
    }

    /**
     * Used for serializing this object into thisObjectElement.
     * @param thisObjectElement
     */
    public void saveToThisElement(Element thisObjectElement) {
        List<Field> fields = this.getAllFields();

        for (Field field : fields) {
            try {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                boolean restoreAccessible = false;

                if (!field.isAccessible()) {
                    field.setAccessible(true);
                    restoreAccessible = true;
                }

                try {
                    if (!this.saveField(field, thisObjectElement)) {
                        if (!this.saveLocalizedField(field, thisObjectElement)) {
                            if (!this.saveXmlObject(field, thisObjectElement)) {
                                // TODO: add other filter
                            }
                        }
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(XmlObjectBase.class.getName()).log(
                            Level.SEVERE,
                            "Field name: " + field.getName(),
                            ex);
                    throw ex;
                }

                if (restoreAccessible) {
                    field.setAccessible(false);
                }
            } catch (Throwable ex) {
                Logger.getLogger(XmlObjectBase.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }

        this.saveToElementInternal(thisObjectElement);
    }

    /**
     * Will append new element with name getRootNodename().
     * @param parentElement
     */
    @Override
    public void saveToElement(Element parentElement) {
        Element thisObjectElement = parentElement.getOwnerDocument().createElement(
                this.getRootNodeName());

        parentElement.appendChild(thisObjectElement);

        this.saveToThisElement(thisObjectElement);
    }

    private boolean saveField(Field field, Element parentElement) throws IllegalArgumentException, IllegalAccessException {
        XmlFieldAnnotation xmlProp =
                field.getAnnotation(XmlFieldAnnotation.class);

        if (xmlProp == null) {
            return false;
        }

        Object value = field.get(this);

        switch (xmlProp.valueType()) {
            case Boolean:
                if (xmlProp.nodeType() == XmlNodeType.XmlAttribute) {
                    XmlUtil.appendBooleanAttribute(parentElement,
                            xmlProp.propertyName(),
                            (Boolean) (value));
                } else {
                    XmlUtil.appendBooleanElement(parentElement,
                            xmlProp.propertyName(),
                            (Boolean) value);
                }
                break;
            case DateTime:

                if (xmlProp.nodeType() == XmlNodeType.XmlAttribute) {
                    XmlUtil.appendDateAttribute(parentElement,
                            xmlProp.propertyName(),
                            (Date) value);
                } else {
                    XmlUtil.appendDateElement(parentElement,
                            xmlProp.propertyName(),
                            (Date) value);
                }

                break;
            case Int:
                if (xmlProp.nodeType() == XmlNodeType.XmlAttribute) {
                    XmlUtil.appendIntAttribute(parentElement,
                            xmlProp.propertyName(),
                            (Integer) value);
                } else {
                    XmlUtil.appendIntElement(parentElement,
                            xmlProp.propertyName(),
                            (Integer) value);
                }
                break;
            case String:
                if (xmlProp.nodeType() == XmlNodeType.XmlAttribute) {
                    XmlUtil.appendStringAttribute(parentElement,
                            xmlProp.propertyName(),
                            String.valueOf(
                            value.toString()));
                } else {
                    XmlUtil.appendStringElement(parentElement,
                            xmlProp.propertyName(),
                            String.valueOf(value.toString()));
                }
                break;
            default:
                throw new IllegalArgumentException(
                        "Unknown value type: " + xmlProp.valueType());
        }

        return true;
    }

    public boolean saveLocalizedField(Field field, Element parentElement) throws IllegalArgumentException, IllegalAccessException {
        XmlLocalizedFieldAnnotation xmlProp =
                field.getAnnotation(
                XmlLocalizedFieldAnnotation.class);

        if (xmlProp == null) {
            return false;
        }

        LocalizedValue value = (LocalizedValue) field.get(this);

        HashMap<Integer, String> values = value.getValuesMap();

        for (Integer key : values.keySet()) {
            Element elem = parentElement.getOwnerDocument().createElement(
                    xmlProp.nodeName());

            XmlUtil.appendStringAttribute(elem, xmlProp.valueAttrName(), values.get(
                    key));
            XmlUtil.appendIntAttribute(elem, xmlProp.langIdAttrName(), key);
            parentElement.appendChild(elem);
        }

        return true;
    }

    private boolean isSuperclass(Class class1, Class class2) {
        if (class1 == class2) {
            return true;
        }

        Class supperClass = class1;
        while ((supperClass = supperClass.getSuperclass()) != null) {
            if (supperClass == class2) {
                return true;
            }
        }

        return false;
    }

    private boolean saveXmlObject(Field field, Element thisObjectElement) {
        if (!this.isSuperclass(field.getType(), XmlObjectBase.class)) {
            return false;
        }

        XmlObjectBase xmlObjectBase = null;

        try {
            xmlObjectBase = (XmlObjectBase) field.get(this);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(XmlObjectBase.class.getName()).log(Level.SEVERE,
                    null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(XmlObjectBase.class.getName()).log(Level.SEVERE,
                    null, ex);
        }

        if (xmlObjectBase == null) {
            Logger.getLogger(XmlObjectBase.class.getName()).log(
                    Level.INFO,
                    "XmlObjectBase field is not initialized, name: {0}",
                    field.getName());
            return false;
        }

        xmlObjectBase.saveToElement(thisObjectElement);

        return true;
    }
}
