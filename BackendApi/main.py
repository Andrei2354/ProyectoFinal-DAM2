import psycopg2
from flask import Flask, jsonify, request

app = Flask(__name__)


def ejecutar_sql(sql_text, params=None):
    host = "localhost"
    port = "5432"
    dbname = "Andrei"
    user = "postgres"
    password = "Andrei2354"

    # postgres Andrei2354

    connection = psycopg2.connect(
        host=host,
        port=port,
        dbname=dbname,
        user=user,
        password=password,
    )
    cursor = connection.cursor()

    if params:
        cursor.execute(sql_text, params)
    else:
        cursor.execute(sql_text)

    if sql_text.strip().upper().startswith(('INSERT', 'UPDATE', 'DELETE')):
        connection.commit()
        affected_rows = cursor.rowcount
        cursor.close()
        connection.close()
        return affected_rows

    if cursor.description: 
        columnas = [desc[0] for desc in cursor.description]
        resultados = cursor.fetchall()
        datos = [dict(zip(columnas, fila)) for fila in resultados]
    else:
        datos = []

    cursor.close()
    connection.close()

    return datos


@app.route('/login_user', methods=['POST'])
def login_user():
    try:
        body_request = request.json
        print(f"Datos recibidos: {body_request}")  # Debug

        user = body_request.get("user")
        passwd = body_request.get("passwd")

        if not user or not passwd:
            return jsonify({"msg": "Usuario y contraseña son requeridos"}), 400

        # Verificar primero si el usuario existe
        query_check = "SELECT COUNT(*) as count FROM usuarios WHERE email = %s"
        check_result = ejecutar_sql(query_check, (user,))
        print(f"Usuario existe: {check_result}")  # Debug

        if not check_result or check_result[0]['count'] == 0:
            return jsonify({"msg": "Usuario no encontrado"}), 404

        # Intentar login
        query = "SELECT id, nombre, email, direccion, telefono, rol FROM usuarios WHERE email = %s AND password = %s"
        print(f"Ejecutando query: {query} con params: {user}, {passwd}")  # Debug

        resultado = ejecutar_sql(query, (user, passwd))
        print(f"Resultado del login: {resultado}")  # Debug

        if not resultado:
            # Verificar si el problema es la contraseña
            query_pass = "SELECT password FROM usuarios WHERE email = %s"
            pass_result = ejecutar_sql(query_pass, (user,))
            print(f"Contraseña en DB: {pass_result}")  # Debug
            return jsonify({"msg": "Contraseña incorrecta"}), 401

        usuario = resultado[0]

        response_data = {
            "id": usuario["id"],
            "nombre": usuario["nombre"],
            "email": usuario["email"],
            "direccion": usuario.get("direccion", ""),
            "telefono": usuario.get("telefono", ""),
            "rol": usuario.get("rol", "usuario")
        }

        print(f"Enviando respuesta: {response_data}")  # Debug
        return jsonify(response_data)

    except Exception as e:
        print(f"Error en login: {str(e)}")  # Debug
        return jsonify({"msg": f"Error en el servidor: {str(e)}"}), 500


@app.route('/register_user', methods=['POST'])
def register_user():
    try:
        body_request = request.json

        nombre = body_request.get("nombre")
        email = body_request.get("email")
        direccion = body_request.get("direccion")
        telefono = body_request.get("telefono", "")  # Optional
        password = body_request.get("password")

        if not all([nombre, email, direccion, password]):
            return jsonify({"msg": "Todos los campos obligatorios deben ser completados"}), 400

        check_query = "SELECT id FROM usuarios WHERE email = %s"
        resultado = ejecutar_sql(check_query, (email,))

        if resultado:
            return jsonify({"msg": "El email ya está registrado"}), 409

        insert_query = """
            INSERT INTO usuarios (nombre, email, direccion, password, telefono) 
            VALUES (%s, %s, %s, %s, %s)
        """
        ejecutar_sql(insert_query, (nombre, email, direccion, password, telefono))

        return jsonify({"msg": "Usuario registrado correctamente"}), 201

    except Exception as e:
        print(f"Error en register_user: {str(e)}")
        return jsonify({"error": "Error en el sistema"}), 500


