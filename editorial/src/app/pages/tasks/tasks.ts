import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Publisher } from '../../models/publisher.model';
import { PublisherApiService } from '../../services/publisher-api';

@Component({
  selector: 'app-tasks',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './tasks.html',
  styleUrl: './tasks.css',
})
export class Tasks implements OnInit {
  tasks: Publisher[] = [];
  errorMsg: string | null = null;
  loading = false;

  constructor(public api: PublisherApiService) { }

  ngOnInit(): void {
    this.load();
  }

  load() {
    this.loading = true;
    this.errorMsg = null;

    this.api.list().subscribe({
      next: data => {
        this.tasks = data;
        this.loading = false;
      },
      error: (e: Error) => {
        this.tasks = [];
        this.errorMsg = e.message;
        this.loading = false;
      }
    });
  }

  remove(id: number) {
    this.errorMsg = null;

    this.api.remove(id).subscribe({
      next: () => this.load(),
      error: (e: Error) => this.errorMsg = e.message
    });
  }
}
