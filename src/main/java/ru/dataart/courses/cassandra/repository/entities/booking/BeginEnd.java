package ru.dataart.courses.cassandra.repository.entities.booking;

public enum BeginEnd {
    B("BEGIN"), E("END");

    private String val;

    private BeginEnd(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
