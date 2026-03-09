package org.writer.exception;

import java.io.IOException;

public class CsvWriteException extends RuntimeException {
    public CsvWriteException(String failedToWriteCsvFile, IOException e) {
    }
}
