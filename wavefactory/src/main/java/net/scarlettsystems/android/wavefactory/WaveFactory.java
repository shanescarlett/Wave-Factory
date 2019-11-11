package net.scarlettsystems.android.wavefactory;

import android.media.AudioFormat;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@SuppressWarnings("unused, WeakerAccess")
public class WaveFactory
{
	@Retention(SOURCE)
	@IntDef({ENCODING_PCM_8BIT, ENCODING_PCM_16BIT, ENCODING_PCM_FLOAT})
	public @interface Format {}
	public static final int ENCODING_PCM_8BIT = AudioFormat.ENCODING_PCM_8BIT;
	public static final int ENCODING_PCM_16BIT = AudioFormat.ENCODING_PCM_16BIT;
	public static final int ENCODING_PCM_FLOAT = 4;

	private interface SampleMapFunction
	{
		float map(int index);
	}

	/**
	 * Generate a sine wave of specified frequency, duration, and sample rate, with a fade-in and
	 * fade-out ramp of specified fraction at the start and end.
	 *
	 * @param frequency frequency of the waveform in Hz
	 * @param duration duration of the waveform in seconds
	 * @param sampleRate sample rate of the waveform in Hz
	 * @param ramp fraction of the waveform to ramp (0.0~0.5 range)
	 * @return 16-bit PCM array of the generated waveform
	 */
	public static byte[] getSineWavePCM16(final float frequency, float duration, final int sampleRate, float ramp)
	{
		validateInputs(frequency, duration, sampleRate, ramp);
		int numSamples = (int)Math.floor(duration * sampleRate);
		byte waveBytes[] = new byte[2 * numSamples];
		SampleMapFunction mapFunction = new SampleMapFunction()
		{
			@Override
			public float map(int index)
			{
				return (float)Math.sin(frequency * 2 * Math.PI * index / (sampleRate));
			}
		};
		generateWavePCM16(waveBytes, mapFunction, ramp);
		return waveBytes;
	}

	/**
	 * Generate a sine wave of specified frequency, duration, and sample rate, with a fade-in and
	 * fade-out ramp of specified fraction at the start and end.
	 *
	 * @param frequency frequency of the waveform in Hz
	 * @param duration duration of the waveform in seconds
	 * @param sampleRate sample rate of the waveform in Hz
	 * @param ramp fraction of the waveform to ramp (0.0~0.5 range)
	 * @return 32-bit float PCM array of the generated waveform
	 */
	public static float[] getSineWavePCMFloat(final float frequency, float duration, final int sampleRate, float ramp)
	{
		validateInputs(frequency, duration, sampleRate, ramp);
		int numSamples = (int)Math.floor(duration * sampleRate);
		float waveFloats[] = new float[numSamples];
		SampleMapFunction mapFunction = new SampleMapFunction()
		{
			@Override
			public float map(int index)
			{
				return (float)Math.sin(frequency * 2 * Math.PI * index / (sampleRate));
			}
		};
		generateWavePCMFloat(waveFloats, mapFunction, ramp);
		return waveFloats;
	}

	/**
	 * Generate a square wave of specified frequency, duration, and sample rate, with a fade-in and
	 * fade-out ramp of specified fraction at the start and end.
	 *
	 * @param frequency frequency of the waveform in Hz
	 * @param duration duration of the waveform in seconds
	 * @param sampleRate sample rate of the waveform in Hz
	 * @param ramp fraction of the waveform to ramp (0.0~0.5 range)
	 * @return 16-bit PCM array of the generated waveform
	 */
	public static byte[] getSquareWavePCM16(final float frequency, float duration, final int sampleRate, float ramp)
	{
		validateInputs(frequency, duration, sampleRate, ramp);
		int numSamples = (int)Math.floor(duration * sampleRate);
		byte waveBytes[] = new byte[2 * numSamples];
		SampleMapFunction mapFunction = new SampleMapFunction()
		{
			@Override
			public float map(int index)
			{
				return (float)Math.signum(Math.sin(frequency * 2 * Math.PI * index / (sampleRate)));
			}
		};
		generateWavePCM16(waveBytes, mapFunction, ramp);
		return waveBytes;
	}

