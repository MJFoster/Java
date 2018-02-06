import java.util.ArrayList;

/**
 * Class to manage a list of Song objects.
 * Methods included in this class allow the Song
 * objects to be iterated through as a group of songs. 
 *  
 * @author MaryJo Foster
 * @version Spring 2016
 */
public class PlayList {

	private String name;                // name of PlayList
	private int playCount;              // # of times this PlayList has played
	private ArrayList<Song> songList;  // Contains all songs for PlayList


	/**
	 * Contructor for a new PlayList, initializes instance variables
	 * as well as an empty list.
	 * 
	 * @param name : name of PlayList
	 */
	public PlayList (String name)
	{
		this.name = name;
		this.playCount = 0;
		this.songList = new ArrayList<Song>();
	}


	/**
	 * Retrieves the name of the PlayList
	 * 
	 * @return String : name of the PlayList
	 */
	public String getName() 
	{
		return this.name;
	}


	/**
	 * Sets the name of the PlayList
	 * 
	 * @param name : New name of PlayList
	 */
	public void setName(String name)
	{
		this.name = name;
	}


	/** 
	 * Retrieves the total number of times the PlayList has been played.
	 * 
	 * @return int : total number of times PlayList has been played.
	 */
	public int getPlayCount()
	{
		return this.playCount;
	}


	/**  
	 * Method adds a Song to the PlayList
	 * 
	 * @param s : A new Song to add to the list
	 */
	public void addSong(Song s)
	{
		songList.add(s);
	}


	/** 
	 * Method removes a song at a specified position
	 * 
	 * @param id : position of Song to be removed.
	 */
	public void removeSong(int id)
	{
		if( (id >= 0)  &&  (id < songList.size()) )
			songList.remove(id);
	}


	/** 
	 * Method returns the size of the PlayList
	 *  
	 * @return int : Number of Songs in the PlayList
	 */
	public int getNumSongs()
	{
		return songList.size();
	}


	/**
	 * Retrieves the PlayList itself
	 * 
	 * @return ArrayList : The entire PlayList of Songs
	 */
	public ArrayList<Song> getSongList()
	{
		return songList;
	}


	/** Use a for-each loop to increment the playCount
	 * Method to play all the songs in the PlayList.
	 * 
	 */
	public void playAll()
	{
		for(Song s : songList)
		{
			s.play();
			s.setNumOfPlays(s.getNumOfPlays() + 1);
			this.playCount = getPlayCount() + 1;
		}
	}


	/** 
	 * Method to override the default toString() method
	 * with a String built to display each song in the PlayList.
	 * 
	 * @return String - A string representing current state of this PlayList
	 */
	public String toString()
	{
		StringBuilder playListStr = new StringBuilder();
		playListStr.append("------------------\n");
		playListStr.append("Test List" + " (" + getNumSongs() + " songs)\n");
		playListStr.append("------------------\n");

		// If list has Songs, append each Song to playListStr
		if (getNumSongs() > 0)
		{
			for(int i = 0; i < getNumSongs(); i++) 
			{
				playListStr.append("(" + i + ") ");
				playListStr.append(songList.get(i).toString() + "\n");
			};
		}
		else  // list is empty
		{
			playListStr.append("There are no songs.\n");
		}

		playListStr.append("------------------\n");
		return playListStr.toString();
	}
}
