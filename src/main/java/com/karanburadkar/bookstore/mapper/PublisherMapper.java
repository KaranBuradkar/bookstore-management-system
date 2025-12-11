package com.karanburadkar.bookstore.mapper;

import com.karanburadkar.bookstore.dto.PublisherDto;
import com.karanburadkar.bookstore.entity.Publisher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PublisherMapper {
    Publisher toEntity(PublisherDto publisherDto);

    @Mapping(source = "user.id", target = "userId")
    PublisherDto toDto(Publisher publisher);
}
