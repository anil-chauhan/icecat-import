/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */
package gc.ICEcatAPI.utilities;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import org.apache.commons.codec.binary.Base64;

/**
 * Used for downloading binary data.
 * @author Anykey Skovorodkin
 */
public abstract class Downloader
{

    public static byte[] download(String urlFrom, String login, String pwd) throws Throwable
    {
        try
        {
            HttpURLConnection uc = prepareConnection(urlFrom, login, pwd);

            uc.connect();

            if (uc.getResponseCode() != HttpURLConnection.HTTP_OK)
            {
                Logger.getLogger(Downloader.class.getName()).log(
                        Level.INFO,
                        "Error, code: {0}, message {1}",
                        new Object[]
                        {
                            uc.getResponseCode(), uc.getResponseMessage()
                        });

                return null;
            }

            BufferedInputStream is = null;

            if ((uc.getContentEncoding() != null && uc.getContentEncoding().toLowerCase().equals(
                 "gzip"))
                || uc.getContentType() != null && uc.getContentType().toLowerCase().contains(
                    "gzip"))
            {
                is = new BufferedInputStream(new GZIPInputStream(
                        uc.getInputStream()));

                Logger.getLogger(Downloader.class.getName()).log(Level.INFO,
                                                                 "Will download gzip data from: {0}",
                                                                 urlFrom);
            }
            else
            {
                is = new BufferedInputStream(
                        uc.getInputStream());

                Logger.getLogger(Downloader.class.getName()).log(Level.INFO,
                                                                 "Will download not compressed data from:{0}",
                                                                 urlFrom);
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            StreamsHelper.copy(is, os);

            byte[] resultBuffer = os.toByteArray();
            os.close();

            Logger.getLogger(Downloader.class.getName()).log(
                    Level.INFO,
                    "Downloaded {0} byte(s)",
                    new Object[]
                    {
                        resultBuffer.length
                    });

            return resultBuffer;
        }
        catch (Exception ex)
        {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE,
                                                             "URL: " + urlFrom,
                                                             ex);

            throw ex;
        }
    }

    private static HttpURLConnection prepareConnection(String urlFrom, String login, String pwd)
            throws ProtocolException, IOException, MalformedURLException
    {
        URL url = new URL(urlFrom);
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        // set up url connection to get retrieve information back
        uc.setRequestMethod("GET");
        uc.setDoInput(true);
        String val = (new StringBuffer(login).append(":").append(pwd)).toString();
        byte[] base = val.getBytes();
        String authorizationString = "Basic " + Base64.encodeBase64String(base);
        uc.setRequestProperty("Authorization", authorizationString);
        uc.setRequestProperty("Accept-Encoding", "gzip, xml");
        return uc;
    }
}
