package ca.bc.gov.open.jag.documentutils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;

public class TestHelpers {
    private TestHelpers() {}

    public static String loadTestDataFromFile(String fileName) throws IOException {

        return new String(Files.readAllBytes(Paths.get(MessageFormat.format("./test_files/{0}", fileName))));

    }

}
