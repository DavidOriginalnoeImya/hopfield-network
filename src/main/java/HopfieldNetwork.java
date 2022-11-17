import java.util.Arrays;

public class HopfieldNetwork {
    private final int[][] weights;

    private final int neuronNum;

    public HopfieldNetwork(int neuronNum) {
        this.neuronNum = neuronNum;
        weights = new int[neuronNum][neuronNum];
    }

    public void learn(int[][] dataSet) {
        for (int[] set: dataSet) {
            if (set.length != neuronNum) throw new RuntimeException("Wrong data set size");

            for (int index1 = 0; index1 < set.length; ++index1) {
                for (int index2 = 0; index2 < set.length; ++index2) {
                    if (index1 != index2) {
                        weights[index1][index2] += set[index1] * set[index2];
                    }
                }
            }
        }
    }

    public int[] recognize(int[] noiseVector) {
        if (noiseVector.length != neuronNum) throw new RuntimeException("Wrong noise vector size");

        int[] recognizedVector = noiseVector.clone(), prevRecognizedVector;

        int[][] alternatingVectors = new int[4][];

        int loopNum = 0;

        do {
            prevRecognizedVector = recognizedVector.clone();

            alternatingVectors[loopNum % 4] = recognizedVector.clone();

            for (int index1 = 0; index1 < weights.length; ++index1) {
                int neuronPotential = 0;

                for (int index2 = 0; index2 < weights[index1].length; ++index2) {
                    neuronPotential += weights[index1][index2] * prevRecognizedVector[index2];
                }

                recognizedVector[index1] = activation(neuronPotential);
            }
            System.out.println(calcNetworkEnergy(recognizedVector));
            ++loopNum;
        } while (!Arrays.equals(recognizedVector, prevRecognizedVector) &&
                !(loopNum > 3 && checkAlternatingStates(alternatingVectors)));

        System.out.println(loopNum);

        return recognizedVector;
    }

    private int activation(int neuronPotential) {
        return neuronPotential >= 0 ? 1 : -1;
    }

    private boolean checkAlternatingStates(int[][] states) {
        if (states.length < 4) throw new RuntimeException("Wrong states array size");

        return Arrays.equals(states[0], states[2]) && Arrays.equals(states[1], states[3]);
    }

    private int calcNetworkEnergy(int[] outVector) {
        int networkEnergy = 0;

        for (int index1 = 0; index1 < outVector.length; ++index1) {
            for (int index2 = 0; index2 < outVector.length; ++index2) {
                networkEnergy += weights[index1][index2] * outVector[index1] * outVector[index2];
            }
        }

        return networkEnergy / -2;
    }

    public static void main(String[] args) {
        int[][] dataSet = {
                {1, 1, 1, 1, -1, 1, 1, -1, 1, 1, -1, 1, 1, 1, 1 },
                {-1, -1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1},
                {1, 1, 1, -1, -1, 1, 1, 1, 1, 1, -1, -1, 1, 1, 1},
//                {1, 1, 1, -1, -1, 1, 1, 1, 1, -1, -1, 1, 1, 1, 1},
//                {1, -1, 1, 1, -1, 1, 1, 1, 1, -1, -1, 1, -1, -1, 1},
//                {1, 1, 1, 1, -1, -1, 1, 1, 1, -1, -1, 1, 1, 1, 1},
//                {1, 1, 1, 1, -1, -1, 1, 1, 1, 1, -1, 1, 1, 1, 1},
//                {1, 1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1},
//                {1, 1, 1, 1, -1, 1, 1, 1, 1, 1, -1, 1, 1, 1, 1},
//                {1, 1, 1, 1, -1, 1, 1, 1, 1, -1, -1, 1, 1, 1, 1},
        };

        int[] noiseVector = {1, -1, 1, 1, -1, 1, 1, 1, 1, 1, -1, 1, 1, -1, 1};

        HopfieldNetwork hopfieldNetwork = new HopfieldNetwork(15);
        hopfieldNetwork.learn(dataSet);
        System.out.println(Arrays.toString(hopfieldNetwork.recognize(noiseVector)));


        for (int index1 = 0; index1 < hopfieldNetwork.weights.length; ++index1) {
            for (int index2 = 0; index2 < hopfieldNetwork.weights[index1].length; ++index2) {
                System.out.print(hopfieldNetwork.weights[index1][index2] + " ");
            }
            System.out.println();
        }


    }
}
