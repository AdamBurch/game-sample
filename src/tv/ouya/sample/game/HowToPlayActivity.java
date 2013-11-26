package tv.ouya.sample.game;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HowToPlayActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.howtoplay);
        
        final TextView howToPlayText = (TextView) findViewById(R.id.howtoplay_textView);
        Resources res = getResources();
        final String[] howToPlayMessages = res.getStringArray(R.array.howtoplay_messages);
        
        Button notEnough = (Button) findViewById(R.id.notenough_button);
        notEnough.setOnClickListener(new View.OnClickListener() {
        	int counter = 0;
        	
            @Override
            public void onClick(View v) {
                if (counter >= howToPlayMessages.length)
                	counter = 0;
                else
                	counter++;
                
                howToPlayText.setText(howToPlayMessages[counter]);
            }
        });
	}
}
