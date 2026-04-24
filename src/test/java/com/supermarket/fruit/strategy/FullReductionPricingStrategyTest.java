package com.supermarket.fruit.strategy;

import com.supermarket.fruit.entity.CartItem;
import com.supermarket.fruit.entity.FruitType;
import com.supermarket.fruit.entity.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link FullReductionPricingStrategy} 单元测试。
 * <p>覆盖顾客 D 的“满 100 减 10”场景，并重点测试边界值。</p>
 */
class FullReductionPricingStrategyTest {

    private PricingStrategy discountThenReduce;

    @BeforeEach
    void setUp() {
        PricingStrategy base = new SimplePricingStrategy();
        PricingStrategy discount = new DiscountPricingStrategy(
                base, new BigDecimal("0.8"), FruitType.STRAWBERRY);
        discountThenReduce = new FullReductionPricingStrategy(
                discount, new BigDecimal("100"), new BigDecimal("10"));
    }

    @Test
    void shouldApplyFullReductionWhenThresholdReached() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new CartItem(FruitType.APPLE, 5));      // 40
        cart.addItem(new CartItem(FruitType.STRAWBERRY, 4)); // 52 * 0.8 = 41.6
        cart.addItem(new CartItem(FruitType.MANGO, 2));      // 40
        // 小计 121.6，满 100 减 10 => 111.6

        assertEquals(new BigDecimal("111.60"), discountThenReduce.calculate(cart));
    }

    @Test
    void shouldNotApplyReductionWhenBelowThreshold() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new CartItem(FruitType.APPLE, 1));      // 8
        cart.addItem(new CartItem(FruitType.STRAWBERRY, 1)); // 13 * 0.8 = 10.4
        // 小计 18.4，不满 100

        assertEquals(new BigDecimal("18.40"), discountThenReduce.calculate(cart));
    }

    @Test
    void shouldApplyReductionExactlyAtThreshold() {
        // 构造一个打折后恰好等于 100 的购物车
        // apple 5 = 40, mango 3 = 60 => 100
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new CartItem(FruitType.APPLE, 5)); // 40
        cart.addItem(new CartItem(FruitType.MANGO, 3)); // 60
        // 恰好 100，减 10 => 90

        assertEquals(new BigDecimal("90.00"), discountThenReduce.calculate(cart));
    }

    @Test
    void shouldApplyReductionJustAboveThreshold() {
        // 打折后 100.01 的情况在整数斤数下难以构造，
        // 改用直接满减策略测试：101 应该减 10
        PricingStrategy simpleReduce = new FullReductionPricingStrategy(
                new SimplePricingStrategy(), new BigDecimal("100"), new BigDecimal("10"));

        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new CartItem(FruitType.APPLE, 13)); // 104
        // 104 >= 100，减 10 => 94

        assertEquals(new BigDecimal("94.00"), simpleReduce.calculate(cart));
    }

    @Test
    void shouldNotApplyReductionJustBelowThreshold() {
        PricingStrategy simpleReduce = new FullReductionPricingStrategy(
                new SimplePricingStrategy(), new BigDecimal("100"), new BigDecimal("10"));

        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new CartItem(FruitType.APPLE, 12)); // 96
        // 96 < 100，不减

        assertEquals(new BigDecimal("96.00"), simpleReduce.calculate(cart));
    }

    @Test
    void shouldReturnZeroForEmptyCart() {
        ShoppingCart cart = new ShoppingCart();
        assertEquals(new BigDecimal("0.00"), discountThenReduce.calculate(cart));
    }

    @Test
    void shouldNotReduceBelowZero() {
        PricingStrategy aggressiveReduce = new FullReductionPricingStrategy(
                new SimplePricingStrategy(), new BigDecimal("10"), new BigDecimal("10"));

        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new CartItem(FruitType.APPLE, 2)); // 16 >= 10，减 10 => 6

        assertEquals(new BigDecimal("6.00"), aggressiveReduce.calculate(cart));
    }

    @Test
    void shouldThrowExceptionWhenBaseStrategyIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new FullReductionPricingStrategy(
                        null, new BigDecimal("100"), new BigDecimal("10"))
        );
        assertEquals("基础计价策略不能为空", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenThresholdIsInvalid() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new FullReductionPricingStrategy(
                        new SimplePricingStrategy(), new BigDecimal("0"), new BigDecimal("10"))
        );
        assertEquals("满减阈值必须大于 0", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenReductionAmountIsInvalid() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new FullReductionPricingStrategy(
                        new SimplePricingStrategy(), new BigDecimal("100"), new BigDecimal("120"))
        );
        assertEquals("减免金额必须大于 0 且小于等于阈值", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCartIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> discountThenReduce.calculate(null)
        );
        assertEquals("购物车不能为空", exception.getMessage());
    }
}
