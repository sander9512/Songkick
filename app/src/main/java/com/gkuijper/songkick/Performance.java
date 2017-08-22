package com.gkuijper.songkick;

import java.io.Serializable;

/**
 * Created by Gabrielle on 06-08-17.
 */

public class Performance implements Serializable {
    private String name;
    private String billing;

    public Performance(String name, String billing) {
        this.name = name;
        this.billing = billing;
    }

    public Performance() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBilling() {
        return billing;
    }

    public void setBilling(String billing) {
        this.billing = billing;
    }

    @Override
    public String toString() {
        return "Performance{" +
                "name='" + name + '\'' +
                ", billing='" + billing + '\'' +
                '}';
    }
}
