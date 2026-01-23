import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PublisherApiService } from '../../services/publisher-api';

@Component({
  selector: 'app-task-new',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './task-new.html',
  styleUrl: './task-new.css',
})
export class TaskNew implements OnInit {
  private fb = inject(FormBuilder);
  private tasksApi = inject(PublisherApiService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  errorMsg: string | null = null;
  loading = false;
  isEditing = false;
  taskId: number | null = null;

  form = this.fb.nonNullable.group({
    name: this.fb.nonNullable.control('', [Validators.required, Validators.minLength(3)]),
    city: this.fb.nonNullable.control(''),
  });

  ngOnInit() {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEditing = true;
      this.taskId = Number(idParam);
      this.loadTask(this.taskId);
    }
  }

  loadTask(id: number) {
    this.loading = true;
    this.tasksApi.getById(id).subscribe({
      next: (task) => {
        this.form.patchValue(task);
        this.loading = false;
      },
      error: () => {
        this.errorMsg = 'Error al cargar la tarea';
        this.loading = false;
      }
    });
  }

  save() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMsg = null;
    const data = this.form.getRawValue();

    const request$ = this.isEditing && this.taskId
      ? this.tasksApi.update(this.taskId, data)
      : this.tasksApi.create(data);

    request$.subscribe({
      next: () => {
        this.loading = false;
        this.router.navigateByUrl('/tareas');
      },
      error: (err) => {
        this.loading = false;
        this.errorMsg = 'Error al guardar la tarea.';
      },
    });
  }

  cancel() {
    this.router.navigateByUrl('/tareas');
  }
}
