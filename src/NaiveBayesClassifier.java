import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaiveBayesClassifier {

    // -> zliczanie giga wazne bo prawd. warunkowe
    // = P(wartosc_cechy / klasa)

// wiwiwiw mapa moze wygladac np tak -> np Map<String, List>
//    "e": [
//    {"x": 900, "f": 1000, "s": 200},
//    {"s": 800, "y": 600},
//            ...
//            ]


    Map<String, Integer> classCounts = new HashMap<>();
    Map<String, Map<Integer, Map<String, Integer>>> featureCounts = new HashMap<>(); // string etykieta -> potem mapa szczegoly dla etykiety i potem jeszcze nizej Map<String, Integer> mozliwa wartosc danej cechy
    int totalExamples = 0;
    int numFeatures = 22;


    public void train(String path) throws IOException {

        List<String[]> data = Reading.loadData(path);

        for (String[] line : data) {   // -> idzieeemy po kazdej linii danych
            String label = line[0];     // -> pobieramy etykiete pe itepe
            classCounts.put(label, classCounts.getOrDefault(label, 0) + 1); // a tu mordeczki zliczamy
            totalExamples++;  // tu zliczamy zebu wiedziec ile wszystkich w gogole

            featureCounts.putIfAbsent(label, new HashMap<>());

            for (int i = 0; i < line.length; i++) {
                String feature = line[i];  // -? dostajemy wartosc cech xyg

                featureCounts.get(label).putIfAbsent(i, new HashMap<>());

                Map<String, Integer> counts = featureCounts.get(label).get(i);
                counts.put(feature, counts.getOrDefault(feature, 0) + 1);
            }
        }
        System.out.println();
    }


    public String classify(String[] features){

        return "";

    }

}
