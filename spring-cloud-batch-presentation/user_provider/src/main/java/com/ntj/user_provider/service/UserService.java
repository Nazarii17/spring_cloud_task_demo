package com.ntj.user_provider.service;

import com.ntj.springcloudbatchcommon.dto.UserRecord;
import com.ntj.springcloudbatchcommon.entity.User;
import com.ntj.user_provider.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.ntj.springcloudbatchcommon.enums.ReadStatus.COMPLETED;
import static com.ntj.springcloudbatchcommon.enums.ReadStatus.NEW;
import static com.ntj.user_provider.util.UserGenerator.FIRST_NAMES;
import static com.ntj.user_provider.util.UserGenerator.LAST_NAMES;


@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public List<UserRecord> getAllAvailableUsers() {
        final List<User> users = userRepository.getAllByReadStatus(NEW);

        if (users.isEmpty()) {
            return generateNewUsers().stream()
                    .map(UserRecord::convert)
                    .collect(Collectors.toList());
        } else {
            return users.stream()
                    .map(UserRecord::convert)
                    .collect(Collectors.toList());
        }
    }

    public List<User> generateNewUsers() {
        final int iteration = userRepository.findAnyWithMaxIteration()
                .map(user -> user.getIteration() + 1)
                .orElse(1);

        final List<User> users = IntStream.range(0, 10)
                .mapToObj(i -> {
                    final String first = FIRST_NAMES[ThreadLocalRandom.current().nextInt(FIRST_NAMES.length)];
                    final String last = LAST_NAMES[ThreadLocalRandom.current().nextInt(LAST_NAMES.length)];
                    final String name = first + " " + last;

                    return  User.builder()
                            .name(name)
                            .readStatus(NEW)
                            .iteration(iteration)
                            .build();
                })
                .collect(Collectors.toList());
        return userRepository.saveAll(users);
    }

    @Transactional
    public void markUsersAsCompleted() {
        final List<User> users = userRepository.getAllByReadStatus(NEW);
        users.forEach(user -> {user.setReadStatus(COMPLETED);});
    }
}
