/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */
package com.grosscommerce.utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 *
 * @author Anykey Skovorodkin
 */
public class LogUtility {

    private static final String logsDirName = "Logs";

    public static void initLogging() {
        try {
            // create Logs dir
            File file = new File(logsDirName);
            if (!file.exists()) {
                file.mkdirs();
            }

            Object obj = LogUtility.class.getResource("/com/grosscommerce/properties/logging.properties");

            InputStream stream = ((URL) obj).openStream();

            LogManager.getLogManager().readConfiguration(stream);
            stream.close();
        } catch (IOException ex) {
            Logger.getLogger(LogUtility.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(LogUtility.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(LogUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