@app.route('/edit_profile', methods=['POST'])
def edit_profile():
    try:
        body_request = request.json

        user_id = int(body_request.get("id"))  # Asegurarse de que es un int
        nombre = body_request.get("nombre")
        email = body_request.get("email")
        direccion = body_request.get("direccion")
        telefono = body_request.get("telefono", "")

        if not all([user_id, nombre, email, direccion]):
            return jsonify({"msg": "Todos los campos obligatorios deben ser completados"}), 400

        # Check if email already exists for another user
        check_query = "SELECT id FROM usuarios WHERE email = %s AND id != %s"
        resultado = ejecutar_sql(check_query, (email, user_id))

        if resultado:
            return jsonify({"msg": "El email ya está registrado por otro usuario"}), 409

        update_query = """
            UPDATE usuarios 
            SET nombre = %s, email = %s, direccion = %s, telefono = %s
            WHERE id = %s
        """

        # Asegurarnos de que la transacción se hace correctamente
        connection = psycopg2.connect(
            host="localhost",
            port="5432",
            dbname="Andrei",
            user="postgres",
            password="Andrei2354",
        )
        cursor = connection.cursor()

        cursor.execute(update_query, (nombre, email, direccion, telefono, user_id))
        connection.commit()  # Asegurarse de que los cambios se guardan

        cursor.close()
        connection.close()

        # Para verificar, consultemos los datos actualizados
        check_update_query = "SELECT * FROM usuarios WHERE id = %s"
        updated_user = ejecutar_sql(check_update_query, (user_id,))

        print(f"Usuario actualizado en BD: {updated_user}")

        return jsonify({
            "msg": "Perfil actualizado correctamente",
            "usuario": {
                "id": user_id,
                "nombre": nombre,
                "email": email,
                "direccion": direccion,
                "telefono": telefono
            }
        }), 200

    except Exception as e:
        print(f"Error en edit_profile: {str(e)}")
        return jsonify({"error": "Error en el sistema"}), 500


# Endpoint para obtener todos los productos
@app.route('/productos', methods=['GET'])
def get_productos():
    try:
        categoria_id = request.args.get('categoria')
        query = """
            SELECT p.*, c.nombre as categoria_nombre, m.nombre as marca_nombre
            FROM productos p
            LEFT JOIN categorias c ON p.id_categoria = c.id
            LEFT JOIN marcas m ON p.id_marca = m.id
        """
        params = None

        if categoria_id:
            query += " WHERE p.id_categoria = %s"
            params = (categoria_id,)

        productos = ejecutar_sql(query, params)

        if productos is None:
            return jsonify({"error": "Error al obtener productos"}), 500

        return jsonify(productos)
    except Exception as e:
        print(f"Error en get_productos: {str(e)}")
        return jsonify({"error": f"Error en el sistema: {str(e)}"}), 500


# Endpoint para obtener todas las categorías
@app.route('/categorias', methods=['GET'])
def get_categorias():
    try:
        query = "SELECT * FROM categorias ORDER BY nombre"
        categorias = ejecutar_sql(query)

        if categorias is None:
            return jsonify({"error": "Error al obtener categorías"}), 500

        return jsonify(categorias)
    except Exception as e:
        print(f"Error en get_categorias: {str(e)}")
        return jsonify({"error": f"Error en el sistema: {str(e)}"}), 500


