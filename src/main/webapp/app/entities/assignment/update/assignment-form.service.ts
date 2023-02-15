import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAssignment, NewAssignment } from '../assignment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAssignment for edit and NewAssignmentFormGroupInput for create.
 */
type AssignmentFormGroupInput = IAssignment | PartialWithRequiredKeyOf<NewAssignment>;

type AssignmentFormDefaults = Pick<NewAssignment, 'id'>;

type AssignmentFormGroupContent = {
  id: FormControl<IAssignment['id'] | NewAssignment['id']>;
  title: FormControl<IAssignment['title']>;
  courseId: FormControl<IAssignment['courseId']>;
  studentId: FormControl<IAssignment['studentId']>;
  examDate: FormControl<IAssignment['examDate']>;
  timeLimit: FormControl<IAssignment['timeLimit']>;
  score: FormControl<IAssignment['score']>;
};

export type AssignmentFormGroup = FormGroup<AssignmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AssignmentFormService {
  createAssignmentFormGroup(assignment: AssignmentFormGroupInput = { id: null }): AssignmentFormGroup {
    const assignmentRawValue = {
      ...this.getFormDefaults(),
      ...assignment,
    };
    return new FormGroup<AssignmentFormGroupContent>({
      id: new FormControl(
        { value: assignmentRawValue.id, disabled: assignmentRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(assignmentRawValue.title),
      courseId: new FormControl(assignmentRawValue.courseId, {
        validators: [Validators.required],
      }),
      studentId: new FormControl(assignmentRawValue.studentId, {
        validators: [Validators.required],
      }),
      examDate: new FormControl(assignmentRawValue.examDate, {
        validators: [Validators.required],
      }),
      timeLimit: new FormControl(assignmentRawValue.timeLimit),
      score: new FormControl(assignmentRawValue.score),
    });
  }

  getAssignment(form: AssignmentFormGroup): IAssignment | NewAssignment {
    return form.getRawValue() as IAssignment | NewAssignment;
  }

  resetForm(form: AssignmentFormGroup, assignment: AssignmentFormGroupInput): void {
    const assignmentRawValue = { ...this.getFormDefaults(), ...assignment };
    form.reset(
      {
        ...assignmentRawValue,
        id: { value: assignmentRawValue.id, disabled: assignmentRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AssignmentFormDefaults {
    return {
      id: null,
    };
  }
}
