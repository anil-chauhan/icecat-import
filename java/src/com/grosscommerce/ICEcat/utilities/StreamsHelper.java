/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */

package com.grosscommerce.ICEcat.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Used for working with streams.
 * @author Anykey Skovorodkin
 */
public abstract class StreamsHelper
{
    public static void copy(InputStream is, OutputStream os) throws IOException
    {
        copy(is, os, 1048576); // 1 mb buffer
    }
    
    public static void copy(InputStream is, OutputStream os, int bufferSize) throws IOException
    {
        int len = 0;
        byte[] buffer = new byte[bufferSize];
        while ((len = is.read(buffer)) > 0)
        {
            os.write(buffer, 0, len);
        }
    }
}