	/**
	 * Generate a square wave of specified frequency, duration, and sample rate, with a fade-in and
	 * fade-out ramp of specified fraction at the start and end.
	 *
	 * @param frequency frequency of the waveform in Hz
	 * @param duration duration of the waveform in seconds
	 * @param sampleRate sample rate of the waveform in Hz
	 * @param ramp fraction of the waveform to ramp (0.0~0.5 range)
	 * @return 32-bit float PCM array of the generated waveform
	 */
	public static float[] getSquareWavePCMFloat(final float frequency, float duration, final int sampleRate, float ramp)
	{
		validateInputs(frequency, duration, sampleRate, ramp);
		int numSamples = (int)Math.floor(duration * sampleRate);
		float waveFloats[] = new float[numSamples];
		SampleMapFunction mapFunction = new SampleMapFunction()
		{
			@Override
			public float map(int index)
			{
				return (float)Math.signum(Math.sin(frequency * 2 * Math.PI * index / (sampleRate)));
			}
		};
		generateWavePCMFloat(waveFloats, mapFunction, ramp);
		return waveFloats;
	}

	/**
	 * Generate a triangular wave of specified frequency, duration, and sample rate, with a fade-in and
	 * fade-out ramp of specified fraction at the start and end.
	 *
	 * @param frequency frequency of the waveform in Hz
	 * @param duration duration of the waveform in seconds
	 * @param sampleRate sample rate of the waveform in Hz
	 * @param ramp fraction of the waveform to ramp (0.0~0.5 range)
	 * @return 16-bit PCM array of the generated waveform
	 */
	public static byte[] getTriangularWavePCM16(final float frequency, float duration, final int sampleRate, float ramp)
	{
		validateInputs(frequency, duration, sampleRate, ramp);
		int numSamples = (int)Math.floor(duration * sampleRate);
		byte waveBytes[] = new byte[2 * numSamples];
		SampleMapFunction mapFunction = new SampleMapFunction()
		{
			@Override
			public float map(int index)
			{
				return (float)((2 / Math.PI) * Math.asin(Math.sin(frequency * 2 * Math.PI * index / (sampleRate))));
			}
		};
		generateWavePCM16(waveBytes, mapFunction, ramp);
		return waveBytes;
	}

	/**
	 * Generate a triangular wave of specified frequency, duration, and sample rate, with a fade-in and
	 * fade-out ramp of specified fraction at the start and end.
	 *
	 * @param frequency frequency of the waveform in Hz
	 * @param duration duration of the waveform in seconds
	 * @param sampleRate sample rate of the waveform in Hz
	 * @param ramp fraction of the waveform to ramp (0.0~0.5 range)
	 * @return 32-bit float PCM array of the generated waveform
	 */
	public static float[] getTriangularWavePCMFloat(final float frequency, float duration, final int sampleRate, float ramp)
	{
		validateInputs(frequency, duration, sampleRate, ramp);
		int numSamples = (int)Math.floor(duration * sampleRate);
		float waveFloats[] = new float[numSamples];
		SampleMapFunction mapFunction = new SampleMapFunction()
		{
			@Override
			public float map(int index)
			{
				return (float)((2 / Math.PI) * Math.asin(Math.sin(frequency * 2 * Math.PI * index / (sampleRate))));
			}
		};
		generateWavePCMFloat(waveFloats, mapFunction, ramp);
		return waveFloats;
	}

	/**
	 * Generate a sawtooth wave of specified frequency, duration, and sample rate, with a fade-in and
	 * fade-out ramp of specified fraction at the start and end.
	 *
	 * @param frequency frequency of the waveform in Hz
	 * @param duration duration of the waveform in seconds
	 * @param sampleRate sample rate of the waveform in Hz
	 * @param ramp fraction of the waveform to ramp (0.0~0.5 range)
	 * @return 16-bit PCM array of the generated waveform
	 */
	public static byte[] getSawtoothWavePCM16(final float frequency, float duration, final int sampleRate, float ramp)
	{
		validateInputs(frequency, duration, sampleRate, ramp);
		int numSamples = (int)Math.floor(duration * sampleRate);
		byte waveBytes[] = new byte[2 * numSamples];
		SampleMapFunction mapFunction = new SampleMapFunction()
		{
			@Override
			public float map(int index)
			{
				return (float)((2 / Math.PI) * Math.atan(Math.tan(frequency * Math.PI * index / (sampleRate))));
			}
		};
		generateWavePCM16(waveBytes, mapFunction, ramp);
		return waveBytes;
	}

