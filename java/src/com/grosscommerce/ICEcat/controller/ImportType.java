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

/**
 * Import type enum.
 * @author Anykey Skovorodkin
 */
public enum ImportType {

    /**
     * Used for importing full catalog.
     */
    Full("files.index.xml"),
    /**
     * Used for updates existing catalog.
     */
    Update("daily.index.xml");
    private String indexFileName;

    private ImportType(String indexFileName) {
        this.indexFileName = indexFileName;
    }

    public String getIndexFileName() {
        return indexFileName;
    }
}
