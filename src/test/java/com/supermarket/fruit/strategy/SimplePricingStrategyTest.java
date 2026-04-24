package com.supermarket.fruit.strategy;

import com.supermarket.fruit.entity.CartItem;
import com.supermarket.fruit.entity.FruitType;
import com.supermarket.fruit.entity.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link SimplePricingStrategy} 单元测试。
 * <p>覆盖顾客 A（苹果+草莓）与顾客 B（苹果+草莓+芒果）的原价场景。</p>
 */
class SimplePricingStrategyTest {

    private SimplePricingStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new SimplePricingStrategy();
    }

    @Test
    void shouldCalculateTotalForCustomerAppleAndStrawberry() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new CartItem(FruitType.APPLE, 5));      // 40
        cart.addItem(new CartItem(FruitType.STRAWBERRY, 4)); // 52

        assertEquals(new BigDecimal("92"), strategy.calculate(cart));
    }

    @Test
    void shouldCalculateTotalForCustomerWithAllFruits() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new CartItem(FruitType.APPLE, 2));      // 16
        cart.addItem(new CartItem(FruitType.STRAWBERRY, 3)); // 39
        cart.addItem(new CartItem(FruitType.MANGO, 2));      // 40

        assertEquals(new BigDecimal("95"), strategy.calculate(cart));
    }

    @Test
    void shouldReturnZeroForEmptyCart() {
        ShoppingCart cart = new ShoppingCart();
        assertEquals(BigDecimal.ZERO, strategy.calculate(cart));
    }

    @Test
    void shouldThrowExceptionWhenCartIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> strategy.calculate(null)
        );
        assertEquals("购物车不能为空", exception.getMessage());
    }
}
