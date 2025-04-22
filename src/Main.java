import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        NaiveBayesClassifier nv = new NaiveBayesClassifier();
        try {
            nv.train("data/agaricus-lepiota.data");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
