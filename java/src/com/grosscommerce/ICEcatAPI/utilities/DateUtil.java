/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */

package com.grosscommerce.ICEcatAPI.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Used for working with IceCat's date.
 * @author Anykey Skovorodkin
 */
public abstract class DateUtil
{
    /**
     * Used for parsing IceCat date time.
     */
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * Used for parsing IceCat's data to java.util.Data.
     * @param strDate
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String strDate) throws ParseException
    {
        return format.parse(strDate);
    }

    /**
     * Converts data to IceCat data string.
     * @param date
     * @return
     */
    public static String dateToString(Date date)
    {
        return format.format(date);
    }
}
