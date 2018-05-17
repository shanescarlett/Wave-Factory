package android.scarlettsystems.net.wavefactory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import net.scarlettsystems.android.wavefactory.WaveFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		float[] wave = WaveFactory.pcmToFloat(WaveFactory.getSawtoothWave(1000, 0.1f, 44100, 0));
		LineChart chart = findViewById(R.id.chart);
		List<Entry> entries = new ArrayList<>();
		for(int c = 0; c < wave.length; c++)
		{
			entries.add(new Entry((float)c / 44100f, wave[c]));
		}
		LineDataSet dataSet = new LineDataSet(entries, "Wave");
		LineData lineData = new LineData(dataSet);
		chart.setData(lineData);
		chart.invalidate();
	}
}
