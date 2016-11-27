package resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import daos.ProductDao;
import models.Product;
import models.QueryFormat;

@Path("/search")
public class SearchResource {

    private ProductDao productDao;

    public SearchResource(ProductDao productDao) {
        this.productDao = productDao;
    }


    @GET
    public List<Product> search(@QueryParam("type") String type) {

        return productDao.findAllByType(type);
    }

    @POST
    public List<Product> search(QueryFormat query) {

        return  productDao.findByTypeAndAttributes(query);
    }
}
