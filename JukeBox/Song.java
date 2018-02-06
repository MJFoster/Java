import java.text.DecimalFormat;

/**
 * Class defines a Song object and all the methods needed 
 * to associate a name, genre, play time, the number of times
 * a Song has been played, and a toString() method to 
 * report state of an individual Song.
 *  
 * @author MaryJo
 * @version Spring 2016
 */
public class Song {

	private String title, artist, fileName;
	private int playTime, numOfPlays;  // length of Song & number of times Song has played

	public static void main(String[] args)
	{
		// Default version of Song object to be tested.
		Song defaultSong = new Song();

		// Test DEFAULT construction of Song and the toString() 
		// on this object.
		String result = defaultSong.toString();
		String expected = "";
		if(!result.equals(expected)) {
			failed("defaultSong.toString()", expected, result);
		} else {
			passed("defaultSong.toString()", expected, result);
		};

		// Test getNumOfPLays() method.
		// NOTE:  setNumOfPlays() tested in constructor.
		if (defaultSong.getNumOfPlays() == defaultSong.numOfPlays)
		{
			passed("defaultSong.getNumOfPlays()", 
					Integer.toString(defaultSong.numOfPlays),
					Integer.toString(defaultSong.getNumOfPlays()));
		}
		else
		{
			failed("defaultSong.getNumOfPlays()", 
					Integer.toString(defaultSong.numOfPlays), 
					Integer.toString(defaultSong.getNumOfPlays()));
		}
	}


	/**
	 * Retrieves the number of times Song has played.
	 * 
	 * @return int numOfPlays : number of times Song played.
	 */
	public int getNumOfPlays()
	{
		return this.numOfPlays;
	}


	/**
	 * Method sets the numOfPlays counter variable
	 * to the value passed in.
	 * 
	 * @param num : number of times song has been played.
	 */
	public void setNumOfPlays (int num)
	{
		this.numOfPlays = num;
	}


	/**
	 * Constructor for a Song object
	 * Uses method above 'setNumOfPlays()' to initialize to 0.
	 * 
	 * @param title - Song title
	 * @param artist - Song artist
	 * @param playTime - Number of seconds of Song length
	 * @param fileName - pathname for the Song '.wav' file.
	 */
	public Song (String title, String artist, int playTime, String fileName)
	{
		this.title = title;
		this.artist = artist;
		this.playTime = playTime;
		this.fileName = fileName;
		setNumOfPlays(0);
	}

	/**
	 * Default constructor for a basic Song object, no parameters
	 */
	public Song ()
	{
		this.title = "";
		this.artist = "";
		this.playTime = 0;
		this.fileName = "";
		setNumOfPlays(0);
	}


	/**
	 * Retrieve title for this Song
	 * 
	 * @return String : title of Song
	 */
	public String getTitle ()
	{
		return this.title;
	}


	/**
	 * Update the title of this Song
	 * 
	 * @param title : title for this Song
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}


	/**
	 * Retrieve the artist's name for this Song
	 * @return String : artist of Song
	 */
	public String getArtist ()
	{
		return this.artist;
	}

	/**
	 * Update the artist's name for this Song
	 * 
	 * @param artist : artist performing this Song
	 */
	public void setArtist(String artist)
	{
		this.artist = artist;
	}

	/**
	 * Retrieve the fileName for this Song
	 * 
	 * @return String : full pathname for song's sound clip
	 */
	public String getFileName()
	{
		return this.fileName;
	}


	/**
	 * Update the fileName of the '.wav' file for this Song
	 * 
	 * @param fileName : The new filename for this Song
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}


	/**
	 * Retrieve the playTime for this Song
	 * 
	 * @return - int : Song length in seconds
	 */
	public int getPlayTime ()
	{
		return this.playTime;
	}


	/**
	 * Update the playTime for this Song
	 * 
	 * @param time : Song length in seconds
	 */
	public void setPlayTime(int time)
	{
		this.playTime = time;
	}



	/**
	 * Play the audio clip of this Song
	 * and increments the numOfPlays variable.
	 */
	public void play()
	{
		setNumOfPlays(numOfPlays++);
		TimedAudioClip clip = new TimedAudioClip(title, fileName, playTime);
		clip.playAndWait();
	}


	/**
	 * Method to override toString() by returning current state of Song
	 * object and its instance variables.
	 * 
	 * @return String - A string representing current state of this Song
	 */
	public String toString()
	{
		if(fileName != "")
		{
			DecimalFormat formatter = new DecimalFormat("00");
			String time = formatter.format(playTime / 60) 
					+ ":"
					+ formatter.format(playTime % 60);
			return String.format("%-20s %-25s %-25s %-20s", title, artist, time, fileName );
		}
		else 
		{
			return "";
		}
	}


	/**
	 * Static method to report FAILED tests from inside class.
	 * 
	 * @param methodName - Name of method being tested.
	 * @param expected - Expected result String.
	 * @param result - Actual result String.
	 */
	public static void failed(String methodName, String expected, String result)
	{
		System.out.println("FAILED: " + methodName +  "\nExpected: " + expected + "\nActual:   " + result);
	}


	/**
	 * Static method to report PASSED tests from inside class.
	 * 
	 * @param methodName - Name of method being tested.
	 * @param expected - Expected result String.
	 * @param result - Actual result String.
	 */
	public static void passed(String methodName, String expected, String result)
	{
		System.out.println("PASSED: " + methodName +  "\nExpected: " + expected + "\nActual:   " + result);
	};
}
