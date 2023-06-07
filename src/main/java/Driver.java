import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Driver {

    public static void main (String[] args) throws IOException {
        if (args.length==0) {
            System.out.println("one parameter needed - input file name");
            System.exit(1);
        }
        try (Reader reader = new FileReader(args[0])) {
            Object obj = new Yaml().load(reader);
            System.out.println(obj);
        }
    }
}
