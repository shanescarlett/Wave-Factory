package net.scarlettsystems.android.wavefactory;

class WaveFileParser
{
	private char[] formatChars = {'f','m','t',' '};
	private char[] dataChars = {'d','a','t','a'};
	private long mSampleRate = 0;
	private long mDataLength = 0;
	private int mDataStartIndex = 0;

	WaveFileParser(byte[] bytes)
	{
		for(int c = 0; c < bytes.length - dataChars.length; c++)
		{
			if(bytes[c] == formatChars[0]
					&& bytes[c+1] == formatChars[1]
					&& bytes[c+2] == formatChars[2]
					&& bytes[c+3] == formatChars[3])
			{
				mSampleRate = getUInt32(bytes[c+16], bytes[c+17], bytes[c+18], bytes[c+19]);
			}

			if(bytes[c] == dataChars[0]
					&& bytes[c+1] == dataChars[1]
					&& bytes[c+2] == dataChars[2]
					&& bytes[c+3] == dataChars[3])
			{
				mDataStartIndex = c + 8;
				mDataLength = getUInt32(bytes[c+4],bytes[c+5],bytes[c+6],bytes[c+7]);
			}
		}
	}

	private long getUInt32(byte smallest, byte small, byte big, byte biggest)
	{
		long value = smallest & 0xFF;
		value |= (small << 8) & 0xFFFF;
		value |= (big << 16) & 0xFFFFFF;
		value |= (biggest << 24);
		return value;
	}

	long getSampleRate()
	{
		return mSampleRate;
	}

	long getDataLength()
	{
		return mDataLength;
	}

	int getDataStartIndex()
	{
		return mDataStartIndex;
	}
}
