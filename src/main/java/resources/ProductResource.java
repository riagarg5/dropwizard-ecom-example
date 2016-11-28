package resources;


import daos.ProductDao;
import models.Product;

import javax.ws.rs.*;

/**
 * Created by Ria on 27/11/16.
 */

@Path("/product")
public class ProductResource {

    private ProductDao productDao;

    public ProductResource(ProductDao productDao) {
        this.productDao = productDao;
    }

    @PUT
    public Product create(Product product) {
        return productDao.store(product);
    }

    @Path("/{id}")
    @DELETE
    public boolean delete(@PathParam("id") String id) {
        return productDao.deleteById(id);
    }

    @Path("/{id}")
    @PUT
    public Boolean post(@PathParam("id") String id, Product product) {

        return productDao.updateProduct(id, product);
    }
}
