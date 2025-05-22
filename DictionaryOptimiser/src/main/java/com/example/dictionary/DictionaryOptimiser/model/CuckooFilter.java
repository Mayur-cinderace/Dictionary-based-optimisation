package com.example.dictionary.DictionaryOptimiser.model;
import java.util.Random;

public class CuckooFilter {
    private static final int FINGERPRINT_SIZE = 16;
    private static final int BUCKET_SIZE = 8; // Increased from 4
    private static final int MAX_KICKS = 500;

    private final short[][] buckets; // changed from byte to short
    private final int bucketCount;
    private final Random random;

    public CuckooFilter(int capacity) {
        this.bucketCount = Math.max(1, capacity / BUCKET_SIZE);
        this.buckets = new short[bucketCount][BUCKET_SIZE];
        this.random = new Random();
    }

    public boolean insert(String word) {
        short fingerprint = fingerprint(word);
        int i1 = hash1(word);
        int i2 = altIndex(i1, fingerprint);

        if (placeInBucket(i1, fingerprint) || placeInBucket(i2, fingerprint)) {
            return true;
        }

        int i = random.nextBoolean() ? i1 : i2;
        for (int n = 0; n < MAX_KICKS; n++) {
            int slot = random.nextInt(BUCKET_SIZE);
            short evicted = buckets[i][slot];
            buckets[i][slot] = fingerprint;

            i = altIndex(i, evicted);
            if (placeInBucket(i, evicted)) {
                return true;
            }
        }

        return false;
    }

    public boolean mightContain(String word) {
        short fingerprint = fingerprint(word);
        int i1 = hash1(word);
        int i2 = altIndex(i1, fingerprint);

        return bucketHas(i1, fingerprint) || bucketHas(i2, fingerprint);
    }

    // Use 16-bit fingerprint
    private short fingerprint(String word) {
        return (short) (Math.abs(word.hashCode()) & 0xFFFF);
    }

    private int hash1(String word) {
        return Math.abs(word.hashCode()) % bucketCount;
    }

    private int altIndex(int index, short fingerprint) {
        int hash = index ^ fingerprint;
        return Math.floorMod(hash, bucketCount);
    }

    private boolean placeInBucket(int index, short fingerprint) {
        for (int i = 0; i < BUCKET_SIZE; i++) {
            if (buckets[index][i] == 0) {
                buckets[index][i] = fingerprint;
                return true;
            }
        }
        return false;
    }

    private boolean bucketHas(int index, short fingerprint) {
        for (int i = 0; i < BUCKET_SIZE; i++) {
            if (buckets[index][i] == fingerprint) {
                return true;
            }
        }
        return false;
    }
}
