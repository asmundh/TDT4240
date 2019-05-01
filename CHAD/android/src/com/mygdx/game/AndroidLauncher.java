package com.mygdx.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.games.AnnotatedData;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.GamesClientStatusCodes;
import com.google.android.gms.games.InvitationsClient;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.TurnBasedMultiplayerClient;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.InvitationCallback;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.turnbased.LoadMatchesResponse;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchUpdateCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AndroidLauncher extends PatchedAndroidApplication implements View.OnClickListener {

	public static final String TAG = "SkeletonActivity";

	private boolean gdxView;

	// Client used to sign in with Google APIs
	private GoogleSignInClient mGoogleSignInClient = null;

	// Client used to interact with the TurnBasedMultiplayer system.
	private TurnBasedMultiplayerClient mTurnBasedMultiplayerClient = null;

	// Callback for matchupdates
	private TurnBasedMatchUpdateCallback mTurnBasedMatchUpdateCallback = null;

	// Flag to see if signed in
	private boolean mSignedIn;

	// Client used to interact with the Invitation system.
	private InvitationsClient mInvitationsClient = null;

	// Local convenience pointers
	public TextView mDataView;
	public TextView mTurnTextView;

	private AlertDialog mAlertDialog;

	// For our intents
	private static final int RC_SIGN_IN = 9001;
	final static int RC_SELECT_PLAYERS = 10000;
	final static int RC_LOOK_AT_MATCHES = 10001;
	final static int LIBGDX_QUICK_MATCH = 10002;

	// Should I be showing the turn API?
	public boolean isDoingTurn = false;

	// This is the current match we're in; null if not loaded
	public TurnBasedMatch mMatch;

	// This is the current match data after being unpersisted.
	// Do not retain references to match data once you have
	// taken an action on the match, such as takeTurn()
	public SkeletonTurn mTurnData;

	private GoogleSignInAccount mGoogleSignInAccount = null;

	private RelativeLayout mLayout;
	private View gameView;
	private View signInView;

	private String mOpponentDisplayName = null;

	private String gameDataFromCore = null;


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = false; // Not sure if this one is needed.

		// Create the androidInterface object that we will pass to CardGame in order to pass data between core and android
		AndroidclassBasicAndroidInterface androidInterface = new AndroidclassBasicAndroidInterface(this);

		// Create the Google API Client with access to Games
		// Create the client used to sign in.
		mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);

		//Initialize CardGame with the androidInterface

		//initiliazeForView returns a view of the cardGame
		gameView = initializeForView(new CardGame(androidInterface), config);

		//Initialize the layout to add the gameView and the mainActivity
		mLayout = new RelativeLayout(this);

		//Initialize views for GPGS
		LayoutInflater inflater = getLayoutInflater();
		signInView = inflater.inflate(R.layout.activity_main, null);

		// Set up buttons
		//Button gdxButton = (Button)signInView.findViewById(R.id.GDXButton);
		signInView.findViewById(R.id.GDXButton).setOnClickListener(this);
		signInView.findViewById(R.id.sign_out_button).setOnClickListener(this);
		signInView.findViewById(R.id.sign_in_button).setOnClickListener(this);
		signInView.findViewById(R.id.GDXButton2).setOnClickListener(this);
		signInView.findViewById(R.id.TakeTurnButton).setOnClickListener(this);
		signInView.findViewById(R.id.CheckMatchStatus).setOnClickListener(this);
		signInView.findViewById(R.id.CheckTurnStatus).setOnClickListener(this);
		signInView.findViewById(R.id.LoadMatchButton).setOnClickListener(this);
		signInView.findViewById(R.id.DismissMatchButton).setOnClickListener(this);
		signInView.findViewById(R.id.CreateNewMatch).setOnClickListener(this);
		signInView.findViewById(R.id.FinishMatch).setOnClickListener(this);

		// Add views to layout
		mLayout.addView(signInView);
		mLayout.addView(gameView);
		mLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

		// Set the contentview to the layout
		setContentView(mLayout);

		gdxView = true;
	}

	@Override
	protected void onResume() {
		Log.d(AppSettings.tag, "onResume()");
		super.onResume();

		// Since the state of the signed in user can change when the activity is not active
		// it is recommended to try and sign in silently from when the app resumes.

		signInSilently();
	}

	// Called by androidInterface to manually start a google sign-in
	public void manualSignIn(){
		startSignInIntent();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(AppSettings.tag, "onPause()");

		// Unregister the invitation callbacks; they will be re-registered via
		// onResume->signInSilently->onConnected.
		if (mInvitationsClient != null) {
			mInvitationsClient.unregisterInvitationCallback(mInvitationCallback);
		}

		if (mTurnBasedMultiplayerClient != null) {
			mTurnBasedMultiplayerClient.unregisterTurnBasedMatchUpdateCallback(mMatchUpdateCallback);
			Log.d(AppSettings.tag, "mMatchUpdateCallBack unregistered from mTurnBasedMultiplayerClient");
		}
	}

	private String mDisplayName;
	private String mPlayerId;

	private void onConnected(GoogleSignInAccount googleSignInAccount) {

		Log.d(TAG, "onConnected(): connected to Google APIs");

		mTurnBasedMultiplayerClient = Games.getTurnBasedMultiplayerClient(this, googleSignInAccount);
		mInvitationsClient = Games.getInvitationsClient(this, googleSignInAccount);

		Games.getPlayersClient(this, googleSignInAccount)
				.getCurrentPlayer()
				.addOnSuccessListener(
						new OnSuccessListener<Player>() {
							@Override
							public void onSuccess(Player player) {
								mDisplayName = player.getDisplayName();
								Log.d(AppSettings.tag, "Displayname: " + mDisplayName);
								mPlayerId = player.getPlayerId();
								gameView.setVisibility(View.VISIBLE);
								//COMMENTED OUT BECAUSE WE DONT WANT TO USE GOOGLE UI setViewVisibility();
							}
						}
				)
				.addOnFailureListener(createFailureListener("There was a problem getting the player!"));

		Log.d(TAG, "onConnected(): Connection successful");

		// Retrieve the TurnBasedMatch from the connectionHint
		GamesClient gamesClient = Games.getGamesClient(this, googleSignInAccount);
		gamesClient.getActivationHint()
				.addOnSuccessListener(new OnSuccessListener<Bundle>() {
					@Override
					public void onSuccess(Bundle hint) {
						if (hint != null) {
							TurnBasedMatch match = hint.getParcelable(Multiplayer.EXTRA_TURN_BASED_MATCH);

							if (match != null) {
								Log.d(AppSettings.tag, "Found a match after connecting!");
								updateMatch(match);
							}
						}
					}
				})
				.addOnFailureListener(createFailureListener(
						"There was a problem getting the activation hint!"));

		//COMMENTED OUT BECAUSE WE DONT WANT TO USE GOOGLE UI setViewVisibility();

		// As a demonstration, we are registering this activity as a handler for
		// invitation and match events.

		// This is *NOT* required; if you do not register a handler for
		// invitation events, you will get standard notifications instead.
		// Standard notifications may be preferable behavior in many cases.
		mInvitationsClient.registerInvitationCallback(mInvitationCallback);

		// Likewise, we are registering the optional MatchUpdateListener, which
		// will replace notifications you would get otherwise. You do *NOT* have
		// to register a MatchUpdateListener.
		mTurnBasedMultiplayerClient.registerTurnBasedMatchUpdateCallback(mMatchUpdateCallback);
	}

	private void onDisconnected() {

		Log.d(TAG, "onDisconnected()");

		mTurnBasedMultiplayerClient = null;
		mInvitationsClient = null;

		//COMMENTED OUT BECAUSE WE DONT WANT TO USE GOOGLE UI setViewVisibility();
		mSignedIn = false;
	}

	// This is a helper functio that will do all the setup to create a simple failure message.
	// Add it to any task and in the case of an failure, it will report the string in an alert
	// dialog.
	private OnFailureListener createFailureListener(final String string) {
		return new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				handleException(e, string);
			}
		};
	}

	// Displays your inbox. You will get back onActivityResult where
	// you will need to figure out what you clicked on.
	public void onCheckGamesClicked(View view) {
		mTurnBasedMultiplayerClient.getInboxIntent()
				.addOnSuccessListener(new OnSuccessListener<Intent>() {
					@Override
					public void onSuccess(Intent intent) {
						startActivityForResult(intent, RC_LOOK_AT_MATCHES);
					}
				})
				.addOnFailureListener(createFailureListener(getString(R.string.error_get_inbox_intent)));
	}

	// Open the create-game UI. You will get back an onActivityResult
	// and figure out what to do.
	public void onStartMatchClicked(View view) {
		mTurnBasedMultiplayerClient.getSelectOpponentsIntent(1, 7, true)
				.addOnSuccessListener(new OnSuccessListener<Intent>() {
					@Override
					public void onSuccess(Intent intent) {
						startActivityForResult(intent, RC_SELECT_PLAYERS);
					}
				})
				.addOnFailureListener(createFailureListener(
						getString(R.string.error_get_select_opponents)));
	}

	// Create a one-on-one automatch game.
	public void onQuickMatchClicked(View view) {

		Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(1, 1, 0);

		TurnBasedMatchConfig turnBasedMatchConfig = TurnBasedMatchConfig.builder()
				.setAutoMatchCriteria(autoMatchCriteria).build();

		showSpinner();

		// Start the match
		mTurnBasedMultiplayerClient.createMatch(turnBasedMatchConfig)
				.addOnSuccessListener(new OnSuccessListener<TurnBasedMatch>() {
					@Override
					public void onSuccess(TurnBasedMatch turnBasedMatch) {
						onInitiateMatch(turnBasedMatch);
					}
				})
				.addOnFailureListener(createFailureListener("There was a problem creating a match!"));
	}

	// In-game controls

	// Cancel the game. Should possibly wait until the game is canceled before
	// giving up on the view.
	public void onCancelClicked(View view) {
		showSpinner();

		mTurnBasedMultiplayerClient.cancelMatch(mMatch.getMatchId())
				.addOnSuccessListener(new OnSuccessListener<String>() {
					@Override
					public void onSuccess(String matchId) {
						onCancelMatch(matchId);
					}
				})
				.addOnFailureListener(createFailureListener("There was a problem cancelling the match!"));

		isDoingTurn = false;
		//COMMENTED OUT BECAUSE WE DONT WANT TO USE GOOGLE UI setViewVisibility();
	}

	// Leave the game during your turn. Note that there is a separate
	// mTurnBasedMultiplayerClient.leaveMatch() if you want to leave NOT on your turn.
	public void onLeaveClicked(View view) {
		showSpinner();
		String nextParticipantId = getNextParticipantId();

		mTurnBasedMultiplayerClient.leaveMatchDuringTurn(mMatch.getMatchId(), nextParticipantId)
				.addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						onLeaveMatch();
					}
				})
				.addOnFailureListener(createFailureListener("There was a problem leaving the match!"));

		//COMMENTED OUT BECAUSE WE DONT WANT TO USE GOOGLE UI setViewVisibility();
	}


	// Finish the game. Sometimes, this is your only choice.
	public void onFinishClicked(View view) {
		showSpinner();
		mTurnBasedMultiplayerClient.finishMatch(mMatch.getMatchId())
				.addOnSuccessListener(new OnSuccessListener<TurnBasedMatch>() {
					@Override
					public void onSuccess(TurnBasedMatch turnBasedMatch) {
						onUpdateMatch(turnBasedMatch);
					}
				})
				.addOnFailureListener(createFailureListener("There was a problem finishing the match!"));

		isDoingTurn = false;
		//COMMENTED OUT BECAUSE WE DONT WANT TO USE GOOGLE UI setViewVisibility();
	}


	// Upload your new gamestate, then take a turn, and pass it on to the next
	// player.
	public void onDoneClicked(View view) {
		showSpinner();

		String nextParticipantId = getNextParticipantId();
		// Create the next turn
		mTurnData.turnCounter += 1;
		mTurnData.data = mDataView.getText().toString();
		mTurnData.data = "Some new Turn data!";

		mTurnBasedMultiplayerClient.takeTurn(mMatch.getMatchId(),
				mTurnData.persist(), nextParticipantId)
				.addOnSuccessListener(new OnSuccessListener<TurnBasedMatch>() {
					@Override
					public void onSuccess(TurnBasedMatch turnBasedMatch) {
						onUpdateMatch(turnBasedMatch);
					}
				})
				.addOnFailureListener(createFailureListener("There was a problem taking a turn!"));

		mTurnData = null;
	}

	// Sign-in, Sign out behavior

	// Update the visibility based on what state we're in.
	public void setViewVisibility() {
		boolean isSignedIn = mTurnBasedMultiplayerClient != null;

		if (!isSignedIn) {
			findViewById(R.id.login_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
			findViewById(R.id.matchup_layout).setVisibility(View.GONE);
			findViewById(R.id.gameplay_layout).setVisibility(View.GONE);

			if (mAlertDialog != null) {
				mAlertDialog.dismiss();
			}
			return;
		}


		((TextView) findViewById(R.id.name_field)).setText(mDisplayName);
		findViewById(R.id.login_layout).setVisibility(View.GONE);

		if (isDoingTurn) {
			findViewById(R.id.matchup_layout).setVisibility(View.GONE);
			findViewById(R.id.gameplay_layout).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.matchup_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.gameplay_layout).setVisibility(View.GONE);
		}
	}

	// Switch to gameplay view.
	public void setGameplayUI() {
		isDoingTurn = true;
		//COMMENTED OUT BECAUSE WE DONT WANT TO USE GOOGLE UI setViewVisibility();
		// COMMENTED OUT BECAUSE WE DONT WANT TO USE GOOGLE UI mDataView.setText(mTurnData.data);
		// COMMENTED OUT BECAUSE WE DONT WANT TO USE GOOGLE UI mTurnTextView.setText(getString(R.string.turn_label, mTurnData.turnCounter));
	}

	// Helpful dialogs

	public void showSpinner() {
		findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
	}

	public void dismissSpinner() {
		findViewById(R.id.progressLayout).setVisibility(View.GONE);
	}

	// Generic warning/info dialog
	public void showWarning(String title, String message) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle(title).setMessage(message);

		// set dialog message
		alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
					}
				});

		// create alert dialog
		mAlertDialog = alertDialogBuilder.create();

		// show it
		mAlertDialog.show();
	}

	// Rematch dialog
	public void askForRematch() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setMessage("Do you want a rematch?");

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Sure, rematch!",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								rematch();
							}
						})
				.setNegativeButton("No.",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
							}
						});

		alertDialogBuilder.show();
	}


	/**
	 * Start a sign in activity.  To properly handle the result, call tryHandleSignInResult from
	 * your Activity's onActivityResult function
	 */
	public void startSignInIntent() {
		startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
	}

	/**
	 * Try to sign in without displaying dialogs to the user.
	 * <p>
	 * If the user has already signed in previously, it will not show dialog.
	 */
	public void signInSilently() {
		Log.d(TAG, "signInSilently()");

		mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
				new OnCompleteListener<GoogleSignInAccount>() {
					@Override
					public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
						if (task.isSuccessful()) {
							Log.d(TAG, "signInSilently(): success");
							mSignedIn = true;
							mGoogleSignInAccount = task.getResult();
							onConnected(task.getResult());
						} else {
							Log.d(TAG, "signInSilently(): failure", task.getException());
							onDisconnected();
						}
					}
				});
	}


	public void signOut() {
		Log.d(TAG, "signOut()");

		mGoogleSignInClient.signOut().addOnCompleteListener(this,
				new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {

						if (task.isSuccessful()) {
							Log.d(TAG, "signOut(): success");
						} else {
							handleException(task.getException(), "signOut() failed!");
						}

						onDisconnected();
					}
				});
	}

	/**
	 * Since a lot of the operations use tasks, we can use a common handler for whenever one fails.
	 *
	 * @param exception The exception to evaluate.  Will try to display a more descriptive reason for
	 *                  the exception.
	 * @param details   Will display alongside the exception if you wish to provide more details for
	 *                  why the exception happened
	 */
	private void handleException(Exception exception, String details) {
		Log.d(TAG, "handling exception");
		int status = 0;

		if (exception instanceof TurnBasedMultiplayerClient.MatchOutOfDateApiException) {
			TurnBasedMultiplayerClient.MatchOutOfDateApiException matchOutOfDateApiException =
					(TurnBasedMultiplayerClient.MatchOutOfDateApiException) exception;

			new AlertDialog.Builder(this)
					.setMessage("Match was out of date, updating with latest match data...")
					.setNeutralButton(android.R.string.ok, null)
					.show();

			TurnBasedMatch match = matchOutOfDateApiException.getMatch();
			updateMatch(match);

			return;
		}

		if (exception instanceof ApiException) {
			Log.d(TAG, "ApiException!");
			ApiException apiException = (ApiException) exception;
			status = apiException.getStatusCode();
			Log.d(TAG, "Statuscode: " + status);
		}

		if (!checkStatusCode(status)) {
			return;
		}

		String message = getString(R.string.status_exception_error, details, status, exception);

		new AlertDialog.Builder(this)
				.setMessage(message)
				.setNeutralButton(android.R.string.ok, null)
				.show();
	}

	private void logBadActivityResult(int requestCode, int resultCode, String message) {
		Log.i(TAG, "Bad activity result(" + resultCode + ") for request (" + requestCode + "): "
				+ message);
	}

	// This function is what gets called when you return from either the Play
	// Games built-in inbox, or else the create game built-in interface.
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == RC_SIGN_IN) {

			Task<GoogleSignInAccount> task =
					GoogleSignIn.getSignedInAccountFromIntent(intent);
			Log.d(TAG, "onActivityResult - RC_SIGN_IN.");

			try {
				GoogleSignInAccount account = task.getResult(ApiException.class);
				onConnected(account);
			} catch (ApiException apiException) {
				String message = apiException.getMessage();
				if (message == null || message.isEmpty()) {
					message = getString(R.string.signin_other_error);
				}

				onDisconnected();

				new AlertDialog.Builder(this)
						.setMessage(message)
						.setNeutralButton(android.R.string.ok, null)
						.show();
			}
		} else if (requestCode == RC_LOOK_AT_MATCHES) {
			// Returning from the 'Select Match' dialog

			if (resultCode != Activity.RESULT_OK) {
				logBadActivityResult(requestCode, resultCode,
						"User cancelled returning from the 'Select Match' dialog.");
				return;
			}

			TurnBasedMatch match = intent
					.getParcelableExtra(Multiplayer.EXTRA_TURN_BASED_MATCH);

			if (match != null) {
				updateMatch(match);
			}

			Log.d(TAG, "Match = " + match);
		} else if (requestCode == RC_SELECT_PLAYERS) {
			// Returning from 'Select players to Invite' dialog

			if (resultCode != Activity.RESULT_OK) {
				// user canceled
				logBadActivityResult(requestCode, resultCode,
						"User cancelled returning from 'Select players to Invite' dialog");
				return;
			}

			// get the invitee list
			ArrayList<String> invitees = intent
					.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

			// get automatch criteria
			Bundle autoMatchCriteria;

			int minAutoMatchPlayers = intent.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
			int maxAutoMatchPlayers = intent.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);

			if (minAutoMatchPlayers > 0) {
				autoMatchCriteria = RoomConfig.createAutoMatchCriteria(minAutoMatchPlayers,
						maxAutoMatchPlayers, 0);
			} else {
				autoMatchCriteria = null;
			}

			TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder()
					.addInvitedPlayers(invitees)
					.setAutoMatchCriteria(autoMatchCriteria).build();

			// Start the match
			mTurnBasedMultiplayerClient.createMatch(tbmc)
					.addOnSuccessListener(new OnSuccessListener<TurnBasedMatch>() {
						@Override
						public void onSuccess(TurnBasedMatch turnBasedMatch) {
							onInitiateMatch(turnBasedMatch);
						}
					})
					.addOnFailureListener(createFailureListener("There was a problem creating a match!"));
			showSpinner();
		}
		else if(requestCode == LIBGDX_QUICK_MATCH){
			if (resultCode != Activity.RESULT_OK) {
				// Canceled or other unrecoverable error.
				logBadActivityResult(requestCode, resultCode,
						"User cancelled returning from 'Select players to Invite' dialog");
				return;
			}
			// get the invitee list
			ArrayList<String> invitees = intent
					.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

			// get automatch criteria
			Bundle autoMatchCriteria;

			int minAutoMatchPlayers = intent.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
			int maxAutoMatchPlayers = intent.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);

			if (minAutoMatchPlayers > 0) {
				autoMatchCriteria = RoomConfig.createAutoMatchCriteria(minAutoMatchPlayers,
						maxAutoMatchPlayers, 0);
			} else {
				autoMatchCriteria = null;
			}

			TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder()
					.addInvitedPlayers(invitees)
					.setAutoMatchCriteria(autoMatchCriteria).build();

			// Start the match
			mTurnBasedMultiplayerClient.createMatch(tbmc)
					.addOnSuccessListener(new OnSuccessListener<TurnBasedMatch>() {
						@Override
						public void onSuccess(TurnBasedMatch turnBasedMatch) {
							onInitiateMatch(turnBasedMatch);
						}
					})
					.addOnFailureListener(createFailureListener("There was a problem creating a match!"));
			showSpinner();
		}
	}

	// startMatch() happens in response to the createTurnBasedMatch()
	// above. This is only called on success, so we should have a
	// valid match object. We're taking this opportunity to setup the
	// game, saving our initial state. Calling takeTurn() will
	// callback to OnTurnBasedMatchUpdated(), which will show the game
	// UI.
	public void startMatch(TurnBasedMatch match) {
		Log.d(TAG, "startMatch()");
		mTurnData = new SkeletonTurn();
		// Some basic turn data
		mTurnData.data = "First turn";

		mMatch = match;

		String myParticipantId = mMatch.getParticipantId(mPlayerId);

		//showSpinner();

		mTurnBasedMultiplayerClient.takeTurn(match.getMatchId(),
				mTurnData.persist(), getNextParticipantId())
				.addOnSuccessListener(new OnSuccessListener<TurnBasedMatch>() {
					@Override
					public void onSuccess(TurnBasedMatch turnBasedMatch) {
						updateMatch(turnBasedMatch);
					}
				})
				.addOnFailureListener(createFailureListener("There was a problem taking a turn!"));
	}

	// If you choose to rematch, then call it and wait for a response.
	public void rematch() {
		showSpinner();
		mTurnBasedMultiplayerClient.rematch(mMatch.getMatchId())
				.addOnSuccessListener(new OnSuccessListener<TurnBasedMatch>() {
					@Override
					public void onSuccess(TurnBasedMatch turnBasedMatch) {
						onInitiateMatch(turnBasedMatch);
					}
				})
				.addOnFailureListener(createFailureListener("There was a problem starting a rematch!"));
		mMatch = null;
		isDoingTurn = false;
	}

	/**
	 * Get the next participant. In this function, we assume that we are
	 * round-robin, with all known players going before all automatch players.
	 * This is not a requirement; players can go in any order. However, you can
	 * take turns in any order.
	 *
	 * @return participantId of next player, or null if automatching
	 */
	public String getNextParticipantId() {

		String myParticipantId = mMatch.getParticipantId(mPlayerId);
		Log.d(AppSettings.tag, "My partcipantID: " + myParticipantId);

		ArrayList<String> participantIds = mMatch.getParticipantIds();
		Log.d(AppSettings.tag, "Participant IDs: "+ String.valueOf(participantIds));

		int desiredIndex = -1;

		for (int i = 0; i < participantIds.size(); i++) {
			if (participantIds.get(i).equals(myParticipantId)) {
				desiredIndex = i + 1;
			}
		}

		if (desiredIndex < participantIds.size()) {
			return participantIds.get(desiredIndex);
		}

		if (mMatch.getAvailableAutoMatchSlots() <= 0) {
			// You've run out of automatch slots, so we start over.
			return participantIds.get(0);
		} else {
			// You have not yet fully automatched, so null will find a new
			// person to play against.
			return null;
		}
	}

	// This is the main function that gets called when players choose a match
	// from the inbox, or else create a match and want to start it.
	public void updateMatch(TurnBasedMatch match) {
		Log.d(TAG, "updateMatch()");
		mMatch = match;
		Log.d(AppSettings.tag, "match.getData() in updateMatch(): " + match.getData());
		Log.d(AppSettings.tag, "match.getStatus() in updateMatch(): " +match.getStatus());
		Log.d(AppSettings.tag, "match.getTurnStatus() in updateMatch(): " +match.getTurnStatus());
		Log.d(AppSettings.tag, "match.getMatchId() in updateMatch(): " +match.getMatchId());

		//mTurnBasedMultiplayerClient.loadMatch(match.getMatchId());

		// set gameData to matchData


		int status = match.getStatus();
		int turnStatus = match.getTurnStatus();

		if(status == TurnBasedMatch.MATCH_STATUS_COMPLETE){
			Log.d(AppSettings.tag, "Match is completed! Someone ended it.");
		}

		switch (status) {
			case TurnBasedMatch.MATCH_STATUS_CANCELED:
				//showWarning("Canceled!", "This game was canceled!");
				Log.d(TAG, "Canceled! This game was canceled!");
				return;
			case TurnBasedMatch.MATCH_STATUS_EXPIRED:
				//showWarning("Expired!", "This game is expired.  So sad!");
				Log.d(TAG, "Expired! This game is expired.  So sad!");
				return;
			case TurnBasedMatch.MATCH_STATUS_AUTO_MATCHING:
				//showWarning("Waiting for auto-match...",
				//		"We're still waiting for an automatch partner.");
				Log.d(TAG, "We're still waiting for an automatch partner.");
				return;
			case TurnBasedMatch.MATCH_STATUS_COMPLETE:
				 showWarning("Complete!",
				"This game is over; someone finished it, and so did you!  " +
				"There is nothing to be done. This game will get dismissed now.");
					Log.d(TAG, "This game is over; someone finished it, and so did you!  ");
					Log.d(AppSettings.tag, "Dismissing the game since it is completed");
					mTurnBasedMultiplayerClient.dismissMatch(match.getMatchId());
					return;

				// Note that in this state, you must still call "Finish" yourself,
				// so we allow this to continue.
				//showWarning("Complete!",
				//		"This game is over; someone finished it!  You can only finish it now.");
		}
		Log.d(TAG, "Match is active, checking turnStatus");
		// OK, it's active. Check on turn status.
		switch (turnStatus) {
			case TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN:
				Log.d(AppSettings.tag, "Turnstatus: Match_turn_status_my_turn");
				mTurnData = SkeletonTurn.unpersist(mMatch.getData());
				setGameplayUI();
				return;
			case TurnBasedMatch.MATCH_TURN_STATUS_THEIR_TURN:
				Log.d(AppSettings.tag, "Turnstatus: Match_turn_status_their_turn");
				// Should return results.
				showWarning("Alas...", "It's not your turn.");
				break;
			case TurnBasedMatch.MATCH_TURN_STATUS_INVITED:
				Log.d(AppSettings.tag, "Turnstatus: Match_turn_status_invited");
				showWarning("Good inititative!",
						"Still waiting for invitations.\n\nBe patient!");
		}
		Log.d(AppSettings.tag, "setting mTurnData to null");
		mTurnData = null;

		//COMMENTED OUT BECAUSE WE DONT WANT TO USE GOOGLE UI setViewVisibility();
	}

	private void onCancelMatch(String matchId) {
		dismissSpinner();

		isDoingTurn = false;

		showWarning("Match", "This match (" + matchId + ") was canceled.  " +
				"All other players will have their game ended.");
	}

	private void onInitiateMatch(TurnBasedMatch match) {
		dismissSpinner();
		Log.d(TAG, "onInitiateMatch()");

		if (match.getData() != null) {
			// This is a game that has been initialized already
			updateMatch(match);
			return;
		}

		startMatch(match);
	}

	private void onLeaveMatch() {
		dismissSpinner();

		isDoingTurn = false;
		showWarning("Left", "You've left this match.");
	}


	public void onUpdateMatch(TurnBasedMatch match) {
		Log.d(AppSettings.tag, "onUpdateMatch()");
		dismissSpinner();

		if (match.canRematch()) {
			askForRematch();
		}

		isDoingTurn = (match.getTurnStatus() == TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN);
		Log.d(AppSettings.tag, "changed isDoingTurn to match.getTurnStatus() == TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN");

		if (isDoingTurn) {
			Log.d(AppSettings.tag, "isDoingTurn is true, so updateMatch(match) will be called");
			updateMatch(match);
			return;
		}

		//COMMENTED OUT BECAUSE WE DONT WANT TO USE THE PREMADE UI setViewVisibility();
	}

	private InvitationCallback mInvitationCallback = new InvitationCallback() {
		// Handle notification events.
		@Override
		public void onInvitationReceived(@NonNull Invitation invitation) {
			Toast.makeText(
					AndroidLauncher.this,
					"An invitation has arrived from "
							+ invitation.getInviter().getDisplayName(), Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onInvitationRemoved(@NonNull String invitationId) {
			Toast.makeText(AndroidLauncher.this, "An invitation was removed.", Toast.LENGTH_SHORT)
					.show();
		}
	};


	private TurnBasedMatchUpdateCallback mMatchUpdateCallback = new TurnBasedMatchUpdateCallback() {
		@Override
		public void onTurnBasedMatchReceived(@NonNull TurnBasedMatch turnBasedMatch) {
			Log.d(AppSettings.tag, "onTurnBasedMatchReceived():A Match was updated");
			Toast.makeText(AndroidLauncher.this, "A match was updated.", Toast.LENGTH_LONG).show();
			updateMatch(turnBasedMatch);
		}

		@Override
		public void onTurnBasedMatchRemoved(@NonNull String matchId) {
			Toast.makeText(AndroidLauncher.this, "A match was removed.", Toast.LENGTH_SHORT).show();
		}
	};

	public void showErrorMessage(int stringId) {
		showWarning("Warning", getResources().getString(stringId));
	}

	// Returns false if something went wrong, probably. This should handle
	// more cases, and probably report more accurate results.
	private boolean checkStatusCode(int statusCode) {
		switch (statusCode) {
			case GamesCallbackStatusCodes.OK:
				return true;

			case GamesClientStatusCodes.MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER:
				showErrorMessage(R.string.status_multiplayer_error_not_trusted_tester);
				break;
			case GamesClientStatusCodes.MATCH_ERROR_ALREADY_REMATCHED:
				showErrorMessage(R.string.match_error_already_rematched);
				break;
			case GamesClientStatusCodes.NETWORK_ERROR_OPERATION_FAILED:
				showErrorMessage(R.string.network_error_operation_failed);
				break;
			case GamesClientStatusCodes.INTERNAL_ERROR:
				showErrorMessage(R.string.internal_error);
				break;
			case GamesClientStatusCodes.MATCH_ERROR_INACTIVE_MATCH:
				showErrorMessage(R.string.match_error_inactive_match);
				break;
			case GamesClientStatusCodes.MATCH_ERROR_LOCALLY_MODIFIED:
				showErrorMessage(R.string.match_error_locally_modified);
				break;
			default:
				showErrorMessage(R.string.unexpected_status);
				Log.d(TAG, "Did not have warning or string to deal with: "
						+ statusCode);
		}

		return false;
	}


	public void changeView(){
		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				// Stuff that updates the UI
				if(gdxView == true){
					gameView.setVisibility(View.GONE);
					gdxView = false;
				}
				else{
					gameView.setVisibility(View.VISIBLE);
					gdxView = true;
				}

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.sign_in_button:
				mMatch = null;
				findViewById(R.id.sign_in_button).setVisibility(View.GONE);
				Log.d(AppSettings.tag, "starting signInIntent");
				startSignInIntent();
				break;
			case R.id.sign_out_button:
				signOut();
				//COMMENTED OUT BECAUSE WE DONT WANT TO USE GOOGLE UI setViewVisibility();
				break;
			case R.id.GDXButton:
				Log.d(AppSettings.tag, "GDXButton was clicked");
				changeView();
				break;
			case R.id.GDXButton2:
				Log.d(AppSettings.tag, "GDXButton was clicked");
				changeView();
				break;
			case R.id.TakeTurnButton:
				Log.d(AppSettings.tag, "Take turn button was clicked");
				takeTurn();
				break;
			case R.id.LoadMatchButton:
				Log.d(AppSettings.tag, "Load Match button was clicked");
				loadMatch();
				break;
			case R.id.DismissMatchButton:
				Log.d(AppSettings.tag, "Dismiss Match button was clicked");
				dismissMatch();
				break;
			case R.id.CreateNewMatch:
				Log.d(AppSettings.tag, "Create New Match button was clicked");
				createNewMatch();
				break;
			case R.id.FinishMatch:
				Log.d(AppSettings.tag, "Finish Match button was clicked");
				endMatch();
				break;
			case R.id.CheckMatchStatus:
				Log.d(AppSettings.tag, "Check Match Status was clicked:");
				mTurnBasedMultiplayerClient.getInboxIntent()
						.addOnSuccessListener(new OnSuccessListener<Intent>() {
							@Override
							public void onSuccess(Intent intent) {
								startActivityForResult(intent, RC_LOOK_AT_MATCHES);
							}
						})
						.addOnFailureListener(createFailureListener(getString(R.string.error_get_inbox_intent)));
				checkMatchStatus();
				break;
			case R.id.CheckTurnStatus:
				Log.d(AppSettings.tag, "Check Turn Status was Clicked:");
				checkTurnStatus();
				break;
		}
	}

	// Create a new match
	public void createNewMatch(){
		// check if gamesclient is not null
		if(mTurnBasedMultiplayerClient != null){
			Log.d(AppSettings.tag, "createNewMatch(): mTurnBasedMultiplayerClient is not null");
		}
		Log.d(AppSettings.tag, "Creating a new match!");
		Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(1, 1, 0);

		TurnBasedMatchConfig turnBasedMatchConfig = TurnBasedMatchConfig.builder()
				.setAutoMatchCriteria(autoMatchCriteria).build();

		Log.d(AppSettings.tag, "Config finished, trying create match through client...");

		//showSpinner();
		// Start the match
		mTurnBasedMultiplayerClient.createMatch(turnBasedMatchConfig)
				.addOnSuccessListener(new OnSuccessListener<TurnBasedMatch>() {
					@Override
					public void onSuccess(TurnBasedMatch turnBasedMatch) {
						Log.d(AppSettings.tag, "success from create match from client...");
						Log.d(AppSettings.tag, "Trying to onInitiateMatch(turnBasedMatch");
						onInitiateMatch(turnBasedMatch);
						Log.d(AppSettings.tag, "Created a new match!");
					}
				})
				.addOnFailureListener(createFailureListener("There was a problem creating a match!"));
	}

	// Dismiss mMatch
	public void dismissMatch(){
		// Check if there is a mMatch first
		if(mMatch != null) {
			// There is a match
			Log.d(AppSettings.tag, "Dismissing match");
			mTurnBasedMultiplayerClient.dismissMatch(mMatch.getMatchId());
		}
		else{
			Log.d(AppSettings.tag, "There was no mMatch to dismiss");
		}
	}

	//CheckMatchStatus for testing
	public void checkMatchStatus(){
		Log.d(AppSettings.tag, "checkMatchStatus():");
		if(mTurnBasedMultiplayerClient != null){
			if(mMatch != null) {
				Log.d(AppSettings.tag, String.valueOf(mMatch.getStatus()));
			}
			else{
				Log.d(AppSettings.tag, "mMatch = null");
			}
		}
	}

	//CheckTurnStatus for testing
	public void checkTurnStatus(){
		Log.d(AppSettings.tag, "checkTurnStatus():");
		if(mTurnBasedMultiplayerClient != null){
			if(mMatch != null) {
				Log.d(AppSettings.tag, String.valueOf(mMatch.getTurnStatus()));
			}
			else{
				Log.d(AppSettings.tag, "mMatch = null");
			}
		}
	}

	// Function to take a turn (For testing)
	public void takeTurn() {
		if(isDoingTurn) {

			showSpinner();

			String nextParticipantId = getNextParticipantId();
			// Create the next turn
			mTurnData.turnCounter += 1;
			// mTurnData.data = mDataView.getText().toString(); commented out because we dont use the textView
			// mTurnData.data = mDisplayName;
			// Set turn data to be equal to gameDataFromCore
			mTurnData.data = gameDataFromCore;

			mTurnBasedMultiplayerClient.takeTurn(mMatch.getMatchId(),
					mTurnData.persist(), nextParticipantId)
					.addOnSuccessListener(new OnSuccessListener<TurnBasedMatch>() {
						@Override
						public void onSuccess(TurnBasedMatch turnBasedMatch) {
							Log.d(AppSettings.tag, "Successfully uploaded turn going to call onUpdateMatch)");
							onUpdateMatch(turnBasedMatch);
						}
					})
					.addOnFailureListener(createFailureListener("There was a problem taking a turn!"));

			mTurnData = null;
		}
		else{
			Log.d(AppSettings.tag, "It is not your turn!");
			showWarning("STOP!", "It is not your turn!");
		}
	}


	// Function to start a quickmatch from libGDX

	public void startQuickMatch(){
		// Check if logged in
		if(mGoogleSignInAccount != null) {
			// Attempting to register turnBasedMatchUpdateCallback
			Log.d(AppSettings.tag, "Registering TurnbasedMatchUpdateCallback");
			mTurnBasedMultiplayerClient.registerTurnBasedMatchUpdateCallback(mMatchUpdateCallback);
			// First  check if we have a mTurnBasedMultiplayerClient
			if (mTurnBasedMultiplayerClient != null) {
				Log.d(AppSettings.tag, "mMatch is: " + mMatch);
				// First figure out if there exists any matches that involves the logged in user.
				int[] mMatchStatuses = new int[2];
				mMatchStatuses[0] = TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN;
				mMatchStatuses[1] = TurnBasedMatch.MATCH_TURN_STATUS_THEIR_TURN;
				mTurnBasedMultiplayerClient.loadMatchesByStatus(mMatchStatuses).addOnSuccessListener(new OnSuccessListener<AnnotatedData<LoadMatchesResponse>>() {
					@Override
					public void onSuccess(AnnotatedData<LoadMatchesResponse> loadMatchesResponseAnnotatedData) {
						Log.d(AppSettings.tag, "Loaded this many My turn matches: " + loadMatchesResponseAnnotatedData.get().getMyTurnMatches().getCount());
						Log.d(AppSettings.tag, "Loaded this many Their turn matches: " + loadMatchesResponseAnnotatedData.get().getTheirTurnMatches().getCount());
						if (loadMatchesResponseAnnotatedData.get().getMyTurnMatches().getCount() > 0) {
							// there is one OR MORE matches where it is my turn
							mMatch = loadMatchesResponseAnnotatedData.get().getMyTurnMatches().get(0); // Pick first match - can there be more than one? Would be a bug if so
						} else if (loadMatchesResponseAnnotatedData.get().getTheirTurnMatches().getCount() > 0) {
							// there is one OR MORE matches where it is not my turn
							mMatch = loadMatchesResponseAnnotatedData.get().getTheirTurnMatches().get(0); // Pick first match - can there be more than one? Would be a bug if so
						} // WE IGNORE COMPLETED MATCHES IN THIS METHOD - MIGHT HAVE TO INVOLVE THEM SOMEHOW?
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Log.d(AppSettings.tag, "There was an error loading matches.");
					}
				});
			}

			// IF there are no matches existing, create a new match with auto-match enabled. From now on this function should never create a new match
		}
	}


	/*public void startQuickMatch() {
			if(mTurnBasedMultiplayerClient != null){
				if (mMatch != null) {
					updateMatch(mMatch);
					Log.d(TAG, "mMatch.getStatus(): "+ String.valueOf(mMatch.getStatus()));
					//mTurnBasedMultiplayerClient.takeTurn(null, null, null);
				}

				else {
					Log.d(AppSettings.tag, "Creating a new match!");
					Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(1, 1, 0);

					TurnBasedMatchConfig turnBasedMatchConfig = TurnBasedMatchConfig.builder()
							.setAutoMatchCriteria(autoMatchCriteria).build();

					//showSpinner();
					Log.d(AppSettings.tag, "startQuickMatch()");
					// Start the match
					mTurnBasedMultiplayerClient.createMatch(turnBasedMatchConfig)
							.addOnSuccessListener(new OnSuccessListener<TurnBasedMatch>() {
								@Override
								public void onSuccess(TurnBasedMatch turnBasedMatch) {
									onInitiateMatch(turnBasedMatch);
								}
							})
							.addOnFailureListener(createFailureListener("There was a problem creating a match!"));
				}
			}
	} */

	// loadMatchButton attempt to load a match


	// called by AndroidInterface to figure out if the user is signed in
	public boolean getIsSignedIn(){
		return mSignedIn;
	}

	public void loadMatch(){
		int[] mMatchStatuses = new int[3];
		mMatchStatuses[0] = TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN;
		mMatchStatuses[1] = TurnBasedMatch.MATCH_TURN_STATUS_THEIR_TURN;
		mMatchStatuses[2] = TurnBasedMatch.MATCH_TURN_STATUS_COMPLETE;
		mTurnBasedMultiplayerClient.loadMatchesByStatus(mMatchStatuses).addOnSuccessListener(new OnSuccessListener<AnnotatedData<LoadMatchesResponse>>() {
			@Override
			public void onSuccess(AnnotatedData<LoadMatchesResponse> loadMatchesResponseAnnotatedData) {
				Log.d(AppSettings.tag, "Loaded My turn matches: " + loadMatchesResponseAnnotatedData.get().getMyTurnMatches().getCount());
				Log.d(AppSettings.tag, "Loaded Their turn matches: " + loadMatchesResponseAnnotatedData.get().getTheirTurnMatches().getCount());
				Log.d(AppSettings.tag, "Loaded completed matches: " + loadMatchesResponseAnnotatedData.get().getCompletedMatches().getCount());
				if(loadMatchesResponseAnnotatedData.get().getMyTurnMatches().getCount() > 0){
					// there is one OR MORE matches where it is my turn
					mMatch = loadMatchesResponseAnnotatedData.get().getMyTurnMatches().get(0); // Pick first match - can there be more than one? Would be a bug if so
					updateMatch(mMatch);
				}
				else if (loadMatchesResponseAnnotatedData.get().getTheirTurnMatches().getCount() > 0){
					// there is one OR MORE matches where it is not my turn
					mMatch = loadMatchesResponseAnnotatedData.get().getTheirTurnMatches().get(0); // Pick first match - can there be more than one? Would be a bug if so
					updateMatch(mMatch);
				}
				if (loadMatchesResponseAnnotatedData.get().getCompletedMatches().getCount() > 0){
					// there is one OR MORE matches that is completed, will loop through all and delete them
					// Amount of matches
					int amountToLoop = loadMatchesResponseAnnotatedData.get().getCompletedMatches().getCount();
					Log.d(AppSettings.tag, "getcompletedMatches().getCount() = " + amountToLoop);
					for(int i = 0; i < amountToLoop; i++){
						Log.d(AppSettings.tag, "loadMatch(): found " + String.valueOf(amountToLoop) + " matches that are completed. Dismissing them all.");
						TurnBasedMatch matchToDismiss = loadMatchesResponseAnnotatedData.get().getCompletedMatches().get(i);
						Log.d(AppSettings.tag, "Dismissing match: " + matchToDismiss.getMatchId());
						mTurnBasedMultiplayerClient.dismissMatch(matchToDismiss.getMatchId());
					}
				}
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Log.d(AppSettings.tag, "There was an error loading matches.");
			}
		});
	}

	// Function used to end the current match, return true if ended, return false if not
	public boolean endMatch(){
		// Attempt to end the match
		try {
			mTurnBasedMultiplayerClient.finishMatch(mMatch.getMatchId())
					.addOnSuccessListener(new OnSuccessListener<TurnBasedMatch>() {
						@Override
						public void onSuccess(TurnBasedMatch turnBasedMatch) {
							onUpdateMatch(turnBasedMatch);
						}
					})
					.addOnFailureListener(createFailureListener("There was a problem finishing the match!"));
			if(mMatch.getStatus() == TurnBasedMatch.MATCH_STATUS_COMPLETE){
				return true;
			}
			else{
				Log.d(AppSettings.tag, "Something went wrong trying to end/finish the game");
				return false;
			}
		}
		catch (Exception e){
			Log.d(AppSettings.tag, "Something went wrong when trying to end/finish the game");
			return false;
		}
	}

	/*
	These are the getFunctions used by core to get information from google play games services
	 */

	// Called from core to get the signed in players displayname
	public String getDisplayName(){

		return mDisplayName;
	}

	// Called from core to get the signed in players id
	public String getPlayerId(){

		return mPlayerId;
	}

	// returns the last data core send to this androidlauncher
	public String getGameDataFromCore(){

		return gameDataFromCore;
	}
	// returns the latest data this androidlauncher has received from matchupdates
	public String getGameData(){
		if(mTurnData != null){
			return mTurnData.data;
		}
		else{
			Log.d(AppSettings.tag, "getGameData(): the mTurnData was null, returning null to core");
			return null;
		}
	}

	public void receiveGameData(String gameData){
		gameDataFromCore = gameData;
	}

	// Called from core to know if it is a players turn
	public boolean getIsDoingTurn(){
		return isDoingTurn;
	}

	// Call to get displayName of opponent
	public String getOpponentDisplayName() {
		Log.d(TAG, "getOpponentDisplayName()");
		if (mMatch == null) {
			// There is no match yet
			Log.d(TAG, "There is no match yet");
			return null;
		} else {
			// Try to find opponent ID
			Log.d(TAG, "Your playerID "+ mPlayerId);
			String opponentId = mMatch.getDescriptionParticipantId();
			Log.d(TAG, "Opponent playerID "+ opponentId);

			ArrayList<String> allIdsInGame = mMatch.getParticipantIds();
			Log.d(TAG, "Size of allIdsInGame: "+ String.valueOf(allIdsInGame.size()));
			Log.d(TAG, "Result of getNextParticipantId(): "+ getNextParticipantId());

			if(opponentId == null){
				// There is no opponent yet
				Log.d(TAG, "There is a match, but there is no opponent yet");
				return null;
			}
			else{
				// There is an opponentID, lets find the Participant object from opponentID
				Participant opponent = mMatch.getParticipant(getNextParticipantId());
				if(opponent == null){
					// Something is wrong? The opponent Participant object is null?
					Log.d(TAG, "Something is wrong? The opponent Participant object is null?");
					return null;
				}
				else{
					// We have the opponent Participant object, we can now call getDisplayName() on it
					mOpponentDisplayName = opponent.getDisplayName();
					return mOpponentDisplayName;
				}
			}
		}
	}
}

