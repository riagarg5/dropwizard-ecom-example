package resources;

import daos.ProductDao;

import javax.ws.rs.*;
import java.util.List;

/**
 * Created by Ria on 27/11/16.
 */
@Path("/attributes")
public class AttributeResource {
    private ProductDao productDao;

    public AttributeResource(ProductDao productDao) {
        this.productDao = productDao;
    }

    @GET
    public List<String> search(@QueryParam("type") String type) {

        return productDao.findAttributesByType(type);
    }

}
