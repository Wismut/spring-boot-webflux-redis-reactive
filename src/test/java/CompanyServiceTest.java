import com.max.stock_feed_api.company.Company;
import com.max.stock_feed_api.company.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class CompanyServiceTest {
    @Mock
    private ReactiveRedisTemplate<String, Company> reactiveRedisCompanyTemplate;

    @Mock
    private ReactiveValueOperations<String, Company> reactiveValueOperations;

    private CompanyService companyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(reactiveRedisCompanyTemplate.opsForValue()).thenReturn(reactiveValueOperations);
        companyService = new CompanyService(reactiveRedisCompanyTemplate);
    }

    @Test
    public void testFindBySymbol() {
        String symbol = "AAPL";
        String key = "company:" + symbol;
        Company company = new Company(symbol, "Apple Inc.");

        when(reactiveRedisCompanyTemplate.opsForValue().get(key))
                .thenReturn(Mono.just(company));

        Mono<Company> result = companyService.findBySymbol(symbol);

        StepVerifier.create(result)
                .expectNext(company)
                .expectComplete()
                .verify();

        verify(reactiveRedisCompanyTemplate.opsForValue(), times(1)).get(key);
        verifyNoMoreInteractions(reactiveValueOperations);
    }

    @Test
    public void testFindAll() {
        String keyPattern = "company:*";
        Company company1 = new Company("AAPL", "Apple Inc.");
        Company company2 = new Company("GOOGL", "Alphabet Inc.");

        when(reactiveRedisCompanyTemplate.keys(keyPattern))
                .thenReturn(Flux.just("company:AAPL", "company:GOOGL"));
        when(reactiveRedisCompanyTemplate.opsForValue().get("company:AAPL"))
                .thenReturn(Mono.just(company1));
        when(reactiveRedisCompanyTemplate.opsForValue().get("company:GOOGL"))
                .thenReturn(Mono.just(company2));

        Flux<Company> result = companyService.findAll();

        StepVerifier.create(result)
                .expectNext(company1, company2)
                .expectComplete()
                .verify();

        verify(reactiveRedisCompanyTemplate, times(1)).keys(keyPattern);
        verify(reactiveValueOperations, times(1)).get("company:AAPL");
        verify(reactiveValueOperations, times(1)).get("company:GOOGL");
        verifyNoMoreInteractions(reactiveValueOperations);
    }

    @Test
    public void testSave() {
        Company company = new Company("AAPL", "Apple Inc.");

        when(reactiveValueOperations.set(any(), eq(company)))
                .thenReturn(Mono.just(true));

        Mono<Company> result = companyService.save(company);

        StepVerifier.create(result)
                .expectNext(company)
                .expectComplete()
                .verify();

        verify(reactiveRedisCompanyTemplate.opsForValue(), times(1)).set(any(), eq(company));
        verifyNoMoreInteractions(reactiveValueOperations);
    }

    @Test
    public void testDeleteById() {
        Long companyId = 1L;
        String key = "company:" + companyId;

        when(reactiveRedisCompanyTemplate.opsForValue().delete(key))
                .thenReturn(Mono.just(true));

        Mono<Boolean> result = companyService.deleteById(companyId);

        StepVerifier.create(result)
                .expectNext(true)
                .expectComplete()
                .verify();

        verify(reactiveRedisCompanyTemplate.opsForValue(), times(1)).delete(key);
        verifyNoMoreInteractions(reactiveValueOperations);
    }

    @Test
    public void testFindBySymbol2() {
        String symbol = "AAPL";
        String key = "company:" + symbol;
        Company company = new Company(symbol, "Apple Inc.");

        when(reactiveValueOperations.get(key))
                .thenReturn(Mono.just(company));

        Mono<Company> result = companyService.findBySymbol(symbol);

        StepVerifier.create(result)
                .expectNext(company)
                .expectComplete()
                .verify();

        verify(reactiveValueOperations, times(1)).get(key);
        verifyNoMoreInteractions(reactiveValueOperations);
    }
}
