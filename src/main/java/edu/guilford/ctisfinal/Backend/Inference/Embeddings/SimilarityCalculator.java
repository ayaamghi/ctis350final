package edu.guilford.ctisfinal.Backend.Inference.Embeddings;

import edu.guilford.ctisfinal.Backend.ContextManager;
import edu.guilford.ctisfinal.Backend.CsvParser;

import javax.naming.Context;
import java.util.ArrayList;
import java.util.Map;

public class SimilarityCalculator {


    /**
     * Calculates the cosine similarity between two vectors.
     *
     * @param vectorA The first vector.
     * @param vectorB The second vector.
     * @return The cosine similarity between the two vectors.
     */
    public static double cosineSimilarity(float[] vectorA, float[] vectorB) {
        if (vectorA.length != vectorB.length) {
            throw new IllegalArgumentException("Vectors must be of the same length");
        }

        double dotProduct = 0.0;
        double magnitudeA = 0.0;
        double magnitudeB = 0.0;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            magnitudeA += Math.pow(vectorA[i], 2);
            magnitudeB += Math.pow(vectorB[i], 2);
        }

        if (magnitudeA == 0 || magnitudeB == 0) {
            return 0.0; // Handle zero vectors
        }

        return dotProduct / (Math.sqrt(magnitudeA) * Math.sqrt(magnitudeB));
    }

    //TODO implement this
    public static ArrayList<String> getKHighestSimilarities(float[] embedding, int k, CsvParser df) {

        ArrayList<Map<CsvParser.Row, Double>> rows = new ArrayList<>();

        for(int i = 0; i < df.getRows().size(); i++) {
            float[] tableEmbedding = parseEmbeddingString(df.get(i, "embedding"));
            rows.add(
                    Map.of(
                            df.getRow(i),
                            cosineSimilarity(embedding, tableEmbedding)
                    )
            );
        }

        // Sort the rows based on similarity
        rows.sort((a, b) -> {
            double similarityA = a.values().iterator().next();
            double similarityB = b.values().iterator().next();
            return Double.compare(similarityB, similarityA); // Sort in descending order
        });

        // Get the top k rows
        ArrayList<String> topKRows = new ArrayList<>();
        for (int i = 0; i < k && i < rows.size(); i++) {


            topKRows.add(rows.get(i).keySet().iterator().next().get("content") +
                 " Impressions " + rows.get(i).keySet().iterator().next().get("updates") +    "  (Similarity: " + rows.get(i).values().iterator().next() + ")"););
        }
        return topKRows;
    }
    private static float[] parseEmbeddingString(String s) {
        String inner = s.replaceAll("[\\[\\]]", "");
        String[] parts = inner.split(",\\s*");
        float[] emb = new float[parts.length];
        for (int i = 0; i < parts.length; i++) {
            emb[i] = Float.parseFloat(parts[i]);
        }
        return emb;
    }

}
