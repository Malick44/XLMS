import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AssessmentComponent } from '../list/assessment.component';
import { AssessmentDetailComponent } from '../detail/assessment-detail.component';
import { AssessmentUpdateComponent } from '../update/assessment-update.component';
import { AssessmentRoutingResolveService } from './assessment-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const assessmentRoute: Routes = [
  {
    path: '',
    component: AssessmentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssessmentDetailComponent,
    resolve: {
      assessment: AssessmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssessmentUpdateComponent,
    resolve: {
      assessment: AssessmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssessmentUpdateComponent,
    resolve: {
      assessment: AssessmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(assessmentRoute)],
  exports: [RouterModule],
})
export class AssessmentRoutingModule {}
