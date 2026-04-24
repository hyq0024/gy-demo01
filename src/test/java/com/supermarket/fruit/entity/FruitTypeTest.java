package com.supermarket.fruit.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link FruitType} 枚举单元测试。
 */
class FruitTypeTest {

    @Test
    void shouldReturnCorrectUnitPriceForApple() {
        assertEquals(new BigDecimal("8"), FruitType.APPLE.getUnitPrice());
    }

    @Test
    void shouldReturnCorrectUnitPriceForStrawberry() {
        assertEquals(new BigDecimal("13"), FruitType.STRAWBERRY.getUnitPrice());
    }

    @Test
    void shouldReturnCorrectUnitPriceForMango() {
        assertEquals(new BigDecimal("20"), FruitType.MANGO.getUnitPrice());
    }
}
