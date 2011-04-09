/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */

package gc.ICEcatAPI.common;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used as base class for task, wich can report about finishing.
 * @author Anykey Skovorodkin
 */
public abstract class AbstractTask implements Runnable
{
    private CountDownLatch taskMonitor;
    private AtomicBoolean stopped = new AtomicBoolean(true);

    public AbstractTask(CountDownLatch taskMonitor)
    {
        this.taskMonitor = taskMonitor;
    }

    public boolean isStopped()
    {
        return this.stopped.get();
    }

    // <editor-fold defaultstate="collapsed" desc="Runnable implementation">
    
    @Override
    public void run()
    {
        Logger.getLogger(AbstractTask.class.getName()).log(Level.INFO, "Task started, hash: {0}", this.hashCode());

        this.stopped.lazySet(false);

        try
        {
            this.runTask();
        }
        finally
        {
            this.taskMonitor.countDown();

            Logger.getLogger(AbstractTask.class.getName()).log(Level.INFO,
                    "Task finished, hash: {0}", this.hashCode());
            
            this.stopped.lazySet(true);
        }
    }

    // </editor-fold>

    /**
     * Runs current task operation.
     */
    protected abstract void runTask();
}
