import { Component } from "@angular/core";
import { Router } from "@angular/router";
import { AuthService } from "../services/auth.service";

@Component({
  selector: "app-login",
  template: `
    <h2>Iniciar sesión</h2>
    <form (ngSubmit)="login()">
      <div>
        <label>Usuario</label>
        <input [(ngModel)]="username" name="username" />
      </div>
      <div>
        <label>Contraseña</label>
        <input [(ngModel)]="password" name="password" type="password" />
      </div>
      <button type="submit">Entrar</button>
    </form>
    <p *ngIf="error" style="color:red">{{ error }}</p>
  `,
})
export class LoginComponent {
  username = "";
  password = "";
  error = "";

  constructor(
    private auth: AuthService,
    private router: Router,
  ) {}

  login() {
    this.error = "";
    this.auth.login(this.username, this.password).subscribe({
      next: () => this.router.navigate(["/"]),
      error: (err) => {
        const msg =
          err && err.error && err.error.message
            ? err.error.message
            : "Credenciales inválidas";
        this.error = msg;
      },
    });
  }
}
