package nl.sander.vocabulair.services;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import nl.sander.vocabulair.domain.Woord;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WoordenService {

    public List<Woord> getWoorden(File file) {
        try {
            Reader reader = new FileReader(file);
            CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withSkipLines(0)
                    .withCSVParser(new CSVParserBuilder()
                            .withIgnoreLeadingWhiteSpace(true)
                            .withSeparator(';')
                            .build())
                    .build();
            List<String[]> list = csvReader.readAll();
            reader.close();
            csvReader.close();
            return list
                    .stream()
                    .filter(item -> item.length == 2)
                    .filter(strings -> !strings[0].isEmpty() && !strings[1].isEmpty())
                    .map(strings -> Woord.builder().nederlands(strings[0]).vreemd(strings[1]).build())
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
