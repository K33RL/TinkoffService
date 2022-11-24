package com.myproject.tinkoffservice.service;

import com.myproject.tinkoffservice.dto.FigiesDto;
import com.myproject.tinkoffservice.dto.StockPricesDto;
import com.myproject.tinkoffservice.dto.StocksDto;
import com.myproject.tinkoffservice.dto.TickersDto;
import com.myproject.tinkoffservice.exception.StockNotFoundException;
import com.myproject.tinkoffservice.model.Currency;
import com.myproject.tinkoffservice.model.Stock;
import com.myproject.tinkoffservice.model.StockPrice;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.MarketContext;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrument;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;
import ru.tinkoff.invest.openapi.model.rest.Orderbook;

@Slf4j
@Service
@RequiredArgsConstructor
public class TinkoffStockService implements StockService {
    private final OpenApi openApi;

    @Async
    public CompletableFuture<MarketInstrumentList> getMarketInstrumentTicker(String ticker) {
        MarketContext marketContext = openApi.getMarketContext();
        return marketContext.searchMarketInstrumentsByTicker(ticker);
    }

    @Override
    public Stock getStockByTicker(String ticker) {
        List<MarketInstrument> instruments = getMarketInstrumentTicker(ticker).join().getInstruments();

        if (instruments.isEmpty()) {
            throw new StockNotFoundException(String.format("Stock %S not found", ticker));
        }

        MarketInstrument item = instruments.get(0);

        return new Stock(
            item.getTicker(),
            item.getFigi(),
            item.getName(),
            item.getType().getValue(),
            Currency.valueOf(item.getCurrency().getValue()), "tinkoff");
    }

    @Override
    public StocksDto getStocksByTicker(TickersDto tickers) {
        List<CompletableFuture<MarketInstrumentList>> marketInstruments = new ArrayList<>();
        tickers.getTickers().forEach(ticker -> marketInstruments.add(getMarketInstrumentTicker(ticker)));
        List<Stock> stocks = marketInstruments.stream()
            .map(CompletableFuture::join)
            .map(mi -> {
                if (!marketInstruments.isEmpty()) {
                    return mi.getInstruments().get(0);
                }
                return null;
            })
            .filter(el -> Objects.nonNull(el))
            .map(mi -> new Stock(
                mi.getTicker(),
                mi.getFigi(),
                mi.getName(),
                mi.getType().getValue(),
                Currency.valueOf(mi.getCurrency().getValue()), "tinkoff"))
            .collect(Collectors.toList());

        return new StocksDto(stocks);
    }


    @Async
    public CompletableFuture<Optional<Orderbook>> getOrderBookByFigi(String figi) {
        CompletableFuture<Optional<Orderbook>> orderbook = openApi.getMarketContext().getMarketOrderbook(figi, 0);
        log.info("getting price {} from tinkoff", figi);
        return orderbook;
    }

    @Override
    public StockPricesDto getPrices(FigiesDto figiesDto) {
        long start = System.currentTimeMillis();

        List<CompletableFuture<Optional<Orderbook>>> orderbooks = new ArrayList<>();
        figiesDto.getFigies().forEach(figi -> orderbooks.add(getOrderBookByFigi(figi)));
        List<StockPrice> prices = orderbooks.stream().map(CompletableFuture::join)
            .map(ob -> ob.orElseThrow(() -> new StockNotFoundException("Stock not found")))
            .map(orderbook -> new StockPrice(orderbook.getFigi(), orderbook.getLastPrice().doubleValue()))
            .collect(Collectors.toList());

        log.info("Time - {}", System.currentTimeMillis() - start);

        return new StockPricesDto(prices);
    }
}
