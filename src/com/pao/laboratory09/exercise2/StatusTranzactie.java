package com.pao.laboratory09.exercise2;

public enum StatusTranzactie {
    PENDING((byte) 0),
    PROCESSED((byte) 1),
    REJECTED((byte) 2);

    private final byte code;

    StatusTranzactie(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static StatusTranzactie fromByte(byte b) {
        return switch (b) {
            case 0 -> PENDING;
            case 1 -> PROCESSED;
            case 2 -> REJECTED;
            default -> throw new IllegalArgumentException("Status invalid: " + b);
        };
    }
}