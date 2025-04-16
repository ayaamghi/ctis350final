package edu.guilford.ctisfinal.Backend;
import java.nio.file.*;
import java.util.Arrays;

import ai.djl.*;
import ai.djl.inference.*;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.*;
import ai.djl.translate.*;

import static edu.guilford.ctisfinal.Backend.Embedding.embedding_example;

public class InferenceEngine {

    public static void main(String[] args) throws Exception {
        Path modelDir = Paths.get("src/main/resources/edu/guilford/ctisfinal/russian_10k_model.pt");

        Criteria<NDList, NDList> criteria = Criteria.builder()
                .setTypes(NDList.class, NDList.class)
                .optModelPath(modelDir)
                .optModelName("mymodel") // exclude .pt
                .optEngine("PyTorch")
                .optTranslator(new NoOpTranslator())
                .build();

        try (Model model = ModelZoo.loadModel(criteria);
             Predictor<NDList, NDList> predictor = model.newPredictor(new NoOpTranslator())) {

            try (NDManager manager = NDManager.newBaseManager()) {
                float followers = 200000000f;
                float timestamp = 1450782900f;
                float[] combinedFeatures = Arrays.copyOf(embedding_example, embedding_example.length + 2);
                combinedFeatures[embedding_example.length] = followers;
                combinedFeatures[embedding_example.length + 1] = timestamp;
                NDArray input = manager.create(combinedFeatures, new Shape(1, combinedFeatures.length));
                NDList result = predictor.predict(new NDList(input));
                System.out.println("Predicted updates: " + result.getFirst());
            }
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

    }

