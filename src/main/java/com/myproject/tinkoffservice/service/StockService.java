package com.myproject.tinkoffservice.service;

import com.myproject.tinkoffservice.dto.FigiesDto;
import com.myproject.tinkoffservice.dto.StockPricesDto;
import com.myproject.tinkoffservice.dto.StocksDto;
import com.myproject.tinkoffservice.dto.TickersDto;
import com.myproject.tinkoffservice.model.Stock;

public interface StockService {
    Stock getStockByTicker(String ticker);

    StocksDto getStocksByTicker(TickersDto tickers);
    StockPricesDto getPrices(FigiesDto figiesDto);
}
