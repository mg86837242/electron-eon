package dev.sy.electroneon.product;

public enum Category {
    LAPTOP("laptop"),
    SMARTPHONE("smartphone"),
    COMPUTER_ACCESSORY("computer-accessory");

    private final String code;

    Category(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}