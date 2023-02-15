import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'student',
        data: { pageTitle: 'opnlmsApp.student.home.title' },
        loadChildren: () => import('./student/student.module').then(m => m.StudentModule),
      },
      {
        path: 'instructor',
        data: { pageTitle: 'opnlmsApp.instructor.home.title' },
        loadChildren: () => import('./instructor/instructor.module').then(m => m.InstructorModule),
      },
      {
        path: 'course',
        data: { pageTitle: 'opnlmsApp.course.home.title' },
        loadChildren: () => import('./course/course.module').then(m => m.CourseModule),
      },
      {
        path: 'section',
        data: { pageTitle: 'opnlmsApp.section.home.title' },
        loadChildren: () => import('./section/section.module').then(m => m.SectionModule),
      },
      {
        path: 'assignment',
        data: { pageTitle: 'opnlmsApp.assignment.home.title' },
        loadChildren: () => import('./assignment/assignment.module').then(m => m.AssignmentModule),
      },
      {
        path: 'question',
        data: { pageTitle: 'opnlmsApp.question.home.title' },
        loadChildren: () => import('./question/question.module').then(m => m.QuestionModule),
      },
      {
        path: 'option',
        data: { pageTitle: 'opnlmsApp.option.home.title' },
        loadChildren: () => import('./option/option.module').then(m => m.OptionModule),
      },
      {
        path: 'assessment',
        data: { pageTitle: 'opnlmsApp.assessment.home.title' },
        loadChildren: () => import('./assessment/assessment.module').then(m => m.AssessmentModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
