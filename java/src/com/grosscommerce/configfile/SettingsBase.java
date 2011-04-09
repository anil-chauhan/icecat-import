/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */

package com.grosscommerce.configfile;

import com.grosscommerce.ICEcatAPI.model.XmlObjectBase;
import com.grosscommerce.ICEcatAPI.model.annotations.ValueTypeEnum;
import com.grosscommerce.ICEcatAPI.model.annotations.XmlFieldAnnotation;
import com.grosscommerce.ICEcatAPI.model.annotations.XmlNodeType;
import org.w3c.dom.Element;

/**
 * Base class for settings nodes.
 * @author Anykey Skovorodkin
 */
public abstract class SettingsBase extends XmlObjectBase
{
    private static final String USER_NAME_PROP = "user_name";
    private static final String PASSWORD_PROP = "password";

    @XmlFieldAnnotation (nodeType = XmlNodeType.XmlNode, propertyName = USER_NAME_PROP, valueType = ValueTypeEnum.String)
    protected String userName = "";
    @XmlFieldAnnotation (nodeType = XmlNodeType.XmlNode, propertyName = PASSWORD_PROP, valueType = ValueTypeEnum.String)
    protected String password = "";

    public SettingsBase()
    {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    
    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUserName()
    {
        return this.userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="XmlObjectBase">
    
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
