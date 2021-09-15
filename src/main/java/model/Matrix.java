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
     * Constructor
     * @param data double[][] : Matriz de datos
     */
    public Matrix(double[][] data) {
        double[][] copy = copyTwoDimensionalArray(data);
        this.data = copy;
        this.dimension = copy[0].length;
        this.samples = copy.length;
        this.covarianceMatrix = Maths.calcCovarianceMatrix(this);
        this.eigenvalues = Maths.getEigenvalues(this.covarianceMatrix);
        this.eigenvectors = Maths.getEigenvectors(this.covarianceMatrix);
    }

    /**
     * Obtiene el elemento en un fila y columna especifica
     * @param row Fila del elemento
     * @param column Columna del elemento
     * @return double : Elemento en la fila y columna ingresados
     */
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

    /**
     * Imprime los valores propios de la matriz de covarianza
     */
    public void printEigenvalues() {
        Arrays.stream(eigenvalues).forEach(System.out::println);
    }

    /**
     * Imprime los vectores propios de la matriz de covarianza
     */
    public void printEigenvectors() {
            System.out.println("Valor Propio Asociado " + "Vector Propio");
        for (int i = 0; i < eigenvalues.length; i++) {
            double currentEigenvalue = eigenvalues[i];
            double[] currentEigenvector = eigenvectors.get(currentEigenvalue);
            System.out.println(currentEigenvalue +String.format("%8s","")+currentEigenvector[0]);
            System.out.println(String.format("%35f", currentEigenvector[1]));

        }
    }

    /**
     * Getter
     * @return double[][] : Matriz de covarianza
     */
    public double[][] getCovarianceMatrix() {
        return covarianceMatrix;
    }

    /**
     * Getter
     * @return int : Dimension de la matriz
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * Getter
     * @return int : Muestras en cada columna de la matriz
     */
    public int getSamples() {
        return samples;
    }

    /**
     * Recibe un array bidimensional y devuelve una copia del mismo
     * @param array Array a copiar
     * @return double[][] : Copia del array ingresado
     */
    private double[][] copyTwoDimensionalArray(double[][] array) {
        int rows = array.length;
        int columns = array[0].length;
        double[][] copy = new double[rows][columns];

        for (int i = 0; i < rows; i++) {
            copy[i] = Arrays.copyOf(array[i], columns);
        }

        return copy;
    }

    /**
     * Getter
     * @return double[] : Valores propios de la matriz de covarianza
     */
    public double[] getEigenvalues() {
        return eigenvalues;
    }

    /**
     * Getter
     * @param eigenvalue Valor propio asociado al vector propio a obtener
     * @return double[] : Vector propio de la matriz de covarianza asociado al valor propio ingresado
     */
    public double[] getEigenvector(double eigenvalue) {
        return eigenvectors.get(eigenvalue);
    }

    /**
     * Getter
     * @return HashMap<Double, double[]> : Vectores propios de la matriz de covarianza
     */
    public HashMap<Double, double[]> getEigenvectors() {
        return eigenvectors;
    }
}
