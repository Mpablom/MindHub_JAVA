import java.util.Scanner;

public class Main {
    static Scanner teclado = new Scanner(System.in);
    static int capacidadMaxima = 500;
    static final int ENTRADA_GENERAL = 1500;
    static final int ENTRADA_VIP = 2000;
    static int recaudacion = 0;

    public static void main(String[] args) {
        menuPrincipal();
    }

    public static void menuPrincipal() {
        do {
            System.out.println("*********** MENU *************");
            System.out.println("Ingrese alguna de las siguientes opciones:");
            System.out.println("1) Ingreso de datos.");
            System.out.println("2) Capacidad disponible.");
            System.out.println("3) Dinero recaudado.");
            System.out.println("0) Salir del sistema.");
            System.out.println("******************************");
            int respuesta = teclado.nextInt();
            if (respuesta == 0) {
                System.out.println("*****************************");
                System.out.println("       Saliendo...");
                System.out.println("*****************************");
                break;
            }
            String resultado = ejecutarOpcion(respuesta);
            System.out.println(resultado);
        } while (true);
    }

    public static String ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                return ingresoDatos();
            case 2:
                return "La capacidad disponible es de: " + capacidadMaxima;
            case 3:
                return "La recaudacion hasta el momento es de " + recaudacion;
            default:
                return "Opcion no valida!";
        }
    }

    public static String ingresoDatos() {
        System.out.println("Ingrese nombre:");
        String nombre = teclado.next();
        System.out.println("Ingrese edad:");
        int edad = teclado.nextInt();
        if (edad < 0) {
            return "Edad invalida. Vuelva a ingresar los datos.";
        }
        System.out.println("Ingrese Dni:");
        long dni = teclado.nextLong();
        System.out.println("Tiene Pase:");
        String pase = teclado.next();
        return verificarPase(edad);
    }

    public static boolean ingresoPorEdad(int edad) {
        if (edad >= 21) return true;
        else return false;
    }

    public static String verificarPase(int edad) {
        if (ingresoPorEdad(edad)) {
            System.out.println("********* MENU PASES ***********");
            System.out.println("¿Qué pase posee?");
            System.out.println("1) VIP.");
            System.out.println("2) Descuento.");
            System.out.println("3) Ninguno.");
            System.out.println("********************************");
            int respuestaPase = teclado.nextInt();
            int valorACobrar = 0;
            if (respuestaPase == 1) {
                /*System.out.println("Bienvenido tiene una entrada VIP");*/
                /*valorACobrar = ENTRADA_VIP;*/
                capacidadMaxima--;
            } else if (respuestaPase == 2) {
                valorACobrar = entradaConDescuento();
                capacidadMaxima--;
            } else if (respuestaPase == 3){
                valorACobrar = entradaSinDescuento();
                capacidadMaxima--;
            }else{
                System.out.println("Error Opcion no válida!!");
                verificarPase(edad);
            }
            recaudacion += valorACobrar;
            return "Compro entrada por un valor de " + valorACobrar + " Bienvenido!";
        } else {
            return "No puede ingresar debido a su edad.";
        }
    }

    public static int entradaConDescuento() {
        System.out.println("********* DESCUENTOS ***********");
        System.out.println("Qué entrada desea comprar:");
        System.out.println("1) Entrada VIP.");
        System.out.println("2) Entrada general.");
        System.out.println("********************************");
        int respuestaEntrada = teclado.nextInt();
        int valorACobrar = 0;
        if (respuestaEntrada == 1) {
            System.out.println("Ha comprado la entrada VIP con descuento");
            valorACobrar = ENTRADA_VIP / 2;
        } else if (respuestaEntrada == 2) {
            System.out.println("Ha comprado la entrada general con descuento");
            valorACobrar = ENTRADA_GENERAL / 2;
        }else{
            System.out.println("Error Opcion no válida!!");
        }
        return valorACobrar;
    }

    public static int entradaSinDescuento() {
        System.out.println("********* SIN DESCUENTOS ***********");
        System.out.println("Qué entrada desea comprar:");
        System.out.println("1) Entrada VIP.");
        System.out.println("2) Entrada general.");
        System.out.println("************************************");
        int respuestaEntrada = teclado.nextInt();
        int valorACobrar = 0;
        if (respuestaEntrada == 1) {
            System.out.println("Ha comprado la entrada VIP");
            valorACobrar = ENTRADA_VIP;
        } else if (respuestaEntrada == 2) {
            System.out.println("Ha comprado la entrada general");
            valorACobrar = ENTRADA_GENERAL;
        }else{
            System.out.println("Error Opcion no válida!!");
        }
        return valorACobrar;
    }
}