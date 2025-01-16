package com.reonfernandes.Taskify.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    @NotBlank(message = "Sender's email is required.")
    private String recipient;
    @NotBlank(message = "Subject of email is required.")
    private String subject;
    @NotBlank(message = "Please compose message.")
    private String message;
}
