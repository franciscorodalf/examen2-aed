import { Role } from "../models";

/**
 * E04 – Tuples y enum
 */

export type JwtParts = [header: string, payload: string, signature: string];

/**
 * @param token Token JWT
 * @returns Tupla con las 3 partes
 * @throws Error Si no tiene 3 partes
 */
export function splitJwt(token: string): JwtParts {
  const partes = token.split(".");
  if (partes.length !== 3) {
    throw new Error("debe tener 3 partes separadas por puntos");
  }
  return partes as JwtParts;
}

/**
 * @param valor String con el rol
 * @returns Enum Role correspondiente
 * @throws Error Si rol invalido
 */
export function roleFromString(valor: string): Role {
  const mayusculas = valor.toUpperCase();
  const roles = Object.values(Role) as string[];

  if (!roles.includes(mayusculas)) {
    throw new Error(`Rol invalido: ${valor}`);
  }

  return mayusculas as Role;
}

/**
 * @param usuario Usuario
 * @param rol Rol
 * @returns String "usuario#ROL"
 * @throws Error Si usuario vacio
 */
export function formatUserTag(usuario: string, rol: Role): string {
  const limpio = usuario.trim();
  if (!limpio) {
    throw new Error("El nombre de usuario no puede estar vacio");
  }
  return `${limpio}#${rol}`;
}
