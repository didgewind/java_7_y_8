package profe.java7y8.ui;

import static java.util.Comparator.comparingInt;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import profe.java7y8.model.Departamento;
import profe.java7y8.model.Empleado;
import profe.java7y8.util.Crono;

/**
 * Clase de pruebas. Contiene los siguientes métodos:
 * 
  	List<Departamento> generaDepartamentos(): Genera los departamentos

	List<Empleado> generaEmpleados(List<Departamento> departamentos): Genera los empleados

	void testOptional(): prueba con Optional. Busca un empleado que cumpla un par de condiciones

	void testDoubleStream(): transforma el stream de empleados en un DoubleStream con los sueldos
		e imprime la media.

	void testFlatMap(): prueba flatmap imprimiendo cada letra de cada nombre de los empleados
		en una línea diferente

	void ejemplosDeLambdas(): ejemplos de declaración de lambdas.

	void ejemploOptionalOrElse(): un findAny sobre empleados con condición que se cumple o no
		e imprimimos una cosa u otra
		
	void testOptionalAnidado(): forzamos un Optional de un método que (podría) devolver null
		(en nuestro código nunca, pero es por mostrar el uso del método Optional.ofNullable(...))
		y usamos map() para navegar por la relación. Map va a devolvernos un Optional con contenido,
		si la relación no es null, o un Optional vacío si la relación es null o el Optional original
		es vacío. Aquí podemos hablar de flatMap (usado si el método que define la relación devuelve
		a su vez un Optional ya que si usáramos map tendríamos Optional<Optional<X>>)

	void imprimeMediaDeEdad(): usamos mapToInt para imprimir la media de edad de los empleados

	void testConcatenaApellidos(): usamos reduce para concatenar apellidos. Partimos del stream
		de empleados o de un stream vacío para mostrar de nuevo el uso de Optional

	void testFibonnacci() throws Exception: para probar el método calculaFibonacci con varias
		llamadas.
	
	void calculaFibonnacci(int limit, boolean writeToFile) throws Exception: método que usa 
		iterate() para generar la serie fibonnacci de n (limit) números.
		Necesitamos tener un atributo externo que almacene el valor n-1 con el que sumar
		el resultado n de la última iteración (declarado como anterior justo encima). Usamos un
	    Supplier para generar el Stream con las operaciones intermedias
	    Para la primera invocación del método hacemos un warm up del código del stream (para 
	    optimizar el jit), y luego hacemos el cálculo real contabilizando los milisegundos empleados.
	    Usamos BigInt porque enseguida se alcanzan cantidades astronómicas. writeToFile es un booleano
	    que nos indica si queremos escribir el stream resultante en un fichero de texto (un número por
	    línea). Si es true, lo escribimos en un .txt cuyo nombre se basa en el valor de limit

	void testAsignaLambda(): crea una variable converter que es un Function para ejecutar
		el parseInt() de Integer.

	void testFiltraEmpleados(): método que filtra empleados por departamento y edad. Lo usamos
		junto con el método filterEmpleados y el paquete comportamiento para ilustrar cómo
		llegamos al behavior parametrizing pattern primero y el paso por la declaración de interfaces,
		clases de implementación, clases anónimas y expresiones lambda después

	List<Empleado> filterEmpleados(Predicate<Empleado> predicate): primero se llama filtraXDepartamento
		y es un filtro cableado, luego recibe un comportamiento.EmpPredicate (behavior parametrizing
		pattern) al que invocamos pasando instancias del predicado, luego lo invocamos con clases
		anónimas y finalmente con lambdas, y cambiamos el EmpPredicate por un Predicate.

	void lista(List<Empleado> lista): imprime la lista de empleados que le pasemos.

	void listaPorEdades(): muestra dos versiones (pre java 8 y java 8) para ordenar los empleados
		por edades. Se puede poner como ejercicio (mostramos la primera versión y hay que sacar la
		segunda)
	
 * @author made
 *
 */
public class Gestor {

	private List<Departamento> departamentos;
	private List<Empleado> empleados;
	
	public static void main(String[] args) throws Exception {
		new Gestor().go();
	}
	
