package com.stock.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity()
@Table(name  = "stockinfo")
public class StockInfo {

    @Id
    @Column(name = "stock_no")
    private Integer stockNo;

    private String name;

    @Column(name = "listing_date")
    private Date listingDate;

    @Column(name = "total_share")
    private Float totalShare;

    @Column(name = "circulation_share")
    private Float circulationShare;

    public Integer getStockNo() {
        return stockNo;
    }

    public void setStockNo(Integer stockNo) {
        this.stockNo = stockNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getListingDate() {
        return listingDate;
    }

    public void setListingDate(Date listingDate) {
        this.listingDate = listingDate;
    }

    public Float getTotalShare() {
        return totalShare;
    }

    public void setTotalShare(Float totalShare) {
        this.totalShare = totalShare;
    }

    public Float getCirculationShare() {
        return circulationShare;
    }

    public void setCirculationShare(Float circulationShare) {
        this.circulationShare = circulationShare;
    }
}
