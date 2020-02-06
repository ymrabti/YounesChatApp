package ma.ymrabti.youneswhatsapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.min;

public class Classtest  extends AppCompatActivity {
    public static int portBackend=3000;
    public static String ip_serverIP="192.168.137.1",ip_server="http://"+ip_serverIP+":"+portBackend,email_connected;

    public static Dialog dialog_universelle;
    public static String sql="SELECT laureats.*,filieres.Nom as Nom_filiere,les_status.nom as status" +
            ",motif,les_status.id_lesstatus,organisme.secteur,organisme.province" +
            " ,organisme.Latitude,organisme.Longitude,organisme.Nom as NomOrg"
            +" FROM  laureats,filieres,les_status ,laureat_statut,organisme"
            +" WHERE laureats.Filiere=filieres.id_filieres and laureats.org=organisme.id_org"
            +" and laureats.email=laureat_statut.id_laureat and laureat_statut.id_statut=les_status.id_lesstatus "
    ,additional_sql="",email_selected;
    public static int shared_org;
    public static Bitmap get_Bitmap(Context context,int drawableRes) {
        Drawable drawable = context.getDrawable(drawableRes);
        Canvas canvas = new Canvas();int target=50;
        Bitmap bitmap = Bitmap.createBitmap(target, target, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, target, target);
        drawable.draw(canvas);
        return bitmap;
    }
    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int minimum = min(scaleBitmapImage.getWidth(),scaleBitmapImage.getHeight());
        Bitmap targetBitmap = Bitmap.createBitmap(minimum, minimum,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) minimum - 1) / 2, ((float) minimum - 1) / 2,
                (min(((float) minimum), ((float) minimum)) / 2),
                Path.Direction.CCW);


        canvas.clipPath(path);
        canvas.drawBitmap(scaleBitmapImage,
                new Rect(0, 0, minimum, minimum),
                new Rect(0, 0, minimum, minimum), null);
        return targetBitmap;
    }
    public static Bitmap add_text(Bitmap icon,String textToDraw){
        Bitmap newBitmap = icon.copy(icon.getConfig(), true);
        Canvas newCanvas = new Canvas(newBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);float scale = (float)icon.getWidth()/textToDraw.length();
        paint.setTextSize(scale);
        Rect bounds = new Rect();
        paint.getTextBounds(textToDraw, 0, textToDraw.length(), bounds);
        int x = (icon.getWidth() - bounds.width())/2;
        int y = (icon.getHeight() + bounds.height())/2;
        newCanvas.drawText(textToDraw, x, y, paint);
        return  newBitmap;
    }
    public static Bitmap resize_drawable(Drawable drawable){
        assert drawable != null;
        Bitmap b = ((BitmapDrawable)drawable).getBitmap();
        int width = drawable.getIntrinsicWidth(),heigh=drawable.getIntrinsicHeight();
        float scaleFactor =(float)200/Math.max(heigh,width);
        int sizeX = Math.round(width * scaleFactor);
        int sizeY =  Math.round(heigh* scaleFactor);
        return Bitmap.createScaledBitmap(b, sizeX, sizeY, false);
    }
    public static Bitmap resize_bitmap(Bitmap bitmap){
        int width = bitmap.getWidth(),heigh=bitmap.getHeight();
        float scaleFactor =(float)200/Math.max(heigh,width);
        int sizeX = Math.round(width * scaleFactor);
        int sizeY =  Math.round(heigh* scaleFactor);
        return Bitmap.createScaledBitmap(bitmap, sizeX, sizeY, false);
    }
    public static Bitmap resize_icon(Bitmap bitmap){
        int width = bitmap.getWidth(),heigh=bitmap.getHeight();
        float scaleFactor =(float)100/Math.max(heigh,width);
        int sizeX = Math.round(width * scaleFactor);
        int sizeY =  Math.round(heigh* scaleFactor);
        return Bitmap.createScaledBitmap(bitmap, sizeX, sizeY, false);
    }
    public static String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public static Bitmap base64toImage(final String imageString){
        Bitmap new_bitmap;
        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        new_bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return new_bitmap;
    }
    public static void set_filter_pref(Context context,long sector,long province,long org,long branch,long promotion){
        SharedPreferences sharedPreferences = context.getSharedPreferences("filter", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("sector",sector);editor.putLong("province",province);
        editor.putLong("organisation",org);
        editor.putLong("promotion",promotion);editor.putLong("branch",branch);
        editor.apply();
    }
    public static long get_filter_pref_long(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("filter", Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key,0);
    }
    public static String get_filter_pref_string(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("filter", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"TOUT");
    }
    public static String get0Pref(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        return sharedPreferences.getString("email","noreply");
    }
    public static void set0Pref(Context context,String user_email){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email",user_email);editor.apply();
    }


    public static void setclipboard(String message,Context context){
        ClipboardManager cManager = (ClipboardManager) Objects.requireNonNull(context).getSystemService(
                Context.CLIPBOARD_SERVICE);
        ClipData cData = ClipData.newPlainText("text", message+"");
        if (cManager != null) {
            cManager.setPrimaryClip(cData);
            Toast.makeText(context,"copied to clipboard",Toast.LENGTH_LONG).show();
        }
    }
    public static void logout(Context context){
        set0Pref(context,"noreply");
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
    public static void connect_to_backend(Context context, int method, String url, JSONObject jsonObject
            , Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                method
                , ip_server + url, jsonObject
                , listener, errorListener);
        requestQueue.add(jsonObjectRequest);
    }
    public static void connect_to_backend_array(Context context, int method, String url, JSONArray jsonArray
            , Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                method
                , ip_server + url, jsonArray
                , listener, errorListener);
        requestQueue.add(jsonArrayRequest);
    }

    @SuppressLint("HardwareIds")
    public static String getUniqueIMEIId() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00";
    }
    /*private void read_json(){
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("formules");
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connecting_rest(final String url) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(url);
                HttpResponse response;
                try {
                    response = httpClient.execute(httpget);
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream instream = entity.getContent();
                        String result= convertStreamToString(instream);
                        instream.close();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }*/
}
