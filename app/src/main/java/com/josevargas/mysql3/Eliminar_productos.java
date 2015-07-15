package com.josevargas.mysql3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Eliminar_productos extends Fragment {

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //si lo trabajan de manera local en xxx.xxx.x.x va su ip local
    // private static final String REGISTER_URL = "http://xxx.xxx.x.x:1234/cas/register.php";

    //testing on Emulator:
    private static final String ELIMINAR_URL = "http://basejoseremota.16mb.com/MySQL3/eliminar_producto.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    Button b_eliminar;
    EditText et_codigo, et_producto, et_precio, et_cantidad;
    String username;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout mLinearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_eliminar_productos, container, false);

        username = this.getArguments().getString("username");

        b_eliminar = (Button) mLinearLayout.findViewById(R.id.b_eliminar);

        et_codigo = (EditText) mLinearLayout.findViewById(R.id.eliminar_et_codigo);
        et_producto = (EditText) mLinearLayout.findViewById(R.id.eliminar_et_producto);
        et_precio = (EditText) mLinearLayout.findViewById(R.id.eliminar_et_precio);
        et_cantidad = (EditText) mLinearLayout.findViewById(R.id.eliminar_et_cantidad);

        b_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), username, Toast.LENGTH_LONG).show();
                new Eliminar_prod().execute();
            }
        });
        return mLinearLayout;
    }

    class Eliminar_prod extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Eliminando producto...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            String codigo = et_codigo.getText().toString();
            String producto = et_producto.getText().toString();
            String precio = et_precio.getText().toString();
            String cantidad = et_cantidad.getText().toString();
            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("codigo", codigo));
                params.add(new BasicNameValuePair("producto", producto));
                params.add(new BasicNameValuePair("precio", precio));
                params.add(new BasicNameValuePair("cantidad", cantidad));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        ELIMINAR_URL, "POST", params);

                // full json response
                Log.d("Registering attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("User Created!", json.toString());
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Registering Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(getActivity(), file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}


