package pos.machine;

import java.util.*;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        Map<String, List<Item>> itemInfo = getItemInfo(barcodes);
        return null;
    }

    private Map<String, List<Item>> getItemInfo(List<String> barcodes){
        List<Item> items = ItemsLoader.loadAllItems();
        Map<String, List<Item>> itemsCache = new HashMap<>();
        barcodes.forEach(barcode -> {
            filterAndMapItems(items, barcode, itemsCache);
        });
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
}
