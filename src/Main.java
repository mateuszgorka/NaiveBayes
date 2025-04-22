import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        NaiveBayesClassifier nv = new NaiveBayesClassifier();
        try {
            nv.train("data/agaricus-lepiota.data");


            String[] features = {"e", "x", "s", "n", "t"};
            nv.classify(features);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
