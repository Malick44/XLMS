import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { AssignmentFormService, AssignmentFormGroup } from './assignment-form.service';
import { IAssignment } from '../assignment.model';
import { AssignmentService } from '../service/assignment.service';

@Component({
  selector: 'jhi-assignment-update',
  templateUrl: './assignment-update.component.html',
})
export class AssignmentUpdateComponent implements OnInit {
  isSaving = false;
  assignment: IAssignment | null = null;

  editForm: AssignmentFormGroup = this.assignmentFormService.createAssignmentFormGroup();

  constructor(
    protected assignmentService: AssignmentService,
    protected assignmentFormService: AssignmentFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assignment }) => {
      this.assignment = assignment;
      if (assignment) {
        this.updateForm(assignment);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assignment = this.assignmentFormService.getAssignment(this.editForm);
    if (assignment.id !== null) {
      this.subscribeToSaveResponse(this.assignmentService.update(assignment));
    } else {
      this.subscribeToSaveResponse(this.assignmentService.create(assignment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssignment>>): void {
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

  protected updateForm(assignment: IAssignment): void {
    this.assignment = assignment;
    this.assignmentFormService.resetForm(this.editForm, assignment);
  }
}
