import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOption, NewOption } from '../option.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOption for edit and NewOptionFormGroupInput for create.
 */
type OptionFormGroupInput = IOption | PartialWithRequiredKeyOf<NewOption>;

type OptionFormDefaults = Pick<NewOption, 'id' | 'correct' | 'isSelected'>;

type OptionFormGroupContent = {
  id: FormControl<IOption['id'] | NewOption['id']>;
  text: FormControl<IOption['text']>;
  questionId: FormControl<IOption['questionId']>;
  correct: FormControl<IOption['correct']>;
  assessmentId: FormControl<IOption['assessmentId']>;
  assignmentId: FormControl<IOption['assignmentId']>;
  isSelected: FormControl<IOption['isSelected']>;
};

export type OptionFormGroup = FormGroup<OptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OptionFormService {
  createOptionFormGroup(option: OptionFormGroupInput = { id: null }): OptionFormGroup {
    const optionRawValue = {
      ...this.getFormDefaults(),
      ...option,
    };
    return new FormGroup<OptionFormGroupContent>({
      id: new FormControl(
        { value: optionRawValue.id, disabled: optionRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      text: new FormControl(optionRawValue.text, {
        validators: [Validators.required],
      }),
      questionId: new FormControl(optionRawValue.questionId, {
        validators: [Validators.required],
      }),
      correct: new FormControl(optionRawValue.correct, {
        validators: [Validators.required],
      }),
      assessmentId: new FormControl(optionRawValue.assessmentId),
      assignmentId: new FormControl(optionRawValue.assignmentId),
      isSelected: new FormControl(optionRawValue.isSelected),
    });
  }

  getOption(form: OptionFormGroup): IOption | NewOption {
    return form.getRawValue() as IOption | NewOption;
  }

  resetForm(form: OptionFormGroup, option: OptionFormGroupInput): void {
    const optionRawValue = { ...this.getFormDefaults(), ...option };
    form.reset(
      {
        ...optionRawValue,
        id: { value: optionRawValue.id, disabled: optionRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OptionFormDefaults {
    return {
      id: null,
      correct: false,
      isSelected: false,
    };
  }
}
