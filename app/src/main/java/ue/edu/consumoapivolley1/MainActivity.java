package ue.edu.consumoapivolley1;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //declaramos variables
    // 1. Constantes actualizadas para el endpoint /posts
    private static final String URL_API = "https://jsonplaceholder.typicode.com/posts";
    private static final String REQUEST_TAG = "GET_POSTS";

    // 2. Variables de la interfaz y Volley
    private ListView lvTodos;
    private ProgressBar progressBar;
    private TextView tvEstado;
    private RequestQueue requestQueue;

    // 3. Lista orientada a objetos (usando la clase Post que creaste)
    private final List<Post> listaPosts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initObjects();
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Llamar al metodo para iniciar la descarga de datos
        consumirApi();
    }

    //3.creamos metodo que recobe parametro tipo boolean, que si es truemuestra el progressBar y listView
    private void mostrarCargando(boolean cargando){

        //usamos operador ternario, si la condicion cargando es true, la rueda y lista se veran, si es false no se mostrara
        progressBar.setVisibility(cargando ? View.VISIBLE : View.GONE);
        lvTodos.setVisibility(cargando ? View.GONE : View.VISIBLE);
    }

    //4.metodo para unificar y mostrar los mensajes de errores y otro para los errores de Volley generados durante el consumo del api
    //Define el metodo que se ejecutara cuando la peticion de Volley falle. Recibe como parametro (mensaje) el texto descriptivo del error para saber qué salió mal.
    private void mostrarError(String mensaje) {
        mostrarCargando(false);
        //Toma el componente TextView de la interfaz y le inserta internamente el mensaje de error.
        tvEstado.setText(mensaje);
        //Cambia el estado del tvEstado de GONE (oculto) a VISIBLE
        tvEstado.setVisibility(View.VISIBLE);
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    //metodo se encarga de interpretar el objeto error que Volley genera automáticamente cuando falla la petición HTTP
    private void procesarError(VolleyError error) {
        String mensaje = error.getMessage();

        if (mensaje == null || mensaje.isBlank()) {
            mensaje = "Verifique la conexión a internet.";
        }

        mostrarError("Error en la solicitud: " + mensaje);
    }
    private void consumirApi() {
        mostrarCargando(true);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL_API,
                null,
                response -> {
                    listaPosts.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject item = response.getJSONObject(i);

                            // Extraer los 4 campos del recurso /posts
                            int userId = item.getInt("userId");
                            int id = item.getInt("id");
                            String title = item.getString("title");
                            String body = item.getString("body");

                            // Crear el objeto y añadirlo a la lista
                            Post nuevoPost = new Post(userId, id, title, body);
                            listaPosts.add(nuevoPost);
                        }

                        // Crear el adaptador pasando la lista de objetos Post
                        ArrayAdapter<Post> adapter = new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_list_item_1,
                                listaPosts
                        );

                        lvTodos.setAdapter(adapter);
                        mostrarCargando(false);
                        tvEstado.setVisibility(View.GONE);

                        // Mostrar Toast con el total de registros
                        String mensajeTotal = "Se recibieron " + response.length() + " registros.";
                        Toast.makeText(this, mensajeTotal, Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        mostrarError("No fue posible procesar la respuesta.");
                    }
                },
                this::procesarError
        );

        request.setTag(REQUEST_TAG);
        requestQueue.add(request);
    }

    //2.enlazamos los objetos
    public void initObjects(){
        lvTodos = findViewById(R.id.lvTodos);
        progressBar = findViewById(R.id.progressBar);
        tvEstado = findViewById(R.id.tvEstado);
    }
}