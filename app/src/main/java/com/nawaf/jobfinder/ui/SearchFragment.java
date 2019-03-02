package com.nawaf.jobfinder.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.nawaf.jobfinder.R;
import com.nawaf.jobfinder.common.OnRecyclerItemClickLister;
import com.nawaf.jobfinder.models.FilterModel;
import com.nawaf.jobfinder.models.Providers;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnSearchListener} interface
 * to handle the search call back.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements Spinner.OnItemSelectedListener, OnRecyclerItemClickLister<AutocompletePrediction> {

    // views
    private SearchView svLocation;

    private PlacesAdapter placesAdapter;

    // constants
    private static final String ARG_FILTER_MODEL = "filter model";

    // variables and objects
    private Context mContext;
    private FilterModel filterModel;
    ArrayList<String> providersList = new ArrayList<>();

    // google places
    private PlacesClient placesClient;
    private AutocompleteSessionToken token;

    // listeners;
    private OnSearchListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param filterModel the filter object filled with previous search values .
     * @return A new instance of fragment SearchFragment.
     */
    public static SearchFragment newInstance(FilterModel filterModel) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FILTER_MODEL, filterModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            filterModel = (FilterModel) getArguments().getSerializable(ARG_FILTER_MODEL);
        } else {
            filterModel = new FilterModel();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initSearchViews(view);

        initProvidersSpinner(view);

        initRecyclerView(view);


    }

    private void initSearchViews(View view) {

        svLocation = view.findViewById(R.id.svLocation);

        svLocation.setQuery(filterModel.getLocation(), false);

        svLocation.setIconified(false);
        svLocation.setIconifiedByDefault(false);

        svLocation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                filterModel.setLocation(query);

                onSearchPressed(filterModel);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterModel.setLocation(newText);

                // initialize the autocomplete api from google
                FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                        .setTypeFilter(TypeFilter.CITIES)
                        .setSessionToken(token)
                        .setQuery(newText)
                        .build();

                // register call back for the query request
                placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
                    //update the list
                    placesAdapter.updateList(response.getAutocompletePredictions());



                }).addOnFailureListener((exception) -> {

                });

                return false;
            }
        });


        SearchView svTitle = view.findViewById(R.id.svTitle);

        svTitle.setIconified(false);
        svTitle.setIconifiedByDefault(false);

        svTitle.setQuery(filterModel.getTitle(),false);

        svTitle.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                filterModel.setTitle(query);

                onSearchPressed(filterModel);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // save user input to filter option
                filterModel.setTitle(newText);
                return false;
            }
        });
    }

    private void initRecyclerView(View view) {

        RecyclerView rvPlaces = view.findViewById(R.id.rvPlaces);

        placesAdapter = new PlacesAdapter();

        placesAdapter.setOnRecyclerItemClickLister(this);

        rvPlaces.setLayoutManager(new LinearLayoutManager(mContext));

        rvPlaces.setAdapter(placesAdapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        initPlacesAPI();

        Places.initialize(mContext.getApplicationContext(), getString(R.string.google_places_key));

        // Create a new Places client instance.
        placesClient = Places.createClient(mContext);

        token = AutocompleteSessionToken.newInstance();

    }

    private void initPlacesAPI() {

    }

    private void initProvidersSpinner(View view) {
        Spinner spProviders = view.findViewById(R.id.spProviders);

        spProviders.setOnItemSelectedListener(this);


        int selectedIndex =0;

        // if we add provider to the enum it we be reflected here
        for (Providers provider : Providers.values()) {
            providersList.add(provider.getName());
            if (provider.getId() == filterModel.getProviders().getId()){
                selectedIndex = provider.ordinal();

            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, providersList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProviders.setAdapter(dataAdapter);

        spProviders.setSelection(selectedIndex);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


    public void onSearchPressed(FilterModel filterModel) {
        if (mListener != null) {
            mListener.onSearchClicked(filterModel);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;

        if (context instanceof OnSearchListener) {
            mListener = (OnSearchListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!providersList.isEmpty()) {

            for (Providers provider : Providers.values()) {
                if (provider.getName().equals(providersList.get(position))) {
                    filterModel.setProviders(provider);

                }

            }


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClicked(AutocompletePrediction value) {
        filterModel.setLocation(value.getPrimaryText(null).toString());

//        String location = String.format("%s, %s",value.getPrimaryText(null).toString(),value.getSecondaryText(null).toString());
        String location = value.getPrimaryText(null).toString();

        svLocation.setQuery(location, false);
    }

    /**
     * This listener to return the search filter to the activity
     */
    public interface OnSearchListener {
        void onSearchClicked(FilterModel filterModel);
    }
}
