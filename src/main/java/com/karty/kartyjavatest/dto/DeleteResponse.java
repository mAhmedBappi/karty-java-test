package com.karty.kartyjavatest.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DeleteResponse {
    private final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private String message;

    public DeleteResponse(String message) {
        this.message = message;
    }
}
