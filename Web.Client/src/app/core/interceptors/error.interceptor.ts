import { HttpInterceptorFn } from '@angular/common/http';
import { HttpRequest, HttpHandlerFn, HttpEvent } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { inject } from '@angular/core';
import { UserService } from '../auth/services/user.service';

export const errorInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {
  const userService = inject(UserService);

  return next(req).pipe(
    catchError((err) => {
      if ([401, 403].includes(err.status)) {
        userService.logout(); // auto-logout if unauthorized
      }

      //reads error message if exists/sends default (for all the other errors)
      const errorMessage = err.error?.message || 'An unknown error occurred';
      console.error(err); //log all error body
      return throwError(() => new Error(errorMessage));
    })
  );
};
