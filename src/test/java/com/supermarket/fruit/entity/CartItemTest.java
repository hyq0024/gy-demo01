package com.supermarket.fruit.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CartItem} 单元测试。
 */
class CartItemTest {

    @Test
    void shouldCalculateSubtotalCorrectly() {
        CartItem item = new CartItem(FruitType.APPLE, 3);
        assertEquals(new BigDecimal("24"), item.getOriginalSubtotal());
    }

    @Test
    void shouldAllowZeroWeight() {
        CartItem item = new CartItem(FruitType.STRAWBERRY, 0);
        assertEquals(BigDecimal.ZERO, item.getOriginalSubtotal());
    }

    @Test
    void shouldThrowExceptionWhenFruitTypeIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CartItem(null, 1)
        );
        assertEquals("水果类型不能为空", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenWeightIsNegative() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CartItem(FruitType.MANGO, -1)
        );
        assertEquals("购买重量不能为负数", exception.getMessage());
    }
}
