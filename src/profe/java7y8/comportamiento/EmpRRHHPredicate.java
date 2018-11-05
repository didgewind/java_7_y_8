package profe.java7y8.comportamiento;

import profe.java7y8.model.Empleado;

/**
 * Filtro para empleados de recursos humanos
 * @author made
 *
 */
public class EmpRRHHPredicate implements EmpPredicate {

	@Override
	public boolean test(Empleado e) {
		return e.getDepartamento().getIdDepartamento().equals("RRHH");
	}


}
