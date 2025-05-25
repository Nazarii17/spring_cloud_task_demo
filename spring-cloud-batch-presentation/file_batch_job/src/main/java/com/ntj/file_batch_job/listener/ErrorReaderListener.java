package com.ntj.file_batch_job.listener;

import com.ntj.file_batch_job.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ErrorReaderListener implements ItemReadListener<Person>, StepExecutionListener {

    private final FlatFileItemWriter<String> errorCsvWriter;
    private final int expectedFieldCount;
    private final List<String> errorLines = new ArrayList<>();

    public ErrorReaderListener(@Qualifier("errorCsvWriter") FlatFileItemWriter<String> errorCsvWriter,
                               @Qualifier("errorCsvHeaderValue") String errorCsvHeader) {
        this.errorCsvWriter = errorCsvWriter;
        this.expectedFieldCount = errorCsvHeader.split(",").length;
    }

    @Override
    public void beforeStep(@NonNull final StepExecution stepExecution) {
        errorLines.clear();
    }

    @Override
    public ExitStatus afterStep(@NonNull final StepExecution stepExecution) {
        if (!errorLines.isEmpty()) {
            ExecutionContext executionContext = stepExecution.getExecutionContext();
            try {
                errorCsvWriter.open(executionContext);
                errorCsvWriter.write(new Chunk<>(errorLines));
            } catch (Exception e) {
                log.error("Failed to write collected error lines to file", e);
            } finally {
                try {
                    errorCsvWriter.close();
                } catch (Exception e) {
                    log.error("Failed to close error writer", e);
                }
            }
        }
        return ExitStatus.COMPLETED;
    }

    @Override
    public void onReadError(@NonNull final Exception ex) {
        if (ex instanceof FlatFileParseException fileParseException) {
            String rawLine = fileParseException.getInput();
            String[] fields = rawLine.split(",", -1);

            String[] fixedFields = new String[expectedFieldCount];
            for (int i = 0; i < expectedFieldCount; i++) {
                fixedFields[i] = (fields.length > i) ? fields[i].trim() : "";
            }

            final String formattedLine = String.join(",", fixedFields);
            errorLines.add(formattedLine);
            log.warn("Collected error line for later writing: {}", formattedLine);
        } else {
            log.error("Unexpected read error: {}", ex.getMessage(), ex);
        }
    }
}
