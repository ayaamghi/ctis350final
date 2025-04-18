package edu.guilford.ctisfinal.Backend.Inference.Embeddings;

import java.util.ArrayList;

public class SimilarityCalculator {


    /**
     * Calculates the cosine similarity between two vectors.
     *
     * @param vectorA The first vector.
     * @param vectorB The second vector.
     * @return The cosine similarity between the two vectors.
     */
    public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
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
    public static ArrayList<String> getKHighestSimilarities(float[] embedding, int k) {



        // Implement logic to find the k highest similarities
        // This is a placeholder implementation
        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            results.add("Similarity " + i);
        }
        return results;
    }

}
