import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { SectionFormService, SectionFormGroup } from './section-form.service';
import { ISection } from '../section.model';
import { SectionService } from '../service/section.service';

@Component({
  selector: 'jhi-section-update',
  templateUrl: './section-update.component.html',
})
export class SectionUpdateComponent implements OnInit {
  isSaving = false;
  section: ISection | null = null;

  editForm: SectionFormGroup = this.sectionFormService.createSectionFormGroup();

  constructor(
    protected sectionService: SectionService,
    protected sectionFormService: SectionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ section }) => {
      this.section = section;
      if (section) {
        this.updateForm(section);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const section = this.sectionFormService.getSection(this.editForm);
    if (section.id !== null) {
      this.subscribeToSaveResponse(this.sectionService.update(section));
    } else {
      this.subscribeToSaveResponse(this.sectionService.create(section));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISection>>): void {
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

  protected updateForm(section: ISection): void {
    this.section = section;
    this.sectionFormService.resetForm(this.editForm, section);
  }
}
