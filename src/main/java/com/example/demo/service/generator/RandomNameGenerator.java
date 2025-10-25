package com.example.demo.service.generator;

import com.example.demo.model.NameRegistry;
import com.example.demo.repository.NameRegistryRepo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
public class RandomNameGenerator {
    private static final Long MAX_SEQUENTIAL_COUNT = 1_000_000L;
    private final NameRegistryRepo nameRegistryRepo;
    private Integer totalNameCount;
    private List<NameRegistry> nameRegistries;
    private Random random = new Random();

    public void init(){
        this.nameRegistries = nameRegistryRepo.findAll();
        this.totalNameCount = nameRegistries.size();
    }

    /**
     * Generates a set of random full names using the provided NameRegistry list.
     * The maximum number of unique combinations is defined by {@code maxUniqCount}
     * combination format is {@code firstName + midName + lastName}.
     *
     * @param count the number of unique full names to generate
     * @return a Set of unique full names
     * @throws IllegalArgumentException if count > max possible unique combinations
     */
    public Set<String> generate(Long count) {
        this.init();
        long maxUniqCount = (long) Math.pow(nameRegistries.size(), 3);

        if (maxUniqCount < count)
            throw new IllegalArgumentException(String.format("Can't generate %s uniq names! max uniq name's count is: %s", count, maxUniqCount));

        Set<String> randomFullNames;
        if (count <= MAX_SEQUENTIAL_COUNT) {
            randomFullNames = new HashSet<>();
            this.sequentialGenerate(randomFullNames, count);
        } else {
            randomFullNames = ConcurrentHashMap.newKeySet();
            this.parallelGenerate(randomFullNames, count);
        }

        return randomFullNames;
    }

    /**
     * Generates full names sequentially
     *
     * @param randomFullNames the set to store generated names
     * @param count           the target number of names
     */
    private void sequentialGenerate(Set<String> randomFullNames, Long count) {
        while (randomFullNames.size() < count) {
            String randFirstName = nameRegistries.get(random.nextInt(totalNameCount)).getFirstName();
            String randMidName = nameRegistries.get(random.nextInt(totalNameCount)).getFirstName();
            String randLastName = nameRegistries.get(random.nextInt(totalNameCount)).getLastName();
            randomFullNames.add(String.format("%s %s %s", randFirstName, randMidName, randLastName));
        }
    }

    /**
     * Generates full names in parallel
     *
     * @param randomFullNames the set to store generated names
     * @param count           the target number of names
     */
    private void parallelGenerate(Set<String> randomFullNames, Long count) {
        LongStream.range(0, count)
                .parallel()
                .forEach(i -> {
                    String randFirstName = nameRegistries.get(ThreadLocalRandom.current().nextInt(totalNameCount)).getFirstName();
                    String randMidName = nameRegistries.get(ThreadLocalRandom.current().nextInt(totalNameCount)).getFirstName();
                    String randLastName = nameRegistries.get(ThreadLocalRandom.current().nextInt(totalNameCount)).getLastName();
                    randomFullNames.add(String.format("%s %s %s", randFirstName, randMidName, randLastName));
                });

        if (randomFullNames.size() < count)
            this.sequentialGenerate(randomFullNames, count);
    }

}
