package jb.filesystem.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathUtils {
    public final String PATH_SEPARATOR = "/";

    public String concatenatePaths(String path, String fileName) {
        return path + PATH_SEPARATOR + fileName;
    }

    public List<String> splitAbsolutePath(String path) {
        return Stream.of(path.split(PATH_SEPARATOR))
                .filter(dir -> !dir.isBlank())
                .collect(Collectors.toList());
    }

}