	public Gestor() {
		this.departamentos = generaDepartamentos();
		this.empleados = generaEmpleados(departamentos);
	}

	private List<Departamento> generaDepartamentos() {
		List<Departamento> lReturn = 
				new ArrayList<>();
		lReturn.add(new Departamento("RRHH", "Recursos Humanos"));
		lReturn.add(new Departamento("I+D", "Informática"));
		return lReturn;				
	}
	
	private List<Empleado> generaEmpleados(List<Departamento> departamentos) {
		List<Empleado> lReturn = 
				new ArrayList<>();
		lReturn.add(new Empleado("23412312H", "Javier", "Pascual", 23, 35000, departamentos.get(0)));
		lReturn.add( new Empleado("123452435T", "Esthela", "Ruíz", 54, 18000, departamentos.get(0))); 
		lReturn.add( new Empleado("223452435A", "Manuel", "Alonso", 64, 45000, departamentos.get(0)));   
		lReturn.add( new Empleado("323452435B", "Mirkka", "Touko", 22, 24500, departamentos.get(0)));    
		lReturn.add( new Empleado("523452435S", "Ethan", "Hawk", 47, 32000, departamentos.get(1)));      
		lReturn.add( new Empleado("623452435D", "Jesús", "Gutiérrez", 81, 25000, departamentos.get(1)));

		return lReturn;
	}
	
	private void go() throws Exception {
		System.out.println("Comienzo de la app...");
		testFibonnacci();
		System.out.println("Terminado");
	}
	
	private void testOptional() {
		System.out.println(empleados.stream()
			.filter(e -> "I+D".equals(e.getDepartamento().getIdDepartamento()))
			.filter(e -> 35000 < e.getSalario())
			.findAny()
			.orElse(null));
	}
	
	private void testDoubleStream() {
		double sueldoMedio = empleados.stream()
								.mapToDouble(Empleado::getSalario)
								.average()
								.orElse(0);
		System.out.println("La media de sueldos es" + sueldoMedio);
	}
	
	private void testFlatMap() {
		empleados.stream()
			.map(emp -> emp.getNombre().split(""))
			.flatMap(array -> Arrays.stream(array))
			.map(str -> str.toUpperCase()) 
			.filter(str -> !(str.equals(" ")))
			.forEach(str -> System.out.println(str));
	}
	
	private void ejemplosDeLambdas() {
		Runnable r = () -> System.out.println("hecho");
		new Thread(r).start();
		
		BinaryOperator<Integer> suma = (a, b) -> {
			if (a > b)
				return a + b;
			else
				return 0;
		};
		
		Supplier<List<String>> listOfString = ArrayList::new;
		
		Supplier<List<String>> AListOfString = () -> new ArrayList();

	}
	
	private void ejemploOptionalOrElse() {
		Optional<Empleado> optionalEmp = empleados.stream()
			.filter(e -> e.getEdad() > 180)
			.findAny();
		if (optionalEmp.isPresent()) {
			System.out.println(optionalEmp.get());
		} else {
			System.out.println("No hay empleados que cumplan la condición");
		}
	}
	
	private void testOptionalAnidado() {
		Optional<Empleado> optEmpleado = Optional.ofNullable(empleados.get(0));
		System.out.println("resultado 1: " + optEmpleado);
		Optional opt = optEmpleado.map(Empleado::getDepartamento);
		System.out.println("resultado 2: " + opt);
		System.out.println(opt.get());
	}
	
	private void imprimeMediaDeEdad() {
		System.out.println(
			empleados.stream()
				.mapToInt(Empleado::getEdad)
				.average()
				.orElse(0)
		);
	}
	

	private void testConcatenaApellidos() {
		Empleado[] aEmp = new Empleado[0];
		System.out.println(
//			empleados.stream()
				Stream.of(aEmp)
					.map(Empleado::getApellidos)
					.reduce((ap1, ap2) -> ap1 + ap2)
					.orElse("No hay empleados")
		);
				
	}
	
	private void testFibonnacci() throws Exception {
		calculaFibonnacci(100, true);
		calculaFibonnacci(1000, true);
		calculaFibonnacci(10000, false);
		calculaFibonnacci(50000, false);
	}
	
