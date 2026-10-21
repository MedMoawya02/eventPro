package com.eventpro.eventproapi.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private String organizerName;
    private List<CategoryResponse> categories;

    @Data
    @Builder
    public static class CategoryResponse {
        private Long id;
        private String categoryName;
        private java.math.BigDecimal unitPrice;
        private int totalQuantity;
        private int availablePlaces;
        private int maxQuotaPerClient;
    }
}