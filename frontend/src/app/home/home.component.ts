import { Component, OnInit } from "@angular/core";
import { AuthService } from "../services/auth.service";
import { Router } from "@angular/router";

@Component({
  selector: "app-home",
  template: `
    <h2>Inicio</h2>
    <div *ngIf="user; else anon">
      <p>Bienvenido, {{ user.username }}</p>
      <pre>{{ user | json }}</pre>
      <button (click)="logout()">Salir</button>
    </div>
    <ng-template #anon>
      <p>
        No autenticado. Por favor <a routerLink="/login">Iniciar sesión</a>.
      </p>
    </ng-template>
  `,
})
export class HomeComponent implements OnInit {
  user: any = null;

  constructor(
    private auth: AuthService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    const stored = this.auth.getUsername();
    if (stored) this.user = { username: stored };
    this.auth.me().subscribe({
      next: (u) => (this.user = u),
      error: () => (this.user = this.user || null),
    });
  }

  logout() {
    this.auth.logout();
    this.user = null;
    this.router.navigate(["/login"]);
  }
}
