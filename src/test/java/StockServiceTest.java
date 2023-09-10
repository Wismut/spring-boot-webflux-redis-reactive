import com.max.stock_feed_api.stock.Stock;
import com.max.stock_feed_api.stock.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class StockServiceTest {

    @Mock
    private ReactiveRedisTemplate<String, Stock> reactiveRedisStockTemplate;

    @Mock
    private ReactiveValueOperations<String, Stock> reactiveValueOperations;

    private StockService stockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stockService = new StockService(reactiveRedisStockTemplate);
    }

    @Test
    void findByCode_ValidCode_ReturnsStock() {
        String code = "AAPL";
        Stock stock = new Stock(code, "Apple Inc.", 150.0f, "");

        when(reactiveValueOperations.get(anyString())).thenReturn(Mono.just(stock));
        when(reactiveRedisStockTemplate.opsForValue()).thenReturn(reactiveValueOperations);

        Mono<Stock> result = stockService.findByCode(code);

        assertNotNull(result);
        assertEquals(stock, result.block());
    }

    @Test
    void findAll_ReturnsListOfStocks() {
        String keyPattern = StockService.KEY + "*";
        Stock stock1 = new Stock("AAPL", "Apple Inc.", 150.0f, "");
        Stock stock2 = new Stock("GOOG", "Alphabet Inc.", 2500.0f, "");

        when(reactiveRedisStockTemplate.keys(keyPattern)).thenReturn(Flux.just("stock:AAPL", "stock:GOOG"));
        when(reactiveValueOperations.get("stock:AAPL")).thenReturn(Mono.just(stock1));
        when(reactiveValueOperations.get("stock:GOOG")).thenReturn(Mono.just(stock2));
        when(reactiveRedisStockTemplate.opsForValue()).thenReturn(reactiveValueOperations);

        Flux<Stock> result = stockService.findAll();

        assertNotNull(result);
        assertEquals(2, result.collectList().block().size());
        assertTrue(result.collectList().block().contains(stock1));
        assertTrue(result.collectList().block().contains(stock2));
    }

    @Test
    void save_ValidStock_ReturnsSavedStock() {
        var stock = new Stock("AAPL", "Apple Inc.", 150.0f, "");

        when(reactiveValueOperations.set(anyString(), any(Stock.class))).thenReturn(Mono.just(true));
        when(reactiveRedisStockTemplate.opsForValue()).thenReturn(reactiveValueOperations);

        var result = stockService.save(stock);

        assertNotNull(result);
        assertEquals(stock, result.block());
    }

    @Test
    void deleteById_ValidId_ReturnsTrue() {
        Long id = 1L;

        when(reactiveValueOperations.delete(anyString())).thenReturn(Mono.just(true));
        when(reactiveRedisStockTemplate.opsForValue()).thenReturn(reactiveValueOperations);

        Mono<Boolean> result = stockService.deleteById(id);

        assertNotNull(result);
        assertTrue(result.block());
    }
}