	/**
	 * Generate a sawtooth wave of specified frequency, duration, and sample rate, with a fade-in and
	 * fade-out ramp of specified fraction at the start and end.
	 *
	 * @param frequency frequency of the waveform in Hz
	 * @param duration duration of the waveform in seconds
	 * @param sampleRate sample rate of the waveform in Hz
	 * @param ramp fraction of the waveform to ramp (0.0~0.5 range)
	 * @return 32-bit float PCM array of the generated waveform
	 */
	public static float[] getSawtoothWavePCMFloat(final float frequency, float duration, final int sampleRate, float ramp)
	{
		validateInputs(frequency, duration, sampleRate, ramp);
		int numSamples = (int)Math.floor(duration * sampleRate);
		float waveFloats[] = new float[numSamples];
		SampleMapFunction mapFunction = new SampleMapFunction()
		{
			@Override
			public float map(int index)
			{
				return (float)((2 / Math.PI) * Math.atan(Math.tan(frequency * Math.PI * index / (sampleRate))));
			}
		};
		generateWavePCMFloat(waveFloats, mapFunction, ramp);
		return waveFloats;
	}

	/**
	 * Generate a sine wave of specified frequency and sample rate, with a minimum duration of
	 * {@code minDuration}, but with additional extra wave cycles to ensure the zero crossover point
	 * coincides with a sample. This is useful when a smooth concatenation to a subsequent wave
	 * (e.g. in a looped tone) is more important than the absolute length.
	 *
	 * @param frequency frequency of the waveform in Hz
	 * @param minDuration minimum duration of the waveform in seconds
	 * @param sampleRate sample rate of the waveform in Hz
	 * @return 16-bit PCM array of the generated waveform
	 */
	public static byte[] getSineToneRoundPCM16(double frequency, double minDuration, int sampleRate)
	{
		return WaveLoader.floatToPcm(getSineToneRoundPCMFloat(frequency, minDuration, sampleRate));
	}

	/**
	 * Generate a sine wave of specified frequency and sample rate, with a minimum duration of
	 * {@code minDuration}, but with additional extra wave cycles to ensure the zero crossover point
	 * coincides with a sample. This is useful when a smooth concatenation to a subsequent wave
	 * (e.g. in a looped tone) is more important than the absolute length.
	 *
	 * @param frequency frequency of the waveform in Hz
	 * @param minDuration minimum duration of the waveform in seconds
	 * @param sampleRate sample rate of the waveform in Hz
	 * @return 32-bit float PCM array of the generated waveform
	 */
	public static float[] getSineToneRoundPCMFloat(double frequency, double minDuration, int sampleRate)
	{
		//Buffer has extra space for 100 extra cycles to detect proper zero crossover
		int minDurationSampleCount = (int) (Math.floor(minDuration * sampleRate));
		int numSamples = minDurationSampleCount * 2;
		int firstCrossoverIndex = 0;
		int properCrossoverIndex = 0;
		float sample[] = new float[numSamples];

		float prevValue = 0;
		for(int c = minDurationSampleCount; c < numSamples; c++)
		{
			float value = (float)Math.sin(frequency * 2 * Math.PI * c / (sampleRate));
			//Find first point where wave crosses from negative to zero (a cycle is finished).
			//This index is kept just in case a minimal crossover point is not found.
			if(firstCrossoverIndex == 0 && prevValue < 0 && value >= 0)
			{
				firstCrossoverIndex = c;
			}
			//Check for a zero crossover point where the end of the wave coincides with the sample bins,
			//or is very close to it.
			if(prevValue < 0 && value >= 0 && value <= 0.005)
			{
				properCrossoverIndex = c;
				break;
			}
			prevValue = value;
		}
		int zeroCrossover = Math.max(firstCrossoverIndex, properCrossoverIndex);
		float output[] = new float[zeroCrossover];

		//Generate Waveform
		for (int c = 0; c < zeroCrossover; c++)
		{
			output[c] = (float)Math.sin(frequency * 2 * Math.PI * c / (sampleRate));
		}
		return output;
	}

