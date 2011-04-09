/**
 * This program is free software:  you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 GrossCommerce
 */

package com.grosscommerce.core;

import com.emission.framework.product.IProduct;
import com.emission.framework.product.Product_Service;
import com.emission.framework.productcategory.IProductCategory;
import com.emission.framework.productcategory.ProductCategory;
import com.emission.framework.productcategory.ProductCategory_Service;
import com.grosscommerce.ICEcat.common.Constants;
import com.grosscommerce.ICEcat.common.QueueProcessorTask;
import com.grosscommerce.ICEcat.controller.ImportContext;
import com.grosscommerce.ICEcat.controller.ImportType;
import com.grosscommerce.ICEcat.model.Product;
import com.grosscommerce.ICEcat.base.ParsedProductInfo;
import com.grosscommerce.ICEcat.base.ProductImporterBase;
import com.grosscommerce.ICEcat.utilities.ThreadPoolUtil;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used for importing products by using emission-framework.
 * @author Anykey Skovorodkin
 */
public class ProductImporter extends ProductImporterBase
{
    private Token token;
    private String accessToken;
    private IProduct productService;
    private IProductCategory categoryService;
    private String userName;
    private String password;
    private AtomicInteger productsCounter = new AtomicInteger(0);
    private CopyOnWriteArrayList<ProductImporterTask> tasks =
                                                      new CopyOnWriteArrayList<ProductImporterTask>();
    private ExecutorService executorService;
    private int countImportThreads = 2;
    private ArrayBlockingQueue<ParsedProductInfo> products =
                                                  new ArrayBlockingQueue<ParsedProductInfo>(
            Constants.BLOCKING_QUEUE_CAPACITY);
    private CountDownLatch tasksMonitor;
    private ConcurrentHashMap<Integer, Integer> categoriesAssociations =
                                                new ConcurrentHashMap<Integer, Integer>();

    public ProductImporter(CountDownLatch taskMonitor,
                           BlockingQueue<ParsedProductInfo> queue,
                           Token token,
                           String userName,
                           String password,
                           ImportContext importContext)
    {
        super(taskMonitor, queue, importContext);

        this.token = token;
        this.userName = userName;
        this.password = password;
        this.countImportThreads = token.getThreadsCount();

        if (countImportThreads <= 0)
        {
            throw new IllegalArgumentException(
                    "Threads count <= 0, token: " + token.getValue());
        }

        if(this.importContext.getImportType() != ImportType.Full)
        {
            throw new IllegalArgumentException(this.importContext.getImportType() + " is not supported yet");
        }
    }

    @Override
    protected void processNextObject(ParsedProductInfo item) throws Throwable
    {
        Product product = item.getProduct();

        if (product == null)
        {
            // may be network errors occurred or other ones
            // in any case, this product is not valid, so we skip it
            return;
        }

        // before scheduling product to import we check product's category
        this.checkCategoriesAssociations(item);

        this.products.put(item);
    }

    @Override
    public void init()
    {
        this.accessToken = AuthHelper.getAccessToken(this.token.getValue(),
                                                     this.userName,
                                                     this.password);

        Product_Service product = new Product_Service();
        this.productService = product.getBasicHttpBindingIProduct();

        // remove all products
        this.productService.truncate(this.accessToken);

        ProductCategory_Service productCategory_Service = new ProductCategory_Service();
        this.categoryService = productCategory_Service.getBasicHttpBindingIProductCategory();

        // remove all categories
        this.categoryService.truncate(this.accessToken);

        // create root category
        ProductCategory category = this.categoryService.create(
                this.accessToken, this.token.getCatName(), 0);

        this.categoriesAssociations.put(this.token.getCatId(), category.getId());

        // init importers
        this.tasksMonitor = new CountDownLatch(this.countImportThreads);

        this.executorService = ThreadPoolUtil.createThreadsPool();

        for (int i = 0; i < this.countImportThreads; i++)
        {
            ProductImporterTask importerTask = new ProductImporterTask(
                    tasksMonitor,
                    products,
                    accessToken,
                    this.categoriesAssociations,
                    productsCounter);

            importerTask.init();
            this.tasks.add(importerTask);

            this.executorService.execute(importerTask);
        }
    }

    @Override
    public void cancel()
    {
        super.cancel();

        Logger.getLogger(ProductImporter.class.getName()).info("Cancelling...");
        for (ProductImporterTask importerTask : tasks)
        {
            importerTask.cancel();
        }

        try
        {
            this.tasksMonitor.await();
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(ProductImporter.class.getName()).log(Level.SEVERE,
                                                                  null, ex);
        }

        Logger.getLogger(ProductImporter.class.getName()).info(
                "Shutdown threads");
        this.executorService.shutdownNow();
    }

    private void checkCategoriesAssociations(ParsedProductInfo item)
    {
        Integer iceCategoryId = item.getProduct().getCatId();
        Integer emissionFrameworkCategoryId = this.categoriesAssociations.get(
                iceCategoryId);

        if (emissionFrameworkCategoryId != null)
        {
            return;
        }

        // we must create new category
        ProductCategory category = this.categoryService.create(this.accessToken,
                                                               this.importContext.getCategoryName(
                iceCategoryId),
                                                               0);

        this.categoriesAssociations.put(iceCategoryId, category.getId());

        Logger.getLogger(ProductImporter.class.getName()).log(
                Level.INFO,
                "New category created: {0}",
                category.getName());
    }

    // <editor-fold defaultstate="collapsed" desc="ProductImporterTask">
    private static class ProductImporterTask extends QueueProcessorTask<ParsedProductInfo>
    {
        private String accessToken;
        private ConcurrentHashMap<Integer, Integer> categoriesAssociations;
        private IProduct productService;
        private IProductCategory categoryService;
        private AtomicInteger productsCounter;

        public ProductImporterTask(CountDownLatch taskMonitor,
                                   BlockingQueue<ParsedProductInfo> queue,
                                   String accessToken,
                                   ConcurrentHashMap<Integer, Integer> categoriesAssociations,
                                   AtomicInteger productsCounter)
        {
            super(taskMonitor, queue);

            this.accessToken = accessToken;
            this.categoriesAssociations = categoriesAssociations;
            this.productsCounter = productsCounter;
        }

        public void init()
        {
            Product_Service product_Service = new Product_Service();
            this.productService = product_Service.getBasicHttpBindingIProduct();

            ProductCategory_Service category_Service = new ProductCategory_Service();
            this.categoryService = category_Service.getBasicHttpBindingIProductCategory();
        }

        @Override
        protected void processNextObject(ParsedProductInfo item) throws Throwable
        {
            Product product = item.getProduct();

            com.emission.framework.product.Product p = this.productService.create(
                    this.accessToken, product.getId(),
                    product.getName(),
                    product.getSummaryDescription().getLongDescription(
                    item.getLanguage().getId()),
                    product.getTitle(),
                    product.getHighPicUrl(),
                    0.0f,
                    0.0f,
                    "");

            Integer emissionFrameworkCategoryId = this.categoriesAssociations.get(
                    item.getProductFileRef().getCatId());

            if (emissionFrameworkCategoryId == null)
            {
                Logger.getLogger(ProductImporterTask.class.getName()).log(
                        Level.WARNING,
                        "Something bad happened, we can't found association with icecat category: {0} in our model",
                        item.getProductFileRef().getCatId());
                return;
            }

            this.categoryService.add(this.accessToken, p.getId(),
                                     emissionFrameworkCategoryId);
            System.out.println(
                    "Count products: " + this.productsCounter.incrementAndGet());
        }
    }// </editor-fold>
}
