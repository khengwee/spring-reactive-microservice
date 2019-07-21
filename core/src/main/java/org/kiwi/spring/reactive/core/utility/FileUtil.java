package org.kiwi.spring.reactive.core.utility;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    private FileUtil() {
        throw new IllegalStateException("FileUtil class cannot be initialized");
    }

    public static String loadFile(String filename) throws IOException {
        LOGGER.debug("loadFile: filename={}", filename);
        InputStream inputStream = FileUtil.class.getClassLoader().getResourceAsStream(filename);
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }
}
