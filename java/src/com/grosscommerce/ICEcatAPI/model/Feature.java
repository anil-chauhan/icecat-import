/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */

package gc.ICEcatAPI.model;

import gc.ICEcatAPI.model.annotations.XmlLocalizedFieldAnnotation;
import org.w3c.dom.Element;

/**
 * Feature description. (Partially parsed)
 * @author Anykey Skovorodkin
 */
public class Feature extends XmlObjectBase
{
    public static final String ROOT_NODE_NAME = "Feature";

    @XmlLocalizedFieldAnnotation(nodeName=NAME_PROP)
    private LocalizedValue name = new LocalizedValue();

    public Feature()
    {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getName(int langId)
    {
        return this.name.getValue(langId);
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
        return true;
    }

    @Override
    protected void saveToElementInternal(Element parentElement)
    {
    }

    // </editor-fold>

    @Override
    public String toString()
    {
        return "Feature{" + "name=" + name + '}';
    }
}
