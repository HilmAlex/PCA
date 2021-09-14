import model.PCA;
import model.util.Maths;

import java.util.Arrays;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        double[][] test = {{-3, 7}, {1, 4},
                {0, 6}, {3, 5}, {1, 9}, {7, 7}, {6, 9}, {8, 10}, {7, 12}, {10, 11}};

        double[][] test2 = new double[][]{{2.5, 2.4}, {0.5, 0.7}, {2.2, 2.9}, {1.9, 2.2}, {3.1, 3.0}, {2.3, 2.7}, {2, 1.6}, {1, 1.1}, {1.5, 1.6}, {1.1, 0.9}};

        PCA pca = new PCA(test2);

        System.out.println("\nDatos Iniciales");
        pca.getData().print();

        System.out.println("\n\nDatos Centrados");
        pca.getDataCenter().print();


        System.out.println("\n\nMatriz de covarianza de los datos centrados");
        pca.getDataCenter().printCovarianceMatrix();

        System.out.println("\n\nValores propios de los datos centrados");
        pca.getDataCenter().printEigenvalues();

        System.out.println("\n\nVectores propios de los datos centrados");
        pca.getDataCenter().printEigenvectors();

        System.out.println("\n\nDatos reducidos con la menor perdida de informacion");
        pca.printReducedData();
    }
    }
