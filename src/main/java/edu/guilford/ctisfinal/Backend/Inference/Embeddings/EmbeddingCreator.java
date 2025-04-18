package edu.guilford.ctisfinal.Backend.Inference.Embeddings;

import ai.djl.MalformedModelException;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.djl.huggingface.translator.TextEmbeddingTranslator;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.util.ArrayList;

public class EmbeddingCreator {

    private String modelName;
    private String modelUrl;
    private final Criteria<String, float[]> criteria; // Criteria for the model
    private HuggingFaceTokenizer huggingFaceTokenizer;
    private TextEmbeddingTranslator textEmbeddingTranslator;

    private ZooModel<String, float[]> model;

    private Predictor<String, float[]> predictor;


    public EmbeddingCreator(String modelName, String modelUrl) throws IOException {
        this.modelName = modelName;
        this.modelUrl = modelUrl;

         huggingFaceTokenizer = HuggingFaceTokenizer.builder()
                .optTokenizerName(modelName)
                .optTruncation(true)
                .optMaxLength(128)
                .optPadToMaxLength()
                .build();

        textEmbeddingTranslator = TextEmbeddingTranslator.builder(huggingFaceTokenizer)
                .optPoolingMode("mean")      // mean pooling to match SentenceTransformer.encode
                .build();

         criteria = Criteria.builder()
                .setTypes(String.class, float[].class)
                .optEngine("PyTorch")
                .optModelUrls(modelUrl)
                .optTranslator(textEmbeddingTranslator)
                .build();

         try {
            model = ModelZoo.loadModel(criteria);
            predictor = model.newPredictor();
         } catch (ModelNotFoundException | MalformedModelException e) {
             throw new RuntimeException(e);
         }
    }

    public float[] getEmbedding(String text) throws  TranslateException {
            return predictor.predict(text);
        }





    }







