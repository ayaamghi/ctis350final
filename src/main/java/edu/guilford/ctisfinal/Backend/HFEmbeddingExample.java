package edu.guilford.ctisfinal.Backend;
import ai.djl.ModelException;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.djl.huggingface.translator.TextEmbeddingTranslator;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.inference.Predictor;
import ai.djl.huggingface.translator.TextEmbeddingTranslatorFactory;  // ← here
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.util.Arrays;

import static edu.guilford.ctisfinal.Backend.Embedding.embedding_example;
public class HFEmbeddingExample {
    public static void main(String[] args) throws Exception {
        String modelName = "sentence-transformers/all-MiniLM-L6-v2";
        String modelUrl  = "djl://ai.djl.huggingface.pytorch/" + modelName;

        // 1. Instantiate the tokenizer for that model
        HuggingFaceTokenizer tokenizer = HuggingFaceTokenizer.builder()
                .optTokenizerName(modelName)
                .optTruncation(true)      // enable truncation
                .optMaxLength(128)        // <— truncate/pad to 128 tokens :contentReference[oaicite:0]{index=0}
                .optPadToMaxLength()      // pad up to maxLength
                .build();
        // 2. Build a mean‑pooling translator, passing the tokenizer to builder(...)
        TextEmbeddingTranslator translator = TextEmbeddingTranslator.builder(tokenizer)
                .optPoolingMode("mean")      // mean pooling to match SentenceTransformer.encode
                .build();

        // 3. Create Criteria pointing at the HF checkpoint
        Criteria<String, float[]> criteria = Criteria.builder()
                .setTypes(String.class, float[].class)
                .optEngine("PyTorch")
                .optModelUrls(modelUrl)
                .optTranslator(translator)
                .build();

        try (ZooModel<String, float[]> model = ModelZoo.loadModel(criteria);
             Predictor<String, float[]> predictor = model.newPredictor()) {

            String text = "Israeli police say Palestinian home in West Bank attacked  #news,United States";
            float[] embedding = predictor.predict(text);
            System.out.printf("First 3 values of embedding: %.5f, %.5f, %.5f%n",
                    embedding[0], embedding[1], embedding[2]);

            try (NDManager mgr = NDManager.newBaseManager()) {
                NDArray a = mgr.create(embedding);   // shape (384,)
                NDArray b = mgr.create(embedding_example);   // shape (384,)

                float dot   = a.dot(b).getFloat();
                float normA = a.norm().getFloat();
                float normB = b.norm().getFloat();

                float cosine = dot / (normA * normB);
                System.out.println("Cosine similarity = " + cosine);
            }

        }
    }
}
