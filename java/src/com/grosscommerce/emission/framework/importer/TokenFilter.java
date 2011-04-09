/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */

package com.grosscommerce.emission.framework.importer;

import com.grosscommerce.ICEcatAPI.controller.ImportContext;
import com.grosscommerce.ICEcatAPI.controller.ProductFileRefFilterBase;
import com.grosscommerce.ICEcatAPI.model.Category;
import com.grosscommerce.ICEcatAPI.model.ProductFileRef;
import com.grosscommerce.ICEcatAPI.productimporterbase.ParsedProductInfo;
import com.grosscommerce.ICEcatAPI.productimporterbase.ProductImporterBase;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anykey Skovorodkin
 */
public class TokenFilter extends ProductFileRefFilterBase
{
    private Token token;
    private String userName;
    private String password;
    
    public TokenFilter(ImportContext importContext,Token token,
            String userName,
            String password,
            int parsingThreadsCount)
    {
        super(importContext, parsingThreadsCount);
        this.token = token;
        this.userName = userName;
        this.password = password;
    }

    @Override
    protected boolean acceptInternal(ProductFileRef productFileRef)
    {
        Category category = this.importContext.getCategories().getById(
                productFileRef.getCatId());

        if (category == null)
        {
            Logger.getLogger(TokenFilter.class.getName()).log(
                    Level.SEVERE, "Unknown category id: {0}",
                                   productFileRef.getCatId());
            return false;
        }

        category = this.importContext.getCategories().getParentCategory(
                category, 0);

        return category != null && (category.getId() == this.token.getCatId());
    }

    @Override
    protected ProductImporterBase getProductImporter(CountDownLatch taskMonitor, 
            ArrayBlockingQueue<ParsedProductInfo> resultQueue,
            ImportContext importContext)
    {
        return new ProductImporter(taskMonitor, resultQueue, this.token,
                this.userName, this.password, importContext);
    }
}
