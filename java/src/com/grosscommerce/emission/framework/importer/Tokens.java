/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */

package com.grosscommerce.emission.framework.importer;

import com.grosscommerce.ICEcatAPI.model.XmlObjectsListBase;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.w3c.dom.Element;

/**
 * Used for storing tokens.
 * @author Anykey Skovorodkin
 */
public class Tokens extends XmlObjectsListBase<Token>
{
    public static final String ROOT_NODE_NAME = "TokensList";

    private ArrayList<Token> tokens = new ArrayList<Token>();

    public Tokens()
    {
    }

    public void add(Token token)
    {
        this.tokens.add(token);
    }

    public boolean remove(Token token)
    {
        return this.tokens.remove(token);
    }

    // <editor-fold defaultstate="collapsed" desc="XmlObjectsListBase">
    @Override
    public Token[] getAll()
    {
        return this.tokens.toArray(new Token[0]);
    }

    @Override
    protected String getChildNodesName()
    {
        return Token.ROOT_NODE_NAME;
    }

    @Override
    protected Token loadFromElement(Element objElement) throws Throwable
    {
        Token token = new Token();
        if (token.parseFromElement(objElement))
        {
            return token;
        }

        Logger.getLogger(Tokens.class.getName()).info("Wrong token...");
        return null;
    }

    @Override
    public String getRootNodeName()
    {
        return ROOT_NODE_NAME;
    }

    @Override
    protected void saveObject(Token object)
    {
        this.tokens.add(object);
    }

    // </editor-fold>

}
