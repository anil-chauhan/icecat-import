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

import java.util.EventListener;

/**
 *
 * @author Anykey Skovorodkin
 */
public interface XmlObjectsListListener<T extends XmlObjectBase> extends
        EventListener
{
    public void onProductFileRefParsed(T object);
}
