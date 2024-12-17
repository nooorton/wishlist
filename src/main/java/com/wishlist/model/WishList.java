package com.wishlist.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "wishlist")
public class WishList {

    @Id
    private Integer userId;

    private List<WishItem> items;

    public WishList() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<WishItem> getItems() {
        return items;
    }

    public void setItems(List<WishItem> items) {
        this.items = items;
    }
}


