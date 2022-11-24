package com.myproject.tinkoffservice.dto;

import com.myproject.tinkoffservice.model.Stock;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StocksDto {
    List<Stock> stocks;
}
