package resources;


import daos.ProductDao;
import models.Product;

import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

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
}
