import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISection, NewSection } from '../section.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISection for edit and NewSectionFormGroupInput for create.
 */
type SectionFormGroupInput = ISection | PartialWithRequiredKeyOf<NewSection>;

type SectionFormDefaults = Pick<NewSection, 'id'>;

type SectionFormGroupContent = {
  id: FormControl<ISection['id'] | NewSection['id']>;
  title: FormControl<ISection['title']>;
  description: FormControl<ISection['description']>;
  courseId: FormControl<ISection['courseId']>;
  servicePath: FormControl<ISection['servicePath']>;
};

export type SectionFormGroup = FormGroup<SectionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SectionFormService {
  createSectionFormGroup(section: SectionFormGroupInput = { id: null }): SectionFormGroup {
    const sectionRawValue = {
      ...this.getFormDefaults(),
      ...section,
    };
    return new FormGroup<SectionFormGroupContent>({
      id: new FormControl(
        { value: sectionRawValue.id, disabled: sectionRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(sectionRawValue.title),
      description: new FormControl(sectionRawValue.description),
      courseId: new FormControl(sectionRawValue.courseId),
      servicePath: new FormControl(sectionRawValue.servicePath),
    });
  }

  getSection(form: SectionFormGroup): ISection | NewSection {
    return form.getRawValue() as ISection | NewSection;
  }

  resetForm(form: SectionFormGroup, section: SectionFormGroupInput): void {
    const sectionRawValue = { ...this.getFormDefaults(), ...section };
    form.reset(
      {
        ...sectionRawValue,
        id: { value: sectionRawValue.id, disabled: sectionRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SectionFormDefaults {
    return {
      id: null,
    };
  }
}
