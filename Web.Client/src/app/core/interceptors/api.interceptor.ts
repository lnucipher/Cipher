import { HttpInterceptorFn } from '@angular/common/http';

export const apiInterceptor: HttpInterceptorFn = (req, next) => {
  const apiReq = req.clone({
    url: `http://212.23.203.37:5000/${req.url}`,
  });
  return next(apiReq);
};