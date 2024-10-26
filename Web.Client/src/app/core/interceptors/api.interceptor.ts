import { HttpInterceptorFn } from '@angular/common/http';

//will be changed with addition of 2nd server
export const apiInterceptor: HttpInterceptorFn = (req, next) => {
  const apiReq = req.clone({ url: `http://localhost:4000/${req.url}` }); //predefined server adress
  return next(apiReq); //passes the modified http
};
