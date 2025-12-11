package com.karanburadkar.bookstore.security;

import com.karanburadkar.bookstore.entity.type.PermissionType;
import com.karanburadkar.bookstore.entity.type.RoleType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.karanburadkar.bookstore.entity.type.PermissionType.*;
import static com.karanburadkar.bookstore.entity.type.RoleType.*;

public class RolePermissionMapping {

    private static final Map<RoleType, Set<PermissionType>> map = Map.of(
            CUSTOMER, Set.of(

                    // JUST READ ONLY
                    BOOK_READ,
                    AUTHOR_READ,
                    PUBLISHER_READ,
                    USER_READ, // SELF
                    ORDER_READ, // SELF
                    ORDER_WRITE, // SELF

                    ADDRESS_READ, // SELF
                    ADDRESS_WRITE, // SELF
                    ADDRESS_UPDATE, // SELF

                    // ONLY SELF ACCOUNT
                    CUSTOMER_READ,
                    CUSTOMER_WRITE,
                    CUSTOMER_UPDATE,
                    CUSTOMER_DELETE,

                    // ONLY SELF PAYMENTS
                    PAYMENT_WRITE,
                    PAYMENT_READ,

                    // ONLY SELF REVIEW
                    REVIEW_READ,
                    REVIEW_WRITE,
                    REVIEW_UPDATE,
                    REVIEW_DELETE
            ),
            PUBLISHER, Set.of(BOOK_READ, BOOK_WRITE, AUTHOR_READ),
            ADMIN, Set.of(BOOK_READ, BOOK_WRITE, AUTHOR_READ, USER_READ),
            MANAGEMENT, Set.of(BOOK_READ, BOOK_WRITE, AUTHOR_READ, USER_READ)
    );

    public static Set<SimpleGrantedAuthority> getAuthoritiesForRole(RoleType roleType) {
        return map.get(roleType).stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
