package org.acme.model;

import javax.persistence.*;

@Entity
@Table(name = "SHOPS")
public class Shop {
    private Long id;
    private String name;
    private Integer num_products;

    @ManyToOne
    @JoinColumn(name="game_id", nullable=false)
    private Game game;



    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="shopSeq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

