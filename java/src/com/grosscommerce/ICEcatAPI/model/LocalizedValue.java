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

import java.util.HashMap;
import java.util.Set;

/**
 * Used for storing localized values
 * @author Anykey Skovorodkin
 */
public class LocalizedValue
{
    /**
     * Used for storing localized values. Key - language's id, Value - string value.
     */
    private HashMap<Integer, String> valuesMap = new HashMap<Integer, String>();

    public void putValue(int langId, String value)
    {
        this.valuesMap.put(langId, value);
    }

    public String getValue(int langId)
    {
        return this.valuesMap.get(langId);
    }

    HashMap<Integer, String> getValuesMap()
    {
        return valuesMap;
    }

    @Override
    public String toString()
    {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("LocalizedValue{");

        Set<Integer> keys = this.valuesMap.keySet();
        for(Integer key : keys)
        {
            strBuilder.append(key).append("=").append(this.valuesMap.get(key));
        }

        strBuilder.append("}");

        return strBuilder.toString();
    }
}
