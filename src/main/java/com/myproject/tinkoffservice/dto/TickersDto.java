package com.myproject.tinkoffservice.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TickersDto {
    private List<String> tickers;
}
