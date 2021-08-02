package zvhir.dev.hexcodeextractor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Dashboard extends AppCompatActivity {

    private Palette.Swatch vibrantSwatch;
    private Palette.Swatch lightVibrantSwatch;
    private Palette.Swatch darkVibrantSwatch;

    Button button;
    private static final int RESULT_LOAD_IMG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });


    }


    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                Dialog dialog = new Dialog(Dashboard.this); // Context, this, etc.
                dialog.setContentView(R.layout.dialog);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); // set adapter background jadi transparent

                ImageView imageView = (ImageView) dialog.findViewById(R.id.imageView);
                imageView.setImageBitmap(selectedImage);

                TextView xMainColour;
                TextView xLightColour;
                TextView xDarkColour;

                ImageView box1 = dialog.findViewById(R.id.box1);
                ImageView box2 = dialog.findViewById(R.id.box2);
                ImageView box3 = dialog.findViewById(R.id.box3);

                xMainColour = (TextView) dialog.findViewById(R.id.primaryDark);
                xLightColour = (TextView) dialog.findViewById(R.id.primaryLight);
                xDarkColour = (TextView) dialog.findViewById(R.id.primaryVibrant);

                TextView titleMainColour;
                TextView titleLightColour;
                TextView titleDarkColour;

                titleMainColour = (TextView) dialog.findViewById(R.id.titleprimarydarklinear);
                titleLightColour = (TextView) dialog.findViewById(R.id.titleprimaryLight);
                titleDarkColour = (TextView) dialog.findViewById(R.id.titleprimaryVibrant);

                xMainColour.setVisibility(View.GONE);
                xLightColour.setVisibility(View.GONE);
                xDarkColour.setVisibility(View.GONE);

                Button extract;
                extract = (Button) dialog.findViewById(R.id.extractButton);
                extract.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        Palette.from(bitmap).maximumColorCount(32).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                vibrantSwatch = palette.getLightMutedSwatch();
                                lightVibrantSwatch = palette.getDarkMutedSwatch();
                                darkVibrantSwatch = palette.getDominantSwatch();

                                String hexMain = vibrantSwatch.toString().substring(16,22);
                                String hexLight = lightVibrantSwatch.toString().substring(16,22);
                                String hexDark = darkVibrantSwatch.toString().substring(16,22);

                                titleMainColour.setVisibility(View.VISIBLE);
                                titleLightColour.setVisibility(View.VISIBLE);
                                titleDarkColour.setVisibility(View.VISIBLE);

                                xMainColour.setText("#" + hexMain);
                                xDarkColour.setText("#" + hexDark);
                                xLightColour.setText("#" + hexLight);

                                box1.setVisibility(View.VISIBLE);
                                box2.setVisibility(View.VISIBLE);
                                box3.setVisibility(View.VISIBLE);

                                box1.setBackgroundColor(Color.parseColor("#" + hexMain));
                                box2.setBackgroundColor(Color.parseColor("#" + hexLight));
                                box3.setBackgroundColor(Color.parseColor("#" + hexDark ));


                                xMainColour.setTextColor(Color.parseColor("#" + hexMain));
                                xDarkColour.setTextColor(Color.parseColor("#" + hexDark));
                                xLightColour.setTextColor(Color.parseColor("#" + hexLight));


                                xMainColour.setVisibility(View.VISIBLE);
                                xLightColour.setVisibility(View.VISIBLE);
                                xDarkColour.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });

                dialog.show();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(Dashboard.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(Dashboard.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }


}