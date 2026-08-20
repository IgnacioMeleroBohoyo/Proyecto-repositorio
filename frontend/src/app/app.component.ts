import { Component } from "@angular/core";

@Component({
  selector: "app-root",
  template: `
    <div style="max-width:800px;margin:2rem auto">
      <h1>Demo Spring Boot + Angular</h1>
      <nav>
        <a routerLink="/">Inicio</a> |
        <a routerLink="/login">Iniciar sesión</a>
      </nav>
      <router-outlet></router-outlet>
    </div>
  `,
})
export class AppComponent {}
