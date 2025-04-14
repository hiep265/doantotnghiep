import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { HomeComponent } from './home/home.component';
import { WishlistComponent } from './wishlist/wishlist.component';
import { ProductListComponent } from './products/product-list/product-list.component';
import { authGuard } from './auth/auth.guard'; // Import functional guard

export const routes: Routes = [
  // Đặt trang products làm trang mặc định
  { path: '', redirectTo: '/products', pathMatch: 'full' }, 
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [authGuard]
  },
  {
    path: 'wishlist',
    component: WishlistComponent,
    canActivate: [authGuard]
  },
  {
    path: 'products',
    component: ProductListComponent
    // Có thể thêm canActivate: [authGuard] nếu cần đăng nhập để xem sản phẩm
  },
  // Thêm route cho trang chi tiết sản phẩm (ví dụ: /products/:slug)
  // { path: 'products/:slug', component: ProductDetailComponent }, 
  // Nếu không khớp route nào, chuyển hướng về trang products
  { path: '**', redirectTo: '/products' } 
];
