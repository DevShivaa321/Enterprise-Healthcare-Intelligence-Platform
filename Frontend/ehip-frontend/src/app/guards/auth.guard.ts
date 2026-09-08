import { CanActivateFn, Router } from '@angular/router'; 

import { AuthService } from '../services/auth.service'; 

import { inject } from '@angular/core'; 

 

export const authGuard: CanActivateFn = () => { 

  const authService = inject(AuthService); 

  const router = inject(Router); 

 

  const token = authService.getToken(); 

 

  if (!token) { 

    router.navigate(['/login']); 

    return false;   //blocks access to the route if not logged in 

  } 

 

  try { 

    // Decode token 

    const payload = JSON.parse(atob(token.split('.')[1])); 

    const expiry = payload.exp; 

 

    // Check expiry 

    if (Date.now() >= expiry * 1000) { 

      authService.logout(); 

      router.navigate(['/login']); 

      return false;    // blocks access to the route if token is expired 

    } 

 

    return true; 

 

  } catch (error) { 

    // If token is invalid 

    authService.logout(); 

    router.navigate(['/login']); 

    return false;     // blocks access to the route if token is invalid 

  } 

}; 