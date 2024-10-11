package dev.sy.electroneon.user;

public enum Role {
    ADMIN("admin"),
    CUSTOMER("customer"),
    GUEST("guest");

    private final String code;

    Role(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
