import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssessment } from '../assessment.model';

@Component({
  selector: 'jhi-assessment-detail',
  templateUrl: './assessment-detail.component.html',
})
export class AssessmentDetailComponent implements OnInit {
  assessment: IAssessment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assessment }) => {
      this.assessment = assessment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
