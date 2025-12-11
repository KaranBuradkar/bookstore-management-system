package com.karanburadkar.bookstore.services.impl;

import com.karanburadkar.bookstore.exception.database.DataIntegrityViolationException;
import jakarta.transaction.Transactional;
import com.karanburadkar.bookstore.dto.AuthorDto;
import com.karanburadkar.bookstore.entity.Author;
import com.karanburadkar.bookstore.exception.database.AuthorNotFoundException;
import com.karanburadkar.bookstore.mapper.AuthorMapper;
import com.karanburadkar.bookstore.repository.AuthorRepository;
import com.karanburadkar.bookstore.services.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private static final Logger log = LoggerFactory.getLogger(AuthorServiceImpl.class);
    private final AuthorMapper authorMapper;
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorMapper authorMapper, AuthorRepository authorRepository) {
        this.authorMapper = authorMapper;
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorDto getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Author Not found with Id: {}", id);
                    return new AuthorNotFoundException("Author Not found with Id: "+id);
                });
        return authorMapper.toDto(author);
    }

    @Override
    public List<AuthorDto> getAuthors(Integer page, Integer size, String sort, String direction, Long bookId) {

        Sort.Direction dir =
                "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Page<Author> authors = (bookId == null) ? authorRepository.findAll(PageRequest.of(page, size, dir, sort))
                : authorRepository.findAuthorsByBookId(bookId, PageRequest.of(page, size, dir, sort));

        return authors.stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public AuthorDto createAuthor(AuthorDto authorDto) {
        if (authorDto.getId() != null) {
            log.error("Author must have NULL id during create.");
            throw new IllegalArgumentException("AuthorId must be Null.");
        }
        Author author = authorMapper.toEntity(authorDto);
        if (authorRepository.existsByName(author.getName())) {
            throw new DataIntegrityViolationException("Author already exist", author.getName() + ": author already exist");
        }
        Author savedAuthor = authorRepository.save(author);
        log.info("New Author created.");
        return authorMapper.toDto(savedAuthor);
    }

    @Override
    @Transactional
    public AuthorDto updateAuthor(AuthorDto authorDto, Long authorId) {
        if (!authorRepository.existsById(authorId)) {
            log.error("Author Not found with Id: {}", authorId);
            throw new AuthorNotFoundException("Author Not found with Id: "+authorId);
        }

        Author author = authorMapper.toEntity(authorDto);
        author.setId(authorId);

        Author updatedAuthor = authorRepository.save(author);

        return authorMapper.toDto(updatedAuthor);
    }

    @Override
    @Transactional
    public AuthorDto updateAuthorSpecific(Map<String, Object> map, Long authorId) {

        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> {
                    log.error("Author Not found.");
                    return new AuthorNotFoundException("Author Not found with Id: "+authorId);
                });

        map.forEach((key, value) -> {
            switch (key) {
                case "name" -> author.setName((String) value);
                case "bio" -> author.setBio((String) value);
                default -> throw new IllegalArgumentException("Invalid data update key: '"+key +"' ");
            }
        });

        return authorMapper.toDto(author);
    }

    @Override
    @Transactional
    public void deleteAuthorById(Long authorId) {
        if (authorRepository.existsById(authorId)) {
            authorRepository.deleteById(authorId);
        } else {
            log.error("Author Not found with Id: {}", authorId);
            throw new AuthorNotFoundException("Author Not found with Id: "+authorId);
        }
        log.info("Author deleted successfully.");
    }
}
