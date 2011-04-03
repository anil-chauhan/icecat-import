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

import gc.ICEcatAPI.common.Constants;
import gc.ICEcatAPI.model.annotations.ValueTypeEnum;
import gc.ICEcatAPI.model.annotations.XmlFieldAnnotation;
import gc.ICEcatAPI.model.annotations.XmlNodeType;

/**
 * ICECat config element.
 * @author Anykey Skovorodkin
 */
public class ICECatSettings extends SettingsBase
{
    public static final String ROOT_NODE_NAME = "ice_settings";
    private static final String PARSING_THREADS_COUNT_PROP = "parsing_threads";

    @XmlFieldAnnotation (nodeType = XmlNodeType.XmlNode, propertyName = PARSING_THREADS_COUNT_PROP, valueType = ValueTypeEnum.Int)
    private int parsingThreadsCount = Constants.THREADS_COUNT;

    public ICECatSettings()
    {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">

    public int getParsingThreadsCount()
    {
        return parsingThreadsCount;
    }

    public void setParsingThreadsCount(int parsingThreadsCount)
    {
        this.parsingThreadsCount = parsingThreadsCount;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SettingsBase">
    
    @Override
    public String getRootNodeName()
    {
        return ROOT_NODE_NAME;
    }

    // </editor-fold>
}
