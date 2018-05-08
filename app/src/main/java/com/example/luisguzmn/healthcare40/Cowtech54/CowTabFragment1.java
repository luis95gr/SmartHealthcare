package com.example.luisguzmn.healthcare40.Cowtech54;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luisguzmn.healthcare40.R;

import org.w3c.dom.Text;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CowTabFragment1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CowTabFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CowTabFragment1 extends Fragment implements View.OnClickListener{

    private final String TAG = CowTabFragment1.class.getSimpleName();

    CowService cowService;
    CowService.CowBinder cowBinder;
    //   IBinder cowBinder;
    boolean sBound = false;

    private Button sStartBtn;
    private Button sStopBtn;
    private Button sBindBtn;
    private Button sUnbindBtn;
    private Button sUpdateBtn;

    private TextView sPairedDeviceTxt;

    //Preferences
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CowTabFragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CowTabFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static CowTabFragment1 newInstance(String param1, String param2) {
        CowTabFragment1 fragment = new CowTabFragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs.edit();




    }

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cow_tab1, container, false);

        sStartBtn = (Button) view.findViewById(R.id.cowTab1StartBtn);
        sStopBtn = (Button) view.findViewById(R.id.cowTab1StopBtn);
        sBindBtn = (Button) view.findViewById(R.id.cowTab1BindBtn);
        sUnbindBtn = (Button) view.findViewById(R.id.cowTab1UnbindBtn);

        sUpdateBtn = (Button) view.findViewById(R.id.cowTab1UpdateBtn);
        sPairedDeviceTxt = (TextView) view.findViewById(R.id.pairedDeviceTxt);

        sStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CowService.class);
                getActivity().startService(intent);
                Log.d(TAG, "START BTN CLICKED");
            }
        });
        sStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CowService.class);
                getActivity().stopService(intent);
            }
        });
        sBindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CowService.class);
                getActivity().bindService(intent,sServerConn, Context.BIND_AUTO_CREATE);
            }
        });
        sUnbindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CowService.class);
                if(sBound) {
                    getActivity().unbindService(sServerConn);
                    sBound=false;
                }
            }
        });
        sUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sBound){
                    String filePathStr;
                    filePathStr = cowService.getFileCSV().getFilePathStr();
                    Toast.makeText(getActivity().getApplicationContext(), filePathStr, Toast.LENGTH_LONG).show();
                }
            }
        });

        //Update the paired device textview
        String pairedDeviceMac = prefs.getString("cow_paired_mac", "Not synced");
        sPairedDeviceTxt.setText("Device: "+ pairedDeviceMac);

        return view;
    }



    protected ServiceConnection sServerConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            CowService.CowBinder binder = (CowService.CowBinder) service;
            cowService = binder.getService();
            //cowService.set
            sBound = true;
            Log.d(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            sBound = false;
            Log.d(TAG, "onServiceDisconnected");
        }
    };


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    public void onResume() {
        super.onResume();
        //Update the paired device textview
        String pairedDeviceMac = prefs.getString("cow_paired_mac", "Not synced");
        sPairedDeviceTxt.setText("Device: "+ pairedDeviceMac);
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
