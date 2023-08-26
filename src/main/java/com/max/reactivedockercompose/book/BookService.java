package com.max.reactivedockercompose.book;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class BookService {
//    private final BookRepository bookRepository;
    private final ReactiveRedisTemplate<String, Book> reactiveRedisTemplate;
    private static final String KEY = "book:";

    public BookService(ReactiveRedisTemplate<String, Book> reactiveRedisTemplate) {
//        this.bookRepository = bookRepository;
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    public Mono<Book> findById(Long id) {
//        String key = KEY + id;
//        return reactiveRedisTemplate.opsForValue().get(key)
//                .switchIfEmpty(bookRepository.findById(id)
//                        .flatMap(book -> reactiveRedisTemplate.opsForValue()
//                                .set(key, book, Duration.ofMinutes(5))
//                                .thenReturn(book))
//                );
        return null;
    }

    public Flux<Book> findAll() {
//        return reactiveRedisTemplate.keys("book:*")
//                .flatMap(key -> reactiveRedisTemplate.opsForValue().get(key))
//                .switchIfEmpty(bookRepository.findAll()
//                        .flatMap(book -> reactiveRedisTemplate.opsForValue()
//                                .set(KEY + book.id(), book, Duration.ofMinutes(5)))
//                        .thenMany(reactiveRedisTemplate.keys(KEY + "*")
//                                .flatMap(key -> reactiveRedisTemplate.opsForValue().get(key)))
//                );
        return null;
    }

    public Mono<Book> save(Book book) {
        return null;
//        return bookRepository.save(book)
//                .flatMap(savedBook -> reactiveRedisTemplate.opsForValue()
//                        .set(KEY + savedBook.id(), savedBook, Duration.ofMinutes(5)).thenReturn(savedBook));
    }

    public Mono<Boolean> deleteById(Long id) {
        return null;
//        return bookRepository.deleteById(id)
//                .then(reactiveRedisTemplate.opsForValue().delete(KEY + id));
    }
}
