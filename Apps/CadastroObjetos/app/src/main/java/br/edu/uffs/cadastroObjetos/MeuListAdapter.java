package br.edu.uffs.cadastroObjetos;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MeuListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] nome;
    private final String[] frase;
    private final byte[][] imgid;
    private final Double[] latitudes;
    private final Double[] longitudes;

    public MeuListAdapter(Activity context, String[] n, String[] f, byte[][] imgid, Double[] lats, Double[] longs) {
        super(context, R.layout.minha_lista_objetos, n);
        this.context=context;
        this.nome=n;
        this.frase=f;
        this.imgid=imgid;
        this.latitudes=lats;
        this.longitudes=longs;
    }

    public View getView(int position,View view,ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();

        View rowView=inflater.inflate(R.layout.minha_lista_objetos, null,true);

        TextView nomeView = (TextView) rowView.findViewById(R.id.item);
        nomeView.setText(nome[position]);

        ImageView imagemMin = (ImageView) rowView.findViewById(R.id.icon);
        Bitmap bmp = BitmapFactory.decodeByteArray(imgid[position], 0, imgid[position].length);
        imagemMin.setImageBitmap(bmp);

        TextView fraseView = (TextView) rowView.findViewById(R.id.textView1);
        fraseView.setText("Frase: " + frase[position]);

        TextView latView = (TextView) rowView.findViewById(R.id.lat);
        latView.setText("Latitude: " + latitudes[position]);

        TextView longView = (TextView) rowView.findViewById(R.id.lon);
        longView.setText("Longitude: " + longitudes[position]);

        return rowView;

    };
}