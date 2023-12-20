package agh.ics.oop.model.genotype;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MoveDirection;

import java.util.Collections;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class AbstractGenotype implements Genotype {

    protected List<MoveDirection> genotypeList;

    private static final String[] sites = {"left", "right"};

    protected AbstractGenotype(int length) {
        genotypeList = IntStream.generate(this::getRandomFromLegalRange).limit(length).mapToObj(MoveDirection::valueOf).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toCollection(ArrayList::new));
    }

    protected AbstractGenotype(Animal animal1, Animal animal2) {
        Animal stronger = animal1.getEnergyLevel() > animal2.getEnergyLevel() ? animal1 : animal2;
        Animal weaker = animal1.getEnergyLevel() <= animal2.getEnergyLevel() ? animal1 : animal2;

        List<MoveDirection> strongerGenotypeList = stronger.getGenotype().getGenotypeList();
        List<MoveDirection> weakerGenotypeList = weaker.getGenotype().getGenotypeList();
        int size = strongerGenotypeList.size();

        int strongerEnergy = stronger.getEnergyLevel();
        int weakerEnergy = weaker.getEnergyLevel();

        String siteForStronger = sites[new Random().nextInt(2)];

        if (siteForStronger.equals("left")) {
            int splitPoint = (strongerEnergy / (strongerEnergy + weakerEnergy) * size);
            genotypeList = strongerGenotypeList.subList(0, splitPoint);
            genotypeList.addAll(weakerGenotypeList.subList(splitPoint, size));
        } else if (siteForStronger.equals("right")) {
            int splitPoint = (weakerEnergy / (strongerEnergy + weakerEnergy) * size);
            genotypeList = weakerGenotypeList.subList(0, splitPoint);
            genotypeList.addAll(strongerGenotypeList.subList(splitPoint, size));
        }

        int numberOfMutations = new Random().nextInt(size);
        mutate(numberOfMutations);
    }

    @Override
    abstract public MoveDirection next();

    @Override
    public List<MoveDirection> getGenotypeList() {
        return genotypeList;
    }

    private int getRandomFromLegalRange(int prev) {
        return new Random().ints(0, 8).filter(i -> i != prev).findFirst().getAsInt();
    }

    private int getRandomFromLegalRange() {
        return getRandomFromLegalRange(-1);
    }


    private void mutate(int mutationNum) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < genotypeList.size(); i++) {
            indexes.add(i);
        }

        Collections.shuffle(indexes);

        List<Integer> indexesToMutate = indexes.subList(0, mutationNum);

        for (Integer idx : indexesToMutate) {
            int prev = genotypeList.get(idx).ordinal();
            int mutated = getRandomFromLegalRange(prev);
            genotypeList.set(idx, MoveDirection.valueOf(mutated).get());
        }

    }
}