package project.ai.customAi.service.fullword;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class FeatureCalculation {

    private static final Set<Character> VOWELS = Set.of('a','e','i','o','u');
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
        // increased value for diff > 1
        return diff <= 1 ? (double) diff / maxLen : diff;
    }

    public double prefixMatchNorm(String a, String b) {
        int len = Math.min(a.length(), b.length());
        if (len == 0) return 0.0;

        int match = 0;
        for (int i = 0; i < len; i++) {
            if (a.charAt(i) == b.charAt(i)) match++;
            else break;
        }
        return (double) match / len;
    }

    public double suffixMatchNorm(String a, String b) {
        int len = Math.min(a.length(), b.length());
        if (len == 0) return 0.0;

        int match = 0;
        for (int i = 1; i <= len; i++) {
            if (a.charAt(a.length() - i) == b.charAt(b.length() - i)) match++;
            else break;
        }
        return (double) match / len;
    }

    public double charOverlapNorm(String a, String b) {
        if (a.isEmpty() || b.isEmpty()) return 0.0;

        long common = a.chars()
                .distinct()
                .filter(c -> b.indexOf(c) >= 0)
                .count();

        return (double) common / Math.max(a.length(), b.length());
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

    private String extractVowels(String word) {
        StringBuilder sb = new StringBuilder();
        for (char c : word.toCharArray()) {
            if (VOWELS.contains(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public double vowelPatternMatch(String a, String b) {
        String va = extractVowels(a);
        String vb = extractVowels(b);

        if (va.isEmpty() && vb.isEmpty()) return 1.0;
        if (va.isEmpty() || vb.isEmpty()) return 0.0;

        int lev = levenshtein.apply(va, vb);
        int maxLen = Math.max(va.length(), vb.length());

        return 1.0 - (double) lev / maxLen;
    }

    public double[] extractFeatures(String input, String candidate) {
        return new double[] {
                Math.pow(levenshteinNorm(input, candidate), 10),
                Math.pow(isDistanceLE(input, candidate, 1) ? 1: 0, 5),
                Math.pow(isDistanceLE(input, candidate, 2) ? 1: 0, 3),
                Math.pow(lengthDiffNorm(input, candidate), 3),
                prefixMatchNorm(input, candidate),
                suffixMatchNorm(input, candidate),
                Math.pow(charOverlapNorm(input, candidate), 10),
                Math.pow(ngramOverlapNorm(input, candidate, 2), 3),
                Math.pow(ngramOverlapNorm(input, candidate, 3), 7.5),
                vowelPatternMatch(input, candidate)
//                Math.pow(levenshteinNorm(input, candidate), 2),
//                Math.pow(isDistanceLE(input, candidate, 1) ? 1: 0, 5),
//                Math.pow(isDistanceLE(input, candidate, 2) ? 1: 0, 3),
//                Math.pow(lengthDiffNorm(input, candidate), 2),
//                Math.pow(prefixMatchNorm(input, candidate), 5),
//                Math.pow(suffixMatchNorm(input, candidate), 5),
//                Math.pow(charOverlapNorm(input, candidate), 0.5),
//                Math.pow(ngramOverlapNorm(input, candidate, 2), 3),
//                Math.pow(ngramOverlapNorm(input, candidate, 3), 10),
//                Math.pow(vowelPatternMatch(input, candidate), 0.75)
        };
    }
}