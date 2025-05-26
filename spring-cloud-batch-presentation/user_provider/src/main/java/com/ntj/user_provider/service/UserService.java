package com.ntj.user_provider.service;

import com.ntj.springcloudbatchcommon.dto.UserRecordV1;
import com.ntj.springcloudbatchcommon.dto.UserRecordV2;
import com.ntj.springcloudbatchcommon.entity.User;
import com.ntj.user_provider.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.ntj.springcloudbatchcommon.enums.ReadStatus.COMPLETED;
import static com.ntj.springcloudbatchcommon.enums.ReadStatus.NEW;
import static com.ntj.springcloudbatchcommon.enums.ReadStatus.UNKNOWN;
import static com.ntj.user_provider.util.UserGenerator.FIRST_NAMES;
import static com.ntj.user_provider.util.UserGenerator.LAST_NAMES;


@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public List<UserRecordV1> getAllAvailableUsersV1() {
        final List<User> users = userRepository
                .findAllByReadStatus(Stream.of(NEW, UNKNOWN).map(Enum::name).toList());

        if (users.isEmpty()) {
            return generateNewUsersV1().stream()
                    .map(UserRecordV1::convert)
                    .collect(Collectors.toList());
        } else {
            return users.stream()
                    .map(UserRecordV1::convert)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public List<UserRecordV2> getAllAvailableUsersV2() {
        final List<User> users = userRepository
                .findAllByReadStatus(Stream.of(NEW, UNKNOWN).map(Enum::name).toList());

        if (users.isEmpty()) {
            return generateNewUsersV2().stream()
                    .map(UserRecordV2::convert)
                    .collect(Collectors.toList());
        } else {
            return users.stream()
                    .map(UserRecordV2::convert)
                    .collect(Collectors.toList());
        }
    }

    public List<User> generateNewUsersV1() {
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

    public List<User> generateNewUsersV2() {
        final int iteration = userRepository.findAnyWithMaxIteration()
                .map(user -> user.getIteration() + 1)
                .orElse(1);

        final List<User> users = IntStream.range(0, 10)
                .mapToObj(i -> {
                    final String first = FIRST_NAMES[ThreadLocalRandom.current().nextInt(FIRST_NAMES.length)];
                    final String last = LAST_NAMES[ThreadLocalRandom.current().nextInt(LAST_NAMES.length)];
                    final String name = first + " " + last;

                    User.UserBuilder userBuilder = User.builder()
                            .name(name)
                            .iteration(iteration);

                    if (i == 7) {
                        userBuilder.readStatus(UNKNOWN);
                    } else {
                        userBuilder.readStatus(NEW);
                    }
                    return userBuilder.build();
                })
                .collect(Collectors.toList());
        return userRepository.saveAll(users);
    }

    @Transactional
    public void markUsersAsCompleted() {
        final List<User> users = userRepository
                .findAllByReadStatus(Stream.of(NEW, UNKNOWN).map(Enum::name).toList());
        users.forEach(user -> {user.setReadStatus(COMPLETED);});
    }
}
