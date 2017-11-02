package ru.javaops.masterjava.matrix;

import java.util.concurrent.*;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    private final static Object LOCK = new Object();

    public static int[][] randomFillMatrix(final int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = (int)(100 * Math.random());
            }
        }
        return matrix;
    }

    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }
                matrixC[i][j] = sum;
            }
        }
        return matrixC;
    }

    public static int[][] multiThreadsMultiply(final int[][] matrixA, final int[][] matrixB, final int maxThreads, final int way) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        ExecutorService service = Executors.newFixedThreadPool(maxThreads);

        switch (way) {
            case 0:

                for (int i = 0; i < matrixSize; i++) {
                    for (int j = 0; j < matrixSize; j++) {
                        service.execute(new RowMultiplicator(matrixC, matrixA, matrixB, i, j));
                    }
                }

                break;

            case 1:

                for (int i = 0; i < maxThreads; i++) {
                    service.execute(new RowsMultiplicator(matrixC, matrixA, matrixB, maxThreads, i));
                }

                break;

            default:
                break;
        }

        service.shutdown();

        while (!service.isTerminated()) {

        }

        return matrixC;
    }

    private static class RowMultiplicator implements Runnable {

        final int[][] resultMatrix;
        final int[][] matrixA;
        final int[][] matrixB;
        final int row;
        final int column;

        public RowMultiplicator(final int[][] resultMatrix, final int[][] matrixA, final int[][] matrixB, final int row, final int column) {
            this.resultMatrix = resultMatrix;
            this.matrixA = matrixA;
            this.matrixB = matrixB;
            this.row = row;
            this.column = column;
        }

        @Override
        public void run() {
            int sum = 0;

            for (int i = 0; i < matrixA[row].length; i++) {
                sum += matrixA[row][i] * matrixB[i][column];
            }

            setElement(resultMatrix, row, column, sum);
        }
    }

    private static class RowsMultiplicator implements Runnable {

        final int[][] resultMatrix;
        final int[][] matrixA;
        final int[][] matrixB;
        final int maxThreads;
        final int threadNumber;

        public RowsMultiplicator(int[][] resultMatrix, int[][] matrixA, int[][] matrixB, int maxThreads, int threadNumber) {
            this.resultMatrix = resultMatrix;
            this.matrixA = matrixA;
            this.matrixB = matrixB;
            this.maxThreads = maxThreads;
            this.threadNumber = threadNumber;
        }

        @Override
        public void run() {
            int matrixSize = matrixA.length;
            int vectorsPerThread = resultMatrix.length / maxThreads;
            int start = threadNumber * vectorsPerThread;
            int end = (threadNumber == maxThreads - 1) ? matrixSize : (threadNumber + 1) * vectorsPerThread;

            for (int i = start; i < end; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    int sum = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        sum += matrixA[i][k] * matrixB[k][j];
                    }
                    setElement(resultMatrix, i, j, sum);
                }
            }
        }
    }


    private static void setElement(final int[][] matrix, final int row, final int column, final int value) {
        synchronized (LOCK) {
            matrix[row][column] = value;
        }
    }
}