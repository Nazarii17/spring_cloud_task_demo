package com.ntj.file_batch_job.processor;

import com.ntj.file_batch_job.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(final Person person) {
        final String firstName = person.getFirstName().toUpperCase();
        final String lastName = person.getLastName().toUpperCase();

        final Person transformedPerson = new Person(firstName, lastName, person.getEmail(), person.getRegistrationState());

        log.info("Converting ({}) into ({})", person, transformedPerson);

        return transformedPerson;
    }
}
