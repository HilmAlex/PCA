package model;

import model.util.Maths;

import java.util.Arrays;

public class PCA {
    private Matrix data;
    private Matrix dataCenter;
    private double[] reducedData;

    public PCA(double[][] data) {
        this.data = new Matrix(data);
        this.dataCenter = new Matrix(Maths.centerData(this.data));
        reduce();
    }

    public void reduce() {
        double[] eigenvalues = dataCenter.getEigenvalues();

        double maxEigenvalue = Arrays.stream(eigenvalues).max().getAsDouble();
        double[] eigenvector = dataCenter.getEigenvector(maxEigenvalue);
        double[] direction = getUnitVector(eigenvector);

        int samples = data.getSamples();
        int dimension = data.getDimension();

        reducedData = new double[samples];

        for (int i = 0; i < data.getSamples(); i++) {
            reducedData[i] = data.getElement(i,0) * direction[0] + data.getElement(i,1) * direction[1];
        }
    }

    public double[] getUnitVector(double[] vector){
        double module = 0;

        for (int i = 0; i < vector.length; i++) {
            module += Math.pow(vector[i], 2);
        }

        module = Math.sqrt(module);

        double finalModule = module;

        double[] unitVector = Arrays.stream(vector).map(c -> c/ finalModule).toArray();

        return unitVector;
    }

    /**
     * Calculo de la proyeccion de un vector sobre otro (direccion)
     *
     * @param data : Vector a proyector
     * @return double : Valor de la magnitud de la proyeccion del vector data sobre el vector direccion
     */
    private double projectData(double[] data, double[] direction) {
        // Dot Product
        double dotProduct = 0;

        for (int i = 0; i < data.length; i++) {
            dotProduct += data[i] * direction[i];
        }

        double directionMagnitude = Arrays.stream(direction).reduce(0, (c1, c2) -> Math.pow(c1, 2) + Math.pow(c2, 2));

        return dotProduct / directionMagnitude;
    }

    public Matrix getData() {
        return data;
    }

    public Matrix getDataCenter() {
        return dataCenter;
    }

    public double[] getReducedData() {
        return reducedData;
    }

    public void printReducedData(){
        for (int i = 0; i < reducedData.length; i++) {
            System.out.println(reducedData[i]);
        }
    }

}
