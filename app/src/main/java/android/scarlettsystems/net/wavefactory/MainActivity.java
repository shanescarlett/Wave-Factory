package android.scarlettsystems.net.wavefactory;

import android.graphics.Color;
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
		drawSineWave();
		drawSquareWave();
		drawTriangularWave();
		drawSawtoothWave();
		drawRawWave();
	}

	private void drawSineWave()
	{
		float[] wave = WaveFactory.pcmToFloat(WaveFactory.getSineWave(200, 0.01f, 44100, 0));
		LineChart chart = findViewById(R.id.sine);
		List<Entry> entries = new ArrayList<>();
		for(int c = 0; c < wave.length; c++)
		{
			entries.add(new Entry((float)c / 44100f, wave[c]));
		}
		LineDataSet dataSet = new LineDataSet(entries, "Sine Wave");
		dataSet.setDrawCircles(false);
		dataSet.setColor(Color.BLACK);
		LineData lineData = new LineData(dataSet);
		chart.setData(lineData);
		chart.invalidate();
	}

	private void drawSquareWave()
	{
		float[] wave = WaveFactory.pcmToFloat(WaveFactory.getSquareWave(200, 0.01f, 44100, 0));
		LineChart chart = findViewById(R.id.square);
		List<Entry> entries = new ArrayList<>();
		for(int c = 0; c < wave.length; c++)
		{
			entries.add(new Entry((float)c / 44100f, wave[c]));
		}
		LineDataSet dataSet = new LineDataSet(entries, "Square Wave");
		dataSet.setDrawCircles(false);
		dataSet.setColor(Color.BLACK);
		LineData lineData = new LineData(dataSet);
		chart.setData(lineData);
		chart.invalidate();
	}

	private void drawTriangularWave()
	{
		float[] wave = WaveFactory.pcmToFloat(WaveFactory.getTriangularWave(200, 0.01f, 44100, 0));
		LineChart chart = findViewById(R.id.triangular);
		List<Entry> entries = new ArrayList<>();
		for(int c = 0; c < wave.length; c++)
		{
			entries.add(new Entry((float)c / 44100f, wave[c]));
		}
		LineDataSet dataSet = new LineDataSet(entries, "Triangular Wave");
		dataSet.setDrawCircles(false);
		dataSet.setColor(Color.BLACK);
		LineData lineData = new LineData(dataSet);
		chart.setData(lineData);
		chart.invalidate();
	}

	private void drawSawtoothWave()
	{
		float[] wave = WaveFactory.pcmToFloat(WaveFactory.getSawtoothWave(200, 0.01f, 44100, 0));
		LineChart chart = findViewById(R.id.sawtooth);
		List<Entry> entries = new ArrayList<>();
		for(int c = 0; c < wave.length; c++)
		{
			entries.add(new Entry((float)c / 44100f, wave[c]));
		}
		LineDataSet dataSet = new LineDataSet(entries, "Sawtooth Wave");
		dataSet.setDrawCircles(false);
		dataSet.setColor(Color.BLACK);
		LineData lineData = new LineData(dataSet);
		chart.setData(lineData);
		chart.invalidate();
	}

	private void drawRawWave()
	{
		float[] wave = WaveFactory.getInstance().getWaveFromResource(R.raw.sound_drum_beat, this);
		LineChart chart = findViewById(R.id.wav);
		List<Entry> entries = new ArrayList<>();
		for(int c = 0; c < wave.length; c++)
		{
			entries.add(new Entry((float)c / 44100f, wave[c]));
		}
		LineDataSet dataSet = new LineDataSet(entries, "Wave File");
		dataSet.setDrawCircles(false);
		dataSet.setColor(Color.BLACK);
		LineData lineData = new LineData(dataSet);
		chart.setData(lineData);
		chart.invalidate();
	}
}
