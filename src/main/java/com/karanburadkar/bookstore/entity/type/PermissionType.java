package com.karanburadkar.bookstore.entity.type;


public enum PermissionType {
    BOOK_READ("book:read"),
    BOOK_WRITE("book:write"),
    BOOK_UPDATE("book:update"),
    BOOK_DELETE("book:delete"),

    BOOK_CATEGORY_READ("book_category:read"),
    BOOK_CATEGORY_WRITE("book_category:write"),
    BOOK_CATEGORY_UPDATE("book_category:update"),
    BOOK_CATEGORY_DELETE("book_category:delete"),

    AUTHOR_READ("author:read"),
    AUTHOR_WRITE("author:write"),
    AUTHOR_UPDATE("author:update"),
    AUTHOR_DELETE("author:delete"),

    PUBLISHER_READ("publisher:read"),
    PUBLISHER_WRITE("publisher:write"),
    PUBLISHER_UPDATE("publisher:update"),
    PUBLISHER_DELETE("publisher:delete"),

    USER_READ("user:read"),
    USER_WRITE("user:write"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete"),

    ROLE_READ("role:read"),
    ROLE_WRITE("role:write"),
    ROLE_UPDATE("role:update"),
    ROLE_DELETE("role:delete"),

    ORDER_READ("order:read"),
    ORDER_WRITE("order:write"),
    ORDER_UPDATE("order:update"),
    ORDER_DELETE("order:delete"),

    ORDER_ITEMS_READ("order_item:read"),
    ORDER_ITEMS_WRITE("order_item:write"),
    ORDER_ITEMS_UPDATE("order_item:update"),
    ORDER_ITEMS_DELETE("order_item:delete"),

    ADDRESS_READ("address:read"),
    ADDRESS_WRITE("address:write"),
    ADDRESS_UPDATE("address:update"),
    ADDRESS_DELETE("address:delete"),

    CUSTOMER_READ("customer:read"),
    CUSTOMER_WRITE("customer:write"),
    CUSTOMER_UPDATE("customer:update"),
    CUSTOMER_DELETE("customer:delete"),

    PAYMENT_READ("payment:read"),
    PAYMENT_WRITE("payment:write"),
    PAYMENT_UPDATE("payment:update"),
    PAYMENT_DELETE("payment:delete"),

    PAYMENT_METHOD_READ("payment_method:read"),
    PAYMENT_METHOD_WRITE("payment_method:write"),
    PAYMENT_METHOD_UPDATE("payment_method:update"),
    PAYMENT_METHOD_DELETE("payment_method:delete"),

    REVIEW_READ("review:read"),
    REVIEW_WRITE("review:write"),
    REVIEW_UPDATE("review:update"),
    REVIEW_DELETE("review:delete");

    private final String permission;

    PermissionType(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
