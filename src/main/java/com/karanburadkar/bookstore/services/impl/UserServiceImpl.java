package com.karanburadkar.bookstore.services.impl;

import jakarta.transaction.Transactional;
import com.karanburadkar.bookstore.dto.UserDto;
import com.karanburadkar.bookstore.entity.User;
import com.karanburadkar.bookstore.exception.database.EntityNotFoundException;
import com.karanburadkar.bookstore.exception.database.UserNotFoundException;
import com.karanburadkar.bookstore.mapper.UserMapper;
import com.karanburadkar.bookstore.repository.UserRepository;
import com.karanburadkar.bookstore.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with Id: {}", userId);
                    return new UserNotFoundException("User does not exist signup please");
                });
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getUsers(Integer page, Integer size, String sort, String direction) {
        Sort.Direction sortDirection =
                "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Page<User> users = userRepository.findAll(PageRequest.of(page, size, Sort.by(sortDirection, sort)));

        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        if (userRepository.existsById(userId)) userRepository.deleteById(userId);
        else {
            String message = "Invalid userId provided for deletion of Review";
            log.error(message);
            throw new EntityNotFoundException(message+", userId: "+userId);
        }
    }
}
