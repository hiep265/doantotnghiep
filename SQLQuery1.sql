-- T?o Database n?u ch?a t?n t?i (Tùy ch?n, b?n có th? t?o th? công)
-- IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'shopapp_clothing')
-- BEGIN
--     CREATE DATABASE shopapp_clothing;
-- END
-- GO -- Dùng GO ?? tách batch trong SSMS ho?c sqlcmd

-- S? d?ng Database
--USE shopapp_clothing;
--GO

-- ========== USER & AUTHENTICATION ==========

CREATE TABLE roles (
    id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(50) NOT NULL UNIQUE -- Tên quy?n (vd: admin, user)
);
GO

-- Chèn d? li?u vai trò m?c ??nh
INSERT INTO roles (name) VALUES (N'admin'), (N'user');
GO

CREATE TABLE addresses ( -- T?o addresses tr??c vì users có FK ??n nó
    id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL, -- S? thêm FK sau khi t?o b?ng users
    fullname NVARCHAR(100) NOT NULL,
    phone_number NVARCHAR(15) NOT NULL,
    street_address NVARCHAR(255) NOT NULL, -- S? nhà, tên ???ng
    ward NVARCHAR(100) NOT NULL, -- Ph??ng/Xã
    district NVARCHAR(100) NOT NULL, -- Qu?n/Huy?n
    city NVARCHAR(100) NOT NULL, -- T?nh/Thành ph?
    country NVARCHAR(100) DEFAULT N'Vietnam',
    is_default BIT DEFAULT 0, -- 1: ??a ch? m?c ??nh
    created_at DATETIME2(7) DEFAULT SYSDATETIME(),
    updated_at DATETIME2(7) DEFAULT SYSDATETIME()
    -- FK ??n users s? thêm sau
);
GO

CREATE TABLE users (
    id INT PRIMARY KEY IDENTITY(1,1),
    fullname NVARCHAR(100) DEFAULT N'',
    phone_number NVARCHAR(15) NOT NULL UNIQUE,
    address_default_id INT NULL, -- FK ??n b?ng addresses, ??a ch? m?c ??nh
    password NVARCHAR(255) NOT NULL DEFAULT N'', -- L?u m?t kh?u ?ã hash
    date_of_birth DATE NULL,
    is_active BIT DEFAULT 1, -- 0: inactive, 1: active
    role_id INT NOT NULL DEFAULT 2, -- M?c ??nh là user (ID=2 theo th? t? insert)
    created_at DATETIME2(7) DEFAULT SYSDATETIME(),
    updated_at DATETIME2(7) DEFAULT SYSDATETIME(),
    CONSTRAINT FK_users_roles FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT FK_users_default_address FOREIGN KEY (address_default_id) REFERENCES addresses (id) ON DELETE SET NULL
);
GO

-- Thêm FK t? addresses ??n users sau khi users ???c t?o
ALTER TABLE addresses ADD CONSTRAINT FK_addresses_users
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
GO

