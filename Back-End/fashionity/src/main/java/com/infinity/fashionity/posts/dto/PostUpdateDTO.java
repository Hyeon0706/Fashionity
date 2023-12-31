package com.infinity.fashionity.posts.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class PostUpdateDTO {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{

        @JsonIgnore
        private long memberSeq;

        @JsonIgnore
        private long postSeq;

        @Size(max = 500, message = "500자까지만 입력 가능합니다.")
        private String content;

        @Builder.Default
        private List<String> hashtag = new ArrayList<>();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private boolean success;
    }
}
