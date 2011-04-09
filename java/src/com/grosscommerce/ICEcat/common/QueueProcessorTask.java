/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */

package com.grosscommerce.ICEcat.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used as base class for task, which used for processing BlockingQueue
 * @author Anykey Skovorodkin
 */
public abstract class QueueProcessorTask<T> extends AbstractTask
{
    private BlockingQueue<T> queue;
    private static final int WAIT_NEXT_OBJECT_TIMEOUT_SEC = 10;
    private AtomicBoolean cancelled = new AtomicBoolean(false);

    public QueueProcessorTask(CountDownLatch taskMonitor, BlockingQueue<T> queue)
    {
        super(taskMonitor);
        this.queue = queue;
    }

    /**
     * Used for cancelling process, which wait new objects from queue.
     */
    public void cancel()
    {
        this.cancelled.lazySet(true);
        Logger.getLogger(QueueProcessorTask.class.getName()).log(Level.INFO,
                "Cancelling task: {0}", this.hashCode());
    }
    
    protected abstract void processNextObject(T object) throws Throwable;

    // <editor-fold defaultstate="collapsed" desc="AbstractTask">

    @Override
    protected void runTask()
    {
        T object = null;
        try
        {
            while ((object = queue.poll(WAIT_NEXT_OBJECT_TIMEOUT_SEC, TimeUnit.SECONDS)) != null ||
                    !this.cancelled.get())
            {
                if (object != null)
                {
                    try
                    {
                        this.processNextObject(object);
                    }
                    catch (Throwable ex)
                    {
                        Logger.getLogger(QueueProcessorTask.class.getName()).log(Level.SEVERE,
                                                                                 null,
                                                                                 ex);
                    }
                }
            }
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(QueueProcessorTask.class.getName()).log(Level.SEVERE,
                                                                     "Task: " + this.hashCode(),
                                                                     ex);
        }
    }

    // </editor-fold>

}
