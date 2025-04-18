
package edu.guilford.ctisfinal.Backend;
import edu.guilford.ctisfinal.Backend.Inference.Embeddings.EmbeddingCreator;
import edu.guilford.ctisfinal.Backend.Inference.TweetPopularityPredictor;

import java.nio.file.Path;

public class ContextManager {

    private static ContextManager instance;

    private String modelName = "sentence-transformers/all-MiniLM-L6-v2";
    private String modelUrl  = "djl://ai.djl.huggingface.pytorch/" + modelName;

    private TweetPopularityPredictor model;
    private EmbeddingCreator embeddingCreator;

    private ContextManager() {
        try {
            embeddingCreator = new EmbeddingCreator(modelName, modelUrl);
            model = new TweetPopularityPredictor(
                    "mymodel",
                    Path.of("src/main/resources/edu/guilford/ctisfinal/russian_10k_model.pt")
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized ContextManager getInstance() {
        if (instance == null) {
            instance = new ContextManager();
        }
        return instance;
    }

    public EmbeddingCreator getEmbeddingCreator() {
        return embeddingCreator;
    }

    public TweetPopularityPredictor getModel() {
        return model;
    }

    public void changeModel(String newModelName, Path modelPath) {
        try {
            model.changeModel(newModelName, modelPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
