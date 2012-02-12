/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */
package com.grosscommerce;

import com.grosscommerce.ICEcat.base.ParsedProductInfo;
import com.grosscommerce.ICEcat.base.ProductImporterBase;
import com.grosscommerce.ICEcat.controller.ImportContext;
import com.grosscommerce.ICEcat.controller.ImportController;
import com.grosscommerce.ICEcat.controller.ProductFileRefFilterBase;
import com.grosscommerce.ICEcat.model.ProductFileRef;
import com.grosscommerce.utilities.LogUtility;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Anykey Skovorodkin
 */
class Program
{
    /**
     * Simple realization of ProductFileRefFilterBase.
     */
    private static class AllProductsFilter extends ProductFileRefFilterBase
    {
        public AllProductsFilter(ImportContext importContext, int parsingThreadsCount)
                throws IllegalArgumentException
        {
            super(importContext, parsingThreadsCount);
        }

        @Override
        protected boolean acceptInternal(ProductFileRef productFileRef)
        {
            return true; // accept all
        }

        @Override
        protected ProductImporterBase getProductImporter(
                CountDownLatch taskMonitor,
                                                         ArrayBlockingQueue<ParsedProductInfo> resultQueue,
                                                         ImportContext importContext)
        {
            return new PrintProjectInfoImporter(taskMonitor, resultQueue, importContext);
        }
    }

    /**
     * This importer will print information about parsed objects.
     */
    private static class PrintProjectInfoImporter extends ProductImporterBase
    {
        public PrintProjectInfoImporter(CountDownLatch taskMonitor,
                            BlockingQueue<ParsedProductInfo> queue,
                            ImportContext importContext)
        {
            super(taskMonitor, queue, importContext);
        }

        @Override
        public void init() throws Throwable
        {
            // TODO: add extra initialisation here
        }

        @Override
        protected void processNextObject(ParsedProductInfo object) throws
                Throwable
        {
            System.out.println("category: {" + this.importContext.getCategoryName(
                    object.getProduct().getCatId()) + "} " + object.getProduct().toString() + " long descr: {" + object.getProduct().getSummaryDescription().getLongDescription(
                    object.getLanguage().getId()) + "}");
        }
    }

    public static void main(String[] params)
    {
        LogUtility.initLogging();
        
        String iceUserName = "";
        String iceUserPassword = "";

        if(params.length < 2)
        {
            Scanner in = new Scanner(System.in);
            System.out.print("ICECat user name: ");
            

            // Reads a single line from the console 
            // and stores into name variable
            iceUserName = in.nextLine();
            System.out.print("ICECat user password: ");
            iceUserPassword = in.nextLine();
        }
        else
        {
            iceUserName = params[0];
            iceUserPassword = params[1];
        }
        
        ImportContext importContext = new ImportContext();
        try
        {
            importContext.init(iceUserName, iceUserPassword); // we planned to implement all catalog


            ImportController importController = new ImportController(
                    importContext);

            importController.registerFilter(new AllProductsFilter(importContext, 5));

            importController.doImport();

        }
        catch (Throwable ex)
        {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
