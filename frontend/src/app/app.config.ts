import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { provideHttpClient, withInterceptors, withFetch } from '@angular/common/http';
// import { ReactiveFormsModule } from '@angular/forms'; // Có thể import ở đây hoặc trong từng component

import { routes } from './app.routes'; // Import routes từ file mới
import { tokenInterceptor } from './auth/token.interceptor'; // Import functional interceptor
// AuthGuard sẽ được cung cấp trong app.routes.ts hoặc ở đây nếu cần global

export const appConfig: ApplicationConfig = {
  providers: [
    // Cung cấp Router với routes và các tính năng tùy chọn
    provideRouter(routes, withComponentInputBinding()), // withComponentInputBinding() là tùy chọn

    // Cung cấp HttpClient và đăng ký functional interceptor
    provideHttpClient(
      withInterceptors([tokenInterceptor]),
      withFetch()
    ),

    // Nếu có module không có hàm provide* riêng, dùng importProvidersFrom
    // importProvidersFrom(ReactiveFormsModule) // Ví dụ, nhưng thường ReactiveFormsModule được import trong component
  ]
};