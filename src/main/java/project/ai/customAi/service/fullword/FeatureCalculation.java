package project.ai.customAi.service.fullword;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class FeatureCalculation {

    private final LevenshteinDistance levenshtein = new LevenshteinDistance();

    public double levenshteinNorm(String a, String b) {
        int maxLen = Math.max(a.length(), b.length());
        if (maxLen == 0) return 0.0;
        return (double) levenshtein.apply(a, b) / maxLen;
    }

    public boolean isDistanceLE(String a, String b, int threshold) {
        return levenshtein.apply(a, b) <= threshold;
    }

    public double lengthDiffNorm(String a, String b) {
        int maxLen = Math.max(a.length(), b.length());
        if (maxLen == 0) return 0.0;
        int diff = Math.abs(a.length() - b.length());
        return diff <= 1 ? (double) diff / maxLen : diff;
    }

    public static double samePositionMatchScore(String a, String b) {
        if (a == null || b == null || a.isEmpty() || b.isEmpty()) return 0.0;

        int len = Math.min(a.length(), b.length());
        int matches = 0;

        for (int i = 0; i < len; i++) {
            if (a.charAt(i) == b.charAt(i)) {
                matches++;
            }
        }
        return (double) matches / Math.max(a.length(), b.length());
    }

    public double charOverlapNorm(String a, String b) {
        if (a.isEmpty() || b.isEmpty()) return 0.0;

        long common = a.chars()
                .distinct()
                .filter(c -> b.indexOf(c) >= 0)
                .count();

        return (double) common / Math.max(a.length(), b.length());
    }


    public double prefixMatchNorm(String a, String b) {
        int len = Math.min(a.length(), b.length());
        if (len == 0) return 0.0;

        int match = 0;
        for (int i = 0; i < len; i++) {
            if (a.charAt(i) == b.charAt(i)) match++;
            else break;
        }
        return match;
    }

    public double suffixMatchNorm(String a, String b) {
        int len = Math.min(a.length(), b.length());
        if (len == 0) return 0.0;

        int match = 0;
        for (int i = 1; i <= len; i++) {
            if (a.charAt(a.length() - i) == b.charAt(b.length() - i)) match++;
            else break;
        }
        return match;
    }

    public double ngramOverlapNorm(String a, String b, int n) {
        if (a.length() < n || b.length() < n) return 0.0;

        Set<String> gramsA = new HashSet<>();
        for (int i = 0; i <= a.length() - n; i++) {
            gramsA.add(a.substring(i, i + n));
        }

        int overlap = 0;
        for (int i = 0; i <= b.length() - n; i++) {
            if (gramsA.contains(b.substring(i, i + n))) overlap++;
        }

        return (double) overlap / Math.max(a.length(), b.length());
    }

    public double[] extractFeatures(String input, String candidate) {
        return new double[] {
                levenshteinNorm(input, candidate),
                Math.pow(lengthDiffNorm(input, candidate), 3),
                Math.pow(samePositionMatchScore(input, candidate), 2),
                charOverlapNorm(input, candidate)
        };
    }
}