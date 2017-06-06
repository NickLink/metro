package ua.metro.mobileapp;

import java.util.EnumMap;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class Out_BarCodeDraw extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.out_barcode_draw);
    
    LinearLayout l = (LinearLayout) findViewById (R.id.barLL);

    // barcode data
    String barcode_data = getIntent().getStringExtra("barcodeString");
    if(null==barcode_data)
     barcode_data = "9848572076528";
    

    // barcode image
    Bitmap bitmap = null;
    ImageView iv = new ImageView(this);

    try {

        bitmap = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 600, 300);
        iv.setImageBitmap(bitmap);

    } catch (WriterException e) {
        e.printStackTrace();
    }

    l.addView(iv);

    //barcode text
    TextView tv = new TextView(this);
    tv.setGravity(Gravity.CENTER_HORIZONTAL);
    tv.setText(barcode_data);

    l.addView(tv);

    }

    /**************************************************************
     * getting from com.google.zxing.client.android.encode.QRCodeEncoder
     * 
     * See the sites below
     * http://code.google.com/p/zxing/
     * http://code.google.com/p/zxing/source/browse/trunk/android/src/com/google/zxing/client/android/encode/EncodeActivity.java
     * http://code.google.com/p/zxing/source/browse/trunk/android/src/com/google/zxing/client/android/encode/QRCodeEncoder.java
     */

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
    String contentsToEncode = contents;
    if (contentsToEncode == null) {
        return null;
    }
    Map<EncodeHintType, Object> hints = null;
    String encoding = guessAppropriateEncoding(contentsToEncode);
    if (encoding != null) {
        hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, encoding);
    }
    MultiFormatWriter writer = new MultiFormatWriter();
    BitMatrix result;
    try {
        result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
    } catch (IllegalArgumentException iae) {
        // Unsupported format
        return null;
    }
    int width = result.getWidth();
    int height = result.getHeight();
    int[] pixels = new int[width * height];
    for (int y = 0; y < height; y++) {
        int offset = y * width;
        for (int x = 0; x < width; x++) {
        pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
        }
    }

    Bitmap bitmap = Bitmap.createBitmap(width, height,
        Bitmap.Config.ARGB_8888);
    bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
    // Very crude at the moment
    for (int i = 0; i < contents.length(); i++) {
        if (contents.charAt(i) > 0xFF) {
        return "UTF-8";
        }
    }
    return null;
    }

}