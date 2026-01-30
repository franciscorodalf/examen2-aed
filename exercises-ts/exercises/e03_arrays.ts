import { Task } from "../models";

/**
 * @param tareas Lista de tareas
 * @returns Tareas no completadas
 */
export function pendingTasks(tareas: Task[]): Task[] {
  return tareas.filter((t) => !t.completed);
}

/**
 * @param tareas Lista de tareas
 * @returns Titulos ordenados alfabeticamente
 */
export function titlesSorted(tareas: Task[]): string[] {
  return tareas.slice().sort((a, b) => a.title.localeCompare(b.title)).map((t) => t.title);
}

/**
 * @param tareas Lista de tareas
 * @returns Porcentaje completado (0-100)
 */
export function completionPercent(tareas: Task[]): number {
  if (tareas.length === 0) return 0;
  const completadas = tareas.filter((t) => t.completed).length;
  return Math.round((completadas / tareas.length) * 100);
}
