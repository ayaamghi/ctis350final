package edu.guilford.ctisfinal.Backend.Inference;

import ai.djl.MalformedModelException;
import ai.djl.Model;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.translate.*;

import ai.djl.repository.zoo.Criteria;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

public class TweetPopularityPredictor {

    private Path modelPath;
    private String modelName;
    private Criteria<NDList, NDList> criteria;

    private Model model;
    private Predictor<NDList, NDList> predictor;

    public TweetPopularityPredictor(String modelName, Path modelPath) throws ModelNotFoundException, MalformedModelException, IOException {
        criteria = Criteria.builder()
                .setTypes(NDList.class, NDList.class)
                .optModelPath(modelPath)
                .optModelName(modelName) // exclude .pt
                .optEngine("PyTorch")
                .optTranslator(new NoOpTranslator())
                .build();

        model = ModelZoo.loadModel(criteria);
        predictor = model.newPredictor(new NoOpTranslator());

    }

    public void changeModel(String modelName, Path modelPath) {
        criteria = Criteria.builder()
                .setTypes(NDList.class, NDList.class)
                .optModelPath(modelPath)
                .optModelName(modelName) // exclude .pt
                .optEngine("PyTorch")
                .optTranslator(new NoOpTranslator())
                .build();
        try {
            model = ModelZoo.loadModel(criteria);
            predictor = model.newPredictor(new NoOpTranslator());
        } catch (ModelNotFoundException | MalformedModelException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static class NoOpTranslator implements Translator<NDList, NDList> {
        @Override
        public NDList processInput(TranslatorContext ctx, NDList input) {
            return input;
        }

        @Override
        public NDList processOutput(TranslatorContext ctx, NDList list) {
            return list;
        }

        @Override
        public Batchifier getBatchifier() {
            return null;
        }
    }

    /***
     * Predicts the popularity of a tweet based on the input features.
     * @param input
     * @return NDList containing the predicted popularity score
     * @throws TranslateException
     */
    public float predict(NDList input) throws TranslateException {
        return predictor.predict(input).getFirst().getFloat(0);
    }


    public float predict(float[] embedding, float followers, float timestamp) throws TranslateException {
        NDList input = createInput(embedding, followers, timestamp);
        return predictor.predict(input).getFirst().getFloat(0);
    }



    public NDList createInput(float[] embedding, float followers, float timestamp) {

        NDManager manager = NDManager.newBaseManager();
        float[] combinedFeatures = Arrays.copyOf(embedding, embedding.length + 2);
        combinedFeatures[embedding.length] = followers;
        combinedFeatures[embedding.length + 1] = timestamp;
        return new NDList(manager.create(combinedFeatures, new Shape(1, combinedFeatures.length)));


    }

}
