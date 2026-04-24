package com.supermarket.fruit.entity;

import java.math.BigDecimal;

/**
 * 购物车条目。
 * <p>表示顾客购买的某一种水果及其重量。</p>
 */
public class CartItem {

    /**
     * 水果类型
     */
    private final FruitType fruitType;

    /**
     * 购买重量（斤），必须大于等于 0
     */
    private final int weight;

    /**
     * 构造购物车条目。
     *
     * @param fruitType 水果类型，不能为 null
     * @param weight    购买重量（斤），必须大于等于 0
     */
    public CartItem(FruitType fruitType, int weight) {
        if (fruitType == null) {
            throw new IllegalArgumentException("水果类型不能为空");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("购买重量不能为负数");
        }
        this.fruitType = fruitType;
        this.weight = weight;
    }

    /**
     * 获取水果类型。
     *
     * @return 水果类型
     */
    public FruitType getFruitType() {
        return fruitType;
    }

    /**
     * 获取购买重量。
     *
     * @return 重量（斤）
     */
    public int getWeight() {
        return weight;
    }

    /**
     * 计算该条目按原价应支付的总金额。
     *
     * @return 原价总金额
     */
    public BigDecimal getOriginalSubtotal() {
        return fruitType.getUnitPrice().multiply(BigDecimal.valueOf(weight));
    }
}
