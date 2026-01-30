import { JwtPayload, Role } from "../models";

/**
 * E10 – Errores típicos + validación de entradas (académico)
 */

export class ValidationError extends Error {
  constructor(message: string) {
    super(message);
    this.name = "ValidationError";
  }
}

/**
 * @param valor Cadena a validar
 * @param nombreCampo Nombre campo
 * @throws ValidationError Si vacio
 */
export function assertNonEmpty(valor: string, nombreCampo: string): void {
  if (!valor.trim()) {
    throw new ValidationError(`${nombreCampo} no puede estar vacío`);
  }
}

/**
 * @param texto String JSON
 * @returns Objeto parseado
 * @throws ValidationError Si JSON invalido
 */
export function parseJson<T>(texto: string): T {
  try {
    return JSON.parse(texto);
  } catch (err) {
    throw new ValidationError("JSON inválido");
  }
}

/**
 * @param datos Datos usuario
 * @param permitidos Roles permitidos
 * @throws ValidationError Si no autorizado
 */
export function authorize(datos: JwtPayload, permitidos: Role[]): void {
  if (!permitidos.includes(datos.role)) {
    throw new ValidationError("Acceso no autorizado");
  }
}
