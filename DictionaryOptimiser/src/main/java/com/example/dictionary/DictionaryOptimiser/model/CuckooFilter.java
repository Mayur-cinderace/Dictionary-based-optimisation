package com.example.dictionary.DictionaryOptimiser.model;
import java.util.Random;

public class CuckooFilter {
    private static final int FINGERPRINT_SIZE = 8;
    private static final int BUCKET_SIZE = 4;
    private static final int MAX_KICKS = 500; // max number of evictions

    private final byte[][] buckets;
    private final int bucketCount;
    private final Random random;

    public CuckooFilter(int capacity) {
        this.bucketCount = capacity / BUCKET_SIZE;
        this.buckets = new byte[bucketCount][BUCKET_SIZE];
        this.random = new Random();
    }

    public boolean insert(String word) {
        byte fingerprint = fingerprint(word);
        int i1 = hash1(word);
        int i2 = altIndex(i1, fingerprint);

        if (placeInBucket(i1, fingerprint) || placeInBucket(i2, fingerprint)) {
            return true;
        }

        int i = random.nextBoolean() ? i1 : i2;
        for (int n = 0; n < MAX_KICKS; n++) {
            int slot = random.nextInt(BUCKET_SIZE);
            byte evicted = buckets[i][slot];
            buckets[i][slot] = fingerprint;

            i = altIndex(i, evicted);
            if (placeInBucket(i, evicted)) {
                return true;
            }
        }

        return false;
    }

    // Public lookup method
    public boolean mightContain(String word) {
        byte fingerprint = fingerprint(word);
        int i1 = hash1(word);
        int i2 = altIndex(i1, fingerprint);

        return bucketHas(i1, fingerprint) || bucketHas(i2, fingerprint);
    }

    private byte fingerprint(String word) {
        return (byte) (Math.abs(word.hashCode()) & 0xFF);
    }

    private int hash1(String word) {
        return Math.abs(word.hashCode()) % bucketCount;
    }

    private int altIndex(int index, byte fingerprint) {
        return (index ^ (fingerprint & 0xFF)) % bucketCount;
    }

    private boolean placeInBucket(int index, byte fingerprint) {
        for (int i = 0; i < BUCKET_SIZE; i++) {
            if (buckets[index][i] == 0) {
                buckets[index][i] = fingerprint;
                return true;
            }
        }
        return false;
    }

    private boolean bucketHas(int index, byte fingerprint) {
        for (int i = 0; i < BUCKET_SIZE; i++) {
            if (buckets[index][i] == fingerprint) {
                return true;
            }
        }
        return false;
    }
}
