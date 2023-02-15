import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { InstructorFormService, InstructorFormGroup } from './instructor-form.service';
import { IInstructor } from '../instructor.model';
import { InstructorService } from '../service/instructor.service';

@Component({
  selector: 'jhi-instructor-update',
  templateUrl: './instructor-update.component.html',
})
export class InstructorUpdateComponent implements OnInit {
  isSaving = false;
  instructor: IInstructor | null = null;

  editForm: InstructorFormGroup = this.instructorFormService.createInstructorFormGroup();

  constructor(
    protected instructorService: InstructorService,
    protected instructorFormService: InstructorFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ instructor }) => {
      this.instructor = instructor;
      if (instructor) {
        this.updateForm(instructor);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const instructor = this.instructorFormService.getInstructor(this.editForm);
    if (instructor.id !== null) {
      this.subscribeToSaveResponse(this.instructorService.update(instructor));
    } else {
      this.subscribeToSaveResponse(this.instructorService.create(instructor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInstructor>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(instructor: IInstructor): void {
    this.instructor = instructor;
    this.instructorFormService.resetForm(this.editForm, instructor);
  }
}
