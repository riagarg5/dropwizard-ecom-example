package daos;

import java.util.List;

import com.sun.org.apache.xpath.internal.operations.Bool;
import models.Product;
import models.QueryFormat;

public interface ProductDao {

    Product store(Product product);

    boolean deleteById(String id);

    List<Product> findByTypeAndAttributes(QueryFormat query);

    List<Product> findAllByType(String type);

    List<String> findAttributesByType(String type);

    List<Product> findByName(String name);

    Boolean updateProduct(String id, Product product);

}
