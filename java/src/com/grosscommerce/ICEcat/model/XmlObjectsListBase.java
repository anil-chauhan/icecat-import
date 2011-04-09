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

import com.grosscommerce.ICEcat.utilities.EventListenerListEx;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Base class for all xml file parsers
 * @author Anykey Skovorodkin
 */
public abstract class XmlObjectsListBase<T extends XmlObjectBase> extends XmlObjectBase {

    private EventListenerListEx eventsList = new EventListenerListEx();
    /**
     * Stores count of parsed files.
     */
    private int countParsedFiles = 0;

    public int getCountParsedFiles() {
        return countParsedFiles;
    }

    protected abstract String getChildNodesName();

    protected abstract T loadFromElement(Element objElement) throws Throwable;

    public abstract T[] getAll();

    protected void processParsedElement(T obj) {
        this.saveObject(obj);
        this.fireObjectParsed(obj);
    }

    protected abstract void saveObject(T object);

    // <editor-fold defaultstate="collapsed" desc="Work with listeners">
    public void addXmlObjectsListParserListener(XmlObjectsListListener<T> l) {
        this.eventsList.addListener(XmlObjectsListListener.class, l);
    }

    public void removeXmlObjectsListParserListener(XmlObjectsListListener<T> l) {
        this.eventsList.removeListener(XmlObjectsListListener.class, l);
    }

    public void fireObjectParsed(T object) {
        List<XmlObjectsListListener> listeners =
                this.eventsList.getListeners(XmlObjectsListListener.class);

        for (XmlObjectsListListener l : listeners) {
            l.onProductFileRefParsed(object);
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="XmlObjectBase">
    @Override
    protected boolean parseFromElementInternal(Element thisObjectElement) {
        this.countParsedFiles = 0;

        NodeList list = thisObjectElement.getElementsByTagName(this.getChildNodesName());

        int size = list.getLength();

        for (int i = 0; i < size; i++) {
            Node node = list.item(i);

            if (node instanceof Element) {
                try {
                    T obj = this.loadFromElement((Element) node);

                    if (obj != null) {
                        this.countParsedFiles++;
                        this.processParsedElement(obj);
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(IndexFileParser.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
            }
        }

        return true;
    }

    @Override
    protected void saveToElementInternal(Element parentElement) {
        T[] allElements = this.getAll();

        for (T elem : allElements) {
            elem.saveToElement(parentElement);
        }
    }
    // </editor-fold>
}
