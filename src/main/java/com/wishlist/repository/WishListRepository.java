package com.wishlist.repository;

import com.wishlist.model.WishList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface WishListRepository extends MongoRepository<WishList, String> {
    @Query("{ 'userId': ?0 }")
    WishList findByUserId(Integer userId);
}