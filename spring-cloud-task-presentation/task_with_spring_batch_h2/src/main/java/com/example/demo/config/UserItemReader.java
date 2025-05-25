package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

import java.util.Iterator;
import java.util.List;

@Slf4j
public class UserItemReader implements ItemReader<String> {

    private final Iterator<String> userListIterator;

    public UserItemReader(final List<String> userList) {
        this.userListIterator = userList.iterator();
    }

    @Override
    public String read() {
        if (this.userListIterator.hasNext()) {
            log.info("🔍 Requesting user from the user_service API... 🚀");

            // Fetch the user
            final String user = this.userListIterator.next();

            // Log the user being fetched
            log.info("🎉 User found: [{}] 🎉", user);
            log.info("✅ User successfully fetched from the user_service API. Ready for processing! 🛠️");

            return user;
        } else {
            log.info("⚠️ No more users available to process. Ending user-fetching task. 🚫");
        }
        return null;
    }
}
