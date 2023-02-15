import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OptionFormService } from './option-form.service';
import { OptionService } from '../service/option.service';
import { IOption } from '../option.model';

import { OptionUpdateComponent } from './option-update.component';

describe('Option Management Update Component', () => {
  let comp: OptionUpdateComponent;
  let fixture: ComponentFixture<OptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let optionFormService: OptionFormService;
  let optionService: OptionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OptionUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(OptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    optionFormService = TestBed.inject(OptionFormService);
    optionService = TestBed.inject(OptionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const option: IOption = { id: 'CBA' };

      activatedRoute.data = of({ option });
      comp.ngOnInit();

      expect(comp.option).toEqual(option);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOption>>();
      const option = { id: 'ABC' };
      jest.spyOn(optionFormService, 'getOption').mockReturnValue(option);
      jest.spyOn(optionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ option });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: option }));
      saveSubject.complete();

      // THEN
      expect(optionFormService.getOption).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(optionService.update).toHaveBeenCalledWith(expect.objectContaining(option));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOption>>();
      const option = { id: 'ABC' };
      jest.spyOn(optionFormService, 'getOption').mockReturnValue({ id: null });
      jest.spyOn(optionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ option: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: option }));
      saveSubject.complete();

      // THEN
      expect(optionFormService.getOption).toHaveBeenCalled();
      expect(optionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOption>>();
      const option = { id: 'ABC' };
      jest.spyOn(optionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ option });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(optionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
