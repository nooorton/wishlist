package com.wishlist.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WishItem {

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    public WishItem() {
    }

    public WishItem(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
