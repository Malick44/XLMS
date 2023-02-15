import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInstructor } from '../instructor.model';
import { InstructorService } from '../service/instructor.service';

@Injectable({ providedIn: 'root' })
export class InstructorRoutingResolveService implements Resolve<IInstructor | null> {
  constructor(protected service: InstructorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInstructor | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((instructor: HttpResponse<IInstructor>) => {
          if (instructor.body) {
            return of(instructor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
