
import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { AuthService } from './auth.service';

// Functional Guard thay thế class AuthGuard
export const authGuard: CanActivateFn = (route, state): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree => {
  const authService = inject(AuthService); // Inject AuthService bằng hàm inject()
  const router = inject(Router); // Inject Router

  return authService.isLoggedIn$.pipe(
    take(1), // Chỉ lấy giá trị hiện tại
    map(isLoggedIn => {
      if (isLoggedIn) {
        return true; // Cho phép truy cập
      } else {
        console.log('AuthGuard: User not logged in, redirecting to login.');
        // Điều hướng về trang login nếu chưa đăng nhập
        return router.createUrlTree(['/login']);
      }
    })
  );
};
