package com.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodDTO {
    @JsonProperty("name")
    private String name;
    @JsonProperty("delivery_date")
    private String delivery_date;
}