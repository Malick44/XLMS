import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISection, NewSection } from '../section.model';

export type PartialUpdateSection = Partial<ISection> & Pick<ISection, 'id'>;

export type EntityResponseType = HttpResponse<ISection>;
export type EntityArrayResponseType = HttpResponse<ISection[]>;

@Injectable({ providedIn: 'root' })
export class SectionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sections');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(section: NewSection): Observable<EntityResponseType> {
    return this.http.post<ISection>(this.resourceUrl, section, { observe: 'response' });
  }

  update(section: ISection): Observable<EntityResponseType> {
    return this.http.put<ISection>(`${this.resourceUrl}/${this.getSectionIdentifier(section)}`, section, { observe: 'response' });
  }

  partialUpdate(section: PartialUpdateSection): Observable<EntityResponseType> {
    return this.http.patch<ISection>(`${this.resourceUrl}/${this.getSectionIdentifier(section)}`, section, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ISection>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISection[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSectionIdentifier(section: Pick<ISection, 'id'>): string {
    return section.id;
  }

  compareSection(o1: Pick<ISection, 'id'> | null, o2: Pick<ISection, 'id'> | null): boolean {
    return o1 && o2 ? this.getSectionIdentifier(o1) === this.getSectionIdentifier(o2) : o1 === o2;
  }

  addSectionToCollectionIfMissing<Type extends Pick<ISection, 'id'>>(
    sectionCollection: Type[],
    ...sectionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sections: Type[] = sectionsToCheck.filter(isPresent);
    if (sections.length > 0) {
      const sectionCollectionIdentifiers = sectionCollection.map(sectionItem => this.getSectionIdentifier(sectionItem)!);
      const sectionsToAdd = sections.filter(sectionItem => {
        const sectionIdentifier = this.getSectionIdentifier(sectionItem);
        if (sectionCollectionIdentifiers.includes(sectionIdentifier)) {
          return false;
        }
        sectionCollectionIdentifiers.push(sectionIdentifier);
        return true;
      });
      return [...sectionsToAdd, ...sectionCollection];
    }
    return sectionCollection;
  }
}
