package com.supermarket.fruit.strategy;

import com.supermarket.fruit.entity.CartItem;
import com.supermarket.fruit.entity.FruitType;
import com.supermarket.fruit.entity.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link DiscountPricingStrategy} 单元测试。
 * <p>覆盖顾客 C 的草莓 8 折场景，以及多水果打折、未打折水果保持原价等扩展场景。</p>
 */
class DiscountPricingStrategyTest {

    private SimplePricingStrategy baseStrategy;

    @BeforeEach
    void setUp() {
        baseStrategy = new SimplePricingStrategy();
    }

    @Test
    void shouldApplyDiscountOnlyToStrawberry() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new CartItem(FruitType.APPLE, 5));      // 40
        cart.addItem(new CartItem(FruitType.STRAWBERRY, 4)); // 52 * 0.8 = 41.6
        cart.addItem(new CartItem(FruitType.MANGO, 2));      // 40

        PricingStrategy discountStrategy = new DiscountPricingStrategy(
                baseStrategy, new BigDecimal("0.8"), FruitType.STRAWBERRY);

        // 40 + 41.6 + 40 = 121.6
        assertEquals(new BigDecimal("121.60"), discountStrategy.calculate(cart));
    }

    @Test
    void shouldApplyDiscountToMultipleFruits() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new CartItem(FruitType.APPLE, 5));      // 40
        cart.addItem(new CartItem(FruitType.STRAWBERRY, 2)); // 26 * 0.8 = 20.8
        cart.addItem(new CartItem(FruitType.MANGO, 1));      // 20 * 0.8 = 16

        PricingStrategy discountStrategy = new DiscountPricingStrategy(
                baseStrategy, new BigDecimal("0.8"),
                FruitType.STRAWBERRY, FruitType.MANGO);

        // 40 + 20.8 + 16 = 76.8
        assertEquals(new BigDecimal("76.80"), discountStrategy.calculate(cart));
    }

    @Test
    void shouldReturnZeroForEmptyCart() {
        PricingStrategy discountStrategy = new DiscountPricingStrategy(
                baseStrategy, new BigDecimal("0.8"), FruitType.STRAWBERRY);

        ShoppingCart cart = new ShoppingCart();
        assertEquals(BigDecimal.ZERO, discountStrategy.calculate(cart));
    }

    @Test
    void shouldThrowExceptionWhenBaseStrategyIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DiscountPricingStrategy(null, new BigDecimal("0.8"), FruitType.STRAWBERRY)
        );
        assertEquals("基础计价策略不能为空", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDiscountRateIsInvalid() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DiscountPricingStrategy(baseStrategy, new BigDecimal("1.2"), FruitType.STRAWBERRY)
        );
        assertEquals("折扣率必须在 (0, 1] 范围内", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNoDiscountFruitProvided() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DiscountPricingStrategy(baseStrategy, new BigDecimal("0.8"))
        );
        assertEquals("至少指定一种需要打折的水果", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCartIsNull() {
        PricingStrategy discountStrategy = new DiscountPricingStrategy(
                baseStrategy, new BigDecimal("0.8"), FruitType.STRAWBERRY);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> discountStrategy.calculate(null)
        );
        assertEquals("购物车不能为空", exception.getMessage());
    }
}
