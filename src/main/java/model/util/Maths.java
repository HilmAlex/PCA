package model.util;

import model.Matrix;

import java.util.Arrays;
import java.util.HashMap;

public class Maths {
    public static double calcAverage(double[] data) {
        int numData = data.length;

        double average = 0;
        for (int i = 0; i < numData; i++) {
            average += data[i];
        }

        return average / numData;
    }

    public static double[][] centerData(Matrix data) {
        int dimension = data.getDimension();
        int samples = data.getSamples();

        double[][] newData = new double[samples][dimension];

        for (int i = 0; i < dimension; i++) {
            double[] newVector = centerVector(data.getAxis(i));
            for (int j = 0; j < samples; j++) {
                newData[j][i] = newVector[j];
            }
        }

        return newData;
    }

    public static double[] centerVector(double[] data) {
        int numData = data.length;

        double[] newVector = Arrays.copyOf(data, numData);
        double average = calcAverage(data);

        for (int i = 0; i < numData; i++) {
            newVector[i] -= average;
        }

        return newVector;
    }

    public static double calcVariance(double[] data) {
        int numData = data.length;
        double variance = 0;

        for (int i = 0; i < numData; i++) {
            variance += Math.pow(data[i], 2);
        }

        return variance / (numData - 1);
    }

    public static double calcCovariance(double[] dataX, double[] dataY) {
        int numData = dataX.length;
        double covariance = 0;

        for (int i = 0; i < numData; i++) {
            covariance += dataX[i] * dataY[i];
        }

        return covariance / (numData - 1);
    }

    public static double[][] calcCovarianceMatrix(Matrix data) {
        int dimension = data.getDimension();
        int samples = data.getSamples();

        double[][] matrix = new double[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (i == j) {
                    matrix[i][j] = calcVariance(data.getAxis(i));
                } else {
                    matrix[i][j] = calcCovariance(data.getAxis(i), data.getAxis(j));
                }
            }
        }

        return matrix;
    }

    public static double[] getEigenvalues(double[][] covarianceMatrix) {
        double[] eigenValues = new double[2];

        double varianceX = covarianceMatrix[0][0];
        double varianceY = covarianceMatrix[1][1];
        double a = 1;
        double b = -(varianceX + varianceY);
        double c = varianceX * varianceY - covarianceMatrix[0][1] * covarianceMatrix[1][0];

        double eigenvalue1 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
        double eigenvalue2 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);

        eigenValues[0] = eigenvalue1;
        eigenValues[1] = eigenvalue2;

        return eigenValues;
    }

    /**
     *
     */
    public static HashMap<Double, double[]> getEigenvectors(Matrix data) {
        double[][] covarianceMatrix = Maths.calcCovarianceMatrix(data);
        HashMap<Double, double[]> eigenvectors = new HashMap<Double, double[]>();
        double[] eigenvalues = data.getEigenvalues();

        for (int i = 0; i < covarianceMatrix.length; i++) {
            double currentEigenvalue = eigenvalues[i];
            double[] currentEigenvector = getEigenvector(covarianceMatrix, currentEigenvalue);
            eigenvectors.put(currentEigenvalue, currentEigenvector);
        }

        return eigenvectors;
    }


    public static double[] getEigenvector(double[][] covarianceMatrix, double eigenvalue) {
        double[] eigenvector = new double[2];

        double varianceX = covarianceMatrix[0][0];
        double covariance = covarianceMatrix[0][1];
        double varianceY = covarianceMatrix[1][1];

        double a = varianceX - eigenvalue;
        double b = covariance;
        double c = varianceY - eigenvalue;

        if (a != 0) {
            double[][] enlargedMatrix = new double[2][2];

            enlargedMatrix[0][0] = a / a;
            enlargedMatrix[0][1] = b / a;
            enlargedMatrix[1][0] = b - b * enlargedMatrix[0][0];
            enlargedMatrix[1][1] = c - b * enlargedMatrix[0][1];

            eigenvector[0] = enlargedMatrix[0][1] * (-1);
            eigenvector[1] = enlargedMatrix[0][0];
        } else {
            throw new Error("Elemento 00 es 0");
        }

        return eigenvector;
    }
}