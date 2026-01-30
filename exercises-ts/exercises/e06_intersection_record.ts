import { Task } from "../models";

/**
 * E06 – Intersection + Record
 */

export type AdminTask = Task & { adminOnly: true };

/**
 * @param tarea Tarea original
 * @returns AdminTask (adminOnly: true)
 */
export function makeAdminTask(tarea: Task): AdminTask {
  return {
    ...tarea,
    adminOnly: true,
  };
}

/**
 * @param token Token auth
 * @returns Headers HTTP
 * @throws Error Si token vacio
 */
export function buildAuthHeaders(token: string): Record<string, string> {
  const t = token.trim();
  if (!t) {
    throw new Error("El token no puede estar vacio");
  }
  return {
    Authorization: `Bearer ${t}`,
    "Content-Type": "application/json",
  };
}

/**
 * @param tareas Tareas
 * @returns Agrupacion done/pending
 */
export function groupByCompleted(tareas: Task[]): Record<"done" | "pending", Task[]> {
  const completadas = tareas.filter((t) => t.completed);
  const pendientes = tareas.filter((t) => !t.completed);
  return { done: completadas, pending: pendientes };
}
