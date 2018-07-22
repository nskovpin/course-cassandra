package ru.dataart.courses.cassandra.entities.hotel;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

@Table(value = "city")
public class City {

    @PrimaryKey
    private CityKey cityKey;

    public City() {
        this.cityKey = new CityKey();
    }

    public CityKey getCityKey() {
        return cityKey;
    }

    public void setCityKey(CityKey cityKey) {
        this.cityKey = cityKey;
    }
}
