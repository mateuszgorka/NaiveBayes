public class ClassificationResults {

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
                "-------------+----------------+----------------+\n" +
                String.format("Prawdziwe e |       %6d    |       %6d    |\n", truePositives, falseNegatives) +
                "-------------+----------------+----------------+\n" +
                String.format("Prawdziwe p |       %6d    |       %6d    |\n", falsePositives, trueNegatives);
    }
}