package cs3724.group.mealmate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Alexia on 5/3/15.
 */
public class MapFragment extends Fragment {
    MapView mapView;
    GoogleMap map;


    public Map() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView)view.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        map = mapView.getMap();
        map.getUiSettings().setCompassEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(true);

        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(new LatLng(37.228350, -80.422172), 17);
        map.animateCamera(camera);

        /* ADD DINING HALL MARKERS TO MAP */
        map.addMarker(new MarkerOptions()
                .position(new LatLng(37.226689, -80.418887))
                .title("Owens"));
        map.addMarker(new MarkerOptions()
                .position(new LatLng(37.229234, -80.418243))
                .title("Burger 37"));
        map.addMarker(new MarkerOptions()
                .position(new LatLng(37.224203, -80.421279))
                .title("Deets Place"));
        map.addMarker(new MarkerOptions()
                .position(new LatLng(37.224561, -80.421140))
                .title("D2"));
        map.addMarker(new MarkerOptions()
                .position(new LatLng(37.223288, -80.421988))
                .title("West End Market"));
        map.addMarker(new MarkerOptions()
                .position(new LatLng(37.230994, -80.422728))
                .title("Turner Place"));

        return view;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
