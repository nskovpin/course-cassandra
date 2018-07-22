package ru.dataart.courses.cassandra.entities.guest;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@PrimaryKeyClass
public class GuestKey implements Serializable{

    @PrimaryKeyColumn(name = "guest_name", type = PrimaryKeyType.PARTITIONED, ordinal = 0)
    private String guestName;

    @PrimaryKeyColumn(name = "id", type = PrimaryKeyType.CLUSTERED, ordinal = 1)
    private UUID id;

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuestKey guestKey = (GuestKey) o;
        return Objects.equals(guestName, guestKey.guestName) &&
                Objects.equals(id, guestKey.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(guestName, id);
    }
}
