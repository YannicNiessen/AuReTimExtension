package de.stzeyetrial.auretim.util;

import java.sql.Array;
import java.util.*;

public  class RandomSequence {

    public static final int MAXIMUM_TRIES = 10000000;



    public static Integer[] getRandomSequenceNBack(int length, int nOptions, int nRepeat, int nMatch, int nLures, int nBackLevel, boolean reUseDigit) throws Exception {
        List<Integer> digits = new ArrayList<>();
        while(digits.size() < nRepeat){
            int newDigit = (int) (Math.random() * nOptions);
            if (!digits.contains(newDigit)){
                digits.add(newDigit);
            }
        }

        int counter = 0;
        while(counter < MAXIMUM_TRIES){
            counter++;
            Integer[] newSequence = new Integer[length];
            List<Integer> usedDigits = new ArrayList<>();

            for (int i = 0; i < length; i++) {
                if (reUseDigit){
                    newSequence[i] = digits.get((int) (Math.random() * nRepeat));
                }else{
                    int newDigit = digits.get((int) (Math.random() * nRepeat));

                    while (usedDigits.contains(newDigit)){
                        newDigit = digits.get((int) (Math.random() * nRepeat));
                    }
                    newSequence[i] = newDigit; //TODO
                    if (i >= nBackLevel && newSequence[i].equals(newSequence[i - nBackLevel])){
                        usedDigits.add(newDigit);
                    }

                }
            }

            int detectedLures = 0;
            int detectedMatches = 0;

            List<Integer> firstN = Arrays.asList(Arrays.copyOfRange(newSequence, 0, nBackLevel));

            for (int i = 0; i < nBackLevel; i++) {
                if (firstN.lastIndexOf(newSequence[i]) != i)
                    detectedLures++;
            }

            for (int i = nBackLevel; i < length; i++) {
                int currentValue = newSequence[i];
                Integer[] relevantSlice = Arrays.copyOfRange(newSequence, i - nBackLevel, i);
                if (relevantSlice[0] == currentValue){
                    detectedMatches++;
                    continue;
                }

                for (int j = 1; j < relevantSlice.length; j++) {
                    if (relevantSlice[j] == currentValue){
                        detectedLures++;
                        break;
                    }

                }

            }

            Set<Integer> uniqueNumbers = new LinkedHashSet<>(Arrays.asList(newSequence));


            if (detectedMatches == nMatch && detectedLures == nLures && uniqueNumbers.size() == nRepeat){
                return newSequence;
            }

        }
        if (counter == MAXIMUM_TRIES){
            throw new Exception("No Random Sequence found");
        }
        return null;
    }

    public static Boolean[] getRandomSequenceMackworthClock(int length, int hits) throws Exception {

        if (hits > length){
            throw new Exception("More hits than length given. No random sequence possible");
        }

        List<Integer> hitIndices = new ArrayList<>();

        while (hitIndices.size() != hits){
            int newIndex = (int) (Math.random() * length);
            if (!hitIndices.contains(newIndex))
                hitIndices.add(newIndex);
        }
        Boolean[] resultSequence = new Boolean[length];
        for (int i = 0; i < length; i++){
            resultSequence[i] = false;
        }

        for (int i = 0; i < hitIndices.size(); i++){
            resultSequence[hitIndices.get(i)] = true;
        }
        return resultSequence;
    }

    public static Boolean[] getMackworthClockSequenceFromTimings(int length, int[] timings){

        Boolean[] resultSequence = new Boolean[length];
        for (int i = 0; i < length; i++){
            resultSequence[i] = false;
        }

        for (int i = 0; i < timings.length; i++){
            resultSequence[timings[i]] = true;
        }

        return resultSequence;

    }


    public static Integer[] spatialWorkingMemoryUpdateSequence(int length){

        Integer[] result = new Integer[length];

        result[0] = (int) (Math.random() * 9);

        for (int i = 1; i < length; i++) {

            int last = result[i-1];

            int col = last % 3;
            int row = (last / 3);

            List<Integer> availableDigits = new ArrayList<>();

            for (int j = 0; j < 9; j++) {

                if (Math.abs(j % 3 - col) < 2 && Math.abs((j / 3) - row) < 2 && j != last){
                    availableDigits.add(j);
                }

            }

            int randomIndex = (int) (Math.random() * availableDigits.size());
            result[i] = availableDigits.get(randomIndex);

        }

        return result;
    }

