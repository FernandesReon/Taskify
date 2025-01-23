package com.reonfernandes.Taskify.models;

import lombok.ToString;

@ToString
public enum Status {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    CANCELED,
    PENDING,
    BLOCKED,
    ARCHIVED;
}
