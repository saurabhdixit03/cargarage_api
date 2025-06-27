package com.example.cargarage.dto;

import java.util.List;

public class RepeatCustomerDiscountDTO {
    private Long discountId;
    private List<Long> repeatCustomerIds;

    public RepeatCustomerDiscountDTO() {}

    public RepeatCustomerDiscountDTO(Long discountId, List<Long> repeatCustomerIds) {
        this.discountId = discountId;
        this.repeatCustomerIds = repeatCustomerIds;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public List<Long> getRepeatCustomerIds() {
        return repeatCustomerIds;
    }

    public void setRepeatCustomerIds(List<Long> repeatCustomerIds) {
        this.repeatCustomerIds = repeatCustomerIds;
    }
}
