package com.mercadolibre.inventory.store.domain.model;

import java.util.List;

public class InventoryCachePageable {
    private final List<InventoryCache> items;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;

    public InventoryCachePageable(List<InventoryCache> items, int page, int size, long totalElements, int totalPages) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<InventoryCache> getItems() { return items; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
}

