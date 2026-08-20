import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { map } from "rxjs/operators";

@Injectable({ providedIn: "root" })
export class AuthService {
  base = "/api";

  constructor(private http: HttpClient) {}

  login(username: string, password: string) {
    return this.http
      .post<any>(this.base + "/auth/login", { username, password })
      .pipe(
        map((r) => {
          if (r && r.token) {
            localStorage.setItem("token", r.token);
            if (r.username) localStorage.setItem("username", r.username);
          }
          return r;
        }),
      );
  }

  logout() {
    localStorage.removeItem("token");
  }

  getToken() {
    return localStorage.getItem("token");
  }

  getUsername() {
    return localStorage.getItem("username");
  }

  me() {
    return this.http.get<any>(this.base + "/users/me");
  }
}
