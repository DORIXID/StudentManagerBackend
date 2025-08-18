package dev.vortsu.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse{

    public StudentResponse(List<StudentDTO> items, long total_items, int total_pages, int current_page, int per_page) {
        this.items = items;
        long remainingCount = Math.max(0, total_items - (long) current_page * per_page);
        this.meta = new Meta(total_items, total_pages, current_page, per_page, remainingCount);
    }

    private List<StudentDTO> items;
    private Meta meta;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class Meta {
        private long total_items;
        private int total_pages;
        private int current_page;
        private int per_page;
        private long remaining_count;
    }
}

