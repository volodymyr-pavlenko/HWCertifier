package se.zipper.hwcertifier.domain.registry;

/**
 * Created with IntelliJ IDEA.
 * User: dreamer
 * Date: 12.03.13
 * Time: 18:31
 * To change this template use File | Settings | File Templates.
 */
public class RegistryValue {
    private String name;
    private String type;
    private String data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }



}
