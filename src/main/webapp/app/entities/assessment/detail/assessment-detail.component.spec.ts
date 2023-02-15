import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AssessmentDetailComponent } from './assessment-detail.component';

describe('Assessment Management Detail Component', () => {
  let comp: AssessmentDetailComponent;
  let fixture: ComponentFixture<AssessmentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssessmentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ assessment: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(AssessmentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AssessmentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load assessment on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.assessment).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
