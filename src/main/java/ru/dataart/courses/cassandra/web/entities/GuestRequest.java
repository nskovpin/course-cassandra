package ru.dataart.courses.cassandra.web.entities;

import javax.validation.constraints.NotNull;

public class GuestRequest {
    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
