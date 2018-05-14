package net.scarlettsystems.android.wavefactory;

import android.content.Context;
import android.support.annotation.RawRes;
import android.util.SparseArray;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

@SuppressWarnings("unused")
public class WaveFactory
{
	private static WaveFactory INSTANCE = null;
	private SparseArray<float[]> mLoadedSounds;

	private WaveFactory()
	{
		mLoadedSounds = new SparseArray<>();
	}

	public static WaveFactory getInstance()
	{
		if(INSTANCE == null)
		{
			INSTANCE = new WaveFactory();
		}
		return INSTANCE;
	}

	public static byte[] getSineTone(double frequency, double duration, int sampleRate, float ramp)
	{
		int numSamples = (int) (Math.round(duration * sampleRate));
		double sample[] = new double[numSamples];
		byte generatedSnd[] = new byte[2 * numSamples];
		ramp = Math.min(0.5f, Math.max(0, ramp));

		//Generate Waveform
		for (int i = 0; i < numSamples; ++i)
		{
			sample[i] = Math.sin(frequency * 2 * Math.PI * i / (sampleRate));
		}
		// convert to 16 bit pcm sound array
		// assumes the sample buffer is normalised.
		int idx = 0;
		int i;

		int rampSamples = Math.round((float)numSamples * ramp);                                    // Amplitude ramp as a percent of sample count

		//Apply Fade In
		for (i = 0; i < rampSamples; ++i)
		{
			double dVal = sample[i];
			// Ramp up to maximum
			final short val = (short) ((dVal * Short.MAX_VALUE * i / rampSamples));
			// in 16 bit wav PCM, first byte is the low order byte
			generatedSnd[idx++] = (byte) (val & 0x00ff);
			generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
		}


		for (; i < numSamples - rampSamples; ++i)
		{
			double dVal = sample[i];
			// scale to maximum amplitude
			final short val = (short) ((dVal * Short.MAX_VALUE));
			// in 16 bit wav PCM, first byte is the low order byte
			generatedSnd[idx++] = (byte) (val & 0x00ff);
			generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
		}

		//Apply Fade Out
		for (; i < numSamples; ++i)
		{                               // Ramp amplitude down
			double dVal = sample[i];
			// Ramp down to zero
			final short val = (short) ((dVal * Short.MAX_VALUE * (numSamples - i) / rampSamples));
			// in 16 bit wav PCM, first byte is the low order byte
			generatedSnd[idx++] = (byte) (val & 0x00ff);
			generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
		}
		return generatedSnd;
	}

	public static byte[] getSquareWave(double frequency, double duration, int sampleRate)
	{
		int numSamples = (int) (Math.round(duration * sampleRate));
		double sample[] = new double[numSamples];
		byte generatedSnd[] = new byte[2 * numSamples];

		for (int i = 0; i < numSamples; ++i)
		{
			sample[i] = Math.signum(Math.sin(frequency * 2 * Math.PI * i / (sampleRate)));
		}
		int idx = 0;

		for (int i = 0; i < numSamples; ++i)
		{
			double dVal = sample[i];
			final short val = (short) ((dVal * Short.MAX_VALUE));
			generatedSnd[idx++] = (byte) (val & 0x00ff);
			generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
		}
		return generatedSnd;
	}

	public static byte[] getTriangleWave(double frequency, double duration, int sampleRate)
	{
		int numSamples = (int) (Math.round(duration * sampleRate));
		double sample[] = new double[numSamples];
		byte generatedSnd[] = new byte[2 * numSamples];

		for (int i = 0; i < numSamples; ++i)
		{
			sample[i] = (2 / Math.PI) * Math.asin(Math.sin(frequency * 2 * Math.PI * i / (sampleRate)));
		}
		int idx = 0;

		for (int i = 0; i < numSamples; ++i)
		{
			double dVal = sample[i];
			final short val = (short) ((dVal * Short.MAX_VALUE));
			generatedSnd[idx++] = (byte) (val & 0x00ff);
			generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
		}
		return generatedSnd;
	}

