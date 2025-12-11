package com.karanburadkar.bookstore.mapper;

import com.karanburadkar.bookstore.dto.UserDto;
import com.karanburadkar.bookstore.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toEntity(UserDto userDto);
    UserDto toDto(User user);
}
