package com.supermarket.fruit.strategy;

import com.supermarket.fruit.entity.ShoppingCart;

import java.math.BigDecimal;

/**
 * 计价策略接口。
 * <p>策略模式的核心接口，定义了根据购物车计算最终价格的规范。</p>
 * <p>不同的促销活动可以通过实现此接口来扩展，例如打折、满减等。</p>
 */
@FunctionalInterface
public interface PricingStrategy {

    /**
     * 根据购物车内容计算最终应支付金额。
     *
     * @param cart 购物车，包含顾客购买的所有商品，不能为 null
     * @return 最终应支付金额，单位：元
     */
    BigDecimal calculate(ShoppingCart cart);
}
