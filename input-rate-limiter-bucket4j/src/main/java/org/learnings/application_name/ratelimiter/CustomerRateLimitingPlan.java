package org.learnings.application_name.ratelimiter;

import io.github.bucket4j.Bandwidth;

import java.time.Duration;

enum CustomerRateLimitingPlan {
    FREE {
        Bandwidth getLimit() {
            return Bandwidth.builder().capacity(2).refillIntervally(2, Duration.ofSeconds(3)).build();
        }
    },
    BASIC {
        Bandwidth getLimit() {
            return Bandwidth.builder().capacity(50).refillIntervally(50, Duration.ofSeconds(1)).build();
        }
    },
    ENTERPRISE {
        Bandwidth getLimit() {
            return Bandwidth.builder().capacity(80).refillIntervally(80, Duration.ofSeconds(1)).build();
        }
    };

    abstract Bandwidth getLimit();

    static CustomerRateLimitingPlan getPlan(String apiKey) {
        if (apiKey.startsWith("BC001-")) return BASIC;
        if (apiKey.startsWith("EC001-"))  return ENTERPRISE;
        return FREE;
    }
}
