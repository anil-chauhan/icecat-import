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

import java.util.HashMap;
import org.w3c.dom.Element;

/**
 * Used for storing languages
 * @author Anykey Skovorodkin
 */
public class Languages extends XmlObjectsListBase<Language>
{
    public static final String ROOT_NODE_NAME = "LanguageList";

    private HashMap<Integer,Language> languagesMap = new HashMap<Integer, Language>();

    public Languages()
    {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">

    /**
     * Gets language by id.
     * @param langId
     * @return
     */
    public Language getById(int langId)
    {
        return this.languagesMap.get(langId);
    }

    /**
     * Returns all possible languages.
     * @return
     */
    @Override
    public Language[] getAll()
    {
        return this.languagesMap.values().toArray(new Language[0]);
    }

    public Language getLanguageByShortCode(String shortCode)
    {
        Language[] languages = this.getAll();

        for(Language language : languages)
        {
            if(language.getShortCode().equalsIgnoreCase(shortCode))
            {
                return language;
            }
        }

        return null; // is not found
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="XmlObjectsListBase implementation">
    
    @Override
    protected String getChildNodesName()
    {
        return Language.ROOT_NODE_NAME;
    }

    @Override
    protected Language loadFromElement(Element objElement) throws Throwable
    {
        Language language = new Language();
        
        if(language.parseFromElement(objElement))
        {
            return language;
        }

        return null;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="XmlObjectBase implementation">
    
    @Override
    public String getRootNodeName()
    {
        return ROOT_NODE_NAME;
    }

    @Override
    public void saveObject(Language object)
    {
        this.languagesMap.put(object.getId(), object);
    }

    // </editor-fold>
}
