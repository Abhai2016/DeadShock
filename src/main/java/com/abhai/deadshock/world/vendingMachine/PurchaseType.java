package com.abhai.deadshock.world.vendingMachine;

public enum PurchaseType {
    BIG_SALT(67, 100),
    LITTLE_SALT(19, 25),
    BIG_MEDICINE(36, 80),
    PISTOL_BULLETS(8, 12),
    LITTLE_MEDICINE(14, 20),
    MACHINE_GUN_BULLETS(8, 35);

    public final int price;
    public final int value;

    PurchaseType(int price, int value) {
        this.price = price;
        this.value = value;
    }
}
