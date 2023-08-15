package pos.machine;

import java.util.*;
import java.util.stream.Collectors;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        Map<String, List<Item>> itemInfo = getItemInfo(barcodes);
        return buildReceipt(itemInfo);
    }

    private Map<String, List<Item>> getItemInfo(List<String> barcodes){
        List<Item> items = ItemsLoader.loadAllItems();
        Map<String, List<Item>> itemsCache = new HashMap<>();
        barcodes.forEach(barcode -> filterAndMapItems(items, barcode, itemsCache));
        return itemsCache;
    }

    private void filterAndMapItems(List<Item> items, String barcode, Map<String, List<Item>> itemsCache){
        Optional<Item> filteredItem = items.stream().filter(item -> item.getBarcode().equals(barcode)).findFirst();
        if(filteredItem.isPresent()) {
            List<Item> finalFilteredItems = itemsCache.get(barcode);
            if (finalFilteredItems == null) {
                finalFilteredItems = new ArrayList<>();
                finalFilteredItems.add(filteredItem.get());
            } else {
                finalFilteredItems.add(filteredItem.get());
            }
            itemsCache.put(barcode, finalFilteredItems);
        }
    }

    private String buildReceipt(Map<String, List<Item>> itemInfo) {
        List<String> keys = itemInfo.keySet().stream().sorted().collect(Collectors.toList());
        StringBuilder receiptOutput = new StringBuilder("***<store earning no money>Receipt***\n");
        int total = 0;
        for(String key : keys){
            List<Item> finalItems = itemInfo.get(key);
            int qty = finalItems.size();
            Item finalItem = finalItems.get(0);
            int unitPrice = finalItem.getPrice();
            int subtotal = calculateSubtotal(qty, unitPrice);
            total += subtotal;
            buildItemDetails(receiptOutput, qty, finalItem, unitPrice, subtotal);
        }
        receiptOutput.append("----------------------\n")
                .append("Total: ").append(total).append(" (yuan)\n")
                .append("**********************");
        return receiptOutput.toString();
    }

    private int calculateSubtotal(int qty, int unitPrice) {
        return qty * unitPrice;
    }
    
    private void buildItemDetails(StringBuilder receiptOutput, int qty, Item finalItem, int unitPrice, int subtotal) {
        receiptOutput.append("Name: ").append(finalItem.getName()).append(", Quantity: ").append(qty)
                .append(", Unit price: ").append(unitPrice).append(" (yuan), ").append("Subtotal: ")
                .append(subtotal).append(" (yuan)\n");
    }
}
