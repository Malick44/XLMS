import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAssessment, NewAssessment } from '../assessment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAssessment for edit and NewAssessmentFormGroupInput for create.
 */
type AssessmentFormGroupInput = IAssessment | PartialWithRequiredKeyOf<NewAssessment>;

type AssessmentFormDefaults = Pick<NewAssessment, 'id'>;

type AssessmentFormGroupContent = {
  id: FormControl<IAssessment['id'] | NewAssessment['id']>;
  title: FormControl<IAssessment['title']>;
  courseId: FormControl<IAssessment['courseId']>;
  sectionId: FormControl<IAssessment['sectionId']>;
  studentId: FormControl<IAssessment['studentId']>;
  examDate: FormControl<IAssessment['examDate']>;
  timeLimit: FormControl<IAssessment['timeLimit']>;
  score: FormControl<IAssessment['score']>;
};

export type AssessmentFormGroup = FormGroup<AssessmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AssessmentFormService {
  createAssessmentFormGroup(assessment: AssessmentFormGroupInput = { id: null }): AssessmentFormGroup {
    const assessmentRawValue = {
      ...this.getFormDefaults(),
      ...assessment,
    };
    return new FormGroup<AssessmentFormGroupContent>({
      id: new FormControl(
        { value: assessmentRawValue.id, disabled: assessmentRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(assessmentRawValue.title),
      courseId: new FormControl(assessmentRawValue.courseId),
      sectionId: new FormControl(assessmentRawValue.sectionId),
      studentId: new FormControl(assessmentRawValue.studentId),
      examDate: new FormControl(assessmentRawValue.examDate),
      timeLimit: new FormControl(assessmentRawValue.timeLimit),
      score: new FormControl(assessmentRawValue.score),
    });
  }

  getAssessment(form: AssessmentFormGroup): IAssessment | NewAssessment {
    return form.getRawValue() as IAssessment | NewAssessment;
  }

  resetForm(form: AssessmentFormGroup, assessment: AssessmentFormGroupInput): void {
    const assessmentRawValue = { ...this.getFormDefaults(), ...assessment };
    form.reset(
      {
        ...assessmentRawValue,
        id: { value: assessmentRawValue.id, disabled: assessmentRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AssessmentFormDefaults {
    return {
      id: null,
    };
  }
}