	/**
	 * Generate a square wave of specified frequency and sample rate, with a minimum duration of
	 * {@code minDuration}, but with additional extra wave cycles to ensure the zero crossover point
	 * coincides with a sample. This is useful when a smooth concatenation to a subsequent wave
	 * (e.g. in a looped tone) is more important than the absolute length.
	 *
	 * @param frequency frequency of the waveform in Hz
	 * @param minDuration minimum duration of the waveform in seconds
	 * @param sampleRate sample rate of the waveform in Hz
	 * @return 16-bit PCM array of the generated waveform
	 */
	public static byte[] getSquareToneRoundPCM16(double frequency, double minDuration, int sampleRate)
	{
		return getSquareToneRoundPCM16(frequency, minDuration, sampleRate, 1.0f);
	}

	/**
	 * Generate a square wave of specified frequency and sample rate, with a minimum duration of
	 * {@code minDuration}, but with additional extra wave cycles to ensure the zero crossover point
	 * coincides with a sample. This is useful when a smooth concatenation to a subsequent wave
	 * (e.g. in a looped tone) is more important than the absolute length.
	 *
	 * @param frequency frequency of the waveform in Hz
	 * @param minDuration minimum duration of the waveform in seconds
	 * @param sampleRate sample rate of the waveform in Hz
	 * @param amplitude magnitude from 0 to 1 of the generated waveform
	 * @return 16-bit PCM array of the generated waveform
	 */
	public static byte[] getSquareToneRoundPCM16(double frequency, double minDuration, int sampleRate, float amplitude)
	{
		return WaveLoader.floatToPcm(getSquareToneRoundPCMFloat(frequency, minDuration, sampleRate, amplitude));
	}

	/**
	 * Generate a square wave of specified frequency and sample rate, with a minimum duration of
	 * {@code minDuration}, but with additional extra wave cycles to ensure the zero crossover point
	 * coincides with a sample. This is useful when a smooth concatenation to a subsequent wave
	 * (e.g. in a looped tone) is more important than the absolute length.
	 *
	 * @param frequency frequency of the waveform in Hz
	 * @param minDuration minimum duration of the waveform in seconds
	 * @param sampleRate sample rate of the waveform in Hz
	 * @param amplitude magnitude from 0 to 1 of the generated waveform
	 * @return 32-bit float PCM array of the generated waveform
	 */
	public static float[] getSquareToneRoundPCMFloat(double frequency, double minDuration, int sampleRate, float amplitude)
	{
		//Amplitude sanity checks
		if(amplitude < 0){amplitude = 0;}
		if(amplitude > 1){amplitude = 1;}
		//Buffer has extra space for 100 extra cycles to detect proper zero crossover
		int minDurationSampleCount = (int) (Math.floor(minDuration * sampleRate));
		int numSamples = minDurationSampleCount * 2;
		int firstCrossoverIndex = 0;
		int properCrossoverIndex = 0;
		float sample[] = new float[numSamples];

		float prevValue = 0;
		for(int c = minDurationSampleCount; c < numSamples; c++)
		{
			float value = (float)Math.sin(frequency * 2 * Math.PI * c / (sampleRate));
			if(value >= 0){value = amplitude;}
			else{value = -amplitude;}
			//Find first point where wave crosses from negative to zero (a cycle is finished).
			//This index is kept just in case a minimal crossover point is not found.
			if(firstCrossoverIndex == 0 && prevValue < 0 && value >= 0)
			{
				firstCrossoverIndex = c;
			}
			prevValue = value;
		}
		float output[] = new float[firstCrossoverIndex];

		//Generate Waveform
		for (int c = 0; c < firstCrossoverIndex; c++)
		{
			float value = (float)Math.sin(frequency * 2 * Math.PI * c / (sampleRate));
			if(value >= 0){value = amplitude;}
			else{value = -amplitude;}
			output[c] = value;
		}
		return output;
	}

	/**
	 * Generate a square wave of specified frequency and sample rate, with a minimum duration of
	 * {@code minDuration}, but with additional extra wave cycles to ensure the zero crossover point
	 * coincides with a sample. This is useful when a smooth concatenation to a subsequent wave
	 * (e.g. in a looped tone) is more important than the absolute length.
	 *
	 * @param frequency frequency of the waveform in Hz
	 * @param minDuration minimum duration of the waveform in seconds
	 * @param sampleRate sample rate of the waveform in Hz
	 * @return 32-bit float PCM array of the generated waveform
	 */
	public static float[] getSquareToneRoundPCMFloat(double frequency, double minDuration, int sampleRate)
	{
		return getSquareToneRoundPCMFloat(frequency, minDuration, sampleRate, 1.0f);
	}

