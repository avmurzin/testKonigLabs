package com.avmurzin;

import java.util.UUID;

/**
 * Created by Andrey V. Murzin on 09.06.17.
 */
public class Nest {
    private String id;
    private String value;

    public Nest() {
        this.id = UUID.randomUUID().toString();
        this.value = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
