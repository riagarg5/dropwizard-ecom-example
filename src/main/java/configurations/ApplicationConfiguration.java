package configurations;


import com.yammer.dropwizard.config.Configuration;

public class ApplicationConfiguration extends Configuration {
    public String datadir;

    public boolean loadInitialData = false;
    public String productDataDirectory;
}
