package com.supermarket.fruit.strategy;

import com.supermarket.fruit.entity.ShoppingCart;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 满减计价策略（装饰器）。
 * <p>先通过基础策略计算出金额，再按“满多少减多少”的规则进行减免。</p>
 * <p>适用于顾客 D 的促销活动：满 100 减 10。</p>
 * <p>支持扩展：可配置任意满减阈值和减免金额。</p>
 */
public class FullReductionPricingStrategy implements PricingStrategy {

    /**
     * 基础计价策略
     */
    private final PricingStrategy baseStrategy;

    /**
     * 满减阈值，达到此金额才触发减免
     */
    private final BigDecimal threshold;

    /**
     * 满足条件后减免的金额
     */
    private final BigDecimal reductionAmount;

    /**
     * 构造满减策略。
     *
     * @param baseStrategy    基础计价策略，不能为 null
     * @param threshold       满减阈值，必须大于 0
     * @param reductionAmount 减免金额，必须大于 0 且小于等于 threshold
     */
    public FullReductionPricingStrategy(PricingStrategy baseStrategy,
                                        BigDecimal threshold,
                                        BigDecimal reductionAmount) {
        if (baseStrategy == null) {
            throw new IllegalArgumentException("基础计价策略不能为空");
        }
        if (threshold == null || threshold.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("满减阈值必须大于 0");
        }
        if (reductionAmount == null
                || reductionAmount.compareTo(BigDecimal.ZERO) <= 0
                || reductionAmount.compareTo(threshold) > 0) {
            throw new IllegalArgumentException("减免金额必须大于 0 且小于等于阈值");
        }
        this.baseStrategy = baseStrategy;
        this.threshold = threshold;
        this.reductionAmount = reductionAmount;
    }

    @Override
    public BigDecimal calculate(ShoppingCart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("购物车不能为空");
        }

        BigDecimal baseAmount = baseStrategy.calculate(cart);

        if (baseAmount.compareTo(threshold) >= 0) {
            baseAmount = baseAmount.subtract(reductionAmount);
        }

        // 确保金额不会减到负数，并统一保留两位小数
        return baseAmount.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }
}
