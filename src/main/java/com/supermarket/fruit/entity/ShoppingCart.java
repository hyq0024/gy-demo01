package com.supermarket.fruit.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 购物车。
 * <p>聚合顾客购买的所有商品条目，提供计算原价总和等功能。</p>
 */
public class ShoppingCart {

    /**
     * 购物车中的商品条目列表
     */
    private final List<CartItem> items = new ArrayList<>();

    /**
     * 向购物车中添加商品。
     *
     * @param item 购物车条目，不能为 null
     */
    public void addItem(CartItem item) {
        if (item == null) {
            throw new IllegalArgumentException("购物车条目不能为空");
        }
        items.add(item);
    }

    /**
     * 获取购物车中所有商品条目（不可修改视图）。
     *
     * @return 商品条目列表
     */
    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * 计算购物车中所有商品的原价总和。
     *
     * @return 原价总金额
     */
    public BigDecimal getOriginalTotal() {
        return items.stream()
                .map(CartItem::getOriginalSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