    public static Integer[] randomNBackSequence(int length, int nOptions, int nRepeat, int nMatch, int nLures, int nBackLevel, boolean reUseDigit){

        List<Integer> digits = new ArrayList<>();
        while(digits.size() < nRepeat){
            int newDigit = (int) (Math.random() * nOptions);
            if (!digits.contains(newDigit)){
                digits.add(newDigit);
            }
        }

        Integer[] sequence = new Integer[length];
        int[] matchIndices = new int[nMatch];

        for (int i = 0; i < nMatch; i++) {

            int newIndex = nBackLevel + ((int) (Math.random() * (length - nBackLevel)));
            while (sequence[newIndex] != null || sequence[newIndex - nBackLevel] != null || ((newIndex + nBackLevel < length) && sequence[newIndex + nBackLevel] != null)) {
                newIndex = nBackLevel + ((int) (Math.random() * (length - nBackLevel)));
            }
            matchIndices[i] = newIndex;
            int digit = digits.get((int) (Math.random() * digits.size()));
            while(matchIndices[i] >= 2 * nBackLevel){
                if (!(sequence[matchIndices[i] - 2 * nBackLevel] != null && sequence[matchIndices[i] - 2 * nBackLevel] == digit)) break;
                digit = digits.get((int) (Math.random() * digits.size()));
            }
            sequence[matchIndices[i]] = digit;
            sequence[matchIndices[i]-nBackLevel] = digit;
        }


        List<Integer> availableDigits = new ArrayList<>(digits);

        int refillIndex = 0;

        for (int i =0; i < length; i++){
            if (sequence[i] != null) continue;
            if(availableDigits.isEmpty()){
                refillIndex = i;
                availableDigits.addAll(digits);
            }

            int randomDigit = availableDigits.get((int) (Math.random() * availableDigits.size()));

            if (i >= nBackLevel) {
                int counter = 0;

                while ((randomDigit == sequence[i - nBackLevel]) || ((i + nBackLevel < length) && (Integer.valueOf(randomDigit).equals(sequence[i + nBackLevel])))) {
                    if (counter == availableDigits.size()){
                        for (int j = i; j >= refillIndex; j--) {
                            int finalJ = j;
                            if (Arrays.stream(matchIndices).anyMatch(x -> x == finalJ || x - nBackLevel == finalJ))continue;
                            sequence[j] = null;
                        }
                        availableDigits.clear();
                        availableDigits.addAll(digits);
                        i = refillIndex;
                        counter = 0;
                    }

                    randomDigit = availableDigits.get((int) (Math.random() * availableDigits.size()));
                    counter++;
                    if (i < nBackLevel){
                        randomDigit = -1;
                        break;
                    }

                }
            }else if(sequence[i + nBackLevel] != null && randomDigit == sequence[i + nBackLevel]){
                while(randomDigit == sequence[i + nBackLevel]){
                    randomDigit = availableDigits.get((int) (Math.random() * availableDigits.size()));
                }
            }
            if(randomDigit == -1){
                i--;
                continue;
            }
            sequence[i] = randomDigit;
            availableDigits.remove((Integer) randomDigit);

        }



     return sequence;




    }

    private static Integer[] verifySequence(Integer[] sequence, int nBackLevel, int length, int nMatch, int nRepeat) throws Exception {
        int detectedLures = 0;
        int detectedMatches = 0;

        List<Integer> firstN = Arrays.asList(Arrays.copyOfRange(sequence, 0, nBackLevel));

        for (int i = 0; i < nBackLevel; i++) {
            if (firstN.lastIndexOf(sequence[i]) != i)
                detectedLures++;
        }

        for (int i = nBackLevel; i < length; i++) {
            int currentValue = sequence[i];
            Integer[] relevantSlice = Arrays.copyOfRange(sequence, i - nBackLevel, i);
            if (relevantSlice[0] == currentValue){
                detectedMatches++;
                continue;
            }

            for (int j = 1; j < relevantSlice.length; j++) {
                if (relevantSlice[j] == currentValue){
                    detectedLures++;
                    break;
                }

            }

        }

        Set<Integer> uniqueNumbers = new LinkedHashSet<>(Arrays.asList(sequence));

        String sequenceString = "";
        for (int i = 0; i < sequence.length; i++) {
            sequenceString += sequence[i] + " ";
        }


        if (length != sequence.length){
            throw new Exception("Lengths do not match. Generated sequence has length " + sequence.length);
        }
        if (detectedMatches != nMatch){
            throw new Exception("Matches do not match. Generated sequence has " + detectedMatches + " matches. Sequence is: " + sequenceString);
        }
        if(nRepeat != uniqueNumbers.size()){
            throw new Exception("nRepeat is not correct. Generated Sequence has " + uniqueNumbers.size() + " repeats");
        }

        return sequence;
    }


    public static void main(String[] args) {


        int nBackLevel = 2;
        int length = 20;
        int matches = 3;
        int nRepeat = 10;

        for (int i = 0; i < 100000; i++) {

            Integer[] s = randomNBackSequence(length, 10, nRepeat, matches, 0, nBackLevel, false);


            try {
                verifySequence(s, nBackLevel, length, matches, nRepeat);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }



    }

}

