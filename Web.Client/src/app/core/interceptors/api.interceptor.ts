import { HttpInterceptorFn } from '@angular/common/http';
import { environment } from '../enviroments/enviroment';

export const apiInterceptor: HttpInterceptorFn = (req, next) => {
  const apiReq = req.clone({
    url: `${environment.apiUrl}${req.url}` // Uses environment-specific API URL
  });
  return next(apiReq);
};
