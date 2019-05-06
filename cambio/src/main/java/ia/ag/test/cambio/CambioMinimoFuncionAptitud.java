package ia.ag.test.cambio;

import org.jgap.*;

public class CambioMinimoFuncionAptitud extends FitnessFunction {
	private final int montoObjetivo;
	// Maximo monto posible 1000 Centimos = 10 Euros
	public static final int MAX_MONTO = 1000;
	// Maxima cantidad de monedas posibles. Es igual al Monto maximo en

	// centimos, ya que si se utilizan monedas de un centimo se llegaria al
	// monton con la mayor cantidad posible de monedas
	public static final int MAX_CANT_MONEDAS = MAX_MONTO;

	// El constructor de la funcion de aptitud debe recibir el monto objetivo
	// del problema y almacenarlo en un atributo. Si el monto es invalido arroja
	// una excepcion
	public CambioMinimoFuncionAptitud(int monto) {
		if (monto < 1 || monto >= MAX_MONTO) {
		throw new IllegalArgumentException("El monto debe ser un numero entre 1 y " + MAX_MONTO + " centimos");
		}
		montoObjetivo = monto;
		}

	/**
	 * El metodo evaluate es el metodo que se debe sobrecargar para que devuelva el
	 * valor de aptitud asociado al cromosoma que se recibe por parametro.
	 *
	 *
	 * @param cromosoma El cromosoma a evaluar
	 *
	 * @return El valor de aptitud de ese cromosoma
	 * @author Gabriel Veloso, Ruben Arce
	 */
	public double evaluate(IChromosome cromosoma) {
		// Se debe tener en cuenta el evaluador que se esta usando. El evaluador
		// estandar le asigna un valor mas apto a los valores mas altos de
		// aptitud. Tambien hay otros evaluadores que asignan mejor aptitud a
		// los valores mas bajos.
		// Es por esto que se chequea si 2 es mas apto que 1. Si esto es asi
		// entonces el valor mas apto sera el mayor y el menos apto el 0
		boolean evaluadorEstandard = cromosoma.getConfiguration().getFitnessEvaluator().isFitter(2, 1);
		int montoCambioMonedas = montoCambioMoneda(cromosoma);
		int totalMonedas = getNumeroTotalMonedas(cromosoma);
		int diferenciaMonto = Math.abs(montoObjetivo - montoCambioMonedas);
		// El primer paso es asignar la menor aptitud a aquellos cromosomas cuyo
		// monto no sea el monto objetivo. Es decir una descomposicion en
		// monedas que no sea del monto ingresado
		if (evaluadorEstandard) {
			if (diferenciaMonto != 0)
				return 0.0d;
		} else {
			if (diferenciaMonto != 0)
				return MAX_CANT_MONEDAS;
		}
		// luego se debe asignar mas aptitud a aquellos cromosomas que posean
		// menor cantidad de monedas.
		if (evaluadorEstandard) {
			// Se debe asegurar devolver un valor de aptitud positivo siempre.
			// Si el valor es negativo se devuelve MAX_CANT_MONEDAS ( elemento
			// menos apto )
			return Math.max(0.0d, MAX_CANT_MONEDAS - totalMonedas);
		} else {
			// Se debe asgurar devolver un valor de aptitud positivo siempre.
			// Si el valor es negativo se devuelve 0 ( elemento menos apto )
			return Math.max(0.0d, totalMonedas);
		}
	}

	/**
	 * Calcula el monto total que suman todas las monedas de un cromosoma
	 *
	 *
	 * @param cromosoma El cromosoma a evaluar
	 * @return Retorna el monto en centimos compuesto por la suma de las monedas de
	 *         ese cromosoma
	 *
	 * @author Gabriel Veloso, Ruben Arce
	 *
	 */
	public static int montoCambioMoneda(IChromosome cromosoma) {
		int Moneda1Dolar = getNumeroDeComendasDeGen(cromosoma, 0);
		int Moneda50Centimos = getNumeroDeComendasDeGen(cromosoma, 1);
		int Moneda25Centimos = getNumeroDeComendasDeGen(cromosoma, 2);
		int Moneda10Centimos = getNumeroDeComendasDeGen(cromosoma, 3);
		int Moneda5Centimos = getNumeroDeComendasDeGen(cromosoma, 4);
		int Moneda1Centimo = getNumeroDeComendasDeGen(cromosoma, 5);
		return (Moneda1Dolar * 100) + (Moneda50Centimos * 50) + (Moneda25Centimos * 25) + (Moneda10Centimos * 10)
				+ (Moneda5Centimos * 5) + Moneda1Centimo;
	}

	/**
	 * Calcula la cantidad de monedas de determinado tipo (gen) de un cromosoma
	 * Ejemplo. Cantidad de monedas de 20 centimos de es cromosoma
	 *
	 * @param cromosoma El cromosoma a evaluar
	 * @param numeroGen El numero gen (tipo de moneda) de que se desea averiguar la
	 *                  cantidad
	 * @return Devuelve la cantidad de monedas de ese tipo de ese cromosoma
	 *
	 *
	 * @author Gabriel Veloso, Ruben Arce
	 */
	public static int getNumeroDeComendasDeGen(IChromosome cromosoma, int numeroGen) {
		Integer numMonedas = (Integer) cromosoma.getGene(numeroGen).getAllele();
		return numMonedas.intValue();
	}

	/**
	 * Calcula el total de monedas que tiene esa solucion. Este valor se utiliza
	 * para calcular la aptitud del cromosoma ya que el objetivo es minimizar la
	 * cantidad de monedas de la solucion
	 *
	 *
	 * @param cromosoma El cromosoma a evaluar
	 * @return El total de monedas que tiene esa solucion
	 *
	 * @author Gabriel Veloso, Ruben Arce
	 * 
	 */
	public static int getNumeroTotalMonedas(IChromosome cromosoma) {
		int totalMonedas = 0;
		int numberOfGenes = cromosoma.size();
		for (int i = 0; i < numberOfGenes; i++) {
			totalMonedas += getNumeroDeComendasDeGen(cromosoma, i);
		}
		return totalMonedas;
	}
}
