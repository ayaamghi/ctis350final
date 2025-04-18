
package edu.guilford.ctisfinal.Backend;
import edu.guilford.ctisfinal.Backend.Inference.Embeddings.EmbeddingCreator;
import edu.guilford.ctisfinal.Backend.Inference.TweetPopularityPredictor;
import edu.guilford.ctisfinal.Backend.Map.USMap;

import java.io.IOException;
import java.nio.file.Path;

/***
 * Singleton class to manage the context of the application-- model, csv, map. Prevents any of these fields from ever being null
 */
public class ContextManager {

    private static ContextManager instance;

    private String modelName = "sentence-transformers/all-MiniLM-L6-v2";
    private String modelUrl  = "djl://ai.djl.huggingface.pytorch/" + modelName;

    private TweetPopularityPredictor model;
    private EmbeddingCreator embeddingCreator;

    private USMap usMap;
    private CsvParser df;
    private ContextManager() {
        try {
            embeddingCreator = new EmbeddingCreator(modelName, modelUrl);
            model = new TweetPopularityPredictor(
                    "mymodel",
                    Path.of("src/main/resources/edu/guilford/ctisfinal/russian_10k_model.pt")
            );
            df = new CsvParser(Path.of("src/main/resources/edu/guilford/ctisfinal/russian_10k_df_state_annotated.csv"));

            usMap = new USMap(df);
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

    public USMap getUsMap() {
        return usMap;
    }
    public CsvParser getDf() {
        return df;
    }
    public void setDf(Path path) throws IOException {;
        df = new CsvParser(path);
    }
    public void changeModel(String newModelName, Path modelPath) {
        try {
            model.changeModel(newModelName, modelPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
