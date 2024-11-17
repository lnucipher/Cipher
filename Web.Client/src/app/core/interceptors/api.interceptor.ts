import { HttpInterceptorFn } from '@angular/common/http';

export const apiInterceptor: HttpInterceptorFn = (req, next) => {
  const apiReq = req.clone({
    url: `https://localhost:5000/${req.url}`,
  });
  return next(apiReq);
};