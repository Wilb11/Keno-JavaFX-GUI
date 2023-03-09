import javafx.application.Application;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.css.Rule;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.Console;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.lang.String.valueOf;

public class JavaFXTemplate extends Application {

	private Button menuButton;
	private Button startButton;
	private Label t1;
	private TextField t2;
	private BorderPane welcome;
	private EventHandler<ActionEvent> myHandler;
	private EventHandler<ActionEvent> startBtnHandler;
	private EventHandler<ActionEvent> menuBtnHandler;

	// Menu variables
	private BorderPane menu;
	private Button RulesButton;
	private Button OddsButton;
	private Button ExitButton;
	private Button BackButton;
	private Button ChangeColour;

	// Screens
	private Scene welcomeScreen;
	private Scene menuScreen;
	private Scene pickNumScreen;
	private Scene numsDrawnScreen;
	private Scene resultsScreen;

	// Global Variables
	private Map<Integer, Map<Integer, Integer>> oddsDictionary;

	private int gamesPlayed;
	private int gamesToPlay;
	private int spotsToPlay;
	private int totalDollarsWon;
	private Map<Integer, Integer> userSpotChoices;
	private Map<Integer, Integer> gridNums;

	private boolean T = true;

	// Result Screen Labels
	private Label numOfNumsMatched;
	private Label numsMatched;
	private Label currentRoundWinnings;
	private Label totalWinnings;

	// Result Screen Text Constants
	final private String numOfNumsText = "Total numbers matched : ";
	final private String numsMatchedText = "Numbers matched : ";
	final private String currentRoundWinningsText = "Current round winnings : $";
	final private String totalWinningsText = "Total winnings : $";

	// Getter for gamesPlayed
	public int getGamesPlayed()
	{
		return gamesPlayed;
	}

	// Setter for gamesPlayed
	public void setGamesPlayed(int val)
	{
		gamesPlayed = val;
	}

	// Getter for gamesToPlay
	public int getGamesToPlay()
	{
		return gamesPlayed;
	}

	// Setter for gamesPlayed
	public void setGamesToPlay(int val)
	{
		gamesPlayed = val;
	}

	// Getter for spotsToPlay
	public int getSpotsToPlay()
	{
		return spotsToPlay;
	}

	// Setter for spotsToPlay
	public void setSpotsToPlay(int val)
	{
		spotsToPlay = val;
	}

	// Getter for totalWinnings
	public int getTotalDollarsWon()
	{
		return totalDollarsWon;
	}

	// Setter for totalWinnings
	public void setTotalDollarsWon(int val)
	{
		totalDollarsWon = val;
	}

	public static void main(String[] args) {
		// auto generated method stub
		launch(args);
	}

	private void showWinningNumbers(BorderPane numsDrawnBorderPane) throws InterruptedException {
		BorderPane borderPane = numsDrawnBorderPane;
		GridPane pane = (GridPane) borderPane.getCenter();

		// Populate the grid with the winning numbers
		// Get all the grid children
		ObservableList<Node> children = pane.getChildren();

		ArrayList<Integer> nums = new ArrayList<>();
		ArrayList<Integer> winningNums = new ArrayList<>();
		int min = 1;
		int max = 80;

		int i = 0;
		while(i < 20)
		{
			int num = (int)(Math.random()*max)+min;
			if(!nums.contains(num))
			{
				// If the user has chosen this number
				// add to an ArrayList of winning numbers.
				if(userSpotChoices.containsKey(num))
				{
					System.out.println("Match found in user choices");
					winningNums.add(num);
				}
				nums.add(num);
				i++;
			}
		}

		i = 0;
		for(Node node : children)
		{
			Button btn = (Button) node;
			btn.setText(String.valueOf(nums.get(i)));
			i++;
			// TO DO : MAKE A DELAY BETWEEN SHOWING EACH NUMBER
//			TimeUnit.MILLISECONDS.sleep(250);
		}

		// Update the results screen
		updateResultsScreen(winningNums);
	}

