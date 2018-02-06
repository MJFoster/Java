import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class to manage a list of Song objects.
 * Methods included in this class allow the Song
 * objects to be iterated through as a group of songs. 
 *  
 * @author MaryJo Foster
 * @version Spring 2016
 */
public class PlayList implements PlayListInterface {

	private String name;                // name of PlayList
	private int playCount;              // # of times this PlayList has played
	private ArrayList<Song> songList;  // Contains all songs for PlayList


	/**
	 * Constructor for a new PlayList, initializes instance variables
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
	 * Creates a PlayList from the song data in the file parameter.  
	 * 
	 * @param dataFile  The name of the file containing the song data.
	 */
	public PlayList(File dataFile) {
		this.playCount = 0;
		this.songList = new ArrayList<Song>();
		
        try {
            Scanner scan = new Scanner(dataFile);
            this.name = scan.nextLine().trim();
            while(scan.hasNextLine()) {
                String title = scan.nextLine().trim();
                String artist = scan.nextLine().trim();
                int playtime = Integer.parseInt(scan.nextLine().trim());
                String soundFile = scan.nextLine().trim();
                Song song = new Song(title, artist, playtime, soundFile);
                this.addSong(song);
            };
            scan.close();
 
        } catch (FileNotFoundException e) {
            System.err.println("Failed to load playlist. " + e.getMessage());
        }
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


	/**
	 * Calculate total play list time in seconds
	 * 
	 * @return total number of seconds to run play list
	 */
	@Override
	public int getTotalPlayTime() {
		int totalPlayTime = 0;
		
		for(int i = 0; i < songList.size(); i++) {
			totalPlayTime += songList.get(i).getPlayTime();
		}
		return totalPlayTime;
	}


	/**
	 * Method to convert the play list into an array of songs.
	 * @return an array of Songs
	 * 
	 */
	@Override
	public Song[] getSongArray() {
		Song[] songArray = new Song[songList.size()];
		for(int i = 0; i < songArray.length; i++) {
			songArray[i] = songList.get(i);
		}

		return songArray;
	}


	/**
	 * Moves the current node in the play list forward one position,
	 * but wrap to the end of the list if currently at beginning. 
	 * 
	 * @param index of the current node.
	 * @return  updated index of current node.
	 */
	@Override
	public int moveUp(int index) {
		int newIndex = 0;
		Song tempSong;
		
		if(index <= 0)
			newIndex = songList.size() -1;
		else
			newIndex = index - 1;
		
		//	swap from index -> newIndex
		tempSong = songList.get(newIndex);
		songList.set(newIndex, songList.get(index));
		songList.set(index, tempSong);
		
		return newIndex;
	}


	/**
	 * Moves the current node in the play list back one position,
	 * but wrap to the beginning of the list if currently at end.
	 * 
	 * @param index of the current node.
	 * @return updated index of current node.
	 */
	@Override
	public int moveDown(int index) {
		int newIndex = 0;
		Song tempSong;
		
		if(index >= songList.size()-1)
			newIndex = 0;
		else
			newIndex = index + 1;
		
		//	swap from index -> newIndex
		tempSong = songList.get(newIndex);
		songList.set(newIndex, songList.get(index));
		songList.set(index, tempSong);
		
		return newIndex;
	}

	
	/**
	 * 
	 * @return 2D array containing all Songs in the songList, filling
	 * any unused elements with Songs from beginning of ArrayList so
	 * that the 2D array is full.
	 */
	@Override
	public Song[][] getMusicSquare() {
		// Create and load a square grid of Song's.
		// Add all songs to songSquare array

		int dim = (int) Math.ceil(Math.sqrt(songList.size()));

		Song[][] songSquare = new Song[dim][dim];
		Song[] songArray = getSongArray();

		for(int i = 0; i < dim; i++) {
			for(int j = 0; j < dim; j++) {
				songSquare[i][j] = songArray[((i * dim) + j) % songList.size()];
			}
		}

		return songSquare;

	}
		
}
