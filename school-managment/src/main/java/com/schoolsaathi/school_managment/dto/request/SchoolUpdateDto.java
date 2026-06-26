package com.schoolsaathi.school_managment.dto.request;



import com.schoolsaathi.school_managment.enums.BoardType;
import com.schoolsaathi.school_managment.enums.SchoolType;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolUpdateDto {

    private String name;
    private String tagline;
    private String phone;
    private String alternatePhone;
    private String website;

    // Address
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String pincode;

    // School Details
    private BoardType boardType;
    private SchoolType schoolType;
    private String medium;
    private String affiliationNumber;
    private String udiseCode;

    // Principal
    private String principalName;
    private String principalPhone;
    private String principalEmail;

    // Bank Details
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private String bankBranch;

    // GST
    private String gstNumber;
    private String panNumber;

    // Branding
    private String logoUrl;
    private String primaryColor;

    // Receipt
    private String receiptPrefix;
    private Integer receiptStartNumber;
}