	private void resetPickNumGrid(GridPane pane) {
		for (Node child : pane.getChildren()) {
			child.setStyle("-fx-background-color:#E1E1E1");
		}
	}

	private Scene setupPickNumScreen(Stage primaryStage, BorderPane numsDrawnBorderPane) {

		GridPane pane = new GridPane();

		pane.setAlignment(Pos.CENTER);

		Button Randomize = new Button("Randomize");
		Button Confirm = new Button("Submit");
		Confirm.setDisable(true);

		BorderPane borderPane = new BorderPane();
		Label title = new Label("Pick your numbers");
		title.setFont(new Font("Times New Roman", 70));
		borderPane.setTop(title);
		borderPane.setCenter(pane);
		borderPane.setLeft(Randomize);
		borderPane.setRight(Confirm);
		BorderPane.setAlignment(Randomize, Pos.BOTTOM_LEFT);
		BorderPane.setAlignment(Confirm, Pos.BOTTOM_RIGHT);

		// Fills the grid with the numbers 1 - 80.
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 10; j++) {
				// Grid number
				// Range : 1 - 80
				int num = j + 1 + (10 * i);
				Button btn = new Button(valueOf(num));
				btn.setStyle("-fx-background-color:#E1E1E1");
				pane.add(btn, j, i);

				final boolean[] isSelected = {false};

				btn.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						if (!isSelected[0] && userSpotChoices.size() < getSpotsToPlay()) {
							btn.setStyle("-fx-background-color:#ff0000");
							userSpotChoices.put(num, num);
						} else if (isSelected[0] && userSpotChoices.containsKey(num)) {
							btn.setStyle("-fx-background-color:#E1E1E1");
							userSpotChoices.remove(num);
						}
						isSelected[0] = !isSelected[0];
					}
				});
			}
		}

		// Event handler :
		// Picks the numbers for the player.
		// TO DO : make randomize pick all 'n' numbers at once
		Randomize.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				Confirm.setDisable(false);
				Random random = new Random();
				int row = random.nextInt(7);
				int column = random.nextInt(9);
				Node result = null;

				// Get all the grid children
				ObservableList<Node> children = pane.getChildren();

				userSpotChoices.clear();
				for(Node a: pane.getChildren()){
					Button s = (Button)a;
					s.setStyle("-fx-background-color:#EBECF0");
				}

				// Randomly select the numbers for the player
				for(int i = 0; i < getSpotsToPlay(); i++)
				{
					// Get a random number
					int num = (row*10) + column + 1;
					// Check if the number exists already
					while(true)
					{
						// If the number does not exist :
						// Store it
						if(!userSpotChoices.containsKey(num))
						{
							userSpotChoices.put(num, num);
							break;
						}
						// If the number does exist :
						// Generate a new number
						else
						{
							row = random.nextInt(7);
							column = random.nextInt(9);
							num = (row*10) + column + 1;
						}
					}
					// Highlight the randomly generated number
					for(Node node : children)
					{
						if(pane.getRowIndex(node) == row && pane.getColumnIndex(node) == column)
						{
							result = node;
							result.setStyle("-fx-background-color:#FF0000");
						}
					}
				}
			}
		});


		Confirm.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				primaryStage.setScene(numsDrawnScreen);
				resetPickNumGrid(pane);
				try {
					showWinningNumbers(numsDrawnBorderPane);
				} catch (InterruptedException error) {
					error.printStackTrace();
				}
			}
		});

		pickNumScreen = new Scene(borderPane, 700, 700);

		return pickNumScreen;
	}

	// Sets up the screen that displays the numbers
	// that were drawn randomly after the user
	// selects the numbers they wish to play.
	// when program pick 20 random numbers
	private BorderPane setupNumsDrawnScreen(Stage primaryStage) {
		GridPane gridPane = new GridPane();
		BorderPane borderPane = new BorderPane();

		gridPane.setAlignment(Pos.CENTER);

		Button Next = new Button("Next");

		Label title = new Label("Results");
		title.setFont(new Font("Times New Roman", 70));
		borderPane.setTop(title);
		borderPane.setCenter(gridPane);
		borderPane.setBottom(Next);
		BorderPane.setAlignment(title, Pos.CENTER);
		BorderPane.setAlignment(Next, Pos.BOTTOM_RIGHT);
		BorderPane.setAlignment(gridPane, Pos.CENTER);
		Random random = new Random();

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 5; j++) {
				// Grid number
				// Range : 1 - 80
				int num = random.nextInt(80);
				Button btn = new Button("");
				btn.setPrefWidth(50);
				btn.setMinWidth(50);
				btn.setMaxWidth(50);
				btn.setStyle("-fx-background-color:#E1E1E1");
				gridPane.add(btn, j, i);
			}
		}

		// Event handler :
		// Takes the user to the results screen.
		Next.setOnAction(new EventHandler<ActionEvent>() {
			// the next when I can see it
			public void handle(ActionEvent e) {
				primaryStage.setScene(resultsScreen);
				setGamesPlayed(getGamesPlayed() + 1);
				// Update the results screen with the current stats
//				updateResultsScreen();
//				fetchCurrentStats();
			}
		});

		numsDrawnScreen = new Scene(borderPane, 700, 700);

		return borderPane;
	}

	// Updates the result screen
	// Values that are updated :
	// How many numbers they matched
	// Which numbers they matched
	// How much they won that round
	// Running total of how much they won so far
	private void updateResultsScreen(ArrayList<Integer> winningNums) {
		Map<Integer, Integer> oddDictionary = oddsDictionary.get(getSpotsToPlay());
		int roundWinnings = oddDictionary.get(winningNums.size()) != null ? oddDictionary.get(winningNums.size()) : 0;
		setTotalDollarsWon(getTotalDollarsWon() + roundWinnings);
		numOfNumsMatched.setText(numOfNumsText + winningNums.size());
		numsMatched.setText(numsMatchedText + Arrays.toString(winningNums.toArray()));
		currentRoundWinnings.setText(currentRoundWinningsText + roundWinnings);
		totalWinnings.setText(totalWinningsText + getTotalDollarsWon());
	}

	// Sets up the screen that displays the
	// stats of the game.
	// The stats include :
	// How many numbers they matched
	// Which numbers they matched
	// How much they won that round
	// Running total of how much they won so far
	private void setupResultsScreen(Stage primaryStage) {

		BorderPane borderPane = new BorderPane();
		Button Continue = new Button("Continue");
		Button Exit = new Button("Exit");
		Button New_Game = new Button("New Game");

		VBox results = new VBox();
		HBox hbox = new HBox();

		results.setSpacing(20);
		hbox.setSpacing(260);

		numOfNumsMatched = new Label(numOfNumsText);
		numsMatched = new Label(numsMatchedText);
		currentRoundWinnings = new Label(currentRoundWinningsText);
		totalWinnings = new Label(totalWinningsText);

		Label title = new Label("Results");
		title.setFont(new Font("Times New Roman", 70));

		results.getChildren().addAll(numOfNumsMatched, numsMatched, currentRoundWinnings, totalWinnings);
		hbox.getChildren().addAll(Continue, New_Game, Exit);

		borderPane.setTop(title);
		borderPane.setCenter(results);
		borderPane.setBottom(hbox);
		results.setAlignment(Pos.CENTER);
		BorderPane.setAlignment(title, Pos.CENTER);
		BorderPane.setAlignment(results, Pos.CENTER);
		BorderPane.setAlignment(hbox, Pos.CENTER);

		// Event handler :
		// Takes the user to the draw screen.
		Continue.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
//				if()
				primaryStage.setScene(pickNumScreen);
			}
		});

		Exit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				Platform.exit();
			}
		});

		New_Game.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				// private void function for the number of spots
				number_of_spots(primaryStage);
			}
		});

		resultsScreen = new Scene(borderPane, 700, 700);
	}

	private void setupOddsDictionary() {
		Map<Integer, Integer> oneSpot = new HashMap<>();
		Map<Integer, Integer> fourSpot = new HashMap<>();
		Map<Integer, Integer> eightSpot = new HashMap<>();
		Map<Integer, Integer> tenSpot = new HashMap<>();

		oneSpot.put(1, 2);

		fourSpot.put(2, 1);
		fourSpot.put(3, 5);
		fourSpot.put(4, 75);

		eightSpot.put(4, 2);
		eightSpot.put(5, 12);
		eightSpot.put(6, 50);
		eightSpot.put(7, 750);
		eightSpot.put(8, 10000);

		tenSpot.put(0, 5);
		tenSpot.put(5, 2);
		tenSpot.put(6, 15);
		tenSpot.put(7, 40);
		tenSpot.put(8, 450);
		tenSpot.put(9, 4250);
		tenSpot.put(10, 100000);

		oddsDictionary = new HashMap<>();
		oddsDictionary.put(1, oneSpot);
		oddsDictionary.put(4, fourSpot);
		oddsDictionary.put(8, eightSpot);
		oddsDictionary.put(10, tenSpot);
	}

	private void setupVariables() {
		setTotalDollarsWon(0);
		setGamesPlayed(0);
	}

	public void setup(Stage primaryStage)
	{
		// Ignore for now.
//		primaryStage.setOnShown((numsDrawnScreen) -> {
//			try {
//				showWinningNumbers(numsDrawnBorderPane);
//				System.out.println("Show winning numbers");
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		});
//		primaryStage.sceneProperty().addListener((observable, oldScene, newScene) -> {
//			if(oldScene == pickNumScreen)
//			{
//				try {
//					showWinningNumbers(numsDrawnBorderPane);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//			System.out.println("New scene: " + newScene);
//			System.out.println("Old scene: " + oldScene);
//		});
		setupVariables();
		setupOddsDictionary();
		userSpotChoices = new HashMap<Integer, Integer>();
		BorderPane numsDrawnBorderPane = setupNumsDrawnScreen(primaryStage);
		setupPickNumScreen(primaryStage, numsDrawnBorderPane);
		setupResultsScreen(primaryStage);
	}

	private void number_of_spots(Stage primaryStage){
		Label title = new Label("How many number of spots\n"+
				"do you want to play?");

		GridPane pane = new GridPane();
		// 10 is the gap between the choice
		HBox hbox = new HBox(10);
		Button Next = new Button("Next");

		String[] spots = {"1", "4", "8", "10"};

		Button Back = new Button("Back");
		pane.setBackground(new Background(new BackgroundFill(Color.MEDIUMPURPLE, new CornerRadii(0), Insets.EMPTY)));

		for(String spot : spots)
		{
			Button temp = new Button(spot);
			temp.setMinSize(100,100);
			//temp.setStyle();
			hbox.getChildren().add(temp);
			Next.setDisable(true);
			temp.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					// private void function for the number of spots
					Next.setDisable(false);
					setSpotsToPlay(Integer.parseInt(spot));
				}
			});
		}

		pane.setHgap(50);
		pane.setVgap(50);

		BorderPane.setAlignment(hbox, Pos.CENTER);
		pane.add(title, 1, 1);
		pane.add(hbox, 1, 3);
		pane.add(Next, 2, 3);

		title.setFont(new Font("Times New Roman",30));
		title.setWrapText(true);
		title.setTextFill(Color.web("#000000"));
		title.setAlignment(Pos.CENTER);

		Back.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				primaryStage.setScene(welcomeScreen);
			}
		});

		Next.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				// private void function for the number of spots
				primaryStage.setScene(pickNumScreen);
			}
		});

		Scene scene = new Scene(pane,700,700);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// setup function
		setup(primaryStage);

		// initialize the welcome screen and buttons
		welcome = new BorderPane();
		// b1 is the menuButton
		menuButton = new Button("Menu");
		t1 = new Label("Welcome to Keno");
		startButton = new Button("Start");

		// size of the font and the colour of the font
		t1.setFont(new Font("Times New Roman", 70));
		menuButton.setStyle("-fx-background-color:#C034EB; -fx-font-size: 7em");
		startButton.setStyle("-fx-background-color:#EB3453; -fx-font-size: 7em");

		// Alignment for the menu
		BorderPane.setAlignment(menuButton, Pos.CENTER);
		welcome.setTop(menuButton);
		welcome.setCenter(t1);
		welcome.setBottom(startButton);

		BorderPane.setAlignment(startButton,Pos.BOTTOM_CENTER);

		// start button handler
		// startBtnHandler is myHandler
		// this is the action after you press it.
		menuButton.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e)
			{
				// making a grid to connect to the 3 options
				GridPane Menu = new GridPane();
				// setting the three buttons
				RulesButton = new Button("Rules");
				OddsButton = new Button("Odds");
				ExitButton = new Button("Exit");
				BackButton = new Button("Back");
				ChangeColour = new Button("Change background");

				// setting the font and style for the button
				RulesButton.setStyle("-fx-background-color:#C034EB; -fx-font-size: 5em");
				OddsButton.setStyle("-fx-background-color:#EB3453; -fx-font-size: 5em");
				ExitButton.setStyle("-fx-background-color:#2BE0D7; -fx-font-size: 5em");
				ChangeColour.setStyle("-fx-background-color:#BBE1F7; -fx-font-size: 3em");

				// divide the frame by 50 so the grid there are 14 by 14
				Menu.setHgap(50);
				Menu.setVgap(50);
				// setting the position of the three option
				Menu.add(RulesButton,5,1);
				Menu.add(OddsButton,5,2);
				Menu.add(ExitButton,5,3);
				Menu.add(BackButton, 0, 5);
				Menu.add(ChangeColour,5, 4);

				menuScreen = new Scene(Menu,700,700);
				Menu.setBackground(new Background(new BackgroundFill(Color.LIGHTSALMON, new CornerRadii(0), Insets.EMPTY)));
				primaryStage.setScene(menuScreen);
				primaryStage.show();

				ChangeColour.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent actionEvent) {
						Menu.setBackground(new Background(new BackgroundFill(Color.GREENYELLOW, new CornerRadii(0), Insets.EMPTY)));
						welcome.setBackground(new Background(new BackgroundFill(Color.YELLOW, new CornerRadii(0), Insets.EMPTY)));
					}
				});

				// Event handler :
				// Takes the user to the rules screen.
				RulesButton.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						GridPane Rules = new GridPane();
						Label title = new Label("Rules Of The Game");

						Label rules_game = new Label(
								"1. Players wager by choosing a set numbers\n" +
										"   pick 2 numbers, pick 10 numbers,\n" +
										"   etc.) ranging from 1 to 80. \n" +
										"2. After all players have made their \n" +
										"   wagers and picked their numbers,\n" +
										"   twenty numbers are drawn at random,\n" +
										"   between 1 and 80 with no duplicates.\n" +
										"3. Players win by matching a set \n" +
										"   amount of their numbers to the \n" +
										"   numbers that are randomly drawn.\n" +
										"4. The amount of numbers drawn and \n" +
										"   the amount of numbers matched that \n" +
										"   players are allowed to wager on will differ \n" +
										"   from casino to casino and state lottery to state lottery. \n");

						rules_game.setFont(new Font("Times New Roman", 20));
						title.setFont(new Font("Times New Roman", 15));

						Button Back = new Button("back");

						// background of the GUI
						Rules.setBackground(new Background(new BackgroundFill(Color.DARKOLIVEGREEN, new CornerRadii(0), Insets.EMPTY)));

						// divide the frame by 50 so the grid there are 14 by 14
						Rules.setHgap(50);
						Rules.setVgap(50);

						Rules.add(title,4,1);
						Rules.add(rules_game, 4, 2 );
						Rules.add(Back,0,4);

						Scene scene = new Scene(Rules,700,700);
						primaryStage.setScene(scene);
						primaryStage.show();

						// Event handler:
						// Takes the user to the menu screen.
						Back.setOnAction(new EventHandler<ActionEvent>() {
							public void handle(ActionEvent e) {
								primaryStage.setScene(menuScreen);
							}
						});
					}
				});

				// Event handler :
				// Takes the user back to the welcome screen.
				BackButton.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						primaryStage.setScene(welcomeScreen);
					}
				});

				// Event handler :
				// Takes the user to the odds screen.
				// this is the odd button
				OddsButton.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						GridPane Odds = new GridPane();
						Label title = new Label("Odds Of The Game");

						Label odds_game = new Label(
								"1 Spot game:\n" +
										"Overall odds: 1 in 4.00\n" +
										"\n" +
										"4 Spot game:\n" +
										"Overall odds: 1 in 3.86\n" +
										"\n" +
										"8 Spot game:\n" +
										"Overall odds: 1 in 9.77\n" +
										"\n" +
										"10 Spot game:\n" +
										"Overall odds: 1 in 9.05\n"
						);

						odds_game.setFont(new Font("Times New Roman", 20));
						title.setFont(new Font("Times New Roman", 30));

						Button Back = new Button("Back");

						// background of the GUI
						Odds.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET, new CornerRadii(0), Insets.EMPTY)));

						// divide the frame by 50 so the grid there are 14 by 14
						Odds.setHgap(50);
						Odds.setVgap(50);

						Odds.add(title,4,1);
						Odds.add(odds_game, 4, 2 );
						Odds.add(Back, 0, 5);
						Scene scene = new Scene(Odds,700,700);
						primaryStage.setScene(scene);
						primaryStage.show();

						// Event handler:
						// Takes the user to the menu screen.
						Back.setOnAction(new EventHandler<ActionEvent>() {
							public void handle(ActionEvent e) {
								primaryStage.setScene(menuScreen);
							}
						});
					}
				});

				// Event handler :
				// Exits the game.
				ExitButton.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						Platform.exit();
					}
				});

			}
		});

		startButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				Label title = new Label("How many consecutive draws\n"+
						"would you like to play?");

				GridPane pane = new GridPane();
				// 10 is the gap between the choice
				HBox hbox = new HBox(10);

				String[] games = {"1", "2", "3", "4"};
				Button Back = new Button("Back");
				Button Next = new Button("Next");
				pane.setBackground(new Background(new BackgroundFill(Color.ROSYBROWN, new CornerRadii(0), Insets.EMPTY)));

				for(String game : games)
				{
					Button temp = new Button(game);
					temp.setMinSize(100,40);
					//temp.setStyle();
					hbox.getChildren().add(temp);
					Next.setDisable(true);
					temp.setOnAction(new EventHandler<ActionEvent>() {
						public void handle(ActionEvent e) {
							// private void function for the number of spots
							Next.setDisable(false);
							setGamesPlayed(Integer.parseInt(game));
						}
					});
				}

				Next.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						// private void function for the number of spots
						number_of_spots(primaryStage);
					}
				});

				pane.setHgap(50);
				pane.setVgap(50);

				BorderPane.setAlignment(hbox, Pos.CENTER);
				pane.add(title, 1, 1);
				pane.add(hbox, 1, 3);
				pane.add(Back, 1, 5);
				pane.add(Next, 2, 5);

				title.setFont(new Font("Times New Roman",40));
				title.setWrapText(true);
				title.setTextFill(Color.web("#000000"));
				title.setAlignment(Pos.CENTER);

				Back.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						primaryStage.setScene(welcomeScreen);
					}
				});

				Scene scene = new Scene(pane,700,700);
				primaryStage.setScene(scene);
				primaryStage.show();
			}
		});

		// setting the background
		welcome.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, new CornerRadii(0), Insets.EMPTY)));
		// setting the size of the border
		welcomeScreen = new Scene(welcome, 700, 700);

		primaryStage.setScene(welcomeScreen);
		primaryStage.setTitle("Keno");
		primaryStage.show();
	}
}