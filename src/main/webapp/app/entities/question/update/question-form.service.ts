import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IQuestion, NewQuestion } from '../question.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuestion for edit and NewQuestionFormGroupInput for create.
 */
type QuestionFormGroupInput = IQuestion | PartialWithRequiredKeyOf<NewQuestion>;

type QuestionFormDefaults = Pick<NewQuestion, 'id'>;

type QuestionFormGroupContent = {
  id: FormControl<IQuestion['id'] | NewQuestion['id']>;
  sectionId: FormControl<IQuestion['sectionId']>;
  courseId: FormControl<IQuestion['courseId']>;
  text: FormControl<IQuestion['text']>;
  assignmentId: FormControl<IQuestion['assignmentId']>;
  assessmentId: FormControl<IQuestion['assessmentId']>;
};

export type QuestionFormGroup = FormGroup<QuestionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuestionFormService {
  createQuestionFormGroup(question: QuestionFormGroupInput = { id: null }): QuestionFormGroup {
    const questionRawValue = {
      ...this.getFormDefaults(),
      ...question,
    };
    return new FormGroup<QuestionFormGroupContent>({
      id: new FormControl(
        { value: questionRawValue.id, disabled: questionRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      sectionId: new FormControl(questionRawValue.sectionId, {
        validators: [Validators.required],
      }),
      courseId: new FormControl(questionRawValue.courseId),
      text: new FormControl(questionRawValue.text),
      assignmentId: new FormControl(questionRawValue.assignmentId),
      assessmentId: new FormControl(questionRawValue.assessmentId),
    });
  }

  getQuestion(form: QuestionFormGroup): IQuestion | NewQuestion {
    return form.getRawValue() as IQuestion | NewQuestion;
  }

  resetForm(form: QuestionFormGroup, question: QuestionFormGroupInput): void {
    const questionRawValue = { ...this.getFormDefaults(), ...question };
    form.reset(
      {
        ...questionRawValue,
        id: { value: questionRawValue.id, disabled: questionRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): QuestionFormDefaults {
    return {
      id: null,
    };
  }
}
