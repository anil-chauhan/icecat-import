/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */

package com.grosscommerce.ICEcatAPI.common;

/**
 *
 * @author Anykey Skovorodkin
 */
public interface Constants
{
    public static final String ICEGAT_DATA_URL           = "http://data.icecat.biz/";
    public static final String CATEGORIES_LIST_FILE_NAME = "http://data.icecat.biz/export/freexml/refs/CategoriesList.xml.gz";
    public static final String LANGUAGE_LIST_FILE_NAME   = "http://data.icecat.biz/export/freexml/refs/LanguageList.xml.gz";
    public static final String FREE_XML_BASE_URL         = "http://data.icecat.biz/export/freexml.int/";

    public static final String DEFAULT_CONFIG_FILE_NAME = "config.xml";
    public static final String DEFAULT_LANGUAGE_SHORT_CODE = "EN";

    /**
     * Default parsing threads count.
     */
    public static final int THREADS_COUNT = 4;

    public static final int BLOCKING_QUEUE_CAPACITY = 100;
}
