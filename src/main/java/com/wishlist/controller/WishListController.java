package com.wishlist.controller;

import com.wishlist.exception.InvalidArgumentException;
import com.wishlist.model.WishList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.wishlist.service.WishListService;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    private final WishListService service;

    public WishListController(WishListService service) {
        this.service = service;
    }

    @GetMapping
    public List<WishList> getAllWishLists() {
        return service.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<WishList> getWishListByUserId(@PathVariable Integer userId) {
        WishList wishList = service.findByUserId(userId);
        if (wishList == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(wishList);
    }

    @PostMapping
    public ResponseEntity<WishList> createOrUpdateWishList(@RequestBody WishList wishList) {

        if (wishList.getItems() != null && wishList.getItems().size() > 20) {
            throw new InvalidArgumentException("A lista de itens não pode ter mais de 20 itens.");
        }
        WishList existingWishList = service.findByUserId(wishList.getUserId());

        if (existingWishList != null) {
            existingWishList.setItems(wishList.getItems());
        } else {
            existingWishList = wishList;
        }

        WishList savedWishList = service.save(existingWishList);
        return ResponseEntity.ok(savedWishList);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteWishListByUserId(@PathVariable Integer userId) {
        WishList wishList = service.findByUserId(userId);
        if (wishList == null) {
            return ResponseEntity.notFound().build();
        }
        service.deleteByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}