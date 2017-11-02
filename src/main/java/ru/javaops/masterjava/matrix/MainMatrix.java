package ru.javaops.masterjava.matrix;

/**
 * gkislin
 * 03.07.2016
 */
public class MainMatrix {
    // Multiplex matrix
    private static final int MATRIX_SIZE = 1000;
    private static final int THREAD_NUMBER = 10;

    public static void main(String[] args) {
        final int[][] matrixA = new int[MATRIX_SIZE][MATRIX_SIZE];
        final int[][] matrixB = new int[MATRIX_SIZE][MATRIX_SIZE];

        MatrixUtil.randomFillMatrix(matrixA);
        MatrixUtil.randomFillMatrix(matrixB);

        long start = System.currentTimeMillis();
        final int[][] matrixC =  MatrixUtil.singleThreadMultiply(matrixA, matrixB);
        System.out.println("Single thread multiplication time, sec: " + (System.currentTimeMillis() - start)/1000.);

        // TODO implement parallel multiplication matrixA*matrixB
        // TODO compare wih matrixC;

        start = System.currentTimeMillis();
        final int[][] matrixD = MatrixUtil.multiThreadsMultiply(matrixA, matrixB, THREAD_NUMBER, 0);
        System.out.println("Multi thread multiplication time, sec: " + (System.currentTimeMillis() - start)/1000.);
        System.out.println("Matrixes are equals=" + checkMatrixEquals(matrixC, matrixD));

        start = System.currentTimeMillis();
        final int[][] matrixE = MatrixUtil.multiThreadsMultiply(matrixA, matrixB, THREAD_NUMBER, 1);
        System.out.println("Multi thread multiplication time, sec: " + (System.currentTimeMillis() - start)/1000.);
        System.out.println("Matrixes are equals=" + checkMatrixEquals(matrixC, matrixE));
    }

    private static boolean checkMatrixEquals(final int[][] matrixA, final int[][] matrixB) {

        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    System.out.printf("Not equals in raw=%d, column=%d | in MatrixC[%d][%d]=%d, in MatrixD[%d][%d]=%d\n",
                            i, j, i, j, i, j, matrixA[i][j], matrixB[i][j]);

                    return false;
                }
            }
        }

        return true;
    }

    private static void printMatrix(final int[][] matrix) {
        /*
        for (int i = 0; i < MATRIX_SIZE; i++) {
            System.out.printf("[%d]   ", i);

            for (int j = 0; j < MATRIX_SIZE; j++) {
                System.out.printf("%d ", matrixC[i][j]);
            }
            System.out.println();
        }

        System.out.println();

        for (int i = 0; i < MATRIX_SIZE; i++) {
            System.out.printf("[%d]   ", i);

            for (int j = 0; j < MATRIX_SIZE; j++) {
                System.out.printf("%d ", matrixD[i][j]);
            }
            System.out.println();
        }
        */
    }
}