	/* 
	 * Almacena el número anterior fibonnacci. No podemos declararla como
	 * variable local dentro del método porque está prohibido
	 */
	private BigInteger anterior;
	private boolean warmedUp = false; // para no warming up en sucesivas llamadas
	
	/* Aquí hacemos varias cosas:
	 * - Calculamos los limit primeros números fibonnacci. Usamos un
	 *   Supplier para generar el Stream con las operaciones intermedias
	 *   Para la primera invocación del método hacemos un warm up
	 *   del código del stream (para optimizar el jit), y luego hacemos el
	 *   cálculo real contabilizando los milisegundos empleados. Usamos
	 *   BigInt porque enseguida se alcanzan cantidades astronómicas
	 * - writeToFile es un booleano que nos indica si queremos escribir
	 *   el stream resultante en un fichero de texto (un número por línea).
	 *   Si es true, lo escribimos en un .txt cuyo nombre se basa en el
	 *   valor de limit
	 */
	private void calculaFibonnacci(int limit, boolean writeToFile) throws Exception {
		//System.out.print("1");
		/* Declaramos el lambda como un supplier para generar el stream varias veces.
		 * Como todo son operaciones intermedias, el lambda no se ejecutará hasta que
		 * anidemos una operación terminal
		 */
		anterior = BigInteger.ONE;
		Supplier<Stream<BigInteger>> fibNumbersSupplier = () ->
			Stream.iterate(anterior, n -> {
				BigInteger result = n.add(anterior);
				anterior = n;
				return result;
			}).limit(limit - 1);
		Stream<BigInteger> fibNumbers = null;
		// Warm up virtual machine
		if (!warmedUp) {
			warmedUp = true;
			anterior = BigInteger.ONE;
			for (int i = 0; i < 100; i++) {
				fibNumbers = fibNumbersSupplier.get();
				fibNumbers.count();
			}
		}
		anterior = BigInteger.ONE;
		// Real calculation
		Crono.start();
		System.out.println("números obtenidos: " + fibNumbersSupplier.get().count());
		long milis = Crono.stop();
		// Escribimos a fichero
		System.out.println();
		System.out.println(String.format("Tiempo transcurrido para %d números: "
				+ "%dms", limit, milis));
		if (writeToFile) {
			anterior = BigInteger.ONE;
			Path baseDir = Paths.get(System.getProperty("user.dir"));
			try(PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
					baseDir.resolve("fibnumbers for " + limit + ".txt")))) {
				pw.println(1);
				fibNumbersSupplier.get().forEach(pw::println);
			}
		}
	}
	
	private void testAsignaLambda() {
		Function<String, Integer> converter = Integer::parseInt;
//		Function<String, Integer> converter = s -> Integer.parseInt(s);
		System.out.println(converter.apply("10"));
	}
	
	private void testFiltraEmpleados() {
		lista(filterEmpleados((Empleado e) ->
		 "RRHH".equals(e.getDepartamento().getIdDepartamento()) &&
				e.getEdad() < 23
		));
	}
	
	private List<Empleado> filterEmpleados(Predicate<Empleado> predicate) {
		List<Empleado> lRRHH = new ArrayList();
		for (Empleado emp : empleados) {
			if (predicate.test(emp)) {
				lRRHH.add(emp);
			}
		}
		return lRRHH;
	}
	
	private void lista(List<Empleado> lista) {
		System.out.println("Listando tradicionalmente");
		for (Empleado o : lista) {
			System.out.println(o);
		}
		System.out.println("Listando en java 8");
		lista.forEach(System.out::println);
	}
	
	private void listaPorEdades() {
		/* Versión pre java 8 */
		Collections.sort(empleados, new Comparator<Empleado>() {
			public int compare(Empleado emp1, Empleado emp2) {
				return Integer.compare(emp1.getEdad(), emp2.getEdad());
			}
		});
		
		/* Versión java 8 */
		empleados.sort(comparingInt(Empleado::getEdad));
		
		lista(empleados);
	}
	
}


