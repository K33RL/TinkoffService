package com.myproject.tinkoffservice.dto;

import com.myproject.tinkoffservice.model.StockPrice;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class StockPricesDto {
    private List<StockPrice> prices;
}
