package services;

import daos.ProductDao;
import daos.impl.ProductDaoImpl;
import healthchecks.ElasticSearchHealthCheck;
import resources.AttributeResource;
import resources.ProductResource;
import resources.SearchResource;

import com.google.common.cache.CacheBuilderSpec;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.bundles.AssetsBundle;
import com.yammer.dropwizard.config.Environment;

import configurations.ApplicationConfiguration;


public class EComService extends Service<ApplicationConfiguration> {

    public static void main(String[] argv) throws Exception {
        new EComService().run(new String[] {"server", "src/main/resources/ecomservice.yaml"});
    }

    public EComService() {
        this("ecom");
    }

    protected EComService(String name) {
        super(name);
        addBundle(new AssetsBundle(AssetsBundle.DEFAULT_PATH, CacheBuilderSpec.disableCaching())); // TODO: Start caching in production mode, but want to clear cache somehow
    }

    @Override
    protected void initialize(ApplicationConfiguration configuration, Environment environment) throws Exception {
        // Create elasticsearch server
        ElasticSearchManager esManager = new ElasticSearchManager(configuration.datadir);
        environment.manage(esManager);

        // Create dao
        ProductDao productDao = new ProductDaoImpl(esManager.getNode(), getJson());

        environment.addResource(new SearchResource(productDao));
        environment.addResource(new ProductResource(productDao));
        environment.addResource(new AttributeResource(productDao));

        // health check
        environment.addHealthCheck(new ElasticSearchHealthCheck(esManager.getNode()));

        if (configuration.loadInitialData) {
            new InitialDataLoader(configuration, productDao, getJson()).load();
        }
    }
}
