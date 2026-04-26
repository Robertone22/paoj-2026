package com.pao.proiect.arenabid.exception;

public class ClosedAuctionException extends RuntimeException {
    public ClosedAuctionException(String message) {
        super(message);
    }
}