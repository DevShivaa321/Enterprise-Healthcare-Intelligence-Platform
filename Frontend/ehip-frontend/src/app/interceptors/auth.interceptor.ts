import { inject } from '@angular/core'; 

import { HttpInterceptorFn} from '@angular/common/http'; 

import { AuthService } from '../services/auth.service'; 

import { Router } from '@angular/router'; 

import { catchError, throwError } from 'rxjs';



export const AuthInterceptor: HttpInterceptorFn = (req, next) => {

    const authService = inject(AuthService);

    const router = inject(Router);

    const token = authService.getToken();    // get JWT token from local storage 
    const cloned = token ? req.clone({

        headers: req.headers.set('Authorization', `Bearer ${token}`)

    }) : req;                                 // Modified req with token and headers if token exists, otherwise original req 



    return next(cloned).pipe(                   // executes the API CALL 

        catchError((error) => {

            const isAuthCall = req.url.includes('/auth');     // to skip login API calls, POST /auth/login 



            if (error.status === 401 && !isAuthCall) {      // If unauthorized AND not login API call 

                authService.logout();

                router.navigate(['/login']);

            }

            return throwError(() => error);

        })

    );

}; 