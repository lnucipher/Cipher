import {
  APP_INITIALIZER,
  ApplicationConfig,
  provideZoneChangeDetection,
} from '@angular/core';
import { provideRouter,Router } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';

import { routes } from './app.routes';
import { JwtService } from './core/auth/services/jwt.service';
import { UserService } from './core/auth/services/user.service';
import { apiInterceptor } from './core/interceptors/api.interceptor';
import { tokenInterceptor } from './core/interceptors/token.interceptor';
import { errorInterceptor } from './core/interceptors/error.interceptor';
import { EMPTY } from 'rxjs';

//initialized auth when the page loads,if user is loggedIn attempts to getCurrentUser 
// export function initAuth(jwtService: JwtService, userService: UserService) {
//   return () => (jwtService.getToken() ? userService.getCurrentUser() : EMPTY);
// }

//added for testing purposes
export function initAuth(jwtService: JwtService, userService: UserService) {
  return () => {
    const token = jwtService.getToken();
    if (token) {
      // Token exists, but we will remove it
      jwtService.destroyToken(); // Method to remove the token
      return EMPTY; // No user data to load, complete immediately
    } else {
      return EMPTY; // No token, complete immediately
    }
  };
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true, runCoalescing: true }),
    provideRouter(routes),
    provideAnimations(),
    provideHttpClient(
      withInterceptors([apiInterceptor, tokenInterceptor, errorInterceptor])
    ),
    {
      provide: APP_INITIALIZER,
      useFactory: initAuth,
      deps: [JwtService, UserService],
      multi: true,
    },
  ],
};
