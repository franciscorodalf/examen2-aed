import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskNew } from './task-new';

describe('TaskNew', () => {
  let component: TaskNew;
  let fixture: ComponentFixture<TaskNew>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TaskNew]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TaskNew);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
