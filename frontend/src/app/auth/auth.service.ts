import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, BehaviorSubject, throwError } from 'rxjs';
import { catchError, tap, map } from 'rxjs/operators';
import { Router } from '@angular/router';
import { environment } from '../../enviroments/enviroment'; // Import environment
import { isPlatformBrowser } from '@angular/common';

interface AuthResponse {
  status: string;
  message: string;
  data: {
    accessToken: string;
    tokenType: string;
  };
  statusCode: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // Sử dụng URL API từ environment
  private apiUrl = environment.apiUrl + '/auth'; // Ví dụ: http://localhost:8080/api/auth

  // BehaviorSubject để theo dõi trạng thái đăng nhập
  // Khởi tạo bằng cách kiểm tra token trong localStorage
  private loggedIn = new BehaviorSubject<boolean>(false);
  isLoggedIn$ = this.loggedIn.asObservable(); // Observable cho các component theo dõi

  private currentUser = new BehaviorSubject<string | null>(null);
  currentUser$ = this.currentUser.asObservable();

  constructor(
    private http: HttpClient, 
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    if (isPlatformBrowser(this.platformId)) {
      this.loggedIn.next(this.hasToken());
      this.currentUser.next(this.getUsernameFromStorage());
    }
  }

  // Kiểm tra xem có token trong localStorage không
  private hasToken(): boolean {
    if (!isPlatformBrowser(this.platformId)) return false;
    return !!localStorage.getItem('authToken');
  }

  // Lấy token từ localStorage
  getToken(): string | null {
    if (!isPlatformBrowser(this.platformId)) return null;
    return localStorage.getItem('authToken');
  }

  // Lấy username từ localStorage (nếu đã lưu)
  private getUsernameFromStorage(): string | null {
    if (!isPlatformBrowser(this.platformId)) return null;
    return localStorage.getItem('username');
  }

  // Lưu token và username vào localStorage, cập nhật trạng thái
  private saveToken(token: string, username?: string): void {
    if (!isPlatformBrowser(this.platformId)) return;
    
    localStorage.setItem('authToken', token);
    if (username) {
        localStorage.setItem('username', username);
        this.currentUser.next(username);
    } else {
        // Nếu API không trả về username, có thể thử decode token (cần thư viện như jwt-decode)
        // Hoặc gọi một API khác để lấy thông tin user
        localStorage.removeItem('username');
        this.currentUser.next(null);
    }
    this.loggedIn.next(true);
  }

  // Xóa token và username, cập nhật trạng thái
  private removeToken(): void {
    if (!isPlatformBrowser(this.platformId)) return;
    
    localStorage.removeItem('authToken');
    localStorage.removeItem('username');
    this.loggedIn.next(false);
    this.currentUser.next(null);
  }

  // Gọi API đăng nhập
  login(credentials: any): Observable<AuthResponse> {
    console.log('Making login request to:', `${this.apiUrl}/login`);
    
    // Đảm bảo dữ liệu đúng định dạng
    const loginData = {
      phoneNumber: credentials.phoneNumber,
      password: credentials.password
    };
    
    console.log('Request payload:', loginData);
    
    const headers = {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    };
    
    // Gửi request không stringify data
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, loginData, { 
      headers,
      observe: 'response'
    }).pipe(
      tap(response => {
        console.log('Full login response:', response);
        if (response.body?.status === 'success') {
          this.saveToken(response.body.data.accessToken);
          console.log('Login successful, token saved.');
        }
      }),
      map(response => response.body as AuthResponse),
      catchError(error => {
        console.error('Login error details:', {
          status: error.status,
          statusText: error.statusText,
          error: error.error,
          message: error.message,
          headers: error.headers
        });
        
        let errorMessage = 'Số điện thoại hoặc mật khẩu không đúng.';
        if (error.error && error.error.message) {
          errorMessage = error.error.message;
        }
        
        return throwError(() => new Error(errorMessage));
      })
    );
  }

  // Gọi API đăng ký
  register(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, userData).pipe(
      tap(() => console.log('Registration successful')), // Chỉ log, không lưu token
      catchError(this.handleError) // Xử lý lỗi
    );
  }

  // Đăng xuất
  logout(): void {
    this.removeToken();
    console.log('Logged out, token removed.');
    this.router.navigate(['/login']); // Điều hướng về trang login
  }

  // Hàm xử lý lỗi HTTP cơ bản
  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Đã xảy ra lỗi không xác định!';
    
    if (error.error instanceof ErrorEvent) {
      // Lỗi phía client
      errorMessage = `Lỗi kết nối: ${error.error.message}`;
    } else {
      // Lỗi phía server
      if (error.error && error.error.message) {
        errorMessage = error.error.message;
      } else if (error.error && typeof error.error === 'string') {
        errorMessage = error.error;
      } else {
        errorMessage = `Lỗi ${error.status}: ${error.statusText}`;
      }
    }
    
    console.error('Chi tiết lỗi:', {
      status: error.status,
      statusText: error.statusText,
      error: error.error,
      message: errorMessage
    });
    
    return throwError(() => new Error(errorMessage));
  }
}
