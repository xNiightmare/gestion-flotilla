package com.grandedev.gestionflotilla.exception;

import java.time.LocalDateTime;

import lombok.*;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Data

public class CustomErrorResponse {
    private int status;
    private String message;
    private String details;
    private LocalDateTime timestamp;
    
}
