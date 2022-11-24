package com.myproject.tinkoffservice.controller;

import com.myproject.tinkoffservice.dto.FigiesDto;
import com.myproject.tinkoffservice.dto.StockPricesDto;
import com.myproject.tinkoffservice.dto.StocksDto;
import com.myproject.tinkoffservice.dto.TickersDto;
import com.myproject.tinkoffservice.model.Stock;
import com.myproject.tinkoffservice.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/stocks/{ticker}")
    public Stock getStock(@PathVariable String ticker) {
        return stockService.getStockByTicker(ticker);
    }

    @PostMapping("/stocks/getStocksByTickers")
    public StocksDto getStocksByTickers(@RequestBody TickersDto tickersDto) {
        return stockService.getStocksByTicker(tickersDto);
    }

    @PostMapping("/prices")
    public StockPricesDto getPrices(@RequestBody FigiesDto figiesDto) {
        return stockService.getPrices(figiesDto);
    }
}
