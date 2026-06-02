import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-config',
  standalone: true,
  imports: [CommonModule, RouterModule, MatCardModule, MatIconModule, MatButtonModule],
  template: `
    <div class="config-container">
      <mat-card>
        <mat-card-header>
          <mat-icon mat-card-avatar>settings</mat-icon>
          <mat-card-title>Ajustes del Sistema</mat-card-title>
          <mat-card-subtitle>Configuración general</mat-card-subtitle>
        </mat-card-header>
        <mat-card-content>
          <p class="placeholder-text">Módulo de configuración en desarrollo.</p>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .config-container { padding: 24px; max-width: 800px; }
    .placeholder-text { color: #666; margin-top: 16px; }
  `],
})
export class ConfigComponent {}
