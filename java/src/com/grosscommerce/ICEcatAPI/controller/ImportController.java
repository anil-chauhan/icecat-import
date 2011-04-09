/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */

package com.grosscommerce.ICEcatAPI.controller;

import com.grosscommerce.ICEcatAPI.common.Constants;
import com.grosscommerce.ICEcatAPI.model.IndexFileParser;
import com.grosscommerce.ICEcatAPI.model.ProductFileRef;
import com.grosscommerce.ICEcatAPI.model.SAXIndexFileParser;
import com.grosscommerce.ICEcatAPI.model.XmlObjectsListListener;
import com.grosscommerce.ICEcatAPI.utilities.Downloader;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used for controlling parsing process.
 * @author Anykey Skovorodkin
 */
public class ImportController
{
    private ImportContext importContext;
    
    private ArrayList<ProductFileRefFilterBase> filters = new ArrayList<ProductFileRefFilterBase>();

    public ImportController(ImportContext context)
    {
        this.importContext = context;
    }

    public void registerFilter(ProductFileRefFilterBase filter)
    {
        this.filters.add(filter);
    }

    public void doImport() throws IllegalStateException
    {
        this.initFilters();

        if(this.importContext.getImportType() != ImportType.Full)
        {
            throw new IllegalStateException(this.importContext.getImportType() + " is not supported yet!");
        }

        try
        {
            this.startProcessingIndexFile();
        }
        catch (Throwable ex)
        {
            Logger.getLogger(ImportController.class.getName()).log(Level.SEVERE,
                                                                   null, ex);
        }
    }

    private void startProcessingIndexFile() throws Throwable
    {
        String indexFileURL = this.getIndexFileURL();

        byte[] data = Downloader.download(indexFileURL,
                this.importContext.getUserName(),
                this.importContext.getPassword());

        IndexFileParser parser = new SAXIndexFileParser();
        parser.addXmlObjectsListParserListener(new IndexFilesParserListener());

        parser.parse(new ByteArrayInputStream(data));

        Logger.getLogger(ImportController.class.getName()).info("Index file successfully parsed!");

        this.waitForFilters();
    }

    private void waitForFilters() throws InterruptedException
    {
        Logger.getLogger(ImportController.class.getName()).info("Wait for finishing");

        for(ProductFileRefFilterBase filter : this.filters)
        {
            filter.waitForExit();
        }
    }

    private String getIndexFileURL()
    {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(Constants.FREE_XML_BASE_URL);
        strBuilder.append(this.importContext.getImportLanguage().getShortCode());
        strBuilder.append("/");
        
        strBuilder.append(this.importContext.getImportType().getIndexFileName());
                
        return strBuilder.toString();
    }

    private void initFilters() throws IllegalStateException
    {
        if(this.filters.isEmpty())
        {
            throw new IllegalStateException("No filters. You must register one for doing import. Use registerFilter method");
        }

        for(ProductFileRefFilterBase filter : this.filters)
        {
            filter.init();
        }
    }

    protected void acceptProductFileRef(ProductFileRef productFileRef) throws InterruptedException
    {
        for(ProductFileRefFilterBase filter : this.filters)
        {
            if(filter.accept(productFileRef))
            {
                break;
            }
        }

        // Logger.getLogger(ImportController.class.getName()).log(Level.INFO, "{0} skipped by filters", productFileRef);
    }

    // <editor-fold defaultstate="collapsed" desc="IndexFilesParserListener">
    private class IndexFilesParserListener implements
            XmlObjectsListListener<ProductFileRef>
    {

        @Override
        public void onProductFileRefParsed(ProductFileRef object)
        {
            try
            {
                acceptProductFileRef(object);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(ImportController.class.getName()).log(
                        Level.SEVERE,
                                                                       null, ex);
            }
        }
    }// </editor-fold>
}
