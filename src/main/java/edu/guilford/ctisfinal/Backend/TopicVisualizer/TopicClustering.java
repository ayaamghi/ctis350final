package edu.guilford.ctisfinal.Backend.TopicVisualizer;

import edu.guilford.ctisfinal.Backend.ContextManager;
import edu.guilford.ctisfinal.Backend.Inference.Embeddings.EmbeddingCreator;

import java.util.ArrayList;
import java.util.List;

public class TopicClustering {

    /**
     * Groups topic strings into clusters: if a topic’s embedding has cosine‐sim >
     * threshold to a cluster’s centroid, it joins that cluster; otherwise a new
     * cluster is started.
     *
     * @param topics    list of topic phrases
     * @param threshold minimum cosine similarity to join cluster (e.g. 0.75f)
     */
    public static List<List<String>> clusterTopics(
            List<String> topics, float threshold) throws Exception {
        EmbeddingCreator embedder = ContextManager.getInstance().getEmbeddingCreator();
        int n = topics.size();
        List<float[]> embs = new ArrayList<>(n);
        for (String t : topics) {
            embs.add(embedder.getEmbedding(t));
        }

        List<List<String>> clusters = new ArrayList<>();
        List<float[]> centroids = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            float[] vec = embs.get(i);
            boolean placed = false;
            for (int c = 0; c < clusters.size(); c++) {
                float sim = cosineSimilarity(centroids.get(c), vec);
                if (sim >= threshold) {
                    clusters.get(c).add(topics.get(i));
                    // update centroid = average of members
                    centroids.set(c, averageVectors(clusters.get(c), embedder, topics, embs));
                    placed = true;
                    break;
                }
            }
            if (!placed) {
                clusters.add(new ArrayList<>(List.of(topics.get(i))));
                centroids.add(vec.clone());
            }
        }
        return clusters;
    }

    private static float[] averageVectors(
            List<String> cluster,
            EmbeddingCreator embedder,
            List<String> allTopics,
            List<float[]> allEmbs) {
        int d = allEmbs.get(0).length;
        float[] sum = new float[d];
        for (String t : cluster) {
            int idx = allTopics.indexOf(t);
            float[] v = allEmbs.get(idx);
            for (int j = 0; j < d; j++) sum[j] += v[j];
        }
        for (int j = 0; j < d; j++) sum[j] /= cluster.size();
        return sum;
    }

    private static float cosineSimilarity(float[] a, float[] b) {
        float dot = 0, na = 0, nb = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            na  += a[i]*a[i];
            nb  += b[i]*b[i];
        }
        return dot / ((float)Math.sqrt(na) * (float)Math.sqrt(nb));
    }
}
