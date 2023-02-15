import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AssignmentService } from '../service/assignment.service';

import { AssignmentComponent } from './assignment.component';

describe('Assignment Management Component', () => {
  let comp: AssignmentComponent;
  let fixture: ComponentFixture<AssignmentComponent>;
  let service: AssignmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'assignment', component: AssignmentComponent }]), HttpClientTestingModule],
      declarations: [AssignmentComponent],
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
      .overrideTemplate(AssignmentComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssignmentComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AssignmentService);

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
    expect(comp.assignments?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to assignmentService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getAssignmentIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getAssignmentIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
