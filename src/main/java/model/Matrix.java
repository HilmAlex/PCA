package model;

import java.util.Arrays;
import java.util.HashMap;

import model.util.Maths;

public class Matrix {
    private double[][] data;
    private int dimension;
    private int samples;
    private double[][] covarianceMatrix;
    private double[] eigenvalues;
    private HashMap<Double, double[]> eigenvectors;

    /**
     * @param data double[][] : Matriz de datos
     */
    public Matrix(double[][] data) {
        double[][] copy = copyTwoDimensionalArray(data);
        this.data = copy;
        this.dimension = copy[0].length;
        this.samples = copy.length;
        this.covarianceMatrix = Maths.calcCovarianceMatrix(this);
        this.eigenvalues = Maths.getEigenvalues(this.covarianceMatrix);
        this.eigenvectors = Maths.getEigenvectors(this);
    }

    public double getElement(int row, int column) {
        return data[row][column];
    }

    /**
     * @param position Posicion de la columna a obtener
     * @return double[] : Arreglo con los datos de la columna en la posicion indicada
     */
    public double[] getAxis(int position) {
        double[] currentData = Arrays.stream(this.data).mapToDouble(coordinates -> {
            return coordinates[position];
        }).toArray();

        return currentData;
    }

    /**
     * Imprime la matriz
     */
    public void print() {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                System.out.print(data[i][j] + "  ");
            }
            System.out.println();
        }
    }

    /**
     * Imprime la matriz de varianza y covarianza
     */
    public void printCovarianceMatrix() {
        for (int i = 0; i < covarianceMatrix.length; i++) {
            System.out.println();
            for (int j = 0; j < covarianceMatrix[0].length; j++) {
                System.out.print(covarianceMatrix[i][j] + "  ");
            }
        }
    }

    public void printEigenvalues() {
        Arrays.stream(eigenvalues).forEach(System.out::println);
    }

    public void printEigenvectors() {
            System.out.println("Valor Propio Asociado " + "Vector Propio");
        for (int i = 0; i < eigenvalues.length; i++) {
            double currentEigenvalue = eigenvalues[i];
            double[] currentEigenvector = eigenvectors.get(currentEigenvalue);
            System.out.println(currentEigenvalue +String.format("%8s","")+currentEigenvector[0]);
            System.out.println(String.format("%35f", currentEigenvector[1]));

        }
    }

    public double[][] getCovarianceMatrix() {
        return covarianceMatrix;
    }

    public int getDimension() {
        return dimension;
    }

    public int getSamples() {
        return samples;
    }

    private double[][] copyTwoDimensionalArray(double[][] array) {
        int rows = array.length;
        int columns = array[0].length;
        double[][] copy = new double[rows][columns];

        for (int i = 0; i < rows; i++) {
            copy[i] = Arrays.copyOf(array[i], columns);
        }

        return copy;
    }

    public double[] getEigenvalues() {
        return eigenvalues;
    }

    public double[] getEigenvector(double eigenvalue) {
        return eigenvectors.get(eigenvalue);
    }

    public HashMap<Double, double[]> getEigenvectors() {
        return eigenvectors;
    }
}