# Endpoint para obtener todas las marcas
@app.route('/marcas', methods=['GET'])
def get_marcas():
    try:
        query = "SELECT * FROM marcas ORDER BY nombre"
        marcas = ejecutar_sql(query)

        if marcas is None:
            return jsonify({"error": "Error al obtener marcas"}), 500

        return jsonify(marcas)
    except Exception as e:
        print(f"Error en get_marcas: {str(e)}")
        return jsonify({"error": f"Error en el sistema: {str(e)}"}), 500


@app.route('/carrito/<int:user_id>', methods=['GET'])
def get_carrito(user_id):
    try:
        query = """
            SELECT 
                c.id_carrito,
                c.id_usuario,
                c.id_producto,
                c.cantidad,
                c.precio_unitario,
                c.fecha_agregado,
                p.nombre as nombre_producto,
                p.imagen_url,
                p.descripcion
            FROM carrito c
            JOIN productos p ON c.id_producto = p.id_producto
            WHERE c.id_usuario = %s
            ORDER BY c.fecha_agregado DESC
        """
        carrito = ejecutar_sql(query, (user_id,))

        if carrito is None:
            return jsonify({
                "success": False,
                "message": "Error al obtener el carrito",
                "carrito": []
            }), 500

        return jsonify({
            "success": True,
            "message": "Carrito obtenido correctamente",
            "carrito": carrito
        })

    except Exception as e:
        print(f"Error en get_carrito: {str(e)}")
        return jsonify({
            "success": False,
            "message": f"Error en el sistema: {str(e)}",
            "carrito": []
        }), 500


@app.route('/carrito/agregar', methods=['POST'])
def agregar_al_carrito():
    try:
        data = request.get_json()
        id_usuario = data.get('id_usuario')
        id_producto = data.get('id_producto')
        cantidad = data.get('cantidad', 1)
        precio_unitario = data.get('precio_unitario')

        if not all([id_usuario, id_producto, precio_unitario]):
            return jsonify({"error": "Faltan datos requeridos"}), 400

        # Verificar si el producto ya está en el carrito
        query_check = """
            SELECT id_carrito, cantidad FROM carrito 
            WHERE id_usuario = %s AND id_producto = %s
        """
        existing_item = ejecutar_sql(query_check, (id_usuario, id_producto))

        if existing_item:
            # Si ya existe, actualizar cantidad
            nueva_cantidad = existing_item[0]['cantidad'] + cantidad
            query_update = """
                UPDATE carrito 
                SET cantidad = %s, fecha_agregado = CURRENT_TIMESTAMP
                WHERE id_carrito = %s
            """
            resultado = ejecutar_sql(query_update, (nueva_cantidad, existing_item[0]['id_carrito']))
        else:
            # Si no existe, crear nuevo item
            query_insert = """
                INSERT INTO carrito (id_usuario, id_producto, cantidad, precio_unitario)
                VALUES (%s, %s, %s, %s)
            """
            resultado = ejecutar_sql(query_insert, (id_usuario, id_producto, cantidad, precio_unitario))

        if resultado is not None:
            return jsonify({"success": True, "message": "Producto agregado al carrito"})
        else:
            return jsonify({"error": "Error al agregar producto al carrito"}), 500

    except Exception as e:
        print(f"Error en agregar_al_carrito: {str(e)}")
        return jsonify({"error": f"Error en el sistema: {str(e)}"}), 500


@app.route('/carrito/actualizar', methods=['PUT'])
def actualizar_cantidad():
    try:
        data = request.get_json()
        id_carrito = data.get('id_carrito')
        cantidad = data.get('cantidad')

        if not all([id_carrito, cantidad]):
            return jsonify({"error": "Faltan datos requeridos"}), 400

        if cantidad <= 0:
            return jsonify({"error": "La cantidad debe ser mayor a 0"}), 400

        query = """
            UPDATE carrito 
            SET cantidad = %s, fecha_agregado = CURRENT_TIMESTAMP
            WHERE id_carrito = %s
        """
        resultado = ejecutar_sql(query, (cantidad, id_carrito))

        if resultado is not None:
            return jsonify({"success": True, "message": "Cantidad actualizada"})
        else:
            return jsonify({"error": "Error al actualizar cantidad"}), 500

    except Exception as e:
        print(f"Error en actualizar_cantidad: {str(e)}")
        return jsonify({"error": f"Error en el sistema: {str(e)}"}), 500


