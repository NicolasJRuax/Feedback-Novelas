package com.myproyect.gestornovelasnjr.gestor_novelas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.myproyect.gestornovelasnjr.R;
import com.myproyect.gestornovelasnjr.gestor_novelas.Novelas.Novel;
import com.myproyect.gestornovelasnjr.gestor_novelas.Novelas.NovelAdapter;
import com.myproyect.gestornovelasnjr.gestor_novelas.Novelas.NovelViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private Button buttonAddBook;
    private RecyclerView recyclerView;
    private NovelAdapter novelAdapter;
    private NovelViewModel novelViewModel;
    private MapView mapView;
    private GoogleMap googleMap;

    private final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    private ActivityResultLauncher<String[]> locationPermissionRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Aplicar el tema según la configuración
        ThemeUtils.applyDarkMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        buttonAddBook = findViewById(R.id.buttonAddBook);
        recyclerView = findViewById(R.id.recyclerView);
        mapView = findViewById(R.id.mapView);

        // Configuración del RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        novelAdapter = new NovelAdapter(this, novel -> {
            // Eliminar novela
            novelViewModel.delete(novel);
            Toast.makeText(this, "Novela eliminada", Toast.LENGTH_SHORT).show();
            // Actualizar marcadores en el mapa
            updateMapMarkers();
        }, this::showNovelDetails);
        recyclerView.setAdapter(novelAdapter);

        // Inicializar ViewModel
        novelViewModel = new ViewModelProvider(this).get(NovelViewModel.class);
        novelViewModel.getAllNovels().observe(this, novels -> {
            novelAdapter.setNovels(novels);
            // Actualizar marcadores en el mapa
            updateMapMarkers();
        });

        // Evento para el botón de agregar novela
        buttonAddBook.setOnClickListener(v -> showAddNovelDialog());

        // Configurar MapView
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Solicitud de permisos
        locationPermissionRequest = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    Boolean fineLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_COARSE_LOCATION, false);
                    if (fineLocationGranted != null && fineLocationGranted) {
                        // Precise location access granted.
                        enableUserLocation();
                    } else if (coarseLocationGranted != null && coarseLocationGranted) {
                        // Only approximate location access granted.
                        enableUserLocation();
                    } else {
                        // No location access granted.
                        Toast.makeText(this, "Permisos de ubicación denegados", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        updateMapMarkers();
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationPermissionRequest.launch(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                });
            } else {
                enableUserLocation();
            }
        } else {
            enableUserLocation();
        }
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            googleMap.setMyLocationEnabled(true);

            // Obtener la ubicación actual y mover la cámara
            LocationServices.getFusedLocationProviderClient(this)
                    .getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
                        }
                    });
        }
    }

    private void updateMapMarkers() {
        if (googleMap != null && novelAdapter != null) {
            googleMap.clear();
            List<Novel> novels = novelAdapter.getNovels();
            if (novels != null) {
                for (Novel novel : novels) {
                    if (novel.getLatitude() != null && novel.getLongitude() != null) {
                        LatLng location = new LatLng(novel.getLatitude(), novel.getLongitude());
                        googleMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(novel.getTitle()));
                    }
                }
            }
        }
    }

    private void showAddNovelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Novela");

        final View customLayout = LayoutInflater.from(this).inflate(R.layout.dialog_add_novel, null);
        builder.setView(customLayout);

        EditText editTextTitle = customLayout.findViewById(R.id.editTextTitle);
        EditText editTextAuthor = customLayout.findViewById(R.id.editTextAuthor);
        EditText editTextYear = customLayout.findViewById(R.id.editTextYear);
        EditText editTextSynopsis = customLayout.findViewById(R.id.editTextSynopsis);
        EditText editTextLocation = customLayout.findViewById(R.id.editTextLocation); // Nuevo campo para ubicación

        builder.setPositiveButton("Agregar", (dialog, which) -> {
            String title = editTextTitle.getText().toString().trim();
            String author = editTextAuthor.getText().toString().trim();
            String yearText = editTextYear.getText().toString().trim();
            String synopsis = editTextSynopsis.getText().toString().trim();
            String locationText = editTextLocation.getText().toString().trim();

            if (title.isEmpty() || author.isEmpty() || yearText.isEmpty() || synopsis.isEmpty() || locationText.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int year;
            try {
                year = Integer.parseInt(yearText);
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Año inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Geocodificación de la dirección
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocationName(locationText, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    double latitude = address.getLatitude();
                    double longitude = address.getLongitude();

                    Novel novel = new Novel(title, author, year, synopsis);
                    novel.setLatitude(latitude);
                    novel.setLongitude(longitude);

                    novelViewModel.insert(novel);
                    Toast.makeText(MainActivity.this, "Novela añadida", Toast.LENGTH_SHORT).show();
                    // Actualizar marcadores en el mapa
                    updateMapMarkers();
                } else {
                    Toast.makeText(MainActivity.this, "Dirección no encontrada", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Error al geocodificar la dirección", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void showNovelDetails(Novel novel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(novel.getTitle());

        // Geocodificación inversa para obtener la dirección de las coordenadas
        String addressText = "";
        if (novel.getLatitude() != null && novel.getLongitude() != null) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(novel.getLatitude(), novel.getLongitude(), 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    addressText = address.getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        builder.setMessage("Autor: " + novel.getAuthor()
                + "\nAño: " + novel.getYear()
                + "\nUbicación: " + addressText
                + "\n\n" + novel.getSynopsis());
        builder.setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    // Métodos del ciclo de vida del MapView
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
