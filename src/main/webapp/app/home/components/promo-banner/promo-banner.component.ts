import { CommonModule } from '@angular/common';
import { Component, input } from '@angular/core';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { IPromocion } from 'app/entities/promocion/promocion.model';

@Component({
  selector: 'jhi-promo-banner',
  templateUrl: './promo-banner.component.html',
  styleUrl: './promo-banner.component.scss',
  imports: [CommonModule, FontAwesomeModule],
})
export class PromoBannerComponent {
  public readonly promocion = input.required<IPromocion>();
}
