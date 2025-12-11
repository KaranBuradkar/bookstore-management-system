package com.karanburadkar.bookstore.services.impl;

import jakarta.transaction.Transactional;
import com.karanburadkar.bookstore.dto.PublisherDto;
import com.karanburadkar.bookstore.entity.Publisher;
import com.karanburadkar.bookstore.exception.database.BookNotFoundException;
import com.karanburadkar.bookstore.exception.database.PublisherNotFoundException;
import com.karanburadkar.bookstore.mapper.PublisherMapper;
import com.karanburadkar.bookstore.repository.PublisherRepository;
import com.karanburadkar.bookstore.services.PublisherService;
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
public class PublisherServiceImpl implements PublisherService {

    private static final Logger log = LoggerFactory.getLogger(PublisherServiceImpl.class);

    private final PublisherMapper publisherMapper;
    private final PublisherRepository publisherRepository;

    public PublisherServiceImpl(PublisherMapper publisherMapper,
                                PublisherRepository publisherRepository) {
        this.publisherMapper = publisherMapper;
        this.publisherRepository = publisherRepository;
    }

    @Override
    public List<PublisherDto> getPublishers(Integer page, Integer size, String sort, String direction) {
        Sort.Direction dir = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        Page<Publisher> publishers = publisherRepository.findAll(PageRequest.of(page, size, dir, sort));

        return publishers.stream()
                .map(publisherMapper::toDto)
                .toList();
    }

    @Override
    public PublisherDto getPublisherById(Long publisherId) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> {
                    log.error("Publisher Not found with Id: {}", publisherId);
                    return new PublisherNotFoundException("Publisher Not found with Id: " + publisherId);
                });
        return publisherMapper.toDto(publisher);
    }

    @Override
    @Transactional
    public PublisherDto createPublisher(PublisherDto publisherDto) {
        if (publisherDto.getId() != null) {
            log.error("Publisher must have NULL id during create.");
            throw new IllegalArgumentException("PublisherId must be Null.");
        }
        Publisher publisher = publisherMapper.toEntity(publisherDto);
        Publisher savedPublisher = publisherRepository.save(publisher);
        log.info("New Publisher created.");
        return publisherMapper.toDto(savedPublisher);
    }

    @Override
    @Transactional
    public PublisherDto updatePublisher(PublisherDto publisherDto, Long publisherId) {
        if (!publisherRepository.existsById(publisherId)) {
            log.error("Publisher Not found");
            throw new PublisherNotFoundException("Publisher Not found with Id: " + publisherId);
        }

        Publisher publisher = publisherMapper.toEntity(publisherDto);
        publisher.setId(publisherId);

        Publisher updatedPublisher = publisherRepository.save(publisher);
        return publisherMapper.toDto(updatedPublisher);
    }

    @Override
    @Transactional
    public PublisherDto updatePublisherSpecific(Map<String, Object> map, Long publisherId) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> {
                    log.error("Publisher Not found.");
                    return new PublisherNotFoundException("Publisher Not found with Id: " + publisherId);
                });

        map.forEach((key, value) -> {
            switch (key) {
                case "name" -> publisher.setName((String) value);
                case "phoneNo" -> publisher.setPhoneNo((String) value);
                default -> throw new IllegalArgumentException("Invalid data attempt to update : " + key);
            }
        });
        Publisher updated = publisherRepository.save(publisher);

        return publisherMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deletePublisherById(Long publisherId) {
        if (publisherRepository.existsById(publisherId)) publisherRepository.deleteById(publisherId);
        else {
            String message = "Publisher not found.";
            log.error(message);
            throw new BookNotFoundException(message+" with bookId: "+publisherId);
        }
        log.info("Publisher deleted successfully");
    }
}
