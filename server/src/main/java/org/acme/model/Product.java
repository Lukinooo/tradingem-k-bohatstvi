package org.acme.model;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCTS")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="productSeq")
    private Long id;
    private String name;
    private Float price;

    @ManyToOne
    @JoinColumn(name = "price_category_id", nullable= false)
    private PriceCategory priceCategory;

}
