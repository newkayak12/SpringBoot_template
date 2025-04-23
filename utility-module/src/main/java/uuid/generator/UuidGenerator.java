package uuid.generator;

import com.fasterxml.uuid.Generators;

public class UuidGenerator {

    private UuidGenerator() {
    }

    public static String generate(String prefix) {
        StringBuilder builder = new StringBuilder(prefix);
        builder.append(Generators.timeBasedGenerator().generate().toString());
        return builder.toString();
    }

    public static String generate() {
        return Generators.timeBasedGenerator().generate().toString();
    }

}
