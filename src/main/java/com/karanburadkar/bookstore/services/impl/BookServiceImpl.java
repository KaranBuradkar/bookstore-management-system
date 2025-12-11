package com.karanburadkar.bookstore.services.impl;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import com.karanburadkar.bookstore.dto.BookRequestDto;
import com.karanburadkar.bookstore.dto.BookCategoryDto;
import com.karanburadkar.bookstore.dto.BookResponseDto;
import com.karanburadkar.bookstore.dto.BookShortResponseDto;
import com.karanburadkar.bookstore.entity.Author;
import com.karanburadkar.bookstore.entity.Book;
import com.karanburadkar.bookstore.entity.BookCategory;
import com.karanburadkar.bookstore.entity.Publisher;
import com.karanburadkar.bookstore.exception.business.BusinessException;
import com.karanburadkar.bookstore.exception.business.DuplicateResourceException;
import com.karanburadkar.bookstore.exception.business.NullForeignKeyNotExceptableException;
import com.karanburadkar.bookstore.exception.database.AuthorNotFoundException;
import com.karanburadkar.bookstore.exception.database.BookNotFoundException;
import com.karanburadkar.bookstore.exception.database.PublisherNotFoundException;
import com.karanburadkar.bookstore.mapper.BookMapper;
import com.karanburadkar.bookstore.repository.AuthorRepository;
import com.karanburadkar.bookstore.repository.BookCategoryRepository;
import com.karanburadkar.bookstore.repository.BookRepository;
import com.karanburadkar.bookstore.repository.PublisherRepository;
import com.karanburadkar.bookstore.services.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);
    private final BookMapper bookMapper;

    private final BookRepository bookRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;

    public BookServiceImpl(BookMapper bookMapper,
                           BookRepository bookRepository,
                           BookCategoryRepository bookCategoryRepository,
                           AuthorRepository authorRepository,
                           PublisherRepository publisherRepository) {

        this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;
        this.bookCategoryRepository = bookCategoryRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }

    @Override
    public List<BookResponseDto> getBooks(Integer page, Integer size, String sort, String direction) {
        Sort.Direction sortDirection =
                "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<Book> list = bookRepository.findBookDetails(pageRequest);

        return list.stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookShortResponseDto> getBooksShort(Integer page, Integer size, String sort, String direction) {
        Sort.Direction sortDirection =
                "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<Book> list = bookRepository.findBookDetails(pageRequest);

        return list.stream()
                .map(bookMapper::toShortResponseDto)
                .toList();
    }

    @Override
    public BookResponseDto getBookById(Long bookId) {
        Book book = resolveBook(bookId);
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional
    public BookResponseDto createBook(@Valid BookRequestDto bookRequestDto) {

        // Validation
        validateNewBookDetail(bookRequestDto);

        // 1. map names -> BookCategory entities
        Set<BookCategory> bookCategorySet = resolveBookCategory(bookRequestDto.getCategories());
        // 2. AuthorIds -> Author Entities
        Set<Author> authors = resolveAuthors(bookRequestDto.getAuthorIds());
        // 3. publisherId -> publisher Entity
        Publisher publisher = resolvePublisher(bookRequestDto.getPublisherId());

        // 4. Merge all for book save
        Book book = bookMapper.toEntity(bookRequestDto);
        book.setCategories(bookCategorySet);
        book.setAuthors(authors);
        book.setPublisher(publisher);

        // 5. Book saved in DB
        Book savedBook = bookRepository.save(book);

        // 6. entity -> book dto and return
        return bookMapper.toDto(savedBook);
    }

    private void validateNewBookDetail(@Valid BookRequestDto bookRequestDto) {
        // 1. Book price must be up to 2 decimal
        int scale = bookRequestDto.getPrice().scale();
        if (scale != 2) throw new IllegalArgumentException("Book price must be up to 2 decimal");

        if (bookRepository.existsByIsbn(bookRequestDto.getIsbn())) {
            throw new com.karanburadkar.bookstore.exception.database.DataIntegrityViolationException(
                    "Isbn book id already exist",
                    bookRequestDto.getIsbn() + ": Book with this ISBN already exists."
            );
        }
    }

    private Book resolveBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    log.warn("Book doesn't exist with bookId: {}", bookId);
                    return new BookNotFoundException(bookId);
                });
    }

    private Publisher resolvePublisher(Long publisherId) {
        return publisherRepository.findById(publisherId)
                .orElseThrow(() -> {
                    log.warn("Publisher not found with Id: {}", publisherId);
                    return new PublisherNotFoundException("Publisher not found with Id: " + publisherId);
                });
    }

    private Set<Author> resolveAuthors(Set<Long> authorIds) {

        if (authorIds.isEmpty()) {
            log.warn("Attempt to create book without any author");
            throw new BusinessException("Attempt to create book without any author");
        }

        Set<Author> authors = new HashSet<>(authorRepository.findAllById(authorIds));
        Set<Long> requestedAuthorIds = new HashSet<>(authorIds);
        Set<Long> foundAuthorIds = authors.stream().map(Author::getId).collect(Collectors.toSet());

        requestedAuthorIds.removeAll(foundAuthorIds);
        if (!requestedAuthorIds.isEmpty()) {
            log.warn("Authors not found with ids: {}", requestedAuthorIds);
            throw new IllegalArgumentException("Authors not found with ids: " + requestedAuthorIds);
        }
        return authors;
    }

    private Set<BookCategory> resolveBookCategory(Set<BookCategoryDto> categories) {
        Set<String> categoryNames = categories.stream()
                .map(BookCategoryDto::getName)
                .collect(Collectors.toSet());

        List<BookCategory> existingCategories = bookCategoryRepository.findByNameIn(categoryNames);

        Map<String, BookCategory> existingMap = existingCategories.stream()
                .collect(Collectors.toMap(BookCategory::getName, Function.identity()));

        return categoryNames.stream()
                .map(name -> existingMap
                        .computeIfAbsent(
                                name, n -> bookCategoryRepository.save(new BookCategory(n))
                        )
                )
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public BookResponseDto updateBook(BookRequestDto bookRequestDto, Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            log.warn("Book doesn't exist with bookId: {}", bookId);
            throw new BookNotFoundException(bookId);
        }
        Book book = bookMapper.toEntity(bookRequestDto);
        book.setId(bookId);

        Book savedBook = bookRepository.save(book);

        return bookMapper.toDto(savedBook);
    }

    @Override
    @Transactional
    public BookResponseDto updateBookSpecifics(Map<String, Object> map, Long bookId) {
        Book book = resolveBook(bookId);

        try {
            map.forEach((key, value) -> {
                switch (key) {
                    case "isbn" -> book.setIsbn((String) value);
                    case "title" -> book.setTitle((String) value);
                    case "description" -> book.setDescription((String) value);
                    case "price" -> book.setPrice(BigDecimal.valueOf((Double) value));
                    case "stock" -> book.setStock((Integer) value);
//                case "authorIds" -> book.setAuthors((Set<Author>) value);
//                case "publisherId" -> book.setPublisher((Publisher) value);
//                case "categories" -> book.setCategories((Set<BookCategory>) value);
                    default -> throw new IllegalArgumentException("Invalid field update book, key: "+key);
                }
            });
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw e;
        }

        return bookMapper.toDto(book);
    }

    @Override
    @Transactional
    public void deleteBookById(Long bookId) {
        Book book = resolveBook(bookId);
        bookRepository.delete(book);
        log.info("Book with id {} successfully deleted", bookId);
    }

    @Override
    @Transactional
    public void deleteAuthorById(Long bookId, Long authorId) {
        removeAuthorById(bookId, authorId);
    }

    @Override
    @Transactional
    public void addAuthorById(Long bookId, Long authorId) {
        addNewAuthorById(bookId, authorId);
    }

    private void addNewAuthorById(Long bookId, Long authorId) {
        Book book = resolveBook(bookId);
        if (book.getAuthors().contains(resolveAuthor(authorId))) {
            log.warn("Already author exist with authorId: {}", authorId);
            throw new BusinessException("Already author exist with authorId: "+authorId);
        }
        book.getAuthors().add(resolveAuthor(authorId));
    }

    @Override
    @Transactional
    public void replaceAuthorById(Long bookId, Long oldAuthorId, Long newAuthorId) {
        addNewAuthorById(bookId, newAuthorId);
        removeAuthorById(bookId, oldAuthorId);
    }

    private void removeAuthorById(Long bookId, Long authorId) {
        Book book = resolveBook(bookId);
        int size = book.getAuthors().size();

        if (size <= 1) {
            throw new NullForeignKeyNotExceptableException("Book without author does not exist", null);
        }

        Author author = resolveAuthor(authorId);

        boolean contains = book.getAuthors().contains(author);

        if (contains) {
            book.getAuthors().remove(author);
        }
    }

    private Author resolveAuthor(Long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> {
                    log.warn("Author not found with authorId: {}", authorId);
                    return new AuthorNotFoundException("Author not found with authorId: "+authorId);
                });
    }
}
