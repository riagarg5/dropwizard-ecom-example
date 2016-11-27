package models;

import java.util.List;

/**
 * Created by Ria on 27/11/16.
 */
public class QueryFormat {
    private String type;
    private List<String> attributes;

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
