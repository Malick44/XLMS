import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssessment } from '../assessment.model';
import { AssessmentService } from '../service/assessment.service';

@Injectable({ providedIn: 'root' })
export class AssessmentRoutingResolveService implements Resolve<IAssessment | null> {
  constructor(protected service: AssessmentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAssessment | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((assessment: HttpResponse<IAssessment>) => {
          if (assessment.body) {
            return of(assessment.body);
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
