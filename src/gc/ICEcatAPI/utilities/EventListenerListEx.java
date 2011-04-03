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

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;

/**
 * Used for storing event listenrs. We can't use swing's EventListenerList,
 * because some platforms (like android) can't spport it.
 * @author Anykey Skovorodkin
 */
public class EventListenerListEx
{
    private HashMap<Class<?>, ArrayList<?>> listeners =
            new HashMap<Class<?>, ArrayList<?>>();

    public synchronized <T extends EventListener> void addListener(Class<T> cl, T listener)
    {
        ArrayList<T> thisClassListeners = null;

        if(this.listeners.containsKey(cl))
        {
            thisClassListeners = (ArrayList<T>)this.listeners.get(cl);
        }
        else
        {
            thisClassListeners = new ArrayList<T>();
            this.listeners.put(cl, thisClassListeners);
        }

        if(!thisClassListeners.contains(listener))
        {
            thisClassListeners.add(listener);
        }
    }

    public synchronized <T extends EventListener> void removeListener(Class<T> cl, T listener)
    {
        if(this.listeners.containsKey(cl))
        {
            ArrayList<T> lst = (ArrayList<T>)this.listeners.get(cl);
            lst.remove(listener);
        }
    }

    public synchronized <T extends EventListener> List<T> getListeners(Class<T> cl)
    {
        List<T> result = new ArrayList<T>();

        if(this.listeners.containsKey(cl))
        {
            ArrayList<T> thisClassListeners =
                    (ArrayList<T>)this.listeners.get(cl);
            
            result.addAll(thisClassListeners);
        }

        return result;
    }
}
