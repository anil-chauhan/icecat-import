/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.grosscommerce.core;

import com.emission.framework.product.IProduct;
import com.emission.framework.product.Product_Service;
import com.emission.framework.productcategory.IProductCategory;
import com.emission.framework.productcategory.ProductCategory;
import com.emission.framework.productcategory.ProductCategory_Service;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helps work with emission framework.
 * @author Ivan Dubinets
 */
public final class EmissionFrameworkHelper
{
    public static final int ATTEMPTS_NUMBER = 3;

    // <editor-fold defaultstate="collapsed" desc="Work with categories">
    public static Boolean truncateCategories(
            final String accessToken,
            final IProductCategory categoryService
            ) throws Throwable
    {
        return doOperation(new Operation<Boolean>() {

            @Override
            public Boolean execute() throws Throwable
            {
                categoryService.truncate(accessToken);
                return true;
            }
        },
        ATTEMPTS_NUMBER);
    }

    public static IProductCategory initCategoryService() throws Throwable
    {
        return doOperation(new Operation<IProductCategory>() {

            @Override
            public IProductCategory execute() throws Throwable
            {
                ProductCategory_Service productCategory_Service = new ProductCategory_Service();
                return productCategory_Service.getBasicHttpBindingIProductCategory();
            }
        }, ATTEMPTS_NUMBER);
    }

    public static ProductCategory createProductCategory(
            final String accessToken,
            final String categoryName,
            final IProductCategory categoryService) throws Throwable
    {
        return doOperation(new Operation<ProductCategory>() {

            @Override
            public ProductCategory execute() throws Throwable
            {
                return categoryService.create(accessToken, categoryName, 0);
            }
        }, ATTEMPTS_NUMBER);
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Work with products">
    
    public static IProduct initProductService() throws Throwable
    {
        return doOperation(new Operation<IProduct>()
        {
            @Override
            public IProduct execute() throws Throwable
            {
                Product_Service product = new Product_Service();
                return product.getBasicHttpBindingIProduct();
            }
        }, ATTEMPTS_NUMBER);
    }

    public static Boolean truncateAllProducts(final String accessToken,
            final IProduct productService) throws Throwable
    {
        return doOperation(new Operation<Boolean>() {

            @Override
            public Boolean execute() throws Throwable
            {
                productService.truncate(accessToken);
                return true;
            }
        }, ATTEMPTS_NUMBER);
    }

    // </editor-fold>

    private static <T> T doOperation(
            Operation operation, 
            int attemptsNumber) throws Throwable
    {
        while(attemptsNumber > 0)
        {
            attemptsNumber--;

            try
            {
                return (T) operation.execute();
            }
            catch (Throwable ex)
            {
                Logger.getLogger(EmissionFrameworkHelper.class.getName()).log(
                        Level.SEVERE,
                        "left attempts: " + attemptsNumber,
                        ex);

                if(attemptsNumber == 0)
                {
                    throw ex;
                }
            }
        }

        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="Base Operations">
    
    private static abstract class Operation<T>
    {
        public abstract T execute() throws Throwable;
    }

    // </editor-fold>
}
