import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssessment } from '../assessment.model';
import { AssessmentService } from '../service/assessment.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './assessment-delete-dialog.component.html',
})
export class AssessmentDeleteDialogComponent {
  assessment?: IAssessment;

  constructor(protected assessmentService: AssessmentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.assessmentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
