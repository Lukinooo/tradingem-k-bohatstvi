package org.acme.models;

import javax.persistence.*;

@Entity
@Table(name = "PRICE_CATEGORIES")
public class PriceCategory {
    @Id
    @GeneratedValue
    private Long id;
    private Integer level;
    private float min_price;
    private float max_price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public float getMin_price() {
        return min_price;
    }

    public void setMin_price(float min_price) {
        this.min_price = min_price;
    }

    public float getMax_price() {
        return max_price;
    }

    public void setMax_price(float max_price) {
        this.max_price = max_price;
    }
}
