package profe.java7y8.comportamiento;

import profe.java7y8.model.Empleado;

/**
 * Filtro para empleados de recursos humanos
 * @author made
 *
 */
public class EmpEdadMenorPredicate implements EmpPredicate {

	private int edad;
	
	public EmpEdadMenorPredicate(int edad) {
		this.edad = edad;
	}
	@Override
	public boolean test(Empleado e) {
		return e.getEdad() < edad;
	}

}
