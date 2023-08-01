import com.sun.source.doctree.SummaryTree;
import com.sun.source.tree.WhileLoopTree;

import java.sql.SQLOutput;
import java.time.temporal.Temporal;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        /* Punto 1*/

        String nombre = "Pablo";
        String apellido = "Moya";
        int edad = 35;
        System.out.println("Mi nombre es " + nombre + " " + apellido + " y tengo " + edad + " años.");

        /*Punto 2*/

        /*List<Integer> numeros = new ArrayList<>();*/
        Scanner teclado = new Scanner(System.in);

        /*numeros.add(numero1);
        numeros.add(numero2);
        /*  int[] numeros = new int[3];
        System.out.println("Ingresa los tres números a comparar:");
        int numero1 = teclado.nextInt();
        int numero2 = teclado.nextInt();
        int numero3 = teclado.nextInt();
        numeros[0] = numero1;
        numeros[1] = numero2;
        numeros[2] = numero3;
        int mayor = 0;
        for (int numero : numeros) {
            if (numero > mayor) {
                mayor = numero;
            }
        }
        System.out.println("El número más grande es: " + mayor);*/
        /*numeros.add(numero3);
        int mayor = numeros.stream().max(Integer::compareTo).orElse(0);
        System.out.println("El número  más grande es: "+mayor);*/

        /*Punto 3*/

        /*System.out.println("Ingresa un número:");
        int numero = teclado.nextInt();
        if (numero%2==0){
            System.out.println("El número es Par");
        }else{
            System.out.println("El número es Impar");
        }*/

        /*Punto 4*/

        /*System.out.println("Ingresa la primer cadena de texto:");
        String cadena1 = teclado.nextLine().strip();
        System.out.println("Ingresa la segunda cadena de texto:");
        String cadena2 = teclado.nextLine().strip();
        if (cadena1.equalsIgnoreCase(cadena2)){
            System.out.println("Las cadenas son igules");
        }else{
            System.out.println("Las cadenas no son iguales");
        }*/

        /*Punto 5*/

        /*List<Integer>numeros = new ArrayList<>();
        System.out.println("Ingresa un número:");
        int numero = teclado.nextInt();
        while (numero > 0){
            numeros.add(numero);
            System.out.println("Ingresa otro número (ingresa 0 para salir):");
            numero = teclado.nextInt();
        }
        System.out.println("Los números almacenados son: "+numeros);*/

        /*Punto6*/

        welcome();

        /*Punto 7*/

        String checkParity = checkParity(2);
        System.out.println(checkParity);

        /*Punto 8*/

        if (primeNumber(2) == true){
            System.out.println("es par");
        }else{
            System.out.println("impar");
        }

        /*Punto 9*/

        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        int oddNumber = oddNumbers(numbers);
        System.out.println("La suma de los números impares es de: "+oddNumber);

        /*Punto 10*/

        String sumEvenNumbersAndPrimes = sumEvenNumbersAndPrimes(numbers);
        System.out.println(sumEvenNumbersAndPrimes);

        /*Punto 11*/

        menuCalculator();

    }
    public static void welcome(){
        System.out.println("Bienvenidos a Java!!");
    }
    public static String checkParity(int number){
        if (number % 2 == 0){
            return number+" Es un número Par";
        }else{
            return number+" Es un número Impar";
        }
    }
    public static boolean primeNumber(int number){
        if (number<=1) return false;
        for (int i = 2; i*i <= number;i++){
            if ((number%i) == 0) return false;
        }
        return true;
    }
    public static int oddNumbers(List<Integer> numbers){
        int suma = 0;
        for (int number : numbers){
            if (number%2 != 0){
                suma+=number;
            }
        }
        return suma;
    }
    public static String sumEvenNumbersAndPrimes(List<Integer> numbers) {
        int sumaPares = 0;
        int sumaPrimos = 0;
        for (int number : numbers) {
            if (number % 2 == 0) {
                sumaPares += number;
            }
            if (primeNumber(number)){
                sumaPrimos += number;
            }
        }
        return "La suma de los pares es: "+sumaPares+". \nLa suma de los primos es: "+sumaPrimos;
    }
    public static void menuCalculator() {
        Scanner teclado = new Scanner(System.in);
        do {
            System.out.println("Que operación desea realizar:");
            System.out.println("1)- Suma");
            System.out.println("2)- Resta");
            System.out.println("3)- Multiplicación");
            System.out.println("4)- División");
            System.out.println("0)- Salir");
            int respuesta = teclado.nextInt();

            if (respuesta == 0) {
                System.out.println("Saliendo de la calculadora...");
                break;
            }

            System.out.println("Ingrese el primer número:");
            float numero1 = teclado.nextFloat();
            System.out.println("Ingrese el segundo número:");
            float numero2 = teclado.nextFloat();
            float resultadoFinal = calculator(numero1, numero2, respuesta);
            System.out.println("El resultado es: " + resultadoFinal);

        } while (true);
    }
    public static float calculator(float numero1,float numero2,int respuesta){
        float resultado = 0;
        switch (respuesta){
            case 1:
                resultado = numero1+numero2;
                break;
            case 2:
                resultado = numero1-numero2;
                break;
            case 3:
                resultado = numero1*numero2;
                break;
            case 4:
                if (numero2 != 0){
                    resultado = numero1/numero2;
                }else{
                    System.out.println("no se puede dividir por 0");
                }
                break;
            default:
                System.out.println("Opción incorrecta!");
        }
        return resultado;
    }
}