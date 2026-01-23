import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Tasks } from './pages/tasks/tasks';
import { TaskNew } from './pages/task-new/task-new';
import { Login } from './pages/login/login';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
    { path: 'login', component: Login },
    { path: 'tareas', component: Tasks, canActivate: [authGuard] },
    { path: 'tareas/nueva', component: TaskNew, canActivate: [authGuard] },
    { path: 'tareas/editar/:id', component: TaskNew, canActivate: [authGuard] },
    { path: '', redirectTo: 'tareas', pathMatch: 'full' },
    { path: '**', redirectTo: 'tareas' },
];
