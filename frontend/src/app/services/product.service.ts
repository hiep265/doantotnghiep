import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, tap, map } from 'rxjs/operators';
import { environment } from '../../enviroments/enviroment';
import { ProductListResponse } from '../shared/models/product.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = `${environment.apiUrl}/products`;

  constructor(private http: HttpClient) { }

  getProducts(): Observable<ProductListResponse> {
    console.log('Fetching products from:', this.apiUrl);
    
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
      // Không cần gửi token ở đây nếu API public
    });

    return this.http.get<ProductListResponse>(this.apiUrl, { headers })
      .pipe(
        tap(response => console.log('Products API response:', response)),
        catchError(this.handleError)
      );
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An unknown error occurred!';
    if (error.error instanceof ErrorEvent) {
      // Client-side or network error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Backend returned an unsuccessful response code.
      errorMessage = `Server returned code ${error.status}, error message is: ${error.message}`;
    }
    console.error('ProductService Error:', errorMessage, error);
    return throwError(() => new Error(errorMessage));
  }
} 