@app.route('/carrito/eliminar', methods=['DELETE'])
def eliminar_del_carrito():
    try:
        data = request.get_json()
        id_carrito = data.get('id_carrito')

        if not id_carrito:
            return jsonify({"error": "ID del carrito requerido"}), 400

        query = "DELETE FROM carrito WHERE id_carrito = %s"
        resultado = ejecutar_sql(query, (id_carrito,))

        if resultado is not None:
            return jsonify({"success": True, "message": "Producto eliminado del carrito"})
        else:
            return jsonify({"error": "Error al eliminar producto"}), 500

    except Exception as e:
        print(f"Error en eliminar_del_carrito: {str(e)}")
        return jsonify({"error": f"Error en el sistema: {str(e)}"}), 500


@app.route('/carrito/vaciar/<int:id_usuario>', methods=['DELETE'])
def vaciar_carrito(id_usuario):
    try:
        query = "DELETE FROM carrito WHERE id_usuario = %s"
        resultado = ejecutar_sql(query, (id_usuario,))

        if resultado is not None:
            return jsonify({"success": True, "message": "Carrito vaciado"})
        else:
            return jsonify({"error": "Error al vaciar carrito"}), 500

    except Exception as e:
        print(f"Error en vaciar_carrito: {str(e)}")
        return jsonify({"error": f"Error en el sistema: {str(e)}"}), 500


# Añadir estos endpoints a tu archivo Flask existente

# ===== ENDPOINTS DE ADMINISTRACIÓN DE USUARIOS =====

@app.route('/admin/usuarios', methods=['GET'])
def get_todos_usuarios():
    try:
        query = """
            SELECT id, nombre, email, direccion, telefono, rol 
            FROM usuarios 
            ORDER BY nombre
        """
        usuarios = ejecutar_sql(query)

        if usuarios is None:
            return jsonify({"error": "Error al obtener usuarios"}), 500

        return jsonify(usuarios)
    except Exception as e:
        print(f"Error en get_todos_usuarios: {str(e)}")
        return jsonify({"error": f"Error en el sistema: {str(e)}"}), 500


@app.route('/admin/editar_usuario', methods=['PUT'])
def editar_usuario():
    try:
        data = request.get_json()

        user_id = data.get('id')
        nombre = data.get('nombre')
        email = data.get('email')
        direccion = data.get('direccion')
        telefono = data.get('telefono')
        rol = data.get('rol')

        if not all([user_id, nombre, email, direccion, rol]):
            return jsonify({"msg": "Todos los campos obligatorios deben ser completados"}), 400

        # Verificar que el email no esté en uso por otro usuario
        check_query = "SELECT id FROM usuarios WHERE email = %s AND id != %s"
        resultado = ejecutar_sql(check_query, (email, user_id))

        if resultado:
            return jsonify({"msg": "El email ya está registrado por otro usuario"}), 409

        # Actualizar usuario
        update_query = """
            UPDATE usuarios 
            SET nombre = %s, email = %s, direccion = %s, telefono = %s, rol = %s
            WHERE id = %s
        """

        resultado = ejecutar_sql(update_query, (nombre, email, direccion, telefono, rol, user_id))

        if resultado is not None:
            return jsonify({"msg": "Usuario actualizado correctamente"}), 200
        else:
            return jsonify({"msg": "Error al actualizar usuario"}), 500

    except Exception as e:
        print(f"Error en editar_usuario: {str(e)}")
        return jsonify({"error": f"Error en el sistema: {str(e)}"}), 500


