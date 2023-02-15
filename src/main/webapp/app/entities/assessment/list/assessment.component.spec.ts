import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AssessmentService } from '../service/assessment.service';

import { AssessmentComponent } from './assessment.component';

describe('Assessment Management Component', () => {
  let comp: AssessmentComponent;
  let fixture: ComponentFixture<AssessmentComponent>;
  let service: AssessmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'assessment', component: AssessmentComponent }]), HttpClientTestingModule],
      declarations: [AssessmentComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(AssessmentComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssessmentComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AssessmentService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 'ABC' }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.assessments?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to assessmentService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getAssessmentIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getAssessmentIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
