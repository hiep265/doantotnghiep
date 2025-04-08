import { HttpInterceptorFn, HttpRequest, HttpHandlerFn, HttpEvent } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

// Functional Interceptor thay thế class TokenInterceptor
export const tokenInterceptor: HttpInterceptorFn = (
    req: HttpRequest<unknown>,
    next: HttpHandlerFn // Kiểu dữ liệu thay đổi một chút
  ): Observable<HttpEvent<unknown>> => {

  // Danh sách các đường dẫn không cần gửi token
  const excludedPaths = ['/api/auth/login', '/api/auth/register']; // Thêm các path khác nếu cần

  // Kiểm tra xem URL của request có nằm trong danh sách loại trừ không
  const isExcluded = excludedPaths.some(path => req.url.includes(path));

  // Nếu request đến các đường dẫn không cần token, gửi đi luôn mà không thêm header
  if (isExcluded) {
    return next(req);
  }

  // Nếu không phải đường dẫn loại trừ, mới lấy token và thêm header
  const authService = inject(AuthService); // Inject AuthService bằng hàm inject()
  const token = authService.getToken();

  // Nếu có token, clone request và thêm header
  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  // Gửi request đi tiếp
  return next(req);
};
