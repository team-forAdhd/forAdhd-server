package com.project.foradhd.global.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class CSVUtil {

    public static String[] readHeader(String classpath) {
        ClassPathResource classPathResource = new ClassPathResource(classpath);
        try (InputStream inputStream = classPathResource.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            CSVReader csvReader = new CSVReader(bufferedReader)) {
            return csvReader.readNext();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String[]> readAll(String classpath) {
        ClassPathResource classPathResource = new ClassPathResource(classpath);
        try (InputStream inputStream = classPathResource.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            CSVReader csvReader = new CSVReader(bufferedReader)) {
            csvReader.skip(1);
            return csvReader.readAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeAll(String filepath, String[] header, List<String[]> data) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filepath);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            CSVWriter csvWriter = new CSVWriter(bufferedWriter)) {
            csvWriter.writeNext(header);
            csvWriter.writeAll(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