	public static byte[] getSineToneRound(double frequency, double minDuration, int sampleRate)
	{
		//Buffer has extra space for 100 extra cycles to detect proper zero crossover
		int minDurationSampleCount = (int) (Math.round(minDuration * sampleRate));
		int numSamples = minDurationSampleCount * 2;
		int firstCrossoverIndex = 0;
		int properCrossoverIndex = 0;
		double sample[] = new double[numSamples];


		//Generate Waveform
		for (int i = 0; i < numSamples; ++i)
		{
			sample[i] = Math.sin(frequency * 2 * Math.PI * i / (sampleRate));
			if(i >= minDurationSampleCount)
			{
				//Find first point where wave crosses from negative to zero (a cycle is finished).
				//This index is kept just in case a minimal crossover point is not found.
				if(firstCrossoverIndex == 0 && sample[i - 1] < 0 && sample[i] >= 0)
				{
					firstCrossoverIndex = i;
				}
				//Check for a zero crossover point where the end of the wave coincides with the sample bins,
				//or is very close to it.
				if(sample[i - 1] < 0 && sample[i] >= 0 && sample[i] <= 0.005)
				{
					properCrossoverIndex = i;
					break;
				}
			}
		}
		int zeroCrossover = Math.max(firstCrossoverIndex, properCrossoverIndex);
		byte generatedSnd[] = new byte[2 * zeroCrossover];
		int idx = 0;

		for (int i = 0; i < zeroCrossover; ++i)
		{
			double dVal = sample[i];
			// scale to maximum amplitude
			final short val = (short) ((dVal * Short.MAX_VALUE));
			// in 16 bit wav PCM, first byte is the low order byte
			generatedSnd[idx++] = (byte) (val & 0x00ff);
			generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
		}
		return generatedSnd;
	}

	public static byte[] getSilence(double duration, int sampleRate)
	{
		int numSamples = (int) (Math.ceil(duration * sampleRate));
		byte generatedSnd[] = new byte[2 * numSamples];
		int idx = 0;
		for (int i = 0; i < numSamples; ++i)
		{
			generatedSnd[idx++] = 0;
			generatedSnd[idx++] = 0;
		}
		return generatedSnd;
	}

	public static void mixWaves(float[] sound, float[] into, int offset)
	{
		int writeableSamples = Math.min(sound.length, into.length - offset);

		for(int c = 0; c < writeableSamples; c++)
		{
			into[c + offset] = (float)Math.tanh(sound[c] + into[c + offset]);
		}
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public float[] getWaveFromResource(@RawRes int resId, Context context)
	{
		if(mLoadedSounds.indexOfKey(resId)<0)
		{
			float[] result;
			try
			{
				byte[] fileBytes;
				InputStream inStream = context.getResources().openRawResource(resId);
				int length = inStream.available();
				fileBytes = new byte[length];
				inStream.read(fileBytes);
				inStream.close();
				WaveFileParser info = new WaveFileParser(fileBytes);
				int start = info.getDataStartIndex();
				int len = (int)info.getDataLength();
				byte[] dataBytes = Arrays.copyOfRange(fileBytes, start, len);
				result = pcmToFloat(dataBytes);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				result = new float[]{};
			}
			mLoadedSounds.put(resId, result);
		}
		return mLoadedSounds.get(resId);
	}

	public static float[] pcmToFloat(byte[] bytes)
	{
		return toFloat(toShort(bytes));
	}

	public static byte[] floatToPcm(float[] floats)
	{
		return fromShort(fromFloat(floats));
	}

	private static float[] toFloat(short[] pcms)
	{
		float[] floaters = new float[pcms.length];
		for (int i = 0; i < pcms.length; i++)
		{
			floaters[i] = (float)pcms[i]/32768.0f;
		}
		return floaters;
	}

	private static short[] toShort(byte[] bytes)
	{
		short[] out = new short[bytes.length / 2]; // will drop last byte if odd number
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		for (int i = 0; i < out.length; i++)
		{
			out[i] = bb.getShort();
		}
		return out;
	}

	private static short[] fromFloat(float[] floats)
	{
		short[] shorts = new short[floats.length];
		for (int i = 0; i < floats.length; i++)
		{
			shorts[i] = (short)(floats[i] * 32768.0f);
		}
		return shorts;
	}

	private static byte[] fromShort(short[] shorts)
	{
		byte[] bytes = new byte[shorts.length * 2];
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		for(short value : shorts)
		{
			bb.putShort(value);
		}
		return bytes;
	}
}
