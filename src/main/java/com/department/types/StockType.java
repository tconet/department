package com.department.types;

public enum StockType {

    STOCK("ST"), FII("FI") ;

    private String code;

    private StockType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
