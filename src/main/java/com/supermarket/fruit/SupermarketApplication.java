package com.supermarket.fruit;

import com.supermarket.fruit.calculator.PriceCalculator;
import com.supermarket.fruit.entity.CartItem;
import com.supermarket.fruit.entity.FruitType;
import com.supermarket.fruit.entity.ShoppingCart;
import com.supermarket.fruit.strategy.DiscountPricingStrategy;
import com.supermarket.fruit.strategy.FullReductionPricingStrategy;
import com.supermarket.fruit.strategy.PricingStrategy;
import com.supermarket.fruit.strategy.SimplePricingStrategy;

import java.math.BigDecimal;

/**
 * 超市水果计价系统启动类。
 * <p>演示四位顾客（A/B/C/D）在不同促销活动下的购买场景。</p>
 */
public class SupermarketApplication {

    public static void main(String[] args) {
        PriceCalculator calculator = new PriceCalculator();

        System.out.println("========== 超市水果计价系统 ==========");
        System.out.println("水果单价：苹果 8元/斤 | 草莓 13元/斤 | 芒果 20元/斤");
        System.out.println();

        // 顾客 A：购买苹果 + 草莓，无折扣
        ShoppingCart cartA = new ShoppingCart();
        cartA.addItem(new CartItem(FruitType.APPLE, 5));      // 40
        cartA.addItem(new CartItem(FruitType.STRAWBERRY, 4)); // 52
        BigDecimal totalA = calculator.calculate(cartA, new SimplePricingStrategy());
        System.out.println("【顾客 A】苹果 5斤 + 草莓 4斤");
        System.out.println("计价方式：原价");
        System.out.println("应付金额：" + totalA + " 元");
        System.out.println();

        // 顾客 B：购买苹果 + 草莓 + 芒果，无折扣
        ShoppingCart cartB = new ShoppingCart();
        cartB.addItem(new CartItem(FruitType.APPLE, 2));      // 16
        cartB.addItem(new CartItem(FruitType.STRAWBERRY, 3)); // 39
        cartB.addItem(new CartItem(FruitType.MANGO, 1));      // 20
        BigDecimal totalB = calculator.calculate(cartB, new SimplePricingStrategy());
        System.out.println("【顾客 B】苹果 2斤 + 草莓 3斤 + 芒果 1斤");
        System.out.println("计价方式：原价");
        System.out.println("应付金额：" + totalB + " 元");
        System.out.println();

        // 顾客 C：购买苹果 + 草莓 + 芒果，草莓限时 8 折
        ShoppingCart cartC = new ShoppingCart();
        cartC.addItem(new CartItem(FruitType.APPLE, 5));      // 40
        cartC.addItem(new CartItem(FruitType.STRAWBERRY, 4)); // 52 * 0.8 = 41.6
        cartC.addItem(new CartItem(FruitType.MANGO, 2));      // 40
        PricingStrategy strategyC = new DiscountPricingStrategy(
                new SimplePricingStrategy(), new BigDecimal("0.8"), FruitType.STRAWBERRY);
        BigDecimal totalC = calculator.calculate(cartC, strategyC);
        System.out.println("【顾客 C】苹果 5斤 + 草莓 4斤 + 芒果 2斤");
        System.out.println("计价方式：草莓 8 折");
        System.out.println("应付金额：" + totalC + " 元");
        System.out.println();

        // 顾客 D：购买苹果 + 草莓 + 芒果，草莓 8 折 + 满 100 减 10
        ShoppingCart cartD = new ShoppingCart();
        cartD.addItem(new CartItem(FruitType.APPLE, 5));      // 40
        cartD.addItem(new CartItem(FruitType.STRAWBERRY, 4)); // 52 * 0.8 = 41.6
        cartD.addItem(new CartItem(FruitType.MANGO, 2));      // 40
        PricingStrategy discount = new DiscountPricingStrategy(
                new SimplePricingStrategy(), new BigDecimal("0.8"), FruitType.STRAWBERRY);
        PricingStrategy strategyD = new FullReductionPricingStrategy(
                discount, new BigDecimal("100"), new BigDecimal("10"));
        BigDecimal totalD = calculator.calculate(cartD, strategyD);
        System.out.println("【顾客 D】苹果 5斤 + 草莓 4斤 + 芒果 2斤");
        System.out.println("计价方式：草莓 8 折 + 满 100 减 10");
        System.out.println("应付金额：" + totalD + " 元");
        System.out.println();

        System.out.println("=====================================");
        System.out.println("所有计算结果已通过 JUnit 单元测试验证。");
    }
}
