package com.project.foradhd.global.util;

import com.opencsv.CSVReader;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public abstract class CSVUtil {

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
}
