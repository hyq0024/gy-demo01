package com.supermarket.fruit.strategy;

import com.supermarket.fruit.entity.CartItem;
import com.supermarket.fruit.entity.FruitType;
import com.supermarket.fruit.entity.ShoppingCart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 打折计价策略（装饰器）。
 * <p>对指定的水果类型按折扣率打折，其余水果保持原价。</p>
 * <p>该策略包装另一个 {@link PricingStrategy} 作为基础价格来源，
 * 但通常传入 {@link SimplePricingStrategy} 以在原价基础上直接打折。</p>
 * <p>支持扩展：可配置任意水果及折扣率，也可同时配置多种水果打折。</p>
 */
public class DiscountPricingStrategy implements PricingStrategy {

    /**
     * 基础计价策略
     */
    private final PricingStrategy baseStrategy;

    /**
     * 需要打折的水果类型集合
     */
    private final Set<FruitType> discountedFruits;

    /**
     * 折扣率，例如 0.8 表示 8 折
     */
    private final BigDecimal discountRate;

    /**
     * 构造打折策略。
     *
     * @param baseStrategy     基础计价策略，不能为 null
     * @param discountRate     折扣率，必须在 (0, 1] 范围内，例如 0.8 表示 8 折
     * @param discountedFruits 需要打折的水果类型，不能为空
     */
    public DiscountPricingStrategy(PricingStrategy baseStrategy,
                                   BigDecimal discountRate,
                                   FruitType... discountedFruits) {
        if (baseStrategy == null) {
            throw new IllegalArgumentException("基础计价策略不能为空");
        }
        if (discountRate == null
                || discountRate.compareTo(BigDecimal.ZERO) <= 0
                || discountRate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("折扣率必须在 (0, 1] 范围内");
        }
        if (discountedFruits == null || discountedFruits.length == 0) {
            throw new IllegalArgumentException("至少指定一种需要打折的水果");
        }
        this.baseStrategy = baseStrategy;
        this.discountRate = discountRate;
        this.discountedFruits = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(discountedFruits)));
    }

    @Override
    public BigDecimal calculate(ShoppingCart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("购物车不能为空");
        }

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            BigDecimal subtotal = item.getOriginalSubtotal();
            if (discountedFruits.contains(item.getFruitType())) {
                subtotal = subtotal.multiply(discountRate)
                        .setScale(2, RoundingMode.HALF_UP);
            }
            total = total.add(subtotal);
        }
        return total;
    }
}
