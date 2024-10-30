import { HttpInterceptorFn } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export const apiInterceptor: HttpInterceptorFn = (req, next) => {
  const apiReq = req.clone({
    url: `http://192.168.88.192:${req.url}` // Uses environment-specific API URL
  });
  return next(apiReq);
};
