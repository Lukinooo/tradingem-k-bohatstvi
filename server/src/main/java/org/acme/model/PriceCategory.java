package org.acme.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

public class PriceCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="priceCategorySeq")
    private Long id;
    private Integer level;
    private float min_price;
    private float max_price;


}
