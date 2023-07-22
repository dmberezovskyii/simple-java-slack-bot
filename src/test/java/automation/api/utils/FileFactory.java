package automation.api.utils;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Objects;

public class FileFactory {

    @Nullable
    public static File getFile(String fileName) {
        switch (fileName) {
            case "b":
                return new File(Objects.requireNonNull(FileFactory
                        .class
                        .getResource("/b.json")).getFile());
            case "h":
                return new File(Objects.requireNonNull(FileFactory
                        .class
                        .getResource("/h.json")).getFile());
            case "k":
                return new File(Objects.requireNonNull(FileFactory
                        .class
                        .getResource("/k.json")).getFile());
            case "p":
                return new File(Objects.requireNonNull(FileFactory
                        .class
                        .getResource("/p.json")).getFile());
            case "s":
                return new File(Objects.requireNonNull(FileFactory
                        .class
                        .getResource("/s.json")).getFile());
            default:
                return null;
        }

    }


}
