package cl.com.nisum.user.api.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class FileTestUtil {

    private FileTestUtil() {
        throw new UnsupportedOperationException();
    }

    public static String readFile(String pathFile) throws IOException {
        File file = new File("src/test/resources" + pathFile);
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }
}
