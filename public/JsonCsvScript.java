import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonCsvScript {

    public static void main(String[] args) throws IOException {
        // 读取JSON文件
        JSONArray products = readJsonFile("products.json");

        // 解析出每个product的name值并存入Set集合
        Set<String> nameArray = StreamSupport.stream(products.spliterator(), false)
                .map(obj -> ((JSONObject) obj).getString("name"))
                .collect(Collectors.toCollection(HashSet::new));

        // 读取CSV文件并修改内容
        File csvFile = new File("data.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(csvFile), StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                     new FileOutputStream("data_update.csv"), StandardCharsets.UTF_8))) {

            // 处理第一行标题
            String headerLine = reader.readLine();
            writer.write(headerLine + ",defaultFilter\n");

            // 处理数据行
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                String name1 = fields[0]; // 假设name1在第一列

                // 判断name1是否在nameArray中存在
                boolean exist = nameArray.contains(name1);

                // 在最后新增一列
                String newLine = line + "," + (exist ? "Y" : "N") + "\n";
                writer.write(newLine);
            }
        }
    }

    private static JSONArray readJsonFile(String fileName) throws IOException {
        try (InputStream inputStream = new FileInputStream(fileName)) {
            JSONTokener tokener = new JSONTokener(inputStream);
            return new JSONArray(tokener);
        }
    }
}
