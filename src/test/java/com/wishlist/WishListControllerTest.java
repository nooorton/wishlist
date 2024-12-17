package com.wishlist;

import com.wishlist.model.WishItem;
import com.wishlist.model.WishList;
import com.wishlist.service.WishListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class WishListControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WishListService wishListService;

    private WishList wishList;

    @BeforeEach
    public void setUp() {
        wishList = new WishList();
        wishList.setUserId(1);
        wishList.setItems(new ArrayList<>());
    }

    private List<WishItem> createListItem(int quantity) {
        return IntStream.rangeClosed(1, quantity)
                .mapToObj(i -> new WishItem("Item " + i, "Description " + i))
                .toList();
    }

    @Test
    public void testCreateOrUpdateWishList01() {
        wishList.setItems(createListItem(15));
        ResponseEntity<WishList> response = restTemplate.postForEntity("/api/wishlist", wishList, WishList.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getItems().size(),15);
        assertEquals(wishList.getUserId(), response.getBody().getUserId());
    }

    @Test
    public void testCreateOrUpdateWishListWithTooManyItems() {
        wishList.setItems(createListItem(21));
        ResponseEntity<String> response = restTemplate.postForEntity("/api/wishlist", wishList, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("A lista de itens não pode ter mais de 20 itens.", response.getBody());
    }

    @Test
    public void testCreateAndUpdateWishList() {
        wishList.setItems(createListItem(10));
        ResponseEntity<WishList> createResponse = restTemplate.postForEntity("/api/wishlist", wishList, WishList.class);

        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertEquals(10, createResponse.getBody().getItems().size());
        assertEquals(wishList.getUserId(), createResponse.getBody().getUserId());

        wishList.setItems(createListItem(5));
        ResponseEntity<WishList> updateResponse = restTemplate.postForEntity("/api/wishlist", wishList, WishList.class);

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        assertEquals(5, updateResponse.getBody().getItems().size());
        assertEquals(wishList.getUserId(), updateResponse.getBody().getUserId());
    }

    @Test
    public void testGetAllWishLists() {
        ResponseEntity<WishList[]> response = restTemplate.getForEntity("/api/wishlist", WishList[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
    }

    @Test
    public void testGetWishListByUserId() {
        wishListService.save(wishList);
        ResponseEntity<WishList> response = restTemplate.getForEntity("/api/wishlist/{userId}", WishList.class, wishList.getUserId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(wishList.getUserId(), response.getBody().getUserId());
    }

    @Test
    public void testDeleteWishListByUserId() {
        wishListService.save(wishList);
        restTemplate.delete("/api/wishlist/{userId}", wishList.getUserId());
        ResponseEntity<WishList> response = restTemplate.getForEntity("/api/wishlist/{userId}", WishList.class, wishList.getUserId());
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void testCreateTwoWishListsAndPerformOperations() {
        WishList wishListUser1 = new WishList();
        wishListUser1.setUserId(1);
        wishListUser1.setItems(createListItem(10));
        ResponseEntity<WishList> response1 = restTemplate.postForEntity("/api/wishlist", wishListUser1, WishList.class);
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response1.getBody());
        assertEquals(10, response1.getBody().getItems().size());

        WishList wishListUser2 = new WishList();
        wishListUser2.setUserId(2);
        wishListUser2.setItems(createListItem(15));
        ResponseEntity<WishList> response2 = restTemplate.postForEntity("/api/wishlist", wishListUser2, WishList.class);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response2.getBody());
        assertEquals(15, response2.getBody().getItems().size());

        ResponseEntity<WishList> user1Response = restTemplate.getForEntity("/api/wishlist/{userId}", WishList.class, 1);
        assertThat(user1Response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(user1Response.getBody());
        assertEquals(1, user1Response.getBody().getUserId());
        assertEquals(10, user1Response.getBody().getItems().size());

        ResponseEntity<WishList> user2Response = restTemplate.getForEntity("/api/wishlist/{userId}", WishList.class, 2);
        assertThat(user2Response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(user2Response.getBody());
        assertEquals(2, user2Response.getBody().getUserId());
        assertEquals(15, user2Response.getBody().getItems().size());

        restTemplate.delete("/api/wishlist/{userId}", 1);

        ResponseEntity<WishList> deletedUser1Response = restTemplate.getForEntity("/api/wishlist/{userId}", WishList.class, 1);
        assertThat(deletedUser1Response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        ResponseEntity<WishList[]> allWishListsResponse = restTemplate.getForEntity("/api/wishlist", WishList[].class);
        assertThat(allWishListsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(allWishListsResponse.getBody());
        assertEquals(1, allWishListsResponse.getBody().length);
        assertEquals(2, allWishListsResponse.getBody()[0].getUserId());
        assertEquals(15, allWishListsResponse.getBody()[0].getItems().size());
    }
}
