package services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import daos.ProductDao;
import models.MyYamlFile;
import models.Product;

import com.yammer.dropwizard.json.Json;
import com.yammer.dropwizard.logging.Log;

import configurations.ApplicationConfiguration;

public class InitialDataLoader {

    private final Log log = Log.forClass(this.getClass());

    private ApplicationConfiguration configuration;
    private ProductDao productDao;
    private Json json;

    public InitialDataLoader(ApplicationConfiguration configuration,
                             ProductDao productDao, Json json) {
        this.configuration = configuration;
        this.productDao = productDao;
        this.json = json;
    }

    public void load() {
        createEntriesIfNeeded();
    }

    private void createEntriesIfNeeded() {
        File productDir = new File(configuration.productDataDirectory);

        if (productDir.exists() && productDir.isDirectory()) {
            for (File productFile : productDir.listFiles()) {
                if (!productFile.getName().endsWith(".yaml")) {
                    log.info("Skipping article file {}", productFile.getName());
                    continue;
                }

                log.info("Checking for article file '{}'", productFile.getName());
                try {
                    log.info("Adding new article [{}]", "a");
                    MyYamlFile product = json.readYamlValue(productFile, MyYamlFile.class);
                    for (int i =0; i<product.getProducts().size(); i++)
                        productDao.store(product.getProducts().get(i));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
