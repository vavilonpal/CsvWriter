package org.writer.csv;

import org.writer.csv.context.CsvContext;
import org.writer.csv.pipeline.Pipeline;
import org.writer.csv.pipeline.steps.*;
import org.writer.exception.ArrayIsEmptyException;

import java.util.List;


/**
 * Writes Java objects to a CSV file using reflection.
 *
 * <p>The writer automatically generates a CSV header based on
 * the object's fields and ensures header consistency when appending data.</p>
 *
 * <p>This class is not thread-safe.</p>
 */
public class CsvFileWriter implements Writable {
    private  Pipeline<CsvContext> writeToCsvFilePipeline;
    private CsvContext csvContext;


    /**
     * Writes a list of objects to a CSV file.
     *
     * <p>All objects in the list must be of the same type. The CSV header
     * is generated automatically using reflection.</p>
     *
     * @param data     list of objects to be written; must not be empty and must contain
     *                 objects of the same type
     * @param fileName name of the target CSV file
     * @throws ArrayIsEmptyException    if the data list is empty
     * @throws IllegalArgumentException if the list contains objects of different types
     * @throws IllegalStateException    if an existing CSV header does not match
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        this.csvContext = new CsvContext(data, fileName);
        this.writeToCsvFilePipeline =  buildPipeline();
        writeToCsvFilePipeline.execute(csvContext);
    }


    private Pipeline<CsvContext> buildPipeline() {
        Pipeline<CsvContext> writeToCsvFilePipeline = new Pipeline<>();

        writeToCsvFilePipeline.addStep(new ValidateDataStep());
        writeToCsvFilePipeline.addStep(new CheckFileExistsStep());

        writeToCsvFilePipeline.addStep(new ResolveMetadataStep());

        writeToCsvFilePipeline.addStep(new GenerateHeaderStep());
        writeToCsvFilePipeline.addStep(new WriteDataToCsvFileStep());

        return writeToCsvFilePipeline;
    }
}
