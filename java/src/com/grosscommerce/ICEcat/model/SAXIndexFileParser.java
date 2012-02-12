/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */
package com.grosscommerce.ICEcat.model;

import java.io.InputStream;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Used for parsing index file by using SAX.
 * @author Anykey Skovorodkin
 */
public class SAXIndexFileParser extends IndexFileParser
{
    @Override
    public void parse(InputStream is) throws Throwable
    {
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        xmlReader.setContentHandler(new SAXIndexFileHandler());
        xmlReader.parse(new InputSource(is));
        is.close();
    }

    private class SAXIndexFileHandler extends DefaultHandler
    {
        private ProductFileRef productFileRef;

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException
        {
            if (localName.equalsIgnoreCase(ProductFileRef.ROOT_NODE_NAME) && this.productFileRef != null)
            {
                processParsedElement(this.productFileRef);
                this.productFileRef = null;
            }
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException
        {
            if (localName.equalsIgnoreCase(ProductFileRef.ROOT_NODE_NAME))
            {
                // create new element
                this.productFileRef = new ProductFileRef();
                this.productFileRef.loadFromAttributes(attributes);
            }
        }
    }
}
