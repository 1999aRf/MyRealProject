package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



import java.lang.String;
import java.util.stream.IntStream;

@Service
public class InfoService {
    private final Logger logger = LoggerFactory.getLogger(InfoService.class);
    private static final Long limit = 1_000_000L;
    @Value("${server.port}")
    private String port;
    public String getPort() {
        return port;
    }

    public void calculate() {
        calculateSumWithFormula();
        calculateSumWithStream();
    }
    public void calculateSumWithFormula() {
        logger.info("Starting sum calculation with formula");
        long start = System.currentTimeMillis();
        logger.info(String.valueOf(start));

        long n = 1_000_000;
        long sum = n * (n + 1) / 2;

        long end = System.currentTimeMillis();
        logger.info(String.valueOf(end));
        logger.info("Sum calculation with formula completed in " + (end - start) + "ms" );

    }
    public void calculateSumWithStream() {
        IntStream.rangeClosed(1, 1_000_000)
                .parallel()
                .reduce(0, Integer::sum);
    }
}
