/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */

package gc.configfile;

import gc.emission.framework.importer.Tokens;

/**
 * Settings for catalog's importer.
 * @author Anykey Skovorodkin
 */
public class ImporterSettings extends SettingsBase
{
    public static final String ROOT_NODE_NAME = "importer_settings";

    private Tokens tokens = new Tokens();
    
    public ImporterSettings()
    {
    }

    public Tokens getTokens()
    {
        return tokens;
    }

    // <editor-fold defaultstate="collapsed" desc="XmlObjectBse">
    @Override
    public String getRootNodeName()
    {
        return ROOT_NODE_NAME;
    }
    // </editor-fold>


}
