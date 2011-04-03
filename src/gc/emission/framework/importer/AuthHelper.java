/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */

package gc.emission.framework.importer;

import com.emission.framework.logon.LogOn;

/**
 * Used for getting access token which can be used for working with Emission Framework
 * @author Anykey Skovorodkin
 */
public class AuthHelper
{
    public static String getAccessToken(String token, String userName, String password)
    {
        LogOn logOn = new LogOn();
        return logOn.getBasicHttpBindingILogOn().process(token, userName, password);
    }
}
