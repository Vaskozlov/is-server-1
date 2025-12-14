package org.vaskozov.is.lab1.parser;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.vaskozov.is.lab1.bean.*;
import org.vaskozov.is.lab1.lib.Result;
import org.vaskozov.is.lab1.validation.PersonValidator;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class CsvPersonParser {
    private static final String[] requiredColumns = {
            "name",
            "coordinates_x",
            "coordinates_y",
            "eyeColor",
            "hairColor",
            "nationality",
            "height",
            "weight",
            "location_x",
            "location_y",
            "location_name",
    };

    private static final String[] optionalColumns = {
            "coordinates_id",
            "location_id",
    };

    @Inject
    private PersonValidator personValidator;

    public Result<List<Person>, String> parsePersonCsv(String csvContent) {
        List<Person> persons = new ArrayList<>();

        // Detect delimiter
        String[] lines = csvContent.split("\n", 2); // Split to get first line
        if (lines.length == 0) {
            return Result.error("Empty CSV file provided");
        }
        String headerLine = lines[0];
        int commaCount = (int) headerLine.chars().filter(ch -> ch == ',').count();
        int semiCount = (int) headerLine.chars().filter(ch -> ch == ';').count();
        char delimiter = (semiCount > commaCount) ? ';' : ',';

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(delimiter)
                .build();

        try (CSVReader csvReader = new CSVReaderBuilder(new StringReader(csvContent))
                .withCSVParser(parser)
                .build()) {
            String[] headers = csvReader.readNext();

            if (headers == null) {
                return Result.error("Empty CSV file provided");
            }

            Map<String, Integer> headerMap = new HashMap<>();

            for (int i = 0; i < headers.length; ++i) {
                System.out.println(headers[i].trim());
                headerMap.put(headers[i].trim(), i);
            }

            for (String col : requiredColumns) {
                if (!headerMap.containsKey(col)) {
                    return Result.error("Missing column: " + col);
                }
            }

            String[] row;

            while ((row = csvReader.readNext()) != null) {
                var parseResult = parseRow(headerMap, row);

                if (parseResult.isError()) {
                    return Result.error(parseResult.getError());
                }

                persons.add(parseResult.getValue());
            }
        } catch (IOException | CsvValidationException e) {
            return Result.error("Failed to parse CSV");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return Result.success(persons);
    }

    private Result<Person, String> parseRow(Map<String, Integer> headerMap, String[] row) {
        try {
            Coordinates coordinates = Coordinates.builder()
                    .id(headerMap.containsKey("coordinates_id") && !row[headerMap.get("coordinates_id")].isBlank()
                            ? Long.parseLong(row[headerMap.get("coordinates_id")])
                            : null)
                    .x(Integer.parseInt(row[headerMap.get("coordinates_x")]))
                    .y(Float.parseFloat(row[headerMap.get("coordinates_y")]))
                    .build();

            Location location = Location.builder()
                    .id(
                            headerMap.containsKey("location_id") && !row[headerMap.get("location_id")].isBlank()
                                    ? Long.parseLong(row[headerMap.get("location_id")])
                                    : null
                    )
                    .x(Double.parseDouble(row[headerMap.get("location_x")]))
                    .y(Double.parseDouble(row[headerMap.get("location_y")]))
                    .name(row[headerMap.get("location_name")])
                    .build();

            Person person = Person.builder()
                    .name(row[headerMap.get("name")])
                    .coordinates(coordinates)
                    .height(Double.parseDouble(row[headerMap.get("height")]))
                    .weight(Float.parseFloat(row[headerMap.get("weight")]))
                    .hairColor(Color.valueOf(row[headerMap.get("hairColor")].toUpperCase()))
                    .eyeColor(Color.valueOf(row[headerMap.get("eyeColor")].toUpperCase()))
                    .nationality(Country.valueOf(row[headerMap.get("nationality")].toUpperCase()))
                    .location(location)
                    .build();

            var validationResult = personValidator.validate(person);

            if (validationResult.isError()) {
                return Result.error(validationResult.getError());
            }

            return Result.success(person);
        } catch (Exception ignored) {
            System.err.println(ignored.getMessage());
            ignored.printStackTrace(System.err);
            return Result.error("Failed to parse person data");
        }
    }
}
