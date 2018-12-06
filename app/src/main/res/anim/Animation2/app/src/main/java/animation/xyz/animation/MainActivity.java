package animation.xyz.animation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b=(Button)findViewById(R.id.android1);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation=AnimationUtils.loadAnimation(MainActivity.this,R.anim.mixed_anim);

                b.startAnimation(animation);

            }
        });


    }
}
