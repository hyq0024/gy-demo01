package com.supermarket.fruit.strategy;

import com.supermarket.fruit.entity.ShoppingCart;

import java.math.BigDecimal;

/**
 * 简单计价策略。
 * <p>按各水果的原价乘以重量求和，不做任何折扣或满减。</p>
 * <p>适用于顾客 A（苹果+草莓）和顾客 B（苹果+草莓+芒果）的原价场景。</p>
 */
public class SimplePricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculate(ShoppingCart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("购物车不能为空");
        }
        return cart.getOriginalTotal();
    }
}
