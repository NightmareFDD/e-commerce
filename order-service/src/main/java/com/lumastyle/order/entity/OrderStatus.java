package com.lumastyle.order.entity;

public enum OrderStatus {
    PENDING,    // objednávka vytvořena, čeká na další zpracování
    CONFIRMED,  // potvrzena (např. rezervace skladu proběhla)
    SHIPPED,    // odeslána zákazníkovi
    CANCELLED   // zrušena
}