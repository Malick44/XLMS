import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { OptionFormService, OptionFormGroup } from './option-form.service';
import { IOption } from '../option.model';
import { OptionService } from '../service/option.service';

@Component({
  selector: 'jhi-option-update',
  templateUrl: './option-update.component.html',
})
export class OptionUpdateComponent implements OnInit {
  isSaving = false;
  option: IOption | null = null;

  editForm: OptionFormGroup = this.optionFormService.createOptionFormGroup();

  constructor(
    protected optionService: OptionService,
    protected optionFormService: OptionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ option }) => {
      this.option = option;
      if (option) {
        this.updateForm(option);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const option = this.optionFormService.getOption(this.editForm);
    if (option.id !== null) {
      this.subscribeToSaveResponse(this.optionService.update(option));
    } else {
      this.subscribeToSaveResponse(this.optionService.create(option));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOption>>): void {
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

  protected updateForm(option: IOption): void {
    this.option = option;
    this.optionFormService.resetForm(this.editForm, option);
  }
}
