package com.schoolsaathi.school_managment.dto.request;



import com.schoolsaathi.school_managment.enums.Relation;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentDto {

    @NotBlank(message = "Parent name required")
    private String name;

    @NotBlank(message = "Phone required")
    @Pattern(regexp = "^[6-9]\\d{9}$",
            message = "Invalid mobile number")
    private String phone;

    private String alternatePhone;
    private String email;

    @NotNull(message = "Relation required")
    private Relation relation;        // FATHER, MOTHER

    private String occupation;
    private String annualIncome;
    private String whatsappNumber;
    private Boolean isWhatsappActive = true;
    private Boolean isPrimary = false;
}