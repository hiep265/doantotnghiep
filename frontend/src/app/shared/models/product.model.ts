export interface Product {
  id: number;
  name: string;
  description?: string;
  categoryId?: number;
  brandId?: number;
  genderTarget?: string;
  season?: string;
  slug?: string;
  isFeatured?: boolean;
  isActive?: boolean;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface ProductVariant {
  id: number;
  productId: number;
  colorId?: number;
  sizeId?: number;
  sku?: string;
  price: number;
  stockQuantity: number;
  isActive?: boolean;
  createdAt?: Date;
  updatedAt?: Date;
  
  // Các trường bổ sung để hiển thị UI
  colorName?: string;
  sizeName?: string;
  imageUrl?: string;
}

export interface ProductDetail {
  product: Product;
  variants: ProductVariant[];
  images?: string[];
}

// --- Model cho trang danh sách sản phẩm ---
// Dựa trên các trường từ bảng products và giả định có thêm giá/ảnh từ variant chính
export interface ProductListItem {
  id: number; // Product ID
  name: string;
  slug?: string;
  // Giả định API trả về giá và ảnh của variant chính/mặc định
  price: number; 
  imageUrl?: string;
  primaryVariantId?: number; // ID của variant chính/mặc định để thêm vào giỏ/wishlist
  brandName?: string; // Có thể được join từ brand_id
  // Thêm các trường khác nếu cần và API trả về
}

export interface ProductListResponse {
  data: {
    content: ProductListItem[]; // Cập nhật cấu trúc lồng nhau
  };
  // Có thể thêm thông tin phân trang nếu API trả về ở cấp ngoài cùng
  status?: string;
  message?: string;
  totalItems?: number;
  totalPages?: number;
  currentPage?: number;
} 