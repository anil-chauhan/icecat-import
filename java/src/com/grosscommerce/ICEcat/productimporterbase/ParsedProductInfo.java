/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */

package com.grosscommerce.ICEcat.productimporterbase;

import com.grosscommerce.ICEcat.controller.ImportType;
import com.grosscommerce.ICEcat.model.Language;
import com.grosscommerce.ICEcat.model.Product;
import com.grosscommerce.ICEcat.model.ProductFileRef;

/**
 * Used by ProductImporter for getting information about parsed product.
 * @author Anykey Skovorodkin
 */
public class ParsedProductInfo
{
    public enum ParsingStatus
    {
        Success,
        Failure
    }

    private ProductFileRef productFileRef;
    private Product product;
    private ParsingStatus parsingStatus = ParsingStatus.Failure;
    private Throwable exeption;
    private ImportType importType;
    private Language language;

    public ParsedProductInfo(ProductFileRef productFileRef, Language language)
    {
        this.productFileRef = productFileRef;
        this.language = language;
    }

    public Throwable getExeption()
    {
        return exeption;
    }

    public ParsingStatus getParsingStatus()
    {
        return parsingStatus;
    }

    public Product getProduct()
    {
        return product;
    }

    public ProductFileRef getProductFileRef()
    {
        return productFileRef;
    }

    public void setExeption(Throwable exeption)
    {
        this.exeption = exeption;
    }

    public void setParsingStatus(ParsingStatus parsingStatus)
    {
        this.parsingStatus = parsingStatus;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public ImportType getImportType()
    {
        return importType;
    }

    public void setImportType(ImportType importType)
    {
        this.importType = importType;
    }

    public Language getLanguage()
    {
        return language;
    }
}
