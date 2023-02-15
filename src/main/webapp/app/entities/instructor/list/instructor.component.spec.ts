import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InstructorService } from '../service/instructor.service';

import { InstructorComponent } from './instructor.component';

describe('Instructor Management Component', () => {
  let comp: InstructorComponent;
  let fixture: ComponentFixture<InstructorComponent>;
  let service: InstructorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'instructor', component: InstructorComponent }]), HttpClientTestingModule],
      declarations: [InstructorComponent],
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
      .overrideTemplate(InstructorComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InstructorComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(InstructorService);

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
    expect(comp.instructors?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to instructorService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getInstructorIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getInstructorIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
