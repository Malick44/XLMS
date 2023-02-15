import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SectionFormService } from './section-form.service';
import { SectionService } from '../service/section.service';
import { ISection } from '../section.model';

import { SectionUpdateComponent } from './section-update.component';

describe('Section Management Update Component', () => {
  let comp: SectionUpdateComponent;
  let fixture: ComponentFixture<SectionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sectionFormService: SectionFormService;
  let sectionService: SectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SectionUpdateComponent],
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
      .overrideTemplate(SectionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SectionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sectionFormService = TestBed.inject(SectionFormService);
    sectionService = TestBed.inject(SectionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const section: ISection = { id: 'CBA' };

      activatedRoute.data = of({ section });
      comp.ngOnInit();

      expect(comp.section).toEqual(section);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISection>>();
      const section = { id: 'ABC' };
      jest.spyOn(sectionFormService, 'getSection').mockReturnValue(section);
      jest.spyOn(sectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ section });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: section }));
      saveSubject.complete();

      // THEN
      expect(sectionFormService.getSection).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sectionService.update).toHaveBeenCalledWith(expect.objectContaining(section));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISection>>();
      const section = { id: 'ABC' };
      jest.spyOn(sectionFormService, 'getSection').mockReturnValue({ id: null });
      jest.spyOn(sectionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ section: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: section }));
      saveSubject.complete();

      // THEN
      expect(sectionFormService.getSection).toHaveBeenCalled();
      expect(sectionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISection>>();
      const section = { id: 'ABC' };
      jest.spyOn(sectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ section });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sectionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
