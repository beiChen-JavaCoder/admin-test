package com.admin.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Xqf
 * @version 1.0
 */
@Data
public class BingUserMerchantDto {
    @NotNull
    Long userId;
    @NotNull
    Long merchantId;

}
