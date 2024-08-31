package org.example;


import java.time.LocalDateTime;
import java.util.Map;

public class PurchaseHistory {
    private LocalDateTime purchaseTime;
    private Map<String, Integer> purchasedItems;

    public PurchaseHistory(LocalDateTime purchaseTime, Map<String, Integer> purchasedItems) {
        this.purchaseTime = purchaseTime;
        this.purchasedItems = purchasedItems;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public Map<String, Integer> getPurchasedItems() {
        return purchasedItems;
    }

    @Override
    public String toString() {
        StringBuilder history = new StringBuilder();
        history.append("Purchase Time: ").append(purchaseTime.toString()).append("\n");
        for (Map.Entry<String, Integer> entry : purchasedItems.entrySet()) {
            history.append("Product ID: ").append(entry.getKey())
                    .append(", Quantity: ").append(entry.getValue()).append("\n");
        }
        return history.toString();
    }
}
