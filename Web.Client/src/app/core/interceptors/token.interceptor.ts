import { inject } from "@angular/core";
import { HttpInterceptorFn } from "@angular/common/http";
import { JwtService } from "../auth/services/jwt.service";

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  const token = inject(JwtService).getToken(); //gets the token

  const request = req.clone({//clones the request sent to the server
    //adds token to message 
    setHeaders: {
      ...(token ? { token: `${token}` } : {}),// no token=empy object / token exists adds token header
    },
  });
  return next(request);
};
