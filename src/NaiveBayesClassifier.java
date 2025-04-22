import java.io.IOException;
import java.util.Arrays;
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

                Map<String, Integer> counts = featureCounts
                        .getOrDefault(label, new HashMap<>())
                        .getOrDefault(i, new HashMap<>());

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
        System.out.println("Apriori dla " + label + ": " + apriori);
        // okej teraz iterujemy po tablicy


        double iloczynPrawdWarunkowych = 1;   //  -> likelihood = P(features[1] | label) * P(features[2] | label) * ... * P(features[n] | label)
        for (int i = 1; i < features.length; i++) {
            String feature = features[i];


            // -> teraz pobieramy dane cechy i statystyki

            Map<String, Integer> featureValMap = featureCounts.getOrDefault(label,new HashMap<>()).getOrDefault(i, new HashMap<>());


            // -> Teraz ten laplace
            // -> ile razy featureValue wystąpiło dla danej etykiety i cechy
            double matchingFeatureVal = featureValMap.getOrDefault(feature, 0);


            // -> a teraz mianownik Laplace, czyli suma wszystkich wartosci dla danej etykiety  cechy rozmiar
            int totalFeatureQuantity = 0;
            for (int counter : featureValMap.values()) {
                totalFeatureQuantity += counter;
            }

            // -> i teraz liczymy prawd. warunkowe z wygładzeniem laplace
            double condProbality = featureValMap.isEmpty() ?
                    1.0 / numFeatures :
                    (double) (matchingFeatureVal + 1) / (totalFeatureQuantity + featureValMap.size());

            //  -> Np  jeśli featureValue="x", label="e", i=1, matchingFeatureCount=3, totalFeatureCount=6 (3+3)
            // , a featureValueCounts.size=2 to : (3+1)/(6+2)=4/8=0.5

            iloczynPrawdWarunkowych *= condProbality;
        }

        return iloczynPrawdWarunkowych * apriori;
    }


    public ClassificationResults test(List<String[]> testData) {
        int correctPredictions = 0;
        int totalPredictions = 0;
        int truePositives = 0;
        int trueNegatives = 0;
        int falsePositives = 0;
        int falseNegatives = 0;


        for (String[] testExample : testData) {

            String trueLabel = testExample[0];
            String[] features = Arrays.copyOfRange(testExample, 1, testExample.length);
            String predictedLabel = classify(features);

            totalPredictions++;

            if (trueLabel.equals("e") && predictedLabel.equals("e")) {
                truePositives++;
            } else if (trueLabel.equals("p") && predictedLabel.equals("p")) {
                trueNegatives++;
            } else if (trueLabel.equals("e") && predictedLabel.equals("p")) {
                falseNegatives++;
            } else if (trueLabel.equals("p") && predictedLabel.equals("e")) {
                falsePositives++;
            }




            if (predictedLabel.equals(trueLabel)) {
                correctPredictions++;
            }
        }

        double accuracy = (double) correctPredictions / totalPredictions;
        double precision = (double) truePositives / (truePositives + falsePositives);
        double recall = (double) truePositives / (truePositives + falseNegatives);
        double f1Score = 2 * (precision * recall) / (precision + recall);


        return new ClassificationResults(correctPredictions, totalPredictions, accuracy, precision, recall, f1Score, truePositives, trueNegatives, falsePositives, falseNegatives);
    }


    static class ClassificationResults {

        public int correctPredictions;
        public int totalPredictions;
        public double accuracy;
        public double precision;
        public double recall;
        public double f1_Score;
        public int truePositives;
        public int trueNegatives;
        public int falsePositives;
        public int falseNegatives;



        public ClassificationResults(int correctPredictions, int totalPredictions, double accuracy, double precision, double recall, double f1Score, int truePositives, int trueNegatives, int falsePositives, int falseNegatives) {
            this.correctPredictions = correctPredictions;
            this.totalPredictions = totalPredictions;
            this.accuracy = accuracy;
            this.precision = precision;
            this.recall = recall;
            this.f1_Score = f1Score;
            this.truePositives = truePositives;
            this.trueNegatives = trueNegatives;
            this.falsePositives = falsePositives;
            this.falseNegatives = falseNegatives;

        }


        @Override
        public String toString() {

            return "Wyniki klasyfikacji:\n" +
                    "Przetworzonych przykładów: " + totalPredictions + "\n" +
                    "Poprawnie sklasyfikowanych: " + correctPredictions + "\n" +
                    String.format("Accuracy: %.2f%%\n", accuracy * 100) +
                    String.format("Precision: %.2f%%\n", precision * 100) +
                    String.format("Recall: %.2f%%\n", recall * 100) +
                    String.format("F1-score: %.2f%%\n", f1_Score * 100) +
                    "\nMacierz pomyłek:\n" +
                    "             | Przewidywane e | Przewidywane p |\n" +
                    "---------------------------------------------\n" +
                    String.format("Prawdziwe e |       %6d    |       %6d    |\n", truePositives, falseNegatives) +
                    "---------------------------------------------\n" +
                    String.format("Prawdziwe p |       %6d    |       %6d    |\n", falsePositives, trueNegatives);
        }
    }

}
