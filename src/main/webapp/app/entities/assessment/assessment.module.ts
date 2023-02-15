import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AssessmentComponent } from './list/assessment.component';
import { AssessmentDetailComponent } from './detail/assessment-detail.component';
import { AssessmentUpdateComponent } from './update/assessment-update.component';
import { AssessmentDeleteDialogComponent } from './delete/assessment-delete-dialog.component';
import { AssessmentRoutingModule } from './route/assessment-routing.module';

@NgModule({
  imports: [SharedModule, AssessmentRoutingModule],
  declarations: [AssessmentComponent, AssessmentDetailComponent, AssessmentUpdateComponent, AssessmentDeleteDialogComponent],
})
export class AssessmentModule {}
