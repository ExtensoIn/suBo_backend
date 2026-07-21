package com.tordoya.subo.gtfs.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class GtfsZipService {
    public Path createZip(Path sourceDirectory, Path targetZip) {
        if (!Files.isDirectory(sourceDirectory)) {
            throw new IllegalArgumentException("GTFS source directory does not exist: " + sourceDirectory);
        }
        try {
            Path parent = targetZip.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            List<Path> files;
            try (Stream<Path> stream = Files.list(sourceDirectory)) {
                files = stream
                        .filter(Files::isRegularFile)
                        .filter(path ->
                                path.getFileName()
                                        .toString()
                                        .endsWith(".txt")
                        )
                        .sorted(
                                Comparator.comparing(path -> path.getFileName().toString())
                        )
                        .toList();
            }
            if (files.isEmpty()) {
                throw new IllegalStateException("No GTFS .txt files found in " + sourceDirectory);
            }
            try (
                    OutputStream outputStream =
                            Files.newOutputStream(
                                    targetZip,
                                    StandardOpenOption.CREATE,
                                    StandardOpenOption.TRUNCATE_EXISTING,
                                    StandardOpenOption.WRITE
                            );
                    ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)
            ) {
                for (Path file : files) {
                    ZipEntry entry = new ZipEntry(file.getFileName().toString());
                    zipOutputStream.putNextEntry(entry);
                    Files.copy(file, zipOutputStream);
                    zipOutputStream.closeEntry();
                }
            }
            return targetZip;
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to create GTFS ZIP", exception);
        }
    }
}