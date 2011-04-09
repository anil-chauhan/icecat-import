/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */
package com.grosscommerce.ICEcat.base;

import com.grosscommerce.ICEcat.common.QueueProcessorTask;
import com.grosscommerce.ICEcat.controller.ImportContext;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * Base class for all product importers.
 * @author Anykey Skovorodkin
 */
public abstract class ProductImporterBase extends QueueProcessorTask<ParsedProductInfo> {

    protected ImportContext importContext;

    public ProductImporterBase(CountDownLatch taskMonitor, BlockingQueue<ParsedProductInfo> queue, ImportContext importContext) {
        super(taskMonitor, queue);
        this.importContext = importContext;
    }

    public abstract void init();
}
