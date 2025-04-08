import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { HomeComponent } from './home/home.component';
import { authGuard } from './auth/auth.guard'; // Import functional guard

export const routes: Routes = [
  // Route mặc định: Nếu đã đăng nhập thì vào /home, chưa thì vào /login
  // Việc kiểm tra đăng nhập có thể thực hiện trong guard hoặc logic khác
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [authGuard] // Sử dụng functional guard trực tiếp
  },
  // Thêm route cho trang 404 nếu cần
  { path: '**', redirectTo: '/login' } // Chuyển hướng về login nếu không khớp route nào
];
