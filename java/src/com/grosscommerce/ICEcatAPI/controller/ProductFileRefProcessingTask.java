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

import com.grosscommerce.ICEcatAPI.productimporterbase.ParsedProductInfo;
import com.grosscommerce.ICEcatAPI.utilities.ResourcesDownloader;
import com.grosscommerce.ICEcatAPI.common.QueueProcessorTask;
import com.grosscommerce.ICEcatAPI.model.Language;
import com.grosscommerce.ICEcatAPI.model.ProductFileRef;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * Used for downloading real information about product.
 * @author Anykey Skovorodkin
 */
public class ProductFileRefProcessingTask extends QueueProcessorTask<ProductFileRef>
{
    private String userName;
    private String password;
    private BlockingQueue<ParsedProductInfo> resultQueue;
    private ImportType importType;
    private Language language;

    public ProductFileRefProcessingTask(CountDownLatch taskMonitor,
                                        BlockingQueue<ProductFileRef> tasksQueue,
                                        BlockingQueue<ParsedProductInfo> resultQueue,
                                        String userName,
                                        String password,
                                        Language language)
    {
        super(taskMonitor, tasksQueue);

        this.userName = userName;
        this.password = password;
        this.resultQueue = resultQueue;
        this.language = language;
    }

    @Override
    protected void processNextObject(ProductFileRef object) throws Throwable
    {
        ParsedProductInfo importTaskItem = new ParsedProductInfo(object, this.language);
        importTaskItem.setImportType(this.importType);

        try
        {
            importTaskItem.setProduct(ResourcesDownloader.downloadProduct(
                    object.getPath(),
                    this.userName,
                    this.password));
            importTaskItem.setParsingStatus(ParsedProductInfo.ParsingStatus.Success);
        }
        catch (Throwable ex)
        {
            importTaskItem.setParsingStatus(ParsedProductInfo.ParsingStatus.Failure);
            importTaskItem.setExeption(ex);
            throw ex;
        }
        finally
        {
            this.resultQueue.put(importTaskItem);
        }
    }
}
