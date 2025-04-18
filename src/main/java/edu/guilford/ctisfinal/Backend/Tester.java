package edu.guilford.ctisfinal.Backend;

import ai.djl.MalformedModelException;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.TranslateException;
import edu.guilford.ctisfinal.Backend.Inference.Embeddings.EmbeddingCreator;
import edu.guilford.ctisfinal.Backend.Inference.TweetPopularityPredictor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Tester {
    static String modelName = "sentence-transformers/all-MiniLM-L6-v2";
    static String modelUrl = "djl://ai.djl.huggingface.pytorch/" + modelName;

    public static void main(String[] args) throws TranslateException, IOException {


//        ContextManager manager = ContextManager.getInstance();
//
//        EmbeddingCreator embeddingCreator = manager.getEmbeddingCreator();
//        TweetPopularityPredictor tweetPopularityPredictor = manager.getModel();
//
//        System.out.println(tweetPopularityPredictor.predict(embeddingCreator.getEmbedding("hello world"), 200000000f, 1450782900f));

        //TODO fix this imports
        Path csvPath = Path.of("/Users/ayaam/Programming/School/final/src/main/resources/edu/guilford/ctisfinal/russian_10k_df.csv");
        CsvParser df = new CsvParser(csvPath);
        System.out.println("Columns: " + df.getColumns());
        System.out.println("Rows:    " + df.getRowCount());

        // Get entire column
        List<String> texts = df.getColumn("embedding");
        System.out.println("First 3 texts: " + texts.subList(0, 3));

        // Get a single cell
        String firstText = df.get(0, "noun_phrases");
        System.out.println("First tweet: " + firstText);

    }


}
//        EmbeddingCreator embeddingCreator = new EmbeddingCreator(modelName, modelUrl);
//
//        float[] embedding = embeddingCreator.getEmbedding("Israeli police say Palestinian home in West Bank attacked  #news");
//
//        System.out.printf("First 3 values of embedding: %.5f, %.5f, %.5f%n",
//                embedding[0], embedding[1], embedding[2]);
//
//        TweetPopularityPredictor tweetPopularityPredictor = new TweetPopularityPredictor("mymodel", Path.of("src/main/resources/edu/guilford/ctisfinal/russian_10k_model.pt"));
//
//        float score= tweetPopularityPredictor.predict(tweetPopularityPredictor.createInput(embedding, 200000000f, 1450782900f));
//        System.out.println(score);


        //                float followers = 200000000f;
//                float timestamp = 1450782900f;
//                float[] combinedFeatures = Arrays.copyOf(200000000f, embedding_example.length + 2);
//                combinedFeatures[embedding_example.length] = followers;
//                combinedFeatures[embedding_example.length + 1] = timestamp;
//                NDArray input = manager.create(combinedFeatures, new Shape(1, combinedFeatures.length));
//                NDList result = tweetPopularityPredictor.predict(new NDList(input));
//                System.out.println("Predicted updates: " + result.getFirst());


