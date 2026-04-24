package com.supermarket.fruit.entity;

import java.math.BigDecimal;

/**
 * 水果类型枚举。
 * <p>定义了超市中所有可售水果的种类及其单价，使用 {@link BigDecimal} 避免浮点数精度问题。</p>
 */
public enum FruitType {

    /**
     * 苹果，单价 8 元/斤
     */
    APPLE(new BigDecimal("8")),

    /**
     * 草莓，单价 13 元/斤
     */
    STRAWBERRY(new BigDecimal("13")),

    /**
     * 芒果，单价 20 元/斤
     */
    MANGO(new BigDecimal("20"));

    /**
     * 水果单价（元/斤）
     */
    private final BigDecimal unitPrice;

    /**
     * 构造方法。
     *
     * @param unitPrice 水果单价，单位：元/斤，必须大于等于 0
     */
    FruitType(BigDecimal unitPrice) {
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("单价必须大于等于 0");
        }
        this.unitPrice = unitPrice;
    }

    /**
     * 获取水果单价。
     *
     * @return 单价（元/斤）
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
}
