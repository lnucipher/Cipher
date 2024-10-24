import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })

//retrieve token for the token interceptor
export class JwtService {
  getToken(): string {
    return window.localStorage['jwtToken'];
  }

  //save jwt token
  saveToken(token: string): void {
    window.localStorage['jwtToken'] = token;
  }

  // destroy the token for logout/token outdated
  destroyToken(): void {
    window.localStorage.removeItem('jwtToken');
  }
}
