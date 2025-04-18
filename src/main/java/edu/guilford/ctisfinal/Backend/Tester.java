package edu.guilford.ctisfinal.Backend;

import ai.djl.MalformedModelException;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.TranslateException;
import edu.guilford.ctisfinal.Backend.Inference.Embeddings.EmbeddingCreator;
import edu.guilford.ctisfinal.Backend.Inference.TweetPopularityPredictor;
import edu.guilford.ctisfinal.Backend.Map.USMap;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/***
 * Tester class to test the functionality of the backend application.
 */
public class Tester {
    static String modelName = "sentence-transformers/all-MiniLM-L6-v2";
    static String modelUrl = "djl://ai.djl.huggingface.pytorch/" + modelName;

    public static void main(String[] args) throws TranslateException, IOException {

        ContextManager.getInstance().getUsMap().getStates().forEach(System.out::println);


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


