import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * GUI to provide all the setup and logic for a song player.
 * 
 * @author MaryJo Foster
 * @version Spring 2016
 */
public class MyTunesGUIPanel extends JPanel {
	// Instance Variables
	private JPanel topPanel, leftPanel, rightPanel, playControlPanel;
	private JLabel songListNameLbl, listStatsLbl, nowPlayingLbl;
	
	// topPanel buttons
	private JButton playWholeListBtn, addSongBtn, delSongBtn;
	
	// rightPanel buttons
	private JButton moveSongUp, moveSongDown, cntrlPlayPrev, cntrlPlayNext, cntrlPlayCurrent;

	// Declare matching 2D arrays for button grid 
	private Song[][] songSquare;
	private JButton[][] songSquareBtns;
	
	// Dimension variable for grid. Calculated in methods.
	private int squareDim;
	
	// MyTunesPlayList object ... "model" 
	private MyTunesPlayList songList;
	
	// GUI reference to the PlayList  ... "view"
	private JList<Song> songJList;

	final int MILLS_PER_SECOND = 1000;
	boolean inPlay = false;
	
	// Declare Timer object
	Timer songTimer;
	
	// Declare PlayButtonListener to be shared by
	// ALL play buttons + music square buttons
	PlayButtonListener playListener;
	
	
	/**
	 * Constructor for GUI Panel
	 * Add all of the components to the JPanel.
	 */
	public MyTunesGUIPanel() {		
		// Instantiate songList from input data file using derived constructor.
		songList = new MyTunesPlayList(new File("sounds/playlist.txt"));
		
		// Instantiate Labels
		nowPlayingLbl = new JLabel();
		playWholeListBtn = new JButton();
		
		// Instantiate main panels
		topPanel = new JPanel();
		leftPanel = new JPanel();
		rightPanel = new JPanel();
		
		
		// Set 'this' layout to be BorderLayout for top, left, and right sub-panels
		this.setLayout(new BorderLayout());
		
		initTopPanel(topPanel); 
		initLeftPanel(leftPanel);
		initRightPanel(rightPanel);
		
		// Instantiate timer for song play with a 1 second delay
		songTimer = new Timer(MILLS_PER_SECOND, new PlayButtonListener());
	}

	
	// 
	//******************* GUI PANELS & JLIST (VIEW) *******************//
	//

	
	/**
	 * Method to initialize all components of GUI topPanel
	 * 
	 * @param top panel of GUI
	 */
	public void initTopPanel(JPanel topPanel) {
		listStatsLbl = new JLabel();
		
		// Instantiate sub-Panels
		JPanel topPanel1 = new JPanel();
		JPanel topPanel2 = new JPanel();

		// Set icon on play button & setup listener.
		playWholeListBtn.setIcon(new ImageIcon("images/play-32.gif"));
		playListener = new PlayButtonListener();
		playWholeListBtn.addActionListener(playListener);
		
		// Instantiate Add & Del JButtons, & song list label for topPanel
		addSongBtn = new JButton("Add Song");
		delSongBtn = new JButton("Delete Song");
		songListNameLbl = new JLabel(songList.getName());
		
		// Add listener to buttons
		addSongBtn.addActionListener(new ChangeSongListListener());
		delSongBtn.addActionListener(new ChangeSongListListener());
		
		// Set layout for topPanel, sub-Panels use default "Flow" layout.
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

		// Build and add topPanel1
		topPanel1.add(songListNameLbl);
		topPanel1.add(playWholeListBtn);
		topPanel.add(topPanel1);

		updateListStatsLbl();
		
		topPanel2.add(listStatsLbl);
		topPanel2.add(Box.createRigidArea(new Dimension(20,0)));
		topPanel2.add(addSongBtn);
		topPanel2.add(Box.createRigidArea(new Dimension(20,0)));
		topPanel2.add(delSongBtn);
		topPanel.add(topPanel2);
		
		this.add(topPanel, BorderLayout.NORTH);
	}
		
	
	/**
	 * Method to initialize all components of GUI leftPanel
	 * 
	 * @param left side panel of GUI
	 */
	public void initLeftPanel(JPanel leftPanel) {
		// Initialize songSquare, and return dimension.
		int gridDim = initMusicSquare();
		
		// Set layout of leftPanel
		leftPanel.setLayout(new GridLayout(gridDim, gridDim));
		
		// Add completed leftPanel to the main panel
		this.add(leftPanel, BorderLayout.WEST);
	}
	
	
	/**
	 * Method to initialize all component of GUI rightPanel
	 * 
	 * @param right side panel of GUI
	 */
	public void initRightPanel(JPanel rightPanel) {
		// Set layout of rightPanel to allow 
		rightPanel.setLayout(new BorderLayout());
		initJList();
		
		// moveSongUp & moveSongDown buttons for songJList, then sync
		initSongJListControlPanel();
		syncSongJList();
		
		// Buttons & Labels for controlling play of songJList
		initPrevNextPanel(); 

		// "CENTER" BorderLayout fills horizontal space, EAST layout does not.
		this.add(rightPanel, BorderLayout.CENTER);
	}
	
	
	/**
	 * Initialize grid panel with newest updated data
	 * @return size of one dimension of the grid
	 */
	public int initMusicSquare() {

		// Initialize 2D Arrays
		songSquare = songList.getMusicSquare();  // Returns 2D Array of Song's in songList
		squareDim = songSquare.length; 
		songSquareBtns = new JButton[squareDim][squareDim];

		for(int row = 0; row < songSquareBtns.length; row++) {
			for(int col = 0; col < songSquareBtns[row].length; col++) {
				songSquareBtns[row][col] = new JButton();

				// Set background color of JButtons based on current # of plays.
				updateSongSquareBtn(row, col);
				
				// Add an ActionListener to each songSquare JButton
				songSquareBtns[row][col].addActionListener(playListener);

				songSquareBtns[row][col].setText(songSquare[row][col].getTitle());
				leftPanel.add(songSquareBtns[row][col]);
			}
		};
		return squareDim;
	}
		
	
	/**
	 * Instantiates JList and control buttons along with their listeners.
	 * Also synchronizes the JList with current songList data.
	 */
	public void initJList() {		
		songJList = new JList<Song>();
		songJList.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		songJList.addListSelectionListener(new SongJListListener());
		
		// Synchronize JList with current songList data
		syncSongJList();
		
		JScrollPane scrollPane = new JScrollPane(songJList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		// Add the scrollPane to rightPanel.
		rightPanel.add(scrollPane, BorderLayout.WEST);
	}
		

	// 
	//******************* CONTROL PANELS (CONTROL) *******************//
	//

	
	/**
	 * Initialize the song list control panel with buttons that
	 * move the selected song up/down in the list.  Song play does not start.
	 */
	public void initSongJListControlPanel() {
		JPanel songJListControlPanel = new JPanel();
		songJListControlPanel.setLayout(new BoxLayout(songJListControlPanel, BoxLayout.Y_AXIS));
		
		// Instantiate listener for both buttons
		SongJListControlListener songListControl = new SongJListControlListener();
		
		moveSongUp = new JButton(new ImageIcon("images/move-up-24.gif"));
		moveSongUp.addActionListener(songListControl);
		moveSongDown = new JButton(new ImageIcon("images/move-down-24.gif"));
		moveSongDown.addActionListener(songListControl);
		
		songJListControlPanel.add(Box.createVerticalGlue());
		songJListControlPanel.add(moveSongUp);
		songJListControlPanel.add(moveSongDown);
		songJListControlPanel.add(Box.createVerticalGlue());
		
		// Add songJListControlPanel to rightPanel.
		rightPanel.add(songJListControlPanel, BorderLayout.CENTER);
	}
	
	
	/**
	 * Initialize the Prev/Next Panel with JButtons, JLabels, and listeners
	 * for playing the previous, next, and current songs.
	 */
	public void initPrevNextPanel() {
		playControlPanel = new JPanel();
		
		playControlPanel.setLayout(new BoxLayout(playControlPanel, BoxLayout.Y_AXIS));
		playControlPanel.setBorder(BorderFactory.createTitledBorder("Currently Playing ..."));
		
		// Place label in it's own sub-panel to center
		// Update with current song title
		JPanel nowPlayingPanel = new JPanel();
		updateNowPlayingLbl();  
		nowPlayingPanel.add(nowPlayingLbl);
		playControlPanel.add(nowPlayingPanel);

		cntrlPlayPrev = new JButton(new ImageIcon("images/media-skip-backward-32.gif"));
		cntrlPlayCurrent = new JButton(new ImageIcon("images/play-32.gif"));
		cntrlPlayNext = new JButton(new ImageIcon("images/media-skip-forward-32.gif"));
		
		// Create & attach action listener for previous / next buttons
		PrevNextListener ctrlListener = new PrevNextListener();
		cntrlPlayPrev.addActionListener(ctrlListener);
		cntrlPlayNext.addActionListener(ctrlListener);
		
		// Create & attach action listener to Play button
		cntrlPlayCurrent.addActionListener(playListener);
		
		// Create X_AXIS sub-panel to display directly below nowPlayingLbl
		JPanel playButtonsPanel = new JPanel();
		playButtonsPanel.setLayout(new BoxLayout(playButtonsPanel, BoxLayout.X_AXIS));	
		playButtonsPanel.add(Box.createHorizontalGlue());
		playButtonsPanel.add(cntrlPlayPrev);
		playButtonsPanel.add(cntrlPlayCurrent);
		playButtonsPanel.add(cntrlPlayNext);
		playButtonsPanel.add(Box.createHorizontalGlue());
		
		playControlPanel.add(playButtonsPanel);

		rightPanel.add(playControlPanel, BorderLayout.SOUTH);
	}  
	
	
	// 
	//******************* LISTENERS *******************//
	//
		
	
	/**
	 * List listener for song list to display usage message to play songs.
	 * 
	 * @author MaryJo Foster
	 */
	private class SongJListListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent event)
		{
			//Display usage message to user
			updateNowPlayingLbl();
		}
	}
	
	
	/**
	 * Action Listener for JList control buttons moveSongUp / moveSongDown
	 *  
	 *  @author MaryJo Foster
	 */
	private class SongJListControlListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			int index = songJList.getSelectedIndex();
			int newIndex = 0;  // New current index after move completes.
			
			JButton currEvent = (JButton) event.getSource();

			if(currEvent == moveSongUp) {
				 newIndex = songList.moveUp(index);
			}
				
			else {  // moveSongDown triggered
				 newIndex = songList.moveDown(index);
			}
			
			// Synchronize the JList with newly updated songList data
			syncSongJList();
			songJList.setSelectedIndex(newIndex);
			updateNowPlayingLbl();
		}
	}
                                                                                     
		
	/**
	 * Action Listener for play control buttons prev / next
	 * When action occurs, stop current song (if playing),
	 * then start the prev / next Song on list & update nowPlayingLabel.
	 * 
	 * @author MaryJo Foster
	 *
	 */
	private class PrevNextListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event) {
			
			// Update NowPlayingLbl & play new song.
			updateNowPlayingLbl();
			
			inPlay = true;
			
			int index = songJList.getSelectedIndex();
			
			JButton currEvent = (JButton) event.getSource();
			if(currEvent == cntrlPlayPrev) { // "Previous" button clicked
				songList.playPrev();
				if(index == 0)
					index = songList.getNumSongs();
				songJList.setSelectedIndex(--index); // decrement index 1st, then set Index

			} else { // "Next" button clicked
				if(index == songList.getNumSongs() -1) 
					index = -1;
				songJList.setSelectedIndex(++index);// increment index 1st, then set Index
			};
			songList.play(index);
		}
	}
	
	
	/**
	 * Action listener for all Play buttons & songSquare.
	 * When triggered, plays currently selected song,
	 * updates 'inPlay' boolean depending upon
	 * if playing or stopped.
	 * 
	 * @author MaryJo Foster
	 *
	 */
	private class PlayButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			JButton currEvent = (JButton) event.getSource();
			
			if(currEvent != playWholeListBtn && currEvent != cntrlPlayCurrent  & !songList.isPlaying()) {
				// songSquareBtns clicked, update JList
				for(int row = 0; row < songSquareBtns.length; row++) {
					for(int col = 0; col < songSquareBtns[row].length; col++) {
						if(currEvent == songSquareBtns[row][col]) {
							// Locate index in the JList to set as selected
							Song[] songArray = songList.getSongArray();
							for(int i = 0; i < songArray.length; i++) {
								//	compare against songSquare
								if(songSquare[row][col].getTitle() == songArray[i].getTitle()) {
									songJList.setSelectedIndex(i);
								}
							}
						}
					}
				}
			};
			
			if(!inPlay) { // If PLAY clicked, start song play & Timer & update GUI
				cntrlPlayCurrent.setIcon(new ImageIcon("images/stop-32.gif"));
				playWholeListBtn.setIcon(new ImageIcon("images/stop-32.gif"));
				inPlay = true;
				songList.play(songJList.getSelectedValue());
				// Set INITIAL delay sets delay from initial start to 1st event
				songTimer.setInitialDelay(songJList.getSelectedValue().getPlayTime() * MILLS_PER_SECOND);
				songTimer.setRepeats(false); // Timer fires ONCE.
				songTimer.start();
				System.out.println("Timer Delay Set to: " + songTimer.getDelay());
				
			} else {  // If STOP JButton clicked OR Timer fires, stop song play, timer, & update GUI.
				songList.stop();
				cntrlPlayCurrent.setIcon(new ImageIcon("images/play-32.gif"));
				playWholeListBtn.setIcon(new ImageIcon("images/play-32.gif"));
				inPlay = false;	
				songTimer.stop();
			}
			updateNowPlayingLbl();
			// Update heat map color on grid
			if(songList.isPlaying()) {
				updateSongSquareBtn(songList.getPlaying().getTitle());
			}
		}
	}
	
	
	/**
	 *  Action listener for add / delete song buttons.
	 *  Calls either helper method, "addNewSong()" or "deleteSong()",
	 *  based on which button was clicked.
	 *  
	 *  Listener also synchronizes with JList after change completes.
	 *  @author MaryJo Foster
	 *
	 */
	private class ChangeSongListListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			JButton currEvent = (JButton) event.getSource();
			int index = 0;
			
			if(currEvent == addSongBtn) {  // addSongBtn clicked
				addNewSong();
			} else {   // delSongBtn clicked
				deleteSong(songJList.getSelectedIndex());
			}
			
			// Synchronize JList & PlayList stats label
			syncSongJList();
			updateListStatsLbl();
		}
	}
	
	
	
	// 
	//******************* HELPER METHODS *******************//
	//
	
	
	/**
	 * Synchronized the current songList data with the JList
	 * 
	 */
	public void syncSongJList() {
		Song[] songs = songList.getSongArray();
		
		songJList.setListData(songs);	
		
		// Set currently selected song to top of list
		songJList.setSelectedIndex(0);  // Initialize selection to top of list
	}
	
		
	/**
	 * Add new song to PlayList from user input data
	 * 
	 */
	public void addNewSong() {
		final String SONG_FILE_DIRECTORY = "./sounds";
		
		File songFile;  // File object to locate for song

		int time = 0;
		int result;    // shared variable for return values from JOptionPanes & JFileChooser

		JPanel addNewSongPanel;
		JTextField inputTitle, inputArtist, inputTime;

		// Instantiate JFilechooser, it's root folder & selection mode to display.
		JFileChooser fileChooser = new JFileChooser(SONG_FILE_DIRECTORY);  // Set root of where JFileChooser will display files to choose
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		result = fileChooser.showOpenDialog(null);

		if(result == JFileChooser.APPROVE_OPTION) {  // If user chose a file ...
			
			songFile = fileChooser.getSelectedFile();

			if(songFile.exists()) {   // If file available, prompt user for remaining Song fields
				inputTitle = new JTextField(25);
				inputArtist = new JTextField(25);
				inputTime = new JTextField(25);

				// Build dialogue box & input fields
				addNewSongPanel = new JPanel();
				addNewSongPanel.setLayout(new BoxLayout(addNewSongPanel, BoxLayout.Y_AXIS));
				addNewSongPanel.add(new JLabel("Song Title: "));
				addNewSongPanel.add(inputTitle);
				addNewSongPanel.add(new JLabel("Artist Name: "));
				addNewSongPanel.add(inputArtist);
				addNewSongPanel.add(new JLabel("Play Time (sec): "));
				addNewSongPanel.add(inputTime);
				addNewSongPanel.add(new JLabel("Song Filename: " + songFile.getName()));

				// Display dialogue box & get user confirmation
				result = JOptionPane.showConfirmDialog(null,  // Use default frame
						addNewSongPanel,					  // Contents of JOption Pane ... a Jpanel here.
						"Add New Song",						  // Title of Pane
						JOptionPane.OK_CANCEL_OPTION,		  // Included buttons
						JOptionPane.PLAIN_MESSAGE);			  // Message type

				if(result == JOptionPane.OK_OPTION) {
					// get data from input fields
					String title = inputTitle.getText();
					String artist = inputArtist.getText();
					
					// If time entered is within bounds and legitimate, then and only then, add song!
					try {
						time = Integer.parseInt(inputTime.getText());
						// All details completed now, add song to songList!
						songList.addSong(new Song(title, artist, time, songFile.getAbsolutePath()));	
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, 
								"Oops, forget something? Can't play your song without legitimate time, try again!");
					}
				} else {   // No confirmation from user 
					JOptionPane.showMessageDialog(null,"No song added, try again...");
				};
			}
		} else {  // User closed or cancelled dialogue box
			JOptionPane.showMessageDialog(null, "NO FILE CHOSEN ...");
		}
	}
	
	
	/**
	 * Delete the currently selected Song from the songList.
	 * 
	 * @param index of the Song to delete in the songList
	 * 
	 */
	public void deleteSong(int index) {
		
		int result = 0; // result value from dialogue box
		String songName;

		songName = songJList.getSelectedValue().getTitle();
		String dialogueMsg = "Delete " + songName + "?";
		result = JOptionPane.showConfirmDialog(null,  // Use default frame
				dialogueMsg,						  // Contents of JOption Pane ... a String here.
				"Delete Song",						  // Title of Pane
				JOptionPane.OK_CANCEL_OPTION,		  // Included buttons
				JOptionPane.PLAIN_MESSAGE);			  // Message type
		if(result == JOptionPane.OK_OPTION) {
			songList.removeSong(index);
			dialogueMsg = songName + " deleted!";
			JOptionPane.showMessageDialog(null, dialogueMsg);
		} else if(result == JOptionPane.CANCEL_OPTION  || result == JOptionPane.CLOSED_OPTION){ 
			JOptionPane.showMessageDialog(null, "Song Kept In Playlist");
		}
	}
	
	
	/**
	 * Update JLabel displaying the current Song count and
	 * number of minutes of play for the songList.
	 *   
	 */
	public void updateListStatsLbl() {
		Double totalPlaytime = 0.0;
		DecimalFormat fmt = new DecimalFormat("0.00");
		
		totalPlaytime = songList.getTotalPlayTime() / 60.0;  // Convert seconds to minutes
		listStatsLbl.setText(songList.getNumSongs() + " Songs, plays ~ " + fmt.format(totalPlaytime) + " minutes");
	}
	
	
	/**
	 * Update Now Playing label with current playing song or 
	 * usage message to user.
	 * 
	 */
	public void updateNowPlayingLbl() {
		if (inPlay) {
			nowPlayingLbl.setText("Now Playing " + songJList.getSelectedValue().getTitle() + " by "
					+ songJList.getSelectedValue().getArtist());
		} else {
			nowPlayingLbl.setText("Please click the play button to start.");
		}
	}
	
	
	/**
     * Given the number of times a song has been played, this method will
     * return a corresponding heat map color.
     *
     * Sample Usage: Color color = getHeatMapColor(song.getTimesPlayed());
     *
     * This algorithm was borrowed from:
     * http://www.andrewnoske.com/wiki/Code_-_heatmaps_and_color_gradients
     *
     * @param plays The number of times the song that you want the color for has been played.
     * @return The color to be used for your heat map.
     * @author CS 121 Instructors
     */
    private Color getHeatMapColor(int plays)
    {
         double minPlays = 0, maxPlays = PlayableSong.MAX_PLAYS;    // upper/lower bounds
         double value = (plays - minPlays) / (maxPlays - minPlays); // normalize play count

         // The range of colors our heat map will pass through. This can be modified if you
         // want a different color scheme.
         Color[] colors = { Color.CYAN, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED };
         int index1, index2; // Our color will lie between these two colors.
         float dist = 0;     // Distance between "index1" and "index2" where our value is.

         if (value <= 0) {
              index1 = index2 = 0;
         } else if (value >= 1) {
              index1 = index2 = colors.length - 1;
         } else {
              value = value * (colors.length - 1);
              index1 = (int) Math.floor(value); // Our desired color will be after this index.
              index2 = index1 + 1;              // ... and before this index (inclusive).
              dist = (float) value - index1; // Distance between the two indexes (0-1).
         }

         int r = (int)((colors[index2].getRed() - colors[index1].getRed()) * dist)
                   + colors[index1].getRed();
         int g = (int)((colors[index2].getGreen() - colors[index1].getGreen()) * dist)
                   + colors[index1].getGreen();
         int b = (int)((colors[index2].getBlue() - colors[index1].getBlue()) * dist)
                   + colors[index1].getBlue();

         return new Color(r, g, b);
    }
    
    
    /**
     * Updates a single music Square (grid) button's
     * background color to reflect the number of times
     * that button's song has played.
     * 
     * @param row   row index of songSquare for this Song
     * @param col   column index of songSquare for this Song
     * 
     */
    public void updateSongSquareBtn(int row, int col) {
    	songSquareBtns[row][col].setBackground(
				getHeatMapColor(
				songSquare[row][col].getNumOfPlays()));
    }
    
    
    /**
     * Overloaded method.
     * 
     * Updates a single music Square (grid) button's
     * background color to reflect the number of times
     * that button's song has played.
     * 
     * @param String   title of song we wish to update grid color on.
     * 
     */
    public void updateSongSquareBtn(String title) {
    	for(int row = 0; row < songSquare.length; row++){
    		for(int col = 0; col < songSquare[row].length; col++) {
    			if(songSquare[row][col].getTitle().equals(title)) {
    				songSquareBtns[row][col].setBackground(
    						getHeatMapColor(
    						songSquare[row][col].getNumOfPlays()));
    			}
    		}
    	}
    	
    }
}