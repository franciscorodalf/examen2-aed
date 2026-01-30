/**
 * @param fechaIso Fecha formato (YYYY-MM-DD)
 * @returns true si es valida
 */
export function isValidISODate(fechaIso: string): boolean {
  if (!/^\d{4}-\d{2}-\d{2}$/.test(fechaIso)) return false;
  const fecha = new Date(fechaIso);
  return !isNaN(fecha.getTime()) && fecha.toISOString().startsWith(fechaIso);
}

/**
 * @param entrada Fecha entrada (YYYY-MM-DD)
 * @param salida Fecha salida (YYYY-MM-DD)
 * @returns Numero de noches
 * @throws Error Si fechas invalidas o salida anterior a entrada
 */
export function nightsBetween(entrada: string, salida: string): number {
  if (!isValidISODate(entrada) || !isValidISODate(salida)) {
    throw new Error("Fechas invalidas");
  }

  const fecha1 = new Date(entrada);
  const fecha2 = new Date(salida);

  if (fecha2 <= fecha1) {
    throw new Error("La fecha de salida debe ser posterior a la de entrada");
  }

  const diffMs = fecha2.getTime() - fecha1.getTime();
  return Math.round(diffMs / (1000 * 60 * 60 * 24));
}

/**
 * @param fecha Objeto Date
 * @returns Fecha en formato YYYY-MM-DD
 * @throws Error Si fecha invalida
 */
export function toIsoDateOnly(fecha: Date): string {
  if (isNaN(fecha.getTime())) {
    throw new Error("Fecha invalida");
  }
  return fecha.toISOString().split("T")[0];
}
