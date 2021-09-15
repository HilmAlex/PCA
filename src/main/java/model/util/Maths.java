package model.util;

import model.Matrix;

import java.util.Arrays;
import java.util.HashMap;

/**
 * La clase Maths tiene metodos estaticos para realizar operaciones con vectores y matrices.
 */
public class Maths {

    /**
     * Calcula el promedio de un vector de datos
     *
     * @param data Vector a calcular el promedio
     * @return double: Promedio del vector ingresado
     */
    public static double calcAverage(double[] data) {
        int numData = data.length;

        double average = 0;
        for (int i = 0; i < numData; i++) {
            average += data[i];
        }

        return average / numData;
    }

    /**
     * Normaliza una matriz de datos restando a cada elemento el promedio de la columna a la que pertenece
     *
     * @param data Matriz de datos a normalizar
     * @return double[][] : Matriz normalizada
     */
    public static double[][] normalizeData(Matrix data) {
        int dimension = data.getDimension();
        int samples = data.getSamples();

        double[][] newData = new double[samples][dimension];

        for (int i = 0; i < dimension; i++) {
            double[] newVector = normalizeVector(data.getAxis(i));
            for (int j = 0; j < samples; j++) {
                newData[j][i] = newVector[j];
            }
        }

        return newData;
    }

    /**
     * Normaliza un vector de datos restando a cada elemento el promedio del vector
     * @param data Vector de datos a normalizar
     * @return double[] : Vector normalizado
     */
    public static double[] normalizeVector(double[] data) {
        int numData = data.length;

        double[] newVector = Arrays.copyOf(data, numData);
        double average = calcAverage(data);

        for (int i = 0; i < numData; i++) {
            newVector[i] -= average;
        }

        return newVector;
    }

    /**
     * Calcula la varianza de un vector normalizado de datos
     * @param normalizedData Vector de datos a calcular la varianza
     * @return double : Varianza del vector ingresado
     */
    public static double calcVariance(double[] normalizedData) {
        int numData = normalizedData.length;
        double variance = 0;

        for (int i = 0; i < numData; i++) {
            variance += Math.pow(normalizedData[i], 2);
        }

        return variance / (numData - 1);
    }

    /**
     * Calcula la covarianza de dos vectores normalizados de datos
     * @param normalizeDataX Primer vector normalizado
     * @param normalizedDataY Segundo vector normalizado
     * @return double : Covarianza de los dos vectores ingresados
     */
    public static double calcCovariance(double[] normalizeDataX, double[] normalizedDataY) {
        int numData = normalizeDataX.length;
        double covariance = 0;

        for (int i = 0; i < numData; i++) {
            covariance += normalizeDataX[i] * normalizedDataY[i];
        }

        return covariance / (numData - 1);
    }

    /**
     * Calcula la matriz de varianza-covarianza de una matriz normalizada
     * @param normalizedData Matriz normalizada a calcular la matriz de varianza-covarianza
     * @return double[][] : Matriz de varianza-covarianza
     */
    public static double[][] calcCovarianceMatrix(Matrix normalizedData) {
        int dimension = normalizedData.getDimension();
        int samples = normalizedData.getSamples();

        double[][] matrix = new double[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (i == j) {
                    matrix[i][j] = calcVariance(normalizedData.getAxis(i));
                } else {
                    matrix[i][j] = calcCovariance(normalizedData.getAxis(i), normalizedData.getAxis(j));
                }
            }
        }

        return matrix;
    }

    /**
     * Obtiene los valores propios a partir de una matriz de varianza-covarianza
     * @param covarianceMatrix Matriz de covarianza a calcular los valores propios
     * @return double[] : Vector con los valores propios de la matriz de varianza-covarianza
     */
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
     * Obtiene los vectores propios de una matriz de varianza-covarianza
     * @param covarianceMatrix Matriz de varianza-covarianza a calcular los vectores propios
     * @return HashMap(Double, double[]) : Hashmap, la clave es el valor propio y el valor es el vector propio asociado al valor propio
     */
    public static HashMap<Double, double[]> getEigenvectors(double[][] covarianceMatrix) {
        HashMap<Double, double[]> eigenvectors = new HashMap<Double, double[]>();
        double[] eigenvalues = getEigenvalues(covarianceMatrix);

        for (int i = 0; i < covarianceMatrix.length; i++) {
            double currentEigenvalue = eigenvalues[i];
            double[] currentEigenvector = getEigenvector(covarianceMatrix, currentEigenvalue);
            eigenvectors.put(currentEigenvalue, currentEigenvector);
        }

        return eigenvectors;
    }


    /**
     * Obtiene el vector propio de una matriz de covarianza asociado a un valor propio
     * @param covarianceMatrix Matriz de covarianza a calcular un vector propio
     * @param eigenvalue Valor propio asociado al vector propio a calcular
     * @return double[] : Vector propio de la matriz de covarianza asociado al valor propio ingresado
     */
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

    /**
     * Obtiene el vector unitario de un vector (modulo 1)
     * @param vector Vector a calcular su vector unitario
     * @return double[] Vector unitario del vector ingresado
     */
    public static double[] getUnitVector(double[] vector) {
        double module = 0;

        for (int i = 0; i < vector.length; i++) {
            module += Math.pow(vector[i], 2);
        }

        module = Math.sqrt(module);

        double finalModule = module;

        double[] unitVector = Arrays.stream(vector).map(c -> c / finalModule).toArray();

        return unitVector;
    }
}