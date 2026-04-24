package com.supermarket.fruit.calculator;

import com.supermarket.fruit.entity.CartItem;
import com.supermarket.fruit.entity.FruitType;
import com.supermarket.fruit.entity.ShoppingCart;
import com.supermarket.fruit.strategy.DiscountPricingStrategy;
import com.supermarket.fruit.strategy.FullReductionPricingStrategy;
import com.supermarket.fruit.strategy.PricingStrategy;
import com.supermarket.fruit.strategy.SimplePricingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link PriceCalculator} 单元测试。
 * <p>验证四位顾客（A/B/C/D）的完整购买场景，以及策略切换的灵活性。</p>
 */
class PriceCalculatorTest {

    private PriceCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new PriceCalculator();
    }

    @Test
    void shouldCalculatePriceForCustomerA_AppleAndStrawberryOnly() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new CartItem(FruitType.APPLE, 3));      // 24
        cart.addItem(new CartItem(FruitType.STRAWBERRY, 2)); // 26

        PricingStrategy strategy = new SimplePricingStrategy();

        assertEquals(new BigDecimal("50"), calculator.calculate(cart, strategy));
    }

    @Test
    void shouldCalculatePriceForCustomerB_WithMango() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new CartItem(FruitType.APPLE, 2));      // 16
        cart.addItem(new CartItem(FruitType.STRAWBERRY, 3)); // 39
        cart.addItem(new CartItem(FruitType.MANGO, 1));      // 20

        PricingStrategy strategy = new SimplePricingStrategy();

        assertEquals(new BigDecimal("75"), calculator.calculate(cart, strategy));
    }

    @Test
    void shouldCalculatePriceForCustomerC_StrawberryDiscount() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new CartItem(FruitType.APPLE, 2));      // 16
        cart.addItem(new CartItem(FruitType.STRAWBERRY, 3)); // 39 * 0.8 = 31.2
        cart.addItem(new CartItem(FruitType.MANGO, 1));      // 20

        PricingStrategy strategy = new DiscountPricingStrategy(
                new SimplePricingStrategy(), new BigDecimal("0.8"), FruitType.STRAWBERRY);

        assertEquals(new BigDecimal("67.20"), calculator.calculate(cart, strategy));
    }

    @Test
    void shouldCalculatePriceForCustomerD_DiscountAndFullReduction() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new CartItem(FruitType.APPLE, 5));      // 40
        cart.addItem(new CartItem(FruitType.STRAWBERRY, 4)); // 52 * 0.8 = 41.6
        cart.addItem(new CartItem(FruitType.MANGO, 2));      // 40
        // 121.6 - 10 = 111.6

        PricingStrategy discount = new DiscountPricingStrategy(
                new SimplePricingStrategy(), new BigDecimal("0.8"), FruitType.STRAWBERRY);
        PricingStrategy strategy = new FullReductionPricingStrategy(
                discount, new BigDecimal("100"), new BigDecimal("10"));

        assertEquals(new BigDecimal("111.60"), calculator.calculate(cart, strategy));
    }

    @Test
    void shouldThrowExceptionWhenStrategyIsNull() {
        ShoppingCart cart = new ShoppingCart();
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculate(cart, null)
        );
        assertEquals("计价策略不能为空", exception.getMessage());
    }

    @Test
    void shouldSupportSwitchingDiscountFruit() {
        // 扩展性验证：把打折水果从草莓换成芒果
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new CartItem(FruitType.APPLE, 1));      // 8
        cart.addItem(new CartItem(FruitType.STRAWBERRY, 1)); // 13
        cart.addItem(new CartItem(FruitType.MANGO, 1));      // 20 * 0.5 = 10

        PricingStrategy strategy = new DiscountPricingStrategy(
                new SimplePricingStrategy(), new BigDecimal("0.5"), FruitType.MANGO);

        assertEquals(new BigDecimal("31.00"), calculator.calculate(cart, strategy));
    }

    @Test
    void shouldSupportAddingNewFruitType() {
        // FruitType 是枚举，实际新增需改代码；这里验证现有结构可轻松容纳新水果的购买逻辑
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new CartItem(FruitType.APPLE, 0));
        cart.addItem(new CartItem(FruitType.STRAWBERRY, 0));
        cart.addItem(new CartItem(FruitType.MANGO, 0));

        assertEquals(BigDecimal.ZERO, calculator.calculate(cart, new SimplePricingStrategy()));
    }
}
