package com.lumastyle.order.entity;

public enum PaymentStatus {
    PENDING,   // čeká na zaplacení
    PAID,      // úspěšně zaplaceno
    FAILED,    // platba selhala
    REFUNDED,  // vráceno
    CANCELLED  // zrušeno platbou či storno
}