	/**
	 * Generate a block of silence of the specified duration and sampling rate
	 *
	 * @param duration duration in seconds
	 * @param sampleRate sample rate in Hz
	 * @return 16-bit PCM array of silence
	 */
	public static byte[] getSilencePCM16(float duration, int sampleRate)
	{
		int numSamples = (int) (Math.floor(duration * sampleRate));
		byte generatedSnd[] = new byte[2 * numSamples];
		int idx = 0;
		for (int i = 0; i < numSamples; ++i)
		{
			generatedSnd[idx++] = 0;
			generatedSnd[idx++] = 0;
		}
		return generatedSnd;
	}

	/**
	 * Generate a block of silence of the specified duration and sampling rate
	 *
	 * @param duration duration in seconds
	 * @param sampleRate sample rate in Hz
	 * @return 32-bit float PCM array of silence
	 */
	public static float[] getSilencePCMFloat(float duration, int sampleRate)
	{
		int numSamples = (int) (Math.floor(duration * sampleRate));
		float generatedSnd[] = new float[numSamples];
		int idx = 0;
		for (int i = 0; i < numSamples; i++)
		{
			generatedSnd[i] = 0;
		}
		return generatedSnd;
	}

	private static void validateInputs(float frequency, float duration, int sampleRate, float ramp)
	{
		if(frequency <= 0)
		{
			throw new IllegalArgumentException("Frequency must be greater than zero.");
		}
		if(frequency > sampleRate/2)
		{
			throw new IllegalArgumentException("Frequency must be smaller than the nyquist frequency; i.e., half of the sampling rate.");
		}
		if(sampleRate <= 0)
		{
			throw new IllegalArgumentException("Sampling rate must be greater than zero.");
		}
		if(ramp < 0 || ramp > 0.5)
		{
			throw new IllegalArgumentException("Ramp must be a positive fraction between 0 and 0.5.");
		}
	}

	private static void generateWavePCM16(byte[] output, SampleMapFunction mapFunction, float ramp)
	{
		int numSamples = output.length / 2;
		int idx = 0;
		int i;

		int rampSamples = Math.round((float)numSamples * ramp);                                    // Amplitude ramp as a percent of sample count

		//Apply Fade In
		for (i = 0; i < rampSamples; ++i)
		{
			double dVal = mapFunction.map(i);
			// Ramp up to maximum
			final short val = (short) ((dVal * Short.MAX_VALUE * i / rampSamples));
			// in 16 bit wav PCM, first byte is the low order byte
			output[idx++] = (byte) (val & 0x00ff);
			output[idx++] = (byte) ((val & 0xff00) >>> 8);
		}


		for (; i < numSamples - rampSamples; ++i)
		{
			double dVal = mapFunction.map(i);
			// scale to maximum amplitude
			final short val = (short) ((dVal * Short.MAX_VALUE));
			// in 16 bit wav PCM, first byte is the low order byte
			output[idx++] = (byte) (val & 0x00ff);
			output[idx++] = (byte) ((val & 0xff00) >>> 8);
		}

		//Apply Fade Out
		for (; i < numSamples; ++i)
		{                               // Ramp amplitude down
			double dVal = mapFunction.map(i);
			// Ramp down to zero
			final short val = (short) ((dVal * Short.MAX_VALUE * (numSamples - i) / rampSamples));
			// in 16 bit wav PCM, first byte is the low order byte
			output[idx++] = (byte) (val & 0x00ff);
			output[idx++] = (byte) ((val & 0xff00) >>> 8);
		}
	}

	private static void generateWavePCMFloat(float[] output, SampleMapFunction mapFunction, float ramp)
	{
		int numSamples = output.length;
		int idx = 0;
		int i;

		int rampSamples = Math.round((float)numSamples * ramp);

		for (i = 0; i < rampSamples; i++)
		{
			output[i] = mapFunction.map(i) * (i / rampSamples);
		}
		for (; i < numSamples - rampSamples; i++)
		{
			output[i] = mapFunction.map(i);
		}
		for (; i < numSamples; ++i)
		{
			output[i] = mapFunction.map(i) * ((numSamples - i) / rampSamples);
		}
	}
}
