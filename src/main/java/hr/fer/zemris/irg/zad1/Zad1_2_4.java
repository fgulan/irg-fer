package hr.fer.zemris.irg.zad1;

import hr.fer.zemris.irg.utility.linear.Vector;
import hr.fer.zemris.irg.utility.linear.interfaces.IVector;
import hr.fer.zemris.irg.utility.linear.interfaces.IncompatibleOperandException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Filip Gulan
 */
public class Zad1_2_4 {

    public static void main(String [] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Za izlaz iz programa upisite 'STOP'. Vektore unosite u obliku npr: '1 2 3'," +
                " gdje razmak razdvaja pojedine komponenete vektora" );

        System.out.print("Unesite vektor kojeg reflektirate: ");
        IVector firstVector = getVectorFromReader(reader);

        System.out.print("Unesite vektor okosnice: ");
        IVector secondVector = getVectorFromReader(reader);

        if (firstVector.getDimension() != secondVector.getDimension()) {
            System.out.println("Dani vektori moraju biti istih dimenzija. Prekidam program.");
            System.exit(-1);
        }

        IVector secondVectorNormalized = secondVector.nNormalize();

        IVector k = secondVectorNormalized.nScalarMultiply(secondVectorNormalized.scalarProduct(firstVector));
        IVector r = k.nScalarMultiply(2).nSub(firstVector);
        System.out.println("Reflektirani vektor: " + r);
    }

    private static IVector getVectorFromReader(BufferedReader reader) throws IOException {
        IVector vector = null;
        for (;;) {
            try {
                String command = reader.readLine();
                if (command.equals("STOP")) {
                    System.exit(0);
                }
                vector = Vector.parseSimple(command);
                break;
            } catch (IncompatibleOperandException exception) {
                System.out.println("Neispravan unos, pokusajte ponovno");
                continue;
            }
        }
        return vector;
    }
}
