package daos.impl;

import com.google.common.collect.Lists;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.yammer.dropwizard.json.Json;
import daos.ProductDao;
import models.Product;
import models.QueryFormat;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ria on 27/11/16.
 */
public class ProductDaoImpl implements ProductDao {
    private static final String INDEX = "products";
    private static final String TYPE = "product";

    private Node node;
    private Json json;

    public ProductDaoImpl(Node node, Json json) {
        this.node = node;
        this.json = json;
    }

    public Product store(Product product) {
        byte[] data = json.writeValueAsBytes(product);
        IndexResponse indexResponse = node.client().prepareIndex(INDEX, TYPE, null).setSource(data).execute().actionGet();

        product.setId(indexResponse.getId());
        return product;

    }

    public boolean deleteById(String id) {
        DeleteResponse deleteResponse = node.client().prepareDelete(INDEX, TYPE, id).execute().actionGet();
        return deleteResponse.isFound();
    }

    public List<Product> findByTypeAndAttributes(QueryFormat query) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchQuery("type", query.getType()));
        List<String> attributes = query.getAttributes();
        for (int i=0; i<attributes.size(); i++) {
            boolQuery.must(QueryBuilders.matchQuery("attributes", attributes.get(i)));
        }
        SearchResponse searchResponse = node.client().prepareSearch(INDEX).setTypes(TYPE).
                setQuery(boolQuery).execute().actionGet();

        return toProducts(searchResponse);
    }

    public List<Product> findAllByType(String type) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchQuery("type", type));
        SearchResponse searchResponse = node.client().prepareSearch(INDEX).setTypes(TYPE).
                setQuery(boolQuery).execute().actionGet();

        return toProducts(searchResponse);
    }

    public List<String> findAttributesByType(String type) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchQuery("type", type));
        SearchResponse searchResponse = node.client().prepareSearch(INDEX).setTypes(TYPE).
                setQuery(boolQuery).execute().actionGet();
        List<String> results = Lists.newArrayList();
        List<Product> products =  toProducts(searchResponse);
        HashMap<String, Integer> hmap = new HashMap<String, Integer>();
        for (int i=0; i<products.size(); i++) {
            List<String> attributes = products.get(i).getAttributes();
            for (int j=0; j<attributes.size(); j++) {
                if (!hmap.containsKey(attributes.get(j))) {
                    results.add(attributes.get(j));
                    hmap.put(attributes.get(j), 1);
                }
            }
        }

        return results;
    }

    public List<Product> findByName(String name) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchQuery("name", name));
        SearchResponse searchResponse = node.client().prepareSearch(INDEX).setTypes(TYPE).
                setQuery(boolQuery).execute().actionGet();

        return toProducts(searchResponse);
    }

    public Boolean updateProduct(String id, Product product) {
        byte[] data = json.writeValueAsBytes(product);
        UpdateResponse updateResponse = node.client().prepareUpdate().setIndex(INDEX).setType(TYPE).
                setId(id).setDoc(data).execute().actionGet();
        return updateResponse.isCreated();
    }

    private List<Product> toProducts(SearchResponse searchResponse) {
        List<Product> products = Lists.newArrayList();

        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            try {
                Product product = json.readValue(searchHit.source(), Product.class);
                product.setId(searchHit.id());
                products.add(product);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return products;
    }
}
