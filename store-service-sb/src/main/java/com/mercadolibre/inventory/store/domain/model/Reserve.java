package com.mercadolibre.inventory.store.domain.model;

public class Reserve {

    private String sku;
    private String siteId;
    private String reservationId;
    private int qty;


    public Reserve(String sku, String siteId, String reservationId, int qty) {
        this.sku = sku;
        this.siteId = siteId;
        this.reservationId = reservationId;
        this.qty = qty;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