CREATE TABLE social_accounts (
    id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    provider NVARCHAR(20) NOT NULL, -- Tên nhà social network (facebook, google)
    provider_id NVARCHAR(100) NOT NULL, -- ID ng??i dùng t? nhà cung c?p
    email NVARCHAR(150) DEFAULT N'', -- Email t? tài kho?n social
    name NVARCHAR(100) DEFAULT N'', -- Tên t? tài kho?n social
    created_at DATETIME2(7) DEFAULT SYSDATETIME(),
    CONSTRAINT UQ_social_provider UNIQUE (provider, provider_id), -- ??m b?o không trùng l?p tài kho?n social
    CONSTRAINT FK_social_accounts_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
GO

-- ========== PRODUCT CATALOG ==========

CREATE TABLE categories (
    id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL DEFAULT N'', -- Tên danh m?c (vd: Áo S? Mi Nam, Váy Công S?)
    parent_id INT NULL, -- FK ??n chính nó, cho danh m?c ?a c?p
    slug NVARCHAR(150) UNIQUE, -- URL-friendly name (vd: ao-so-mi-nam)
    description NVARCHAR(MAX) NULL,
    image_url NVARCHAR(300) DEFAULT N'',
    is_active BIT DEFAULT 1,
    created_at DATETIME2(7) DEFAULT SYSDATETIME(),
    updated_at DATETIME2(7) DEFAULT SYSDATETIME(),
    CONSTRAINT FK_categories_parent FOREIGN KEY (parent_id) REFERENCES categories (id) ON DELETE NO ACTION -- Ho?c SET NULL tùy logic
);
GO

CREATE TABLE brands (
    id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL UNIQUE, -- Tên th??ng hi?u
    slug NVARCHAR(150) UNIQUE,
    description NVARCHAR(MAX) NULL,
    logo_url NVARCHAR(300) DEFAULT N'',
    is_active BIT DEFAULT 1,
    created_at DATETIME2(7) DEFAULT SYSDATETIME(),
    updated_at DATETIME2(7) DEFAULT SYSDATETIME()
);
GO

CREATE TABLE colors (
    id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(50) NOT NULL UNIQUE, -- Tên màu (vd: Xanh Navy, Tr?ng)
    hex_code NVARCHAR(7) NULL UNIQUE -- Mã màu HEX (vd: #000080)
);
GO

CREATE TABLE sizes (
    id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(20) NOT NULL UNIQUE -- Tên size (vd: S, M, L, XL, 38, 39)
);
GO

CREATE TABLE materials (
    id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL UNIQUE -- Tên ch?t li?u (vd: Cotton, L?a, Polyester)
);
GO

CREATE TABLE styles (
    id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL UNIQUE -- Tên ki?u dáng (vd: Công s?, Streetwear, Vintage)
);
GO

CREATE TABLE occasions (
    id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL UNIQUE -- D?p s? d?ng (vd: ?i ti?c, ?i làm, ?i bi?n)
);
GO

CREATE TABLE products (
    id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(350) NOT NULL, -- Tên s?n ph?m
    description NVARCHAR(MAX) DEFAULT N'',
    category_id INT NOT NULL,
    brand_id INT NULL,
    gender_target NVARCHAR(10) NULL, -- Gi?i tính m?c tiêu
    season NVARCHAR(20) NULL, -- Mùa phù h?p
    slug NVARCHAR(400) UNIQUE, -- URL-friendly name
    is_featured BIT DEFAULT 0, -- S?n ph?m n?i b?t?
    is_active BIT DEFAULT 1,
    created_at DATETIME2(7) DEFAULT SYSDATETIME(),
    updated_at DATETIME2(7) DEFAULT SYSDATETIME(),
    CONSTRAINT FK_products_categories FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT FK_products_brands FOREIGN KEY (brand_id) REFERENCES brands (id) ON DELETE SET NULL,
    CONSTRAINT CK_products_gender CHECK (gender_target IN (N'male', N'female', N'unisex') OR gender_target IS NULL),
    CONSTRAINT CK_products_season CHECK (season IN (N'spring', N'summer', N'autumn', N'winter', N'all_season') OR season IS NULL)
);
GO

CREATE TABLE product_variants (
    id INT PRIMARY KEY IDENTITY(1,1),
    product_id INT NOT NULL,
    color_id INT NOT NULL,
    size_id INT NOT NULL,
    sku NVARCHAR(100) UNIQUE, -- Stock Keeping Unit - Mã ??nh danh duy nh?t cho variant
    price DECIMAL(15, 2) NOT NULL CHECK (price >= 0), -- Giá c?a bi?n th? này
    stock_quantity INT NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0), -- S? l??ng t?n kho
    is_active BIT DEFAULT 1,
    created_at DATETIME2(7) DEFAULT SYSDATETIME(),
    updated_at DATETIME2(7) DEFAULT SYSDATETIME(),
    CONSTRAINT UQ_product_variant UNIQUE (product_id, color_id, size_id), -- ??m b?o m?i combo product-color-size là duy nh?t
    CONSTRAINT FK_product_variants_products FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    CONSTRAINT FK_product_variants_colors FOREIGN KEY (color_id) REFERENCES colors (id),
    CONSTRAINT FK_product_variants_sizes FOREIGN KEY (size_id) REFERENCES sizes (id)
);
GO

CREATE TABLE product_images (
    id INT PRIMARY KEY IDENTITY(1,1),
    product_variant_id INT NOT NULL, -- Liên k?t v?i bi?n th? c? th?
    image_url NVARCHAR(300) NOT NULL,
    alt_text NVARCHAR(200) DEFAULT N'',
    is_thumbnail BIT DEFAULT 0, -- ?nh ??i di?n chính cho variant?
    display_order INT DEFAULT 0, -- Th? t? hi?n th?
    CONSTRAINT FK_product_images_variants FOREIGN KEY (product_variant_id) REFERENCES product_variants (id) ON DELETE CASCADE
);
GO

-- B?ng n?i Many-to-Many
CREATE TABLE product_materials (
    product_id INT NOT NULL,
    material_id INT NOT NULL,
    PRIMARY KEY (product_id, material_id),
    CONSTRAINT FK_product_materials_products FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    CONSTRAINT FK_product_materials_materials FOREIGN KEY (material_id) REFERENCES materials (id) ON DELETE CASCADE
);
GO

CREATE TABLE product_styles (
    product_id INT NOT NULL,
    style_id INT NOT NULL,
    PRIMARY KEY (product_id, style_id),
    CONSTRAINT FK_product_styles_products FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    CONSTRAINT FK_product_styles_styles FOREIGN KEY (style_id) REFERENCES styles (id) ON DELETE CASCADE
);
GO

CREATE TABLE product_occasions (
    product_id INT NOT NULL,
    occasion_id INT NOT NULL,
    PRIMARY KEY (product_id, occasion_id),
    CONSTRAINT FK_product_occasions_products FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    CONSTRAINT FK_product_occasions_occasions FOREIGN KEY (occasion_id) REFERENCES occasions (id) ON DELETE CASCADE
);
GO

-- ========== ORDER PROCESSING ==========

CREATE TABLE orders (
    id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NULL, -- Null n?u là khách vãng lai
    fullname NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NULL, -- Có th? null n?u user không ??ng nh?p
    phone_number NVARCHAR(15) NOT NULL,
    shipping_address_id INT NULL, -- FK ??n b?ng addresses
    shipping_address_snapshot NVARCHAR(MAX) NULL, -- L?u tr? ??a ch? d?ng text t?i th?i ?i?m ??t hàng
    note NVARCHAR(255) DEFAULT N'',
    order_date DATETIME2(7) DEFAULT SYSDATETIME(),
    status NVARCHAR(20) NOT NULL DEFAULT N'pending', -- Thêm tr?ng thái returned
    sub_total DECIMAL(15, 2) NOT NULL DEFAULT 0 CHECK(sub_total >= 0), -- T?ng ti?n hàng tr??c gi?m giá/phí
    shipping_fee DECIMAL(10, 2) NOT NULL DEFAULT 0 CHECK(shipping_fee >= 0),
    discount_amount DECIMAL(15, 2) NOT NULL DEFAULT 0 CHECK(discount_amount >= 0),
    total_money DECIMAL(15, 2) NOT NULL CHECK(total_money >= 0), -- T?ng ti?n cu?i cùng
    shipping_method NVARCHAR(100) DEFAULT N'',
    shipping_date DATE NULL,
    tracking_number NVARCHAR(100) DEFAULT N'',
    payment_method NVARCHAR(100) DEFAULT N'',
    payment_status NVARCHAR(20) NOT NULL DEFAULT N'pending',
    is_active BIT DEFAULT 1, -- Cho phép xóa m?m
    created_at DATETIME2(7) DEFAULT SYSDATETIME(),
    updated_at DATETIME2(7) DEFAULT SYSDATETIME(),
    CONSTRAINT FK_orders_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL,
    CONSTRAINT FK_orders_addresses FOREIGN KEY (shipping_address_id) REFERENCES addresses(id) ON DELETE SET NULL,
    CONSTRAINT CK_orders_status CHECK (status IN (N'pending', N'processing', N'shipped', N'delivered', N'cancelled', N'returned')),
    CONSTRAINT CK_orders_payment_status CHECK (payment_status IN (N'pending', N'paid', N'failed', N'refunded'))
);
GO

CREATE TABLE order_details (
    id INT PRIMARY KEY IDENTITY(1,1),
    order_id INT NOT NULL,
    product_variant_id INT NOT NULL, -- FK ??n bi?n th? s?n ph?m c? th? ?ã mua
    price_per_unit DECIMAL(15, 2) NOT NULL CHECK(price_per_unit >= 0), -- Giá c?a 1 s?n ph?m t?i th?i ?i?m mua
    number_of_products INT NOT NULL CHECK(number_of_products > 0),
    CONSTRAINT FK_order_details_orders FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    CONSTRAINT FK_order_details_variants FOREIGN KEY (product_variant_id) REFERENCES product_variants (id) -- ON DELETE NO ACTION (m?c ??nh)
);
GO

-- ========== FEATURES FOR PERSUASION & LLM INPUT ==========

CREATE TABLE reviews (
    id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    product_id INT NOT NULL, -- ?ánh giá cho s?n ph?m g?c
    rating TINYINT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment NVARCHAR(MAX) NULL,
    is_approved BIT DEFAULT 0, -- Admin duy?t?
    created_at DATETIME2(7) DEFAULT SYSDATETIME(),
    updated_at DATETIME2(7) DEFAULT SYSDATETIME(),
    CONSTRAINT FK_reviews_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT FK_reviews_products FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
);
GO

CREATE TABLE wishlists (
    id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL UNIQUE, -- M?i user có 1 wishlist chính
    created_at DATETIME2(7) DEFAULT SYSDATETIME(),
    CONSTRAINT FK_wishlists_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
GO

CREATE TABLE wishlist_items (
    id INT PRIMARY KEY IDENTITY(1,1),
    wishlist_id INT NOT NULL,
    product_variant_id INT NOT NULL, -- Thêm bi?n th? c? th? vào wishlist
    added_at DATETIME2(7) DEFAULT SYSDATETIME(),
    CONSTRAINT UQ_wishlist_item UNIQUE (wishlist_id, product_variant_id),
    CONSTRAINT FK_wishlist_items_wishlists FOREIGN KEY (wishlist_id) REFERENCES wishlists (id) ON DELETE CASCADE,
    CONSTRAINT FK_wishlist_items_variants FOREIGN KEY (product_variant_id) REFERENCES product_variants (id) ON DELETE CASCADE
);
GO

CREATE TABLE promotions (
    id INT PRIMARY KEY IDENTITY(1,1),
    code NVARCHAR(50) UNIQUE NULL, -- Mã gi?m giá (có th? null n?u là KM t? ??ng)
    description NVARCHAR(MAX) NOT NULL,
    discount_type NVARCHAR(20) NOT NULL,
    discount_value DECIMAL(15, 2) NOT NULL,
    start_date DATETIME2(7) NOT NULL,
    end_date DATETIME2(7) NOT NULL,
    max_uses INT NULL, -- S? l?n s? d?ng t?i ?a t?ng c?ng
    max_uses_per_user INT NULL, -- S? l?n s? d?ng t?i ?a cho m?i user
    min_order_value DECIMAL(15, 2) NULL, -- Giá tr? ??n hàng t?i thi?u ?? áp d?ng
    applicable_to NVARCHAR(30) DEFAULT N'all',
    is_active BIT DEFAULT 1,
    created_at DATETIME2(7) DEFAULT SYSDATETIME(),
    updated_at DATETIME2(7) DEFAULT SYSDATETIME(),
    CONSTRAINT CK_promotions_discount_type CHECK (discount_type IN (N'percentage', N'fixed_amount')),
    CONSTRAINT CK_promotions_applicable_to CHECK (applicable_to IN (N'all', N'specific_categories', N'specific_products'))
);
GO

CREATE TABLE promotion_categories (
    promotion_id INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (promotion_id, category_id),
    CONSTRAINT FK_promo_cat_promotions FOREIGN KEY (promotion_id) REFERENCES promotions (id) ON DELETE CASCADE,
    CONSTRAINT FK_promo_cat_categories FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
);
GO

CREATE TABLE promotion_products (
    promotion_id INT NOT NULL,
    product_id INT NOT NULL,
    PRIMARY KEY (promotion_id, product_id),
    CONSTRAINT FK_promo_prod_promotions FOREIGN KEY (promotion_id) REFERENCES promotions (id) ON DELETE CASCADE,
    CONSTRAINT FK_promo_prod_products FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
);
GO

CREATE TABLE user_product_views (
    id BIGINT PRIMARY KEY IDENTITY(1,1), -- Dùng BIGINT vì b?ng này có th? r?t l?n
    user_id INT NULL, -- Null n?u là khách vãng lai
    product_variant_id INT NOT NULL,
    viewed_at DATETIME2(7) DEFAULT SYSDATETIME(),
    session_id NVARCHAR(100) NULL, -- ?? nhóm các view c?a khách vãng lai trong 1 phiên
    CONSTRAINT FK_user_views_users FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL, -- SET NULL khi user b? xóa
    CONSTRAINT FK_user_views_variants FOREIGN KEY (product_variant_id) REFERENCES product_variants(id) ON DELETE CASCADE
);
GO

CREATE TABLE user_searches (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    user_id INT NULL,
    search_query NVARCHAR(255) NOT NULL,
    searched_at DATETIME2(7) DEFAULT SYSDATETIME(),
    session_id NVARCHAR(100) NULL,
    CONSTRAINT FK_user_searches_users FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL -- SET NULL khi user b? xóa
);
GO

-- ========== TRIGGERS FOR updated_at ==========

-- Trigger cho b?ng 'users'
CREATE TRIGGER TRG_users_updated_at
ON users
AFTER UPDATE
AS
BEGIN
    IF UPDATE(updated_at) -- Ng?n trigger ch?y l?p l?i n?u chính nó c?p nh?t updated_at
        RETURN;

    UPDATE users
    SET updated_at = SYSDATETIME()
    FROM users u
    INNER JOIN inserted i ON u.id = i.id;
END;
GO

-- Trigger cho b?ng 'addresses'
CREATE TRIGGER TRG_addresses_updated_at
ON addresses
AFTER UPDATE
AS
BEGIN
     IF UPDATE(updated_at) RETURN;
     UPDATE addresses SET updated_at = SYSDATETIME() FROM addresses a INNER JOIN inserted i ON a.id = i.id;
END;
GO

-- Trigger cho b?ng 'categories'
CREATE TRIGGER TRG_categories_updated_at
ON categories
AFTER UPDATE
AS
BEGIN
     IF UPDATE(updated_at) RETURN;
     UPDATE categories SET updated_at = SYSDATETIME() FROM categories c INNER JOIN inserted i ON c.id = i.id;
END;
GO

-- Trigger cho b?ng 'brands'
CREATE TRIGGER TRG_brands_updated_at
ON brands
AFTER UPDATE
AS
BEGIN
     IF UPDATE(updated_at) RETURN;
     UPDATE brands SET updated_at = SYSDATETIME() FROM brands b INNER JOIN inserted i ON b.id = i.id;
END;
GO

-- Trigger cho b?ng 'products'
CREATE TRIGGER TRG_products_updated_at
ON products
AFTER UPDATE
AS
BEGIN
     IF UPDATE(updated_at) RETURN;
     UPDATE products SET updated_at = SYSDATETIME() FROM products p INNER JOIN inserted i ON p.id = i.id;
END;
GO

-- Trigger cho b?ng 'product_variants'
CREATE TRIGGER TRG_product_variants_updated_at
ON product_variants
AFTER UPDATE
AS
BEGIN
     IF UPDATE(updated_at) RETURN;
     UPDATE product_variants SET updated_at = SYSDATETIME() FROM product_variants pv INNER JOIN inserted i ON pv.id = i.id;
END;
GO

-- Trigger cho b?ng 'orders'
CREATE TRIGGER TRG_orders_updated_at
ON orders
AFTER UPDATE
AS
BEGIN
     IF UPDATE(updated_at) RETURN;
     UPDATE orders SET updated_at = SYSDATETIME() FROM orders o INNER JOIN inserted i ON o.id = i.id;
END;
GO

-- Trigger cho b?ng 'reviews'
CREATE TRIGGER TRG_reviews_updated_at
ON reviews
AFTER UPDATE
AS
BEGIN
     IF UPDATE(updated_at) RETURN;
     UPDATE reviews SET updated_at = SYSDATETIME() FROM reviews r INNER JOIN inserted i ON r.id = i.id;
END;
GO

-- Trigger cho b?ng 'promotions'
CREATE TRIGGER TRG_promotions_updated_at
ON promotions
AFTER UPDATE
AS
BEGIN
     IF UPDATE(updated_at) RETURN;
     UPDATE promotions SET updated_at = SYSDATETIME() FROM promotions p INNER JOIN inserted i ON p.id = i.id;
END;
GO


-- ========== INDEXES FOR PERFORMANCE ==========
-- (Cú pháp t?o index t??ng t? MySQL, ch? c?n ??m b?o tên b?ng/c?t ?úng)

-- users
CREATE INDEX idx_user_phone ON users(phone_number);
CREATE INDEX idx_user_role ON users(role_id);
CREATE INDEX idx_user_default_address ON users(address_default_id) WHERE address_default_id IS NOT NULL; -- Index có ?i?u ki?n

-- addresses
CREATE INDEX idx_address_user ON addresses(user_id);

-- social_accounts
CREATE INDEX idx_social_user ON social_accounts(user_id);
-- UQ_social_provider ?ã t?o index r?i

-- categories
CREATE INDEX idx_category_parent ON categories(parent_id) WHERE parent_id IS NOT NULL;
CREATE INDEX idx_category_slug ON categories(slug) WHERE slug IS NOT NULL;

-- brands
CREATE INDEX idx_brand_slug ON brands(slug) WHERE slug IS NOT NULL;

-- products
CREATE INDEX idx_product_category ON products(category_id);
CREATE INDEX idx_product_brand ON products(brand_id) WHERE brand_id IS NOT NULL;
CREATE INDEX idx_product_slug ON products(slug) WHERE slug IS NOT NULL;
-- Cân nh?c FULLTEXT index cho tìm ki?m tên/mô t?
-- CREATE FULLTEXT INDEX idx_ft_product_name_desc ON products(name, description);

-- product_variants
CREATE INDEX idx_variant_product ON product_variants(product_id);
CREATE INDEX idx_variant_sku ON product_variants(sku) WHERE sku IS NOT NULL;
CREATE INDEX idx_variant_color ON product_variants(color_id);
CREATE INDEX idx_variant_size ON product_variants(size_id);

-- product_images
CREATE INDEX idx_image_variant ON product_images(product_variant_id);

-- orders
CREATE INDEX idx_order_user ON orders(user_id) WHERE user_id IS NOT NULL;
CREATE INDEX idx_order_status ON orders(status);
CREATE INDEX idx_order_date ON orders(order_date);
CREATE INDEX idx_order_shipping_address ON orders(shipping_address_id) WHERE shipping_address_id IS NOT NULL;

-- order_details
CREATE INDEX idx_orderdetail_order ON order_details(order_id);
CREATE INDEX idx_orderdetail_variant ON order_details(product_variant_id);

-- reviews
CREATE INDEX idx_review_user ON reviews(user_id);
CREATE INDEX idx_review_product ON reviews(product_id);
CREATE INDEX idx_review_approved ON reviews(is_approved);

-- wishlists
-- user_id ?ã là UNIQUE nên có index

-- wishlist_items
CREATE INDEX idx_wishlistitem_wishlist ON wishlist_items(wishlist_id);
-- UQ_wishlist_item ?ã t?o index r?i

-- promotions
CREATE INDEX idx_promo_code ON promotions(code) WHERE code IS NOT NULL;
CREATE INDEX idx_promo_dates ON promotions(start_date, end_date);
CREATE INDEX idx_promo_active ON promotions(is_active);

-- user_product_views
CREATE INDEX idx_view_user ON user_product_views(user_id) WHERE user_id IS NOT NULL;
CREATE INDEX idx_view_variant ON user_product_views(product_variant_id);
CREATE INDEX idx_view_session ON user_product_views(session_id) WHERE session_id IS NOT NULL;
CREATE INDEX idx_view_time ON user_product_views(viewed_at);

-- user_searches
CREATE INDEX idx_search_user ON user_searches(user_id) WHERE user_id IS NOT NULL;
CREATE INDEX idx_search_session ON user_searches(session_id) WHERE session_id IS NOT NULL;
CREATE INDEX idx_search_time ON user_searches(searched_at);
GO

PRINT 'Database schema created successfully for SQL Server!';
GO
-- Ho?c s?a b?ng ALTER TABLE (n?u b?n thêm FK sau):
ALTER TABLE addresses DROP CONSTRAINT IF EXISTS FK_addresses_users; -- Xóa constraint c? n?u có
GO
ALTER TABLE addresses ADD CONSTRAINT FK_addresses_users
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE NO ACTION; -- S?a ? ?ây
GO