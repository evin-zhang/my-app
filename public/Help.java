import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class CsvUpdater {
    public static void main(String[] args) throws IOException {
        // 读取JSON文件并解析出a属性的值
        Set<String> as = new HashSet<>();
        try (InputStream is = new FileInputStream("data.json")) {
            Json.createReader(is).readArray().forEach(obj -> as.add(obj.getString("a")));
        }

        // 读取CSV文件并更新每一行
        try (Stream<String> lines = Files.lines(Path.of("data.csv"))) {
            PrintWriter writer = new PrintWriter(new FileWriter("data_update.csv"));
            lines.map(line -> {
                String[] values = line.split(",");
                if (values[0].equals("b")) {
                    return line + ",updated";
                } else {
                    String updatedValue = as.contains(values[1]) ? "Y" : "N";
                    return line + "," + updatedValue;
                }
            }).forEach(writer::println);
            writer.close();
        }
    }
}
