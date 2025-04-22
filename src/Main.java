import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        NaiveBayesClassifier nv = new NaiveBayesClassifier();
        try {
            NaiveBayesClassifier classifier = new NaiveBayesClassifier(); // Jedna instancja
            classifier.train("data/agaricus-lepiota.data"); // Trenujemy instancję

            List<String[]> testData = Reading.loadData("data/agaricus-lepiota.test.data");

            NaiveBayesClassifier.ClassificationResults result = classifier.test(testData);
            System.out.println(result);




        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
