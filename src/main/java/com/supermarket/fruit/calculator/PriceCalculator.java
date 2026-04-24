package com.supermarket.fruit.calculator;

import com.supermarket.fruit.entity.ShoppingCart;
import com.supermarket.fruit.strategy.PricingStrategy;

import java.math.BigDecimal;

/**
 * 价格计算器。
 * <p>作为策略模式的外部调用入口，封装了“购物车 + 计价策略 = 最终价格”的计算流程。</p>
 */
public class PriceCalculator {

    /**
     * 根据给定的计价策略计算购物车应付金额。
     *
     * @param cart     购物车，不能为 null
     * @param strategy 计价策略，不能为 null
     * @return 最终应付金额
     */
    public BigDecimal calculate(ShoppingCart cart, PricingStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("计价策略不能为空");
        }
        return strategy.calculate(cart);
    }
}
