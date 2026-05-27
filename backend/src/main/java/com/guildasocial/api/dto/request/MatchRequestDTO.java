package com.guildasocial.api.dto.request;

import java.util.UUID;

public class MatchRequestDTO {
    private UUID alvoId;

    public UUID getAlvoId() {
        return alvoId;
    }

    public void setAlvoId(UUID alvoId) {
        this.alvoId = alvoId;
    }
}
