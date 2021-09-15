package model;

import model.util.Maths;

import java.util.Arrays;

public class PCA {
    private Matrix data;
    private Matrix normalizedData;
    private double[] reducedData;

    /**
     * Constructor
     * @param data Datos a procesar utilizando PCA
     */
    public PCA(double[][] data) {
        this.data = new Matrix(data);
        this.normalizedData = new Matrix(Maths.normalizeData(this.data));
        reduce();
    }

    /**
     * Reduce los datos originales a datos con una menor dimension y con la maxima conservacion de informacion y
     * los almacena en el vector reducedData
     */
    public void reduce() {
        double[] eigenvalues = normalizedData.getEigenvalues();

        double maxEigenvalue = Arrays.stream(eigenvalues).max().getAsDouble();
        double[] eigenvector = normalizedData.getEigenvector(maxEigenvalue);
        double[] direction = Maths.getUnitVector(eigenvector);

        int samples = data.getSamples();

        reducedData = new double[samples];

        for (int i = 0; i < data.getSamples(); i++) {
            reducedData[i] = data.getElement(i,0) * direction[0] + data.getElement(i,1) * direction[1];
        }
    }

    /**
     * Getter
     * @return Matrix : Matriz de datos originales
     */
    public Matrix getData() {
        return data;
    }

    /**
     * Getter
     * @return Matrix : Matriz normalizada de datos
     */
    public Matrix getNormalizedData() {
        return normalizedData;
    }

    /**
     * Getter
     * @return double[] : Vector con la informacion reducida
     */
    public double[] getReducedData() {
        return reducedData;
    }

    /**
     * Imprime la nueva informacion reducida con la mínima perdida de datos
     */
    public void printReducedData(){
        for (int i = 0; i < reducedData.length; i++) {
            System.out.println(reducedData[i]);
        }
    }

}
