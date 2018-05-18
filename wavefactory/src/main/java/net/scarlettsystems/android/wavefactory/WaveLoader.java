package net.scarlettsystems.android.wavefactory;

import android.content.Context;
import android.support.annotation.RawRes;
import android.util.SparseArray;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

@SuppressWarnings("unused, WeakerAccess")
public class WaveLoader
{
	private static WaveLoader INSTANCE = null;
	private SparseArray<float[]> mLoadedSounds;

	private WaveLoader()
	{
		mLoadedSounds = new SparseArray<>();
	}

	public static WaveLoader getInstance()
	{
		if(INSTANCE == null)
		{
			INSTANCE = new WaveLoader();
		}
		return INSTANCE;
	}


	/**
	 * Mix a sound into another sound via addition and hyperbolic tangent compression.
	 * If the full length of the sample cannot fit into the destination at the specified offset,
	 * the sample will be truncated.
	 *
	 * @param sound sound sample to mix
	 * @param destination destination array
	 * @param offset offset index from the start at which to start writing the sound sample
	 */
	public static void mixWaves(float[] sound, float[] destination, int offset)
	{
		int writeAbleSamples = Math.min(sound.length, destination.length - offset);

		for(int c = 0; c < writeAbleSamples; c++)
		{
			destination[c + offset] = (float)Math.tanh(sound[c] + destination[c + offset]);
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

	/**
	 * Convert a PCM byte array into a float array
	 *
	 * @param bytes byte array of PCM audio
	 * @return float array of audio
	 */
	public static float[] pcmToFloat(byte[] bytes)
	{
		return toFloat(toShort(bytes));
	}

	/**
	 * Convert a float audio array to a PCM byte array
	 *
	 * @param floats float array of audio
	 * @return byte array of PCM audio
	 */
	public static byte[] floatToPcm(float[] floats)
	{
		return fromShort(fromFloat(floats));
	}

	private static float[] toFloat(short[] PCMs)
	{
		float[] floaters = new float[PCMs.length];
		for (int i = 0; i < PCMs.length; i++)
		{
			floaters[i] = (float)PCMs[i]/32768.0f;
		}
		return floaters;
	}

	private static short[] toShort(byte[] bytes)
	{
		short[] out = new short[bytes.length / 2];
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
