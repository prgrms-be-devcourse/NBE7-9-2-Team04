package com.backend.domain.ranking.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Tier {
    UNRATED("Unrated", 0),
    BRONZE("Bronze", 300),
    SILVER("Silver", 600),
    GOLD("Gold", 900),
    PLATINUM("Platinum", 1200),
    DIAMOND("Diamond", 1500),
    RUBY("Ruby", 1800),
    MASTER("Master", 2100);

    private final String label;
    private final int minScore;

    public static Tier fromScore(int score) {
        if (score < BRONZE.minScore) return UNRATED;
        if (score < SILVER.minScore) return BRONZE;
        if (score < GOLD.minScore) return SILVER;
        if (score < PLATINUM.minScore) return GOLD;
        if (score < DIAMOND.minScore) return PLATINUM;
        if (score < RUBY.minScore) return DIAMOND;
        if (score < MASTER.minScore) return RUBY;
        return MASTER;
    }
}
