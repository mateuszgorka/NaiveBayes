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


        // -> mamy miec probability dla (p)oisonus i (e)dible
        double poisonusProbability = calcProbability("p", features);  // -> tu metoda calcProbality
        double edibleProbability = calcProbability("e", features);



        return poisonusProbability > edibleProbability ? "p" : "e";

    }

    double calcProbability(String label, String[] features){

        // -> najpierw prawd apriori
        double apriori = (double) classCounts.getOrDefault(label, 0) / totalExamples;
        // ->>>>>>>>> Np., dla label="e" i totalExamples = 10, a classCounts.get("e") = 6, to prior = 6/10 = 0.6

        // okej teraz iterujemy po tablicy


        double iloczynPrawdWarunkowych = 1;   //  -> likelihood = P(features[1] | label) * P(features[2] | label) * ... * P(features[n] | label)
        for (int i = 0; i < features.length; i++) {
            String feature = features[i];


            // -> teraz pobieramy dane cechy i statystyki

            Map<String, Integer> featureValMap = featureCounts.get(label).get(i);

            // -> Teraz ten laplace
            // -> ile razy featureValue wystąpiło dla danej etykiety i cechy
            double matchingFeatureVal = featureValMap.get(feature);


            // -> a teraz mianownik Laplace, czyli suma wszystkich wartosci dla danej etykiety  cechy rozmiar
            int totalFeatureQuantity = 0;
            for (int counter : featureValMap.values()) {
                totalFeatureQuantity += counter;
            }

            // -> i teraz liczymy prawd. warunkowe z wygładzeniem laplace
            double condProbality = (matchingFeatureVal + 1) / (totalFeatureQuantity + featureValMap.size());
            //  -> Np  jeśli featureValue="x", label="e", i=1, matchingFeatureCount=3, totalFeatureCount=6 (3+3)
            // , a featureValueCounts.size=2 to : (3+1)/(6+2)=4/8=0.5
            iloczynPrawdWarunkowych *= condProbality;
        }
        return iloczynPrawdWarunkowych * apriori;

    }
}