@app.route('/admin/eliminar_usuario/<int:user_id>', methods=['DELETE'])
def eliminar_usuario(user_id):
    try:
        # Verificar que el usuario existe
        check_query = "SELECT id FROM usuarios WHERE id = %s"
        usuario_existe = ejecutar_sql(check_query, (user_id,))

        if not usuario_existe:
            return jsonify({"msg": "Usuario no encontrado"}), 404

        # Eliminar primero los registros relacionados (carrito, pedidos, etc.)
        # Eliminar items del carrito
        ejecutar_sql("DELETE FROM carrito WHERE id_usuario = %s", (user_id,))

        # Eliminar detalles de pedidos del usuario
        ejecutar_sql("""
            DELETE FROM detalles_pedido 
            WHERE id_pedido IN (SELECT id_pedido FROM pedidos WHERE id_usuario = %s)
        """, (user_id,))

        # Eliminar pedidos del usuario
        ejecutar_sql("DELETE FROM pedidos WHERE id_usuario = %s", (user_id,))

        # Finalmente, eliminar el usuario
        delete_query = "DELETE FROM usuarios WHERE id = %s"
        resultado = ejecutar_sql(delete_query, (user_id,))

        if resultado is not None:
            return jsonify({"msg": "Usuario eliminado correctamente"}), 200
        else:
            return jsonify({"msg": "Error al eliminar usuario"}), 500

    except Exception as e:
        print(f"Error en eliminar_usuario: {str(e)}")
        return jsonify({"error": f"Error en el sistema: {str(e)}"}), 500


# ===== ENDPOINTS DE ADMINISTRACIÓN DE PRODUCTOS =====

@app.route('/admin/editar_producto', methods=['PUT'])
def editar_producto():
    try:
        data = request.get_json()

        id_producto = data.get('id_producto')
        nombre = data.get('nombre')
        descripcion = data.get('descripcion')
        id_categoria = data.get('id_categoria')
        id_marca = data.get('id_marca')
        precio = data.get('precio')
        imagen_url = data.get('imagen_url')

        if not all([id_producto, nombre, descripcion, id_categoria, id_marca, precio]):
            return jsonify({"msg": "Todos los campos obligatorios deben ser completados"}), 400

        # Verificar que el producto existe
        check_query = "SELECT id_producto FROM productos WHERE id_producto = %s"
        producto_existe = ejecutar_sql(check_query, (id_producto,))

        if not producto_existe:
            return jsonify({"msg": "Producto no encontrado"}), 404

        # Verificar que la categoría y marca existen
        cat_query = "SELECT id FROM categorias WHERE id = %s"
        categoria_existe = ejecutar_sql(cat_query, (id_categoria,))

        marca_query = "SELECT id FROM marcas WHERE id = %s"
        marca_existe = ejecutar_sql(marca_query, (id_marca,))

        if not categoria_existe:
            return jsonify({"msg": "Categoría no válida"}), 400

        if not marca_existe:
            return jsonify({"msg": "Marca no válida"}), 400

        # Actualizar producto
        update_query = """
            UPDATE productos 
            SET nombre = %s, descripcion = %s, id_categoria = %s, 
                id_marca = %s, precio = %s, imagen_url = %s
            WHERE id_producto = %s
        """

        resultado = ejecutar_sql(update_query, (
            nombre, descripcion, id_categoria, id_marca,
            precio, imagen_url, id_producto
        ))


        if resultado is not None:
            return jsonify({"msg": "Producto actualizado correctamente"}), 200
        else:
            return jsonify({"msg": "Error al actualizar producto"}), 500

    except Exception as e:
        print(f"Error en editar_producto: {str(e)}")
        return jsonify({"error": f"Error en el sistema: {str(e)}"}), 500


