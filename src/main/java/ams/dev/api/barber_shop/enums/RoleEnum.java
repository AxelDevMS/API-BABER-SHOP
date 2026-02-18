package ams.dev.api.barber_shop.enums;


import java.util.Set;

public enum RoleEnum {
    ROLE_ADMIN(Set.of(PermissionEnum.PRODUCT_ADD, PermissionEnum.PRODUCT_VIEW, PermissionEnum.PRODUCT_VIEW_ALL)),
    ROLE_STAFF(Set.of(PermissionEnum.PRODUCT_VIEW, PermissionEnum.PRODUCT_VIEW_ALL)),
    ROLE_GUEST(Set.of(PermissionEnum.PRODUCT_VIEW));

    private final Set<PermissionEnum> permissions;

    RoleEnum(Set<PermissionEnum> permissions) {
        this.permissions = permissions;
    }

    public Set<PermissionEnum> getPermissions(){
        return permissions;
    }

}
