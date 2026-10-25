import { CommonModule } from '@angular/common';
import { Component, input, output } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Account } from 'app/core/auth/account.model';

@Component({
  selector: 'jhi-hero-banner',
  templateUrl: './hero-banner.component.html',
  styleUrl: './hero-banner.component.scss',
  imports: [CommonModule, RouterLink, FontAwesomeModule],
})
export class HeroBannerComponent {
  public readonly account = input<Account | null>(null);
  public readonly librosCount = input<number>(0);
  public readonly categoriasCount = input<number>(0);
  public readonly promocionesCount = input<number>(0);

  public readonly loginRequested = output();

  public onLogin(): void {
    this.loginRequested.emit();
  }
}
