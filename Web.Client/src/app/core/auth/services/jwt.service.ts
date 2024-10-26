import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })


export class JwtService {
  getToken(): string {
    return window.localStorage['jwtToken'];
  }

  saveToken(token: string): void {
    window.localStorage['jwtToken'] = token;
  }

  // destroy the token for logout/token outdated
  destroyToken(): void {
    window.localStorage.removeItem('jwtToken');
  }
}
