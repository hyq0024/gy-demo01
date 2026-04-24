package com.supermarket.fruit.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ShoppingCart} 单元测试。
 */
class ShoppingCartTest {

    @Test
    void shouldCalculateOriginalTotalForMultipleItems() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new CartItem(FruitType.APPLE, 2));      // 16
        cart.addItem(new CartItem(FruitType.STRAWBERRY, 3)); // 39
        cart.addItem(new CartItem(FruitType.MANGO, 1));      // 20

        assertEquals(new BigDecimal("75"), cart.getOriginalTotal());
    }

    @Test
    void shouldReturnZeroForEmptyCart() {
        ShoppingCart cart = new ShoppingCart();
        assertEquals(BigDecimal.ZERO, cart.getOriginalTotal());
    }

    @Test
    void shouldThrowExceptionWhenAddingNullItem() {
        ShoppingCart cart = new ShoppingCart();
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cart.addItem(null)
        );
        assertEquals("购物车条目不能为空", exception.getMessage());
    }

    @Test
    void shouldReturnUnmodifiableItemsList() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new CartItem(FruitType.APPLE, 1));

        assertThrows(UnsupportedOperationException.class,
                () -> cart.getItems().add(new CartItem(FruitType.MANGO, 1)));
    }
}
