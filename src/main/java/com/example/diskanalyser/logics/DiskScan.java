package com.example.diskanalyser.logics;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DiskScan {
    private HashMap<String, Long> sizes;

    public Map<String, Long> checkSize(Path path) {
        try {
            sizes = new HashMap<>();
            Set<Path> visitedPaths = new HashSet<>();
            Files.walkFileTree(
                    path,
                    new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
                            try {
                                long size = Files.size(file);
                                updateDirSize(file, size);
                            } catch (IOException e) {
                                // File access error handling
                            }
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFileFailed(Path file, IOException exc) {
                            if (visitedPaths.contains(file)) {
                                return FileVisitResult.SKIP_SUBTREE; // Skip revisited folders
                            } else {
                                visitedPaths.add(file);
                                return FileVisitResult.CONTINUE;
                            }
                        }
                    }
            );
            return sizes;
        } catch (IOException e) {
            Errors.errorCheck(1);
            throw new RuntimeException();
        }
    }

    private void updateDirSize(Path path, Long size) {
        String key = path.toString();
        sizes.put(key, size + sizes.getOrDefault(key, 0L));

        Path parent = path.getParent();

        if (parent != null) {
            updateDirSize(parent, size);
        }
    }
}