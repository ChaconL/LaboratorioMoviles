package com.example.leidychacn.lab1_leidychaconsalas;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button save;
    Button picture;
    TextView profession;
    ListView list;
    EditText name;
    RadioButton female;
    RadioButton male;
    private static final int REQUEST_CODE = 1234;
    Dialog matchTextDialog;
    ListView textListView;
    ArrayList<String> matchesText;
    private static final int VIDEO_CAPTURE = 101;
    Uri fotoUri;
    String newProfession="";
    ArrayList<Persona> listaPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        save = (Button) findViewById(R.id.save);
        picture = (Button) findViewById(R.id.picture);
        profession = (TextView)  findViewById(R.id.profession);
        list = (ListView) findViewById(R.id.listView);
        name = (EditText) findViewById(R.id.name);
        female =(RadioButton) findViewById(R.id.female);
        male =(RadioButton) findViewById(R.id.male);

        listaPersonas = new ArrayList<Persona>();

        profession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected()) {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE , "es-ES");
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                }

            }
        });

        if (! hasCamera () )
            picture.setEnabled( false ) ;


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sexo = "";
                String nombre = name.getText().toString();



                if(female.isChecked())
                    sexo = "female";

                else if(male.isChecked())
                    sexo = "male";


                if(sexo!="" && newProfession != "" && nombre != ""){


                    Persona newPerson = new Persona(nombre,newProfession,sexo,fotoUri);
                    listaPersonas.add(newPerson);

                    list.setAdapter(new viewAdapter(MainActivity.this));


                }
                else {

                    Toast.makeText(MainActivity.this, "Datos imcompletos", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Uri videoUri = data.getData () ;

        if ( requestCode == VIDEO_CAPTURE ) {
            if ( resultCode == RESULT_OK ) {
                Toast . makeText (this , " Video saved to :\n" + videoUri , Toast.LENGTH_LONG ).show () ;
                fotoUri = data.getData ();

            } else if ( resultCode == RESULT_CANCELED ) {
                Toast . makeText (this , " Video recording cancelled .", Toast.LENGTH_LONG ).show() ;
            } else {
                Toast . makeText (this , " Failed to record video ", Toast.LENGTH_LONG ).show() ;
            }
        }
        else {

            matchTextDialog = new Dialog(MainActivity.this); //Create a Dialog
            matchTextDialog.setContentView(R.layout.dialog_matches_frag); //Link the new Dialog with the dialog_matches frag
            matchTextDialog.setTitle("Select Matching Text"); //Add title to the Dialog
            textListView = (ListView) matchTextDialog.findViewById(R.id.listView1);
            matchesText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS); //Get data of data
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matchesText );
            textListView.setAdapter(adapter);
            textListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    profession.setText(matchesText.get(i));
                    newProfession = matchesText.get(i);
                    matchTextDialog.hide();
                }
            });
            matchTextDialog.show();

            }
    }

    /**
     To Check if the net is available and conected
     * @return true if the net is available and conected
     * and false in other case
     */
    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net!= null && net.isAvailable() && net.isConnected()){
            return true;
        }   else {
            return false;
        }
    }


    public void startRecording ( View view )
    {

        Intent intent = new Intent ( MediaStore.ACTION_IMAGE_CAPTURE ) ;
        startActivityForResult ( intent , VIDEO_CAPTURE ) ;
    }



    private boolean hasCamera () {
        return ( getPackageManager () . hasSystemFeature (
                PackageManager. FEATURE_CAMERA_ANY ) ) ;
    }


    public class viewAdapter extends BaseAdapter {
        LayoutInflater mInflater;
        public viewAdapter(Context context){
            mInflater= LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return listaPersonas.size();
        }

        @Override
        public Object getItem(int i) {
            return listaPersonas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return  i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            if(view==null){
                view=mInflater.inflate(R.layout.person_item,null);
            }
            final TextView nombre= (TextView) view.findViewById(R.id.nombre);
            TextView profesion= (TextView) view.findViewById(R.id.profesion);
            TextView sexo= (TextView) view.findViewById(R.id.sexo);
            ImageView img = (ImageView) view.findViewById(R.id.img);

            nombre.setText(listaPersonas.get(i).getNombre());
            profesion.setText(listaPersonas.get(i).getProfession());
            sexo.setText(listaPersonas.get(i).getSex());
            img.setImageURI(listaPersonas.get(i).getFoto());


            return view;

        }
    }

}
