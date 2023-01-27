package com.caseydement;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HostCheckerTest {

    @Test
    public void canMatchHosts() throws Exception {
        HostChecker hostChecker = new HostChecker();

        Stream<String> stream = Files.lines(Path.of(ClassLoader.getSystemResource("hosts.csv").toURI()));
        stream.forEach(line -> {
            hostChecker.addHost(line);
        });

        Stream<String> goodHostStream = Files.lines(Path.of(ClassLoader.getSystemResource("goodhosts.csv").toURI()));
        goodHostStream.forEach(line -> {
            assertTrue(hostChecker.matchHost(line));
        });

        Stream<String> badHostStream = Files.lines(Path.of(ClassLoader.getSystemResource("badhosts.csv").toURI()));
        badHostStream.forEach(line -> {
            assertFalse(hostChecker.matchHost(line));
        });

        System.out.println(hostChecker.stepsPerRun());
    }
}
