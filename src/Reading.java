import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Reading {

    public static List<String[]> loadData(String filePath) throws IOException {

        List<String[]> data = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                String[] row = line.split(",");
                data.add(row);
            }
        }

        reader.close();
        return data;
    }



}