package com.wishlist.service;

import com.wishlist.model.WishList;
import org.springframework.stereotype.Service;
import com.wishlist.repository.WishListRepository;

import java.util.List;
@Service
public class WishListService {

    private final WishListRepository repository;

    public WishListService(WishListRepository repository) {
        this.repository = repository;
    }

    public List<WishList> findAll() {
        return repository.findAll();
    }

    public WishList findByUserId(Integer userId) {
        return repository.findByUserId(userId);
    }

    public WishList save(WishList wishList) {
        return repository.save(wishList);
    }

    public void deleteByUserId(Integer userId) {
        WishList wishList = findByUserId(userId);
        if (wishList != null) {
            repository.delete(wishList);
        }
    }
}