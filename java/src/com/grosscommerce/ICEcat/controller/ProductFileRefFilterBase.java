/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */
package com.grosscommerce.ICEcat.controller;

import com.grosscommerce.ICEcat.common.Constants;
import com.grosscommerce.ICEcat.base.ParsedProductInfo;
import com.grosscommerce.ICEcat.base.ProductImporterBase;
import com.grosscommerce.ICEcat.common.QueueProcessorTask;
import com.grosscommerce.ICEcat.model.ProductFileRef;
import com.grosscommerce.ICEcat.utilities.ThreadPoolUtil;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

/**
 * Used for filtering parsed ProductFileRef
 * @author Anykey Skovorodkin
 */
public abstract class ProductFileRefFilterBase
{
    private ArrayBlockingQueue<ProductFileRef> productRefs = new ArrayBlockingQueue<ProductFileRef>(
            Constants.BLOCKING_QUEUE_CAPACITY);
    private volatile CountDownLatch taskMonitor;
    protected int parsingThreadsCount = 0;
    private ExecutorService executorService;
    protected ImportContext importContext;
    protected ArrayList<QueueProcessorTask> tasksList =
            new ArrayList<QueueProcessorTask>();
    protected CountDownLatch importerMonitor;
    protected ProductImporterBase productImporter;

    public ProductFileRefFilterBase(ImportContext importContext,
                                    int parsingThreadsCount) throws
            IllegalArgumentException
    {
        this.importContext = importContext;
        this.parsingThreadsCount = parsingThreadsCount;

        if (parsingThreadsCount < 1)
        {
            throw new IllegalArgumentException(
                    "parsingThreadsCount must be >= 1");
        }

        this.executorService = ThreadPoolUtil.createThreadsPool();
    }

    protected abstract ProductImporterBase getProductImporter(
            CountDownLatch taskMonitor,
            ArrayBlockingQueue<ParsedProductInfo> resultQueue,
            ImportContext importContext);

    public void init() throws Throwable
    {
        this.productRefs.clear();
        this.tasksList.clear();

        this.taskMonitor = new CountDownLatch(this.parsingThreadsCount);

        ArrayBlockingQueue<ParsedProductInfo> resultQueue = new ArrayBlockingQueue<ParsedProductInfo>(
                Constants.BLOCKING_QUEUE_CAPACITY);

        this.initImporter(resultQueue);

        for (int i = 0; i < this.parsingThreadsCount; i++)
        {
            this.executeTask(new ProductFileRefProcessingTask(taskMonitor,
                                                              this.productRefs,
                                                              resultQueue,
                                                              this.importContext.
                    getUserName(),
                                                              this.importContext.
                    getPassword(),
                                                              this.importContext.
                    getImportLanguage()));
        }
    }

    private void initImporter(ArrayBlockingQueue<ParsedProductInfo> resultQueue)
            throws Throwable
    {
        this.importerMonitor = new CountDownLatch(1);
        this.productImporter = this.getProductImporter(this.importerMonitor,
                                                       resultQueue,
                                                       this.importContext);
        this.productImporter.init();
        this.executorService.execute(this.productImporter);
    }

    private void executeTask(QueueProcessorTask task)
    {
        this.executorService.execute(task);
        this.tasksList.add(task);
    }

    protected abstract boolean acceptInternal(ProductFileRef productFileRef);

    public boolean accept(ProductFileRef productFileRef) throws
            InterruptedException
    {
        if (this.acceptInternal(productFileRef))
        {
            this.productRefs.put(productFileRef);
            return true;
        }

        return false;
    }

    public void waitForExit() throws InterruptedException
    {
        for (QueueProcessorTask task : this.tasksList)
        {
            task.cancel();
        }

        // wait for finishing all parser tasks
        this.taskMonitor.await();

        // canceling importer
        this.productImporter.cancel();
        // wait for cancelling product importer
        this.importerMonitor.await();

        // shutdown pool
        this.executorService.shutdownNow();

        Logger.getLogger(ProductFileRef.class.getName()).info(
                "Filter stoped...");
    }
}
