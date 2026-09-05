package com.mycompany.tmdd_java.dto;

import com.mycompany.tmdd_java.entity.Shop;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class CartDto {
    private List<CartItemDto> items = new ArrayList<>();

    public CartDto() {
    }

    public List<CartItemDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemDto> items) {
        this.items = items;
    }

    public void addItem(CartItemDto newItem) {
        for (CartItemDto item : items) {
            if (item.getProduct().getId().equals(newItem.getProduct().getId())) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                return;
            }
        }
        items.add(newItem);
    }

    public void updateQuantity(Long productId, int quantity) {
        for (CartItemDto item : items) {
            if (item.getProduct().getId().equals(productId)) {
                if (quantity <= 0) {
                    items.remove(item);
                } else {
                    item.setQuantity(quantity);
                }
                return;
            }
        }
    }

    public void removeItem(Long productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public void clear() {
        items.clear();
    }

    public int getTotalQuantity() {
        return items.stream().mapToInt(CartItemDto::getQuantity).sum();
    }

    public BigDecimal getTotalAmount() {
        return items.stream()
                .map(CartItemDto::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Group cart items by Vendor Shop for multi-vendor layout
    public Map<Shop, List<CartItemDto>> getItemsGroupedByShop() {
        Map<Shop, List<CartItemDto>> grouped = new LinkedHashMap<>();
        for (CartItemDto item : items) {
            Shop shop = item.getProduct().getShop();
            grouped.computeIfAbsent(shop, k -> new ArrayList<>()).add(item);
        }
        return grouped;
    }
}
