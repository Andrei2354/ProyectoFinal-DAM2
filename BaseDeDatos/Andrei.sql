DROP TABLE IF EXISTS detalles_pedido CASCADE;
DROP TABLE IF EXISTS pedidos CASCADE;
DROP TABLE IF EXISTS carrito CASCADE;
DROP TABLE IF EXISTS productos CASCADE;
DROP TABLE IF EXISTS categorias CASCADE;
DROP TABLE IF EXISTS marcas CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;

CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    direccion VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    rol VARCHAR(20) DEFAULT 'usuario' 
);

CREATE TABLE IF NOT EXISTS categorias (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS marcas (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla de Productos con campo imagen_url directamente incluido
CREATE TABLE IF NOT EXISTS productos (
    id_producto SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    id_categoria INT REFERENCES categorias(id),
    id_marca INT REFERENCES marcas(id),
    precio DECIMAL(10, 2) NOT NULL,
    imagen_url VARCHAR(255)  -- Campo añadido directamente en la definición
);

-- La tabla imagenes_producto ya no es necesaria

-- Tabla de Carrito
CREATE TABLE IF NOT EXISTS carrito (
    id_carrito SERIAL PRIMARY KEY,
    id_usuario INT REFERENCES usuarios(id) ON DELETE CASCADE,
    id_producto INT REFERENCES productos(id_producto) ON DELETE CASCADE,
    cantidad INT NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    fecha_agregado TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS pedidos (
    id_pedido SERIAL PRIMARY KEY,
    id_usuario INT REFERENCES usuarios(id) ON DELETE CASCADE,
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(50) DEFAULT 'Pendiente',
    total DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS detalles_pedido (
    id SERIAL PRIMARY KEY,
    id_pedido INT REFERENCES pedidos(id_pedido) ON DELETE CASCADE,
    id_producto INT REFERENCES productos(id_producto) ON DELETE CASCADE,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL
);

INSERT INTO usuarios (nombre, email, direccion, password, telefono, rol) VALUES 
('Admin Usuario', 'admin@gmail.com', 'Madrid,Sevilla','1234', '666777888', 'admin'),
('Prueba Usuario', 'prueba@gmail.com', 'Madrid,Sevilla','1234', '666777888', 'usuario'),
('María López', 'maria@gmail.com','Madrid,Sevilla', '1234', '611222333', 'usuario'),
('Juan García', 'juan@gmail.com','Madrid,Sevilla', '1234', '622333444', 'usuario');

-- Insertar categorías
INSERT INTO categorias (nombre) VALUES 
('Consolas'),
('Auriculares'),
('Sillas'),
('Periféricos'),
('Televisores'),
('Electrodomésticos'),
('Tarjetas Gráficas'),
('Portátiles'),
('Smartphones');

-- Insertar marcas
INSERT INTO marcas (nombre) VALUES 
('Nintendo'),
('Microsoft'),
('Sony'),
('Logitech'),
('HyperX'),
('SteelSeries'),
('PcCom'),
('Tempest'),
('Owlotech'),
('Samsung'),
('Razer'),
('Forgeon'),
('EcoFlow'),
('Braun'),
('Origial'),
('Hisense'),
('Orbegozo'),
('Philips'),
('LG'),
('AMD'),
('ASUS'),
('NVIDIA'),
('Apple'),
('MSI'),
('Lenovo'),
('HP'),
('Realme'),
('Xiaomi');

-- Insertar productos: Consolas
INSERT INTO productos (nombre, descripcion, id_categoria, id_marca, precio, imagen_url) VALUES
('Nintendo Switch 2', 'La nueva generación de consolas Nintendo con mayor potencia y mejor pantalla', (SELECT id FROM categorias WHERE nombre = 'Consolas'), (SELECT id FROM marcas WHERE nombre = 'Nintendo'), 349.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741245/Nintendo_Switch_2_wv2dyf.jpg'),
('Nintendo Switch Lite Amarillo', 'Versión portátil de Nintendo Switch en color amarillo', (SELECT id FROM categorias WHERE nombre = 'Consolas'), (SELECT id FROM marcas WHERE nombre = 'Nintendo'), 199.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741245/Nintendo_Switch_Lite_Amarillo_lnaqul.jpg'),
('Xbox Series X 1TB Blanca', 'Consola de última generación de Microsoft con 1TB de almacenamiento en color blanco', (SELECT id FROM categorias WHERE nombre = 'Consolas'), (SELECT id FROM marcas WHERE nombre = 'Microsoft'), 499.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741245/Microsoft_Xbox_Series_X_1TB_vlanca_imz6y3.jpg'),
('Nintendo Switch OLED Azul Neón/Rojo Neón', 'Versión mejorada de Nintendo Switch con pantalla OLED en colores neón', (SELECT id FROM categorias WHERE nombre = 'Consolas'), (SELECT id FROM marcas WHERE nombre = 'Nintendo'), 349.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741245/Nintendo_Switch_OLED_Azul_Ne%C3%B3nRojo_Ne%C3%B3n_gdlcuh.jpg'),
('Xbox Series X 1TB Negra', 'Consola de última generación de Microsoft con 1TB de almacenamiento en color negro', (SELECT id FROM categorias WHERE nombre = 'Consolas'), (SELECT id FROM marcas WHERE nombre = 'Microsoft'), 499.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741244/Microsoft_Xbox_Series_X_1TB_Negra_wpqmut.jpg'),
('PlayStation 5 Pro', 'Versión mejorada de PS5 con mayor potencia gráfica y rendimiento', (SELECT id FROM categorias WHERE nombre = 'Consolas'), (SELECT id FROM marcas WHERE nombre = 'Sony'), 699.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741244/Sony_PlayStation_5_Pro_b5pzbv.jpg'),
('PlayStation 5', 'Consola de última generación de Sony con tecnología de vanguardia', (SELECT id FROM categorias WHERE nombre = 'Consolas'), (SELECT id FROM marcas WHERE nombre = 'Sony'), 499.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741244/Sony_PlayStation_5_dmktqi.jpg');

-- Insertar productos: Auriculares
INSERT INTO productos (nombre, descripcion, id_categoria, id_marca, precio, imagen_url) VALUES
('Logitech G435 LIGHTSPEED Auriculares Gaming Inalámbricos Blancos', 'Auriculares gaming inalámbricos con tecnología LIGHTSPEED para baja latencia', (SELECT id FROM categorias WHERE nombre = 'Auriculares'), (SELECT id FROM marcas WHERE nombre = 'Logitech'), 79.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741244/Logitech_G435_LIGHTSPEED_Auriculares_Gaming_Inal%C3%A1mbricos_Blancos_ue8qzn.jpg'),
('HyperX Cloud III Auriculares Gaming Multiplataforma', 'Auriculares gaming de alta calidad compatibles con todas las plataformas', (SELECT id FROM categorias WHERE nombre = 'Auriculares'), (SELECT id FROM marcas WHERE nombre = 'HyperX'), 99.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741244/HyperX_Cloud_III_Auriculares_Gaming_Multiplataforma_jeswq1.jpg'),
('Logitech G PRO X Auriculares Gaming 7.1 Negros', 'Auriculares gaming profesionales con sonido envolvente 7.1', (SELECT id FROM categorias WHERE nombre = 'Auriculares'), (SELECT id FROM marcas WHERE nombre = 'Logitech'), 129.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741240/Logitech_G_PRO_X_Auriculares_Gaming_7.1_Negros_f9ygyo.jpg'),
('SteelSeries Arctis Nova Pro Wireless Negros', 'Auriculares gaming premium inalámbricos con cancelación activa de ruido', (SELECT id FROM categorias WHERE nombre = 'Auriculares'), (SELECT id FROM marcas WHERE nombre = 'SteelSeries'), 349.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741240/SteelSeries_Arctis_Nova_Pro_Wireless_Negros_iyz8al.jpg'),
('PcCom Essential Twins Auricular Bluetooth con Cancelación de Ruido Ambiental Blanco', 'Auriculares bluetooth con cancelación de ruido para uso diario', (SELECT id FROM categorias WHERE nombre = 'Auriculares'), (SELECT id FROM marcas WHERE nombre = 'PcCom'), 59.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741240/PcCom_Essential_Twins_Auricular_Bluetooth_con_Cancelaci%C3%B3n_de_Ruido_Ambiental_Blanco_qctgpi.jpg'),
('Tempest GHS400 Inquisitor Auriculares Gaming RGB Wireless PC', 'Auriculares gaming inalámbricos con iluminación RGB personalizable', (SELECT id FROM categorias WHERE nombre = 'Auriculares'), (SELECT id FROM marcas WHERE nombre = 'Tempest'), 69.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741239/Tempest_GHS400_Inquisitor_Auriculares_Gaming_RGB_Wireless_PC_kikkmm.jpg');

-- Insertar productos: Sillas
INSERT INTO productos (nombre, descripcion, id_categoria, id_marca, precio, imagen_url) VALUES
('Tempest Conquer Silla Gaming Gris/Negra Tela Transpirable', 'Silla gaming ergonómica con tela transpirable para mayor comodidad', (SELECT id FROM categorias WHERE nombre = 'Sillas'), (SELECT id FROM marcas WHERE nombre = 'Tempest'), 149.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741239/Tempest_Conquer_Silla_Gaming_GrisNegra_Tela_Transpirable_lrnmhd.jpg'),
('Owlotech Berkeley Silla Escritorio Ergonómica Gris', 'Silla de escritorio ergonómica ideal para largas jornadas de trabajo', (SELECT id FROM categorias WHERE nombre = 'Sillas'), (SELECT id FROM marcas WHERE nombre = 'Owlotech'), 129.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741239/Owlotech_Berkeley_Silla_Escritorio_Ergon%C3%B3mica_Gris_fuwxmx.jpg'),
('Owlotech Stanford Silla de Oficina Blanca', 'Silla de oficina elegante y cómoda en color blanco', (SELECT id FROM categorias WHERE nombre = 'Sillas'), (SELECT id FROM marcas WHERE nombre = 'Owlotech'), 119.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741239/Owlotech_Stanford_Silla_de_Oficina_Blanca_tk40bh.jpg'),
('Owlotech Yale V2 Silla Oficina', 'Silla de oficina versátil con soporte lumbar ajustable', (SELECT id FROM categorias WHERE nombre = 'Sillas'), (SELECT id FROM marcas WHERE nombre = 'Owlotech'), 139.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741236/Owlotech_Yale_V2_Silla_Oficina_btguum.jpg'),
('PcCom Essential Smart Silla Escritorio Negra', 'Silla de escritorio básica pero resistente para uso diario', (SELECT id FROM categorias WHERE nombre = 'Sillas'), (SELECT id FROM marcas WHERE nombre = 'PcCom'), 89.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741236/PcCom_Essential_Smart_Silla_Escritorio_Negra_bhb5vu.jpg');

-- Insertar productos: Periféricos
INSERT INTO productos (nombre, descripcion, id_categoria, id_marca, precio, imagen_url) VALUES
('Owlotech K500TV Teclado Bluetooth con Touchpad', 'Teclado bluetooth compacto con touchpad integrado', (SELECT id FROM categorias WHERE nombre = 'Periféricos'), (SELECT id FROM marcas WHERE nombre = 'Owlotech'), 39.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741237/Owlotech_K500TV_Teclado_Bluetooth_con_Touchpad_ma2wce.jpg'),
('Razer Deathadder Essential Ratón Gaming 6400 DPI Negro', 'Ratón gaming ergonómico con sensor de 6400 DPI', (SELECT id FROM categorias WHERE nombre = 'Periféricos'), (SELECT id FROM marcas WHERE nombre = 'Razer'), 29.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741236/Razer_Deathadder_Essential_Rat%C3%B3n_Gaming_6400_DPI_Negro_xqqcwm.jpg'),
('Logitech G502 Hero Ratón Gaming 25600DPI', 'Ratón gaming de alto rendimiento con sensor HERO de 25600 DPI', (SELECT id FROM categorias WHERE nombre = 'Periféricos'), (SELECT id FROM marcas WHERE nombre = 'Logitech'), 59.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741236/Logitech_G502_Hero_Rat%C3%B3n_Gaming_25600DPI_nrvkaj.jpg'),
('SteelSeries Aerox 3 2022 Snow Ratón Gaming 8500DPI', 'Ratón gaming ultraligero con diseño perforado para mejor ventilación', (SELECT id FROM categorias WHERE nombre = 'Periféricos'), (SELECT id FROM marcas WHERE nombre = 'SteelSeries'), 69.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741236/SteelSeries_Aerox_3_2022_Snow_Rat%C3%B3n_Gaming_8500DPI_mkkxuu.jpg'),
('Forgeon Vendetta Ratón Gaming RGB 16000DPI Negro', 'Ratón gaming con iluminación RGB personalizable y sensor de alta precisión', (SELECT id FROM categorias WHERE nombre = 'Periféricos'), (SELECT id FROM marcas WHERE nombre = 'Forgeon'), 49.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741236/Forgeon_Vendetta_Rat%C3%B3n_Gaming_RGB_16000DPI_Negro_lco33w.jpg'),
('Tempest X20W Vigilant RGB Ratón Gaming Inalámbrico 16.000 DPI Negro', 'Ratón gaming inalámbrico con alto rendimiento y baja latencia', (SELECT id FROM categorias WHERE nombre = 'Periféricos'), (SELECT id FROM marcas WHERE nombre = 'Tempest'), 39.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741235/Tempest_X20W_Vigilant_RGB_Rat%C3%B3n_Gaming_Inal%C3%A1mbrico_16.000_DPI_Negro_ezvqlt.jpg'),
('Tempest K20 Beast Teclado Mecánico Inalámbrico Gaming RGB TKL Mini Blanco', 'Teclado mecánico compacto con iluminación RGB y conectividad inalámbrica', (SELECT id FROM categorias WHERE nombre = 'Periféricos'), (SELECT id FROM marcas WHERE nombre = 'Tempest'), 79.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741232/Tempest_K20_Beast_Teclado_Mec%C3%A1nico_Inal%C3%A1mbrico_Gaming_RGB_TKL_Mini_Blanco_vuiwsh.jpg'),
('Logitech MK235 Combo Teclado y Ratón Inalámbrico', 'Combo de teclado y ratón inalámbricos para uso diario', (SELECT id FROM categorias WHERE nombre = 'Periféricos'), (SELECT id FROM marcas WHERE nombre = 'Logitech'), 29.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741232/Logitech_MK235_Combo_Teclado_y_Rat%C3%B3n_Inal%C3%A1mbrico_eghezx.jpg'),
('Forgeon Clutch Teclado Gaming Wireless RGB 60 Switch Brown', 'Teclado gaming compacto 60% con switches táctiles y RGB personalizable', (SELECT id FROM categorias WHERE nombre = 'Periféricos'), (SELECT id FROM marcas WHERE nombre = 'Forgeon'), 89.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741232/Forgeon_Clutch_Teclado_Gaming_Wireless_RGB_60_Switch_Brown_bjigmv.jpg');

-- Insertar productos: Televisores
INSERT INTO productos (nombre, descripcion, id_categoria, id_marca, precio, imagen_url) VALUES
('TV Samsung TU65DU7105KXXC 65" LED UltraHD 4K HDR', 'Televisor LED de 65 pulgadas con resolución 4K y soporte HDR', (SELECT id FROM categorias WHERE nombre = 'Televisores'), (SELECT id FROM marcas WHERE nombre = 'Samsung'), 699.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741235/TV_Samsung_TU65DU7105KXXC_65_22_LED_UltraHD_4K_HDR_mp1h78.jpg'),
('Philips 40PFS6009 40" Full HD LED TV Dolby Audio Titan OS', 'Televisor LED de 40 pulgadas con resolución Full HD y sistema Titan OS', (SELECT id FROM categorias WHERE nombre = 'Televisores'), (SELECT id FROM marcas WHERE nombre = 'Philips'), 329.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741227/Philips_40PFS6009_40_102cm_Full_HD_LED_TV_Dolby_Audio_Titan_OS_ozkhdb.jpg'),
('TV LG NanoCell 43" 43NANO UltraHD 4K WebOS AI ThinQ', 'Televisor NanoCell de 43 pulgadas con resolución 4K e inteligencia artificial', (SELECT id FROM categorias WHERE nombre = 'Televisores'), (SELECT id FROM marcas WHERE nombre = 'LG'), 499.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741227/TV_LG_NanoCell_43_22_43NANO_UltraHD_4K_WebOS_AI_ThinQ_PcCom_Essential_HDMI_2.0_30AWG_CCS_dtbmfb.jpg'),
('TV LG 55QNED866RE 55" QNED MiniLED UltraHD 4K HDR10', 'Televisor QNED MiniLED de 55 pulgadas con resolución 4K y soporte HDR10', (SELECT id FROM categorias WHERE nombre = 'Televisores'), (SELECT id FROM marcas WHERE nombre = 'LG'), 899.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741227/TV_LG_55QNED866RE_55_22_QNED_MiniLED_UltraHD_4K_HDR10_id5b26.jpg'),
('TV Samsung TQ43Q60DAUXXC 43" QLED UltraHD 4K HDR10 Tizen', 'Televisor QLED de 43 pulgadas con resolución 4K y sistema operativo Tizen', (SELECT id FROM categorias WHERE nombre = 'Televisores'), (SELECT id FROM marcas WHERE nombre = 'Samsung'), 599.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741227/TV_Samsung_TQ43Q60DAUXXC_43_22_QLED_UltraHD_4K_HDR10_Tizen_wbudea.jpg'),
('Philips 75PML8709 75" QD Mini LED UltraHD 4K Google TV', 'Televisor QD Mini LED de 75 pulgadas con resolución 4K y sistema Google TV', (SELECT id FROM categorias WHERE nombre = 'Televisores'), (SELECT id FROM marcas WHERE nombre = 'Philips'), 1499.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741226/Philips_75PML8709_75_22_QD_Mini_LED_UltraHD_4K_Google_TV_m1dsgs.jpg');

-- Insertar productos: Electrodomésticos
INSERT INTO productos (nombre, descripcion, id_categoria, id_marca, precio, imagen_url) VALUES
('EcoFlow Panel Solar Portátil de 60W', 'Panel solar portátil para cargar dispositivos o estaciones de energía', (SELECT id FROM categorias WHERE nombre = 'Electrodomésticos'), (SELECT id FROM marcas WHERE nombre = 'EcoFlow'), 199.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741231/EcoFlow_Panel_Solar_Port%C3%A1til_de_60W_uvk6jz.jpg'),
('Braun Series 3 3020s Afeitadora Eléctrica', 'Afeitadora eléctrica con tecnología de corte preciso', (SELECT id FROM categorias WHERE nombre = 'Electrodomésticos'), (SELECT id FROM marcas WHERE nombre = 'Braun'), 69.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741231/Braun_Series_3_3020s_Afeitadora_El%C3%A9ctrica_z9ingh.jpg'),
('Origial FRYFIT 6.5L Freidora de Aire con Ventana 6.5L 1700W Acero Inoxidable', 'Freidora de aire de gran capacidad con ventana para visualizar la cocción', (SELECT id FROM categorias WHERE nombre = 'Electrodomésticos'), (SELECT id FROM marcas WHERE nombre = 'Origial'), 89.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741231/Origial_FRYFIT_6.5L_Freidora_de_Aire_con_Ventana_6.5L_1700W_Acero_Inoxidable_k4wpre.jpg'),
('Hisense Luso Connect KC35YR03 Aire Acondicionado Split 1x1 con Bomba de Calor 2924 Frigorías', 'Aire acondicionado split con función de calefacción y refrigeración', (SELECT id FROM categorias WHERE nombre = 'Electrodomésticos'), (SELECT id FROM marcas WHERE nombre = 'Hisense'), 399.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741231/Hisense_Luso_Connect_KC35YR03_Aire_Acondicionado_Split_1x1_con_Bomba_de_Calor_2924_Frigor%C3%ADas_t6wiwa.jpg'),
('Orbegozo EX 6000 Cafetera Espresso 20 Bares Inox', 'Cafetera espresso con bomba de 20 bares para café de calidad profesional', (SELECT id FROM categorias WHERE nombre = 'Electrodomésticos'), (SELECT id FROM marcas WHERE nombre = 'Orbegozo'), 159.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741227/Orbegozo_EX_6000_Cafetera_Espresso_20_Bares_Inox_opswb0.jpg'),
('Origial COOL FREEZE 80 White Frigorífico Bajo Encimera 80L Clase E Blanco', 'Frigorífico compacto ideal para espacios reducidos o como complemento', (SELECT id FROM categorias WHERE nombre = 'Electrodomésticos'), (SELECT id FROM marcas WHERE nombre = 'Origial'), 179.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741227/Origial_COOL_FREEZE_80_White_Frigor%C3%ADfico_Bajo_Encimera_80L_Clase_E_Blanco_grg49f.jpg'),
('Origial Prowash Inverter ORIWM9AW-24 Lavadora Carga Frontal 9Kg A Blanca', 'Lavadora con tecnología inverter para mayor eficiencia energética', (SELECT id FROM categorias WHERE nombre = 'Electrodomésticos'), (SELECT id FROM marcas WHERE nombre = 'Origial'), 349.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741227/Origial_Prowash_Inverter_ORIWM9AW-24_Lavadora_Carga_Frontal_9Kg_A_Blanca_xi3o5a.jpg');

-- Insertar productos: Tarjetas Gráficas
INSERT INTO productos (nombre, descripcion, id_categoria, id_marca, precio, imagen_url) VALUES
('AMD Radeon RX 6600', 'Tarjeta gráfica de gama media con 8GB GDDR6 para gaming en 1080p', (SELECT id FROM categorias WHERE nombre = 'Tarjetas Gráficas'), (SELECT id FROM marcas WHERE nombre = 'AMD'), 299.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741222/AMD_Radeon_RX_6600_x2dloc.jpg'),
('AMD Radeon RX 7800 XT', 'Tarjeta gráfica de gama alta con 16GB GDDR6 para gaming en 1440p y 4K', (SELECT id FROM categorias WHERE nombre = 'Tarjetas Gráficas'), (SELECT id FROM marcas WHERE nombre = 'AMD'), 549.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741222/AMD_Radeon_RX_7800_XT_uwuuvg.jpg'),
('AMD Radeon RX 7900XT', 'Tarjeta gráfica tope de gama con 24GB GDDR6 para gaming en 4K', (SELECT id FROM categorias WHERE nombre = 'Tarjetas Gráficas'), (SELECT id FROM marcas WHERE nombre = 'AMD'), 899.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741222/AMD_Radeon_RX_7900XT_mrudkm.jpg'),
('GeForce RTX 3060', 'Tarjeta gráfica de gama media con 12GB GDDR6 y tecnología Ray Tracing', (SELECT id FROM categorias WHERE nombre = 'Tarjetas Gráficas'), (SELECT id FROM marcas WHERE nombre = 'NVIDIA'), 329.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741222/Tarjeta_Gr%C3%A1ficaGeForce_RTX_3060_sdrcs3.jpg'),
('ASUS PRIME GeForce RTX 5070', 'Tarjeta gráfica de nueva generación con rendimiento excepcional', (SELECT id FROM categorias WHERE nombre = 'Tarjetas Gráficas'), (SELECT id FROM marcas WHERE nombre = 'ASUS'), 799.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741222/Tarjeta_Gr%C3%A1fica_ASUS_PRIME_GeForce_RTX_5070_pgbvve.jpg'),
('GeForce RTX 4060Ti', 'Tarjeta gráfica de gama media-alta con tecnología DLSS 3.0', (SELECT id FROM categorias WHERE nombre = 'Tarjetas Gráficas'), (SELECT id FROM marcas WHERE nombre = 'NVIDIA'), 449.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741222/TarjetaGr%C3%A1ficaGeForceRTX_4060Ti_vrbdwy.jpg');

-- Insertar productos: Portátiles
INSERT INTO productos (nombre, descripcion, id_categoria, id_marca, precio, imagen_url) VALUES
('Apple Macbook Air Apple M4', 'Portátil ultraligero con procesador Apple Silicon de última generación', (SELECT id FROM categorias WHERE nombre = 'Portátiles'), (SELECT id FROM marcas WHERE nombre = 'Apple'), 1299.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741222/Port%C3%A1til_Apple_Macbook_Air_Apple_M4_stdgv3.jpg'),
('MSI Cyborg 15 A13VF-879XES', 'Portátil gaming con diseño futurista y potentes componentes', (SELECT id FROM categorias WHERE nombre = 'Portátiles'), (SELECT id FROM marcas WHERE nombre = 'MSI'), 1099.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741221/Port%C3%A1til_MSI_Cyborg_15_A13VF-879XES_utazxk.jpg'),
('Lenovo IdeaPad Slim 3', 'Portátil delgado y ligero para uso diario y productividad', (SELECT id FROM categorias WHERE nombre = 'Portátiles'), (SELECT id FROM marcas WHERE nombre = 'Lenovo'), 649.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741221/Port%C3%A1tilLenovoIdeaPadSlim3_irxpvi.jpg'),
('HP 15-fc0069ns', 'Portátil versátil con buena relación calidad-precio', (SELECT id FROM categorias WHERE nombre = 'Portátiles'), (SELECT id FROM marcas WHERE nombre = 'HP'), 599.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741221/Port%C3%A1tilHP15-fc0069ns_hl3lih.jpg'),
('ASUS TUF Gaming A15 FA507NVR-LP070 AMD Ryzen', 'Portátil gaming resistente con procesador AMD Ryzen de última generación', (SELECT id FROM categorias WHERE nombre = 'Portátiles'), (SELECT id FROM marcas WHERE nombre = 'ASUS'), 1199.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741216/Port%C3%A1tilASUSTUFGamingA15FA507NVR-LP070AMDRyzen_fgnhvm.jpg');

-- Continuar insertando productos: Smartphones (los que faltan)
INSERT INTO productos (nombre, descripcion, id_categoria, id_marca, precio, imagen_url) VALUES
('Realme C63 Dorado', 'Smartphone de gama media con acabado en color dorado', (SELECT id FROM categorias WHERE nombre = 'Smartphones'), (SELECT id FROM marcas WHERE nombre = 'Realme'), 199.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741216/RealmeC63Dorado_ih0kzz.jpg'),
('Realme C63 Azul', 'Smartphone de gama media con acabado en color azul', (SELECT id FROM categorias WHERE nombre = 'Smartphones'), (SELECT id FROM marcas WHERE nombre = 'Realme'), 199.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741216/RealmeC63Azul_bgnktl.jpg'),
('Realme C63 Verde', 'Smartphone de gama media con acabado en color verde', (SELECT id FROM categorias WHERE nombre = 'Smartphones'), (SELECT id FROM marcas WHERE nombre = 'Realme'), 199.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741216/RealmeC63Verde_k7ldlf.jpg'),
('Samsung Galaxy S25 Negro', 'Smartphone de alta gama con acabado en color negro', (SELECT id FROM categorias WHERE nombre = 'Smartphones'), (SELECT id FROM marcas WHERE nombre = 'Samsung'), 999.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741216/SamsungGalaxyS25Negro_zuqpym.jpg'),
('Samsung Galaxy S25 Celeste', 'Smartphone de alta gama con acabado en color celeste', (SELECT id FROM categorias WHERE nombre = 'Smartphones'), (SELECT id FROM marcas WHERE nombre = 'Samsung'), 999.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741216/SamsungGalaxyS25Celeste_g96nny.jpg'),
('Samsung Galaxy S25 Azul', 'Smartphone de alta gama con acabado en color azul', (SELECT id FROM categorias WHERE nombre = 'Smartphones'), (SELECT id FROM marcas WHERE nombre = 'Samsung'), 999.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741215/SamsungGalaxyS25Azul_jh7i5c.jpg'),
('Samsung Galaxy S25 Blanco', 'Smartphone de alta gama con acabado en color blanco', (SELECT id FROM categorias WHERE nombre = 'Smartphones'), (SELECT id FROM marcas WHERE nombre = 'Samsung'), 999.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741215/SamsungGalaxyS25Blanco_xqc1ug.jpg'),
('Apple iPhone 16 Pro Max Blanco', 'Smartphone premium con acabado en color blanco', (SELECT id FROM categorias WHERE nombre = 'Smartphones'), (SELECT id FROM marcas WHERE nombre = 'Apple'), 1299.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741216/apple-iphone-16-pro-maxBlanco_geugil.jpg'),
('Apple iPhone 16 Pro Max Negro', 'Smartphone premium con acabado en color negro', (SELECT id FROM categorias WHERE nombre = 'Smartphones'), (SELECT id FROM marcas WHERE nombre = 'Apple'), 1299.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741216/apple-iphone-16-pro-maxNegro_oyg1pc.jpg'),
('Apple iPhone 16 Pro Max Metal', 'Smartphone premium con acabado en color metal', (SELECT id FROM categorias WHERE nombre = 'Smartphones'), (SELECT id FROM marcas WHERE nombre = 'Apple'), 1299.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741215/apple-iphone-16-pro-maxMetal_ungcuh.jpg'),
('Xiaomi Redmi Note 14 Azul', 'Smartphone de gama media con acabado en color azul', (SELECT id FROM categorias WHERE nombre = 'Smartphones'), (SELECT id FROM marcas WHERE nombre = 'Xiaomi'), 299.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741215/tf-XiaomiRedmiNoteAzul14_zalmuk.jpg'),
('Xiaomi Redmi Note 14 Negro', 'Smartphone de gama media con acabado en color negro', (SELECT id FROM categorias WHERE nombre = 'Smartphones'), (SELECT id FROM marcas WHERE nombre = 'Xiaomi'), 299.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741215/tf-XiaomiRedmiNoteNegro14_izqy0i.jpg'),
('Xiaomi Redmi Note 14', 'Smartphone de gama media', (SELECT id FROM categorias WHERE nombre = 'Smartphones'), (SELECT id FROM marcas WHERE nombre = 'Xiaomi'), 299.99, 'https://res.cloudinary.com/doi9wo8r3/image/upload/v1747741215/tf-XiaomiRedmiNote14_tbgzjc.jpg');