@app.route('/admin/eliminar_producto/<int:producto_id>', methods=['DELETE'])
def eliminar_producto(producto_id):
    try:
        # Verificar que el producto existe
        check_query = "SELECT id_producto FROM productos WHERE id_producto = %s"
        producto_existe = ejecutar_sql(check_query, (producto_id,))

        if not producto_existe:
            return jsonify({"msg": "Producto no encontrado"}), 404

        # Eliminar primero los registros relacionados
        # Eliminar del carrito
        ejecutar_sql("DELETE FROM carrito WHERE id_producto = %s", (producto_id,))

        # Eliminar de detalles de pedidos
        ejecutar_sql("DELETE FROM detalles_pedido WHERE id_producto = %s", (producto_id,))

        # Finalmente, eliminar el producto
        delete_query = "DELETE FROM productos WHERE id_producto = %s"
        resultado = ejecutar_sql(delete_query, (producto_id,))

        if resultado is not None:
            return jsonify({"msg": "Producto eliminado correctamente"}), 200
        else:
            return jsonify({"msg": "Error al eliminar producto"}), 500

    except Exception as e:
        print(f"Error en eliminar_producto: {str(e)}")
        return jsonify({"error": f"Error en el sistema: {str(e)}"}), 500


# ===== ENDPOINT PARA CREAR NUEVO PRODUCTO (OPCIONAL) =====

@app.route('/admin/crear_producto', methods=['POST'])
def crear_producto():
    try:
        data = request.get_json()

        nombre = data.get('nombre')
        descripcion = data.get('descripcion')
        id_categoria = data.get('id_categoria')
        id_marca = data.get('id_marca')
        precio = data.get('precio')
        imagen_url = data.get('imagen_url', '')

        if not all([nombre, descripcion, id_categoria, id_marca, precio]):
            return jsonify({"msg": "Todos los campos obligatorios deben ser completados"}), 400

        # Verificar que la categoría y marca existen
        cat_query = "SELECT id FROM categorias WHERE id = %s"
        categoria_existe = ejecutar_sql(cat_query, (id_categoria,))

        marca_query = "SELECT id FROM marcas WHERE id = %s"
        marca_existe = ejecutar_sql(marca_query, (id_marca,))

        if not categoria_existe:
            return jsonify({"msg": "Categoría no válida"}), 400

        if not marca_existe:
            return jsonify({"msg": "Marca no válida"}), 400

        # Crear producto
        insert_query = """
            INSERT INTO productos (nombre, descripcion, id_categoria, id_marca, precio, imagen_url)
            VALUES (%s, %s, %s, %s, %s, %s)
        """

        resultado = ejecutar_sql(insert_query, (
            nombre, descripcion, id_categoria, id_marca, precio, imagen_url
        ))

        if resultado is not None:
            return jsonify({"msg": "Producto creado correctamente"}), 201
        else:
            return jsonify({"msg": "Error al crear producto"}), 500

    except Exception as e:
        print(f"Error en crear_producto: {str(e)}")
        return jsonify({"error": f"Error en el sistema: {str(e)}"}), 500


# ===== ENDPOINT PARA VERIFICAR PERMISOS DE ADMIN =====

@app.route('/admin/verificar_permisos/<int:user_id>', methods=['GET'])
def verificar_permisos_admin(user_id):
    try:
        query = "SELECT rol FROM usuarios WHERE id = %s"
        resultado = ejecutar_sql(query, (user_id,))

        if not resultado:
            return jsonify({"error": "Usuario no encontrado"}), 404

        es_admin = resultado[0]['rol'] == 'admin'

        return jsonify({
            "es_admin": es_admin,
            "rol": resultado[0]['rol']
        })

    except Exception as e:
        print(f"Error en verificar_permisos_admin: {str(e)}")
        return jsonify({"error": f"Error en el sistema: {str(e)}"}), 500

if __name__ == '__main__':
    app.run(debug=True)

# https://console.cloudinary.com/app/c-9269b9831bb97dd9b9cea6fc49e195/assets/media_library/search?q=&view_mode=